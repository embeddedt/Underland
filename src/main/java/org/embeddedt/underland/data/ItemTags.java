package org.embeddedt.underland.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.embeddedt.underland.Underland;

public class ItemTags extends ItemTagsProvider {

    public ItemTags(DataGenerator generator, BlockTagsProvider blockTags, ExistingFileHelper helper) {
        super(generator, blockTags, Underland.MODID, helper);
    }

    @Override
    protected void addTags() {
    }

    @Override
    public String getName() {
        return "Underland Tags";
    }
}