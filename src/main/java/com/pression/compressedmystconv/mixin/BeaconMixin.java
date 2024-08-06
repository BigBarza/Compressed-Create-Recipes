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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

//This makes beacons scan upwards for items can be processed with radiant conversion recipes.
//Every tick is...maybe not the best thing ever, but it's far better than making items check for it.
@Mixin(BeaconBlockEntity.class)
public class BeaconMixin implements IBeaconLevel {
    @Shadow int levels;
    @Inject(method = "tick", at = @At("TAIL"))
    private static void onTick(Level level, BlockPos pos, BlockState block, BeaconBlockEntity beacon, CallbackInfo ci){
        IBeaconLevel bLevel = (IBeaconLevel) beacon; //Horrible, terrible, vile. I don't like this, but i can't get the beacon's level any other way.
        if(bLevel.getLevels() > 0 && !beacon.getBeamSections().isEmpty()){
            AABB searchArea = new AABB(pos, pos.offset(1,level.getMaxBuildHeight()-pos.getY(),1)); //Think of it as a 1 block thick column starting from the beacon to build limit.
            List<ItemEntity> items = level.getEntitiesOfClass(ItemEntity.class, searchArea); //I actually have no idea how efficient this is, but i grabbed this from the Rosa Arcana, so can't imagine it's horrible.
            for(ItemEntity item : items){
                RadiantConversionRecipe recipe = RadiantConversionRecipe.getRecipe(level, item.getItem(), bLevel.getLevels());
                if(recipe != null){
                    Vec3 itemPos = item.position();
                    ItemEntity newItem = new ItemEntity(level, itemPos.x, itemPos.y, itemPos.z, recipe.getOutput());
                    newItem.getItem().setCount(0);
                    //TODO: Clean this up a bit and change it to do the first step in one go, then do a while to split up overloaded stacks
                    while(item.getItem().getCount() >= recipe.getInput().getCount()){
                        newItem.getItem().grow(recipe.getOutput().getCount());
                        item.getItem().shrink(recipe.getInput().getCount());
                        if(newItem.getItem().getCount() > newItem.getItem().getMaxStackSize()){
                            newItem.getItem().shrink(newItem.getItem().getMaxStackSize());
                            if(!level.isClientSide()){
                                ItemEntity splitItem = new ItemEntity(level, itemPos.x, itemPos.y, itemPos.z, new ItemStack(newItem.getItem().getItem(), newItem.getItem().getMaxStackSize()));
                                Vec3 newDelta = item.getDeltaMovement().add((Math.random()/10)-0.05,(Math.random()/10)+0.05,(Math.random()/10)-0.05); //Scramble a bit the movement of the new item, not TOO much, but always a bit upwards.
                                splitItem.setDeltaMovement(newDelta);
                                level.addFreshEntity(splitItem);
                            }
                        }
                    }
                    if(!level.isClientSide()){
                        Vec3 newDelta = item.getDeltaMovement().add((Math.random()/10)-0.05,(Math.random()/10)+0.05,(Math.random()/10)-0.05); //Same as above
                        newItem.setDeltaMovement(newDelta);
                        level.addFreshEntity(newItem);
                    }
                    if(item.getItem().isEmpty()) item.discard();

                }
            }
        }

    }

    //This ties in with IBeaconLevel to be able to read the beacon's level.
    public int getLevels(){
        return levels;
    }

}
