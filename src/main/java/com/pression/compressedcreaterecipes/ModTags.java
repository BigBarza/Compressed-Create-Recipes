package com.pression.compressedcreaterecipes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

//I don't really need this separated, at least for now, but i'll keep it here anyway in case i add more tags.
public class ModTags {
    public static final TagKey<Item> VOID_PROOF_TAG = ItemTags.create(new ResourceLocation(CompressedCreateRecipes.MODID, "void_proof"));
}
