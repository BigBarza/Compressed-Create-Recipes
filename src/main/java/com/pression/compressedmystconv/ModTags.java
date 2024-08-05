package com.pression.compressedmystconv;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModTags {
    public static final TagKey<Item> VOID_PROOF_TAG = ItemTags.create(new ResourceLocation(CompressedMystConv.MODID, "void_proof"));
}
