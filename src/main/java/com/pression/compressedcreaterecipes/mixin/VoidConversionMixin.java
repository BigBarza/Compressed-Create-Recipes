package com.pression.compressedcreaterecipes.mixin;

import com.pression.compressedcreaterecipes.ModTags;
import com.pression.compressedcreaterecipes.recipe.VoidConversionRecipe;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

//This mixin intercepts entities that fall into the void, specifically items, right as they're about to disappear.
//It checks for void conversion recipes and the void_proof tag
@Mixin(Entity.class)
public class VoidConversionMixin {

    //That's a method in Entity. It's called by another mini-method that checks if it's 64 blocks into the void. All it does is call discard(). Confusing, but perfect for our purposes.
    @Inject(method = "outOfWorld", at = @At("HEAD"), cancellable = true)
    private void onOutOfWorld(CallbackInfo ci){
        Entity self = (Entity) (Object) this;
        if(self instanceof ItemEntity item){
            VoidConversionRecipe recipe = VoidConversionRecipe.getRecipe(item.getLevel(), item.getItem());
            if(recipe!=null){ //The recipe can be null if there is no recipe for that item
                int multiplier = item.getItem().getCount() / recipe.getInput().getCount(); //This is integer division so there should be no decimals.
                if(multiplier > 0){ //We don't abort here on a 0 multiplier, we still need to return the original item.
                    Vec3 pos = item.position();
                    ItemEntity newItem = new ItemEntity(item.getLevel(), pos.x, item.getLevel().getMinBuildHeight() - 10, pos.z, recipe.getOutput());
                    newItem.getItem().setCount(0); //We're going to calculate how many times the recipe would have been processed.
                    newItem.getItem().grow(recipe.getOutput().getCount() * multiplier);
                    item.getItem().shrink(recipe.getInput().getCount() * multiplier);

                    while(newItem.getItem().getCount() >= newItem.getItem().getMaxStackSize()){ //It's...not great to spawn in oversized stacks. Player can handle them fine, hoppers can't.
                        newItem.getItem().shrink(newItem.getItem().getMaxStackSize());
                        if(!item.getLevel().isClientSide()){
                            ItemEntity splitItem = new ItemEntity(item.getLevel(), pos.x, pos.y, pos.z, new ItemStack(newItem.getItem().getItem(), newItem.getItem().getMaxStackSize()));
                            unVoidItem(splitItem, item.fallDistance);
                            item.getLevel().addFreshEntity(splitItem); //Note: cannot merge this in unVoidItem, as it's used on existing items as well.
                        }
                    }
                    if(!item.getLevel().isClientSide()){
                        unVoidItem(newItem, item.fallDistance);
                        item.getLevel().addFreshEntity(newItem);
                    }
                }
                if(item.getItem().isEmpty()) item.discard();
                else unVoidItem(item, item.fallDistance);
                ci.cancel();
            }else if(item.getItem().is(ModTags.VOID_PROOF_TAG)){
                unVoidItem(item, item.fallDistance);
                ci.cancel();
            }
        }
    }
    //This takes an item entity, ideally into the void, and a falling distance. It searches for a hole in the bedrock and pops the item back up from 10 blocks deep.
    private void unVoidItem(ItemEntity item, float fall){ //Takes a separate value for fall distance in case of items created into the void
        Level level = item.getLevel();
        Vec3 pos = item.position().add(Math.random()-0.5,0,Math.random()-0.5); //Randomize the position a bit.
        fall = Math.max(fall, 60) - 54; //That number can be adjusted to configure how hard the item comes back up. Might bump it up to 55 if the items still end up a bit too high up.
        item.setPos(new Vec3(pos.x, level.getMinBuildHeight(), pos.z));
        AABB aabb = item.getBoundingBox().inflate(20,0,20).move(0,0.5,0);
        Optional<Vec3> freePos = level.findFreePosition(item, Shapes.create(aabb), item.position(), 0.25,0.25,0.25); //HOPEFULLY this fixes the thing where it gets stuck under bedrock
        if(!freePos.isEmpty()) item.setPos(freePos.get());
        item.setPos(item.position().add(0,-10,0)); //This is to make it so that it visually doesn't just pop up at y-64
        item.setDeltaMovement(0, (fall)/50f, 0);
        item.setNoGravity(true);
    }

}
