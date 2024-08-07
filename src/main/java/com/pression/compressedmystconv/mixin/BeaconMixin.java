package com.pression.compressedmystconv.mixin;

import com.pression.compressedmystconv.interfaces.IBeaconLevel;
import com.pression.compressedmystconv.recipe.RadiantConversionRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.List;

//This makes beacons scan upwards for items can be processed with radiant conversion recipes.
//Every tick is...maybe not the best thing ever, but it's far better than making items check for it.
@Mixin(BeaconBlockEntity.class)
public class BeaconMixin implements IBeaconLevel {

    //This cannot be used on its own, cast the beacon to IBeaconLevel and call getBeaconLevel()
    @Shadow int levels;

    @Inject(method = "tick", at = @At("TAIL"))
    private static void onTick(Level level, BlockPos blockPos, BlockState block, BeaconBlockEntity beacon, CallbackInfo ci){
        IBeaconLevel bLevel = (IBeaconLevel) beacon; //Horrible, terrible, vile. I don't like this, but i can't get the beacon's level any other way.
        if(bLevel.getBeaconLevels() > 0 && !beacon.getBeamSections().isEmpty()){
            AABB searchArea = new AABB(blockPos, blockPos.offset(1,level.getMaxBuildHeight()-blockPos.getY(),1)); //Think of it as a 1 block thick column starting from the beacon to build limit.
            List<ItemEntity> items = level.getEntitiesOfClass(ItemEntity.class, searchArea); //I actually have no idea how efficient this is, but i grabbed this from the Rosa Arcana, so can't imagine it's horrible.
            for(ItemEntity item : items){
                RadiantConversionRecipe recipe = RadiantConversionRecipe.getRecipe(level, item.getItem(), bLevel.getBeaconLevels());
                if(recipe != null){ //The recipe can be null if there is no recipe for that item, or there is, but the beacon's level is not enough.
                    int multiplier = item.getItem().getCount() / recipe.getInput().getCount(); //This is integer division so there should be no decimals.
                    if(multiplier <= 0) return; //If there isn't enough of the input to start the recipe, don't do the rest.

                    Vec3 pos = item.position();
                    ItemEntity newItem = new ItemEntity(level, pos.x, pos.y, pos.z, recipe.getOutput());
                    newItem.getItem().setCount(0); //We're going to calculate how many times the recipe would have been processed.
                    newItem.getItem().grow(recipe.getOutput().getCount() * multiplier);
                    item.getItem().shrink(recipe.getInput().getCount() * multiplier);

                    while(newItem.getItem().getCount() >= newItem.getItem().getMaxStackSize()){ //It's...not great to spawn in oversized stacks. Player can handle them fine, hoppers can't.
                        newItem.getItem().shrink(newItem.getItem().getMaxStackSize());
                        if(!level.isClientSide()){
                            ItemEntity splitItem = new ItemEntity(level, pos.x, pos.y, pos.z, new ItemStack(newItem.getItem().getItem(), newItem.getItem().getMaxStackSize()));
                            spawnItem(level, splitItem, item.getDeltaMovement());
                        }
                    }

                    if(!level.isClientSide()) spawnItem(level, newItem, item.getDeltaMovement());
                    if(item.getItem().isEmpty()) item.discard();

                }
            }
        }
    }

    //Takes the new item, scrambles a bit the delta movement and spawns it in the world.
    @Unique private static void spawnItem(Level level, ItemEntity item, Vec3 deltaV){
        Vec3 newDelta = deltaV.add((Math.random()/10)-0.05,(Math.random()/20)+0.05,(Math.random()/10)-0.05); //Scramble a bit the movement of the new item, not TOO much, but always a bit upwards. Far more noticeable with no-gravity items.
        item.setDeltaMovement(newDelta);
        item.setPickUpDelay(10); //Makes it so that it doesn't just immediately pop into the inventory if a player is close by. Bit of fiendish fun when no-gravity stuff is involved :)
        level.addFreshEntity(item);
    }

    //This ties in with IBeaconLevel to be able to read the beacon's level.
    @Unique public int getBeaconLevels(){
        return levels;
    }

}
