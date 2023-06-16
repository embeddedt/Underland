package org.embeddedt.underland.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.embeddedt.underland.Underland;

import java.util.concurrent.CompletableFuture;

public class ItemTags extends ItemTagsProvider {

    public ItemTags(PackOutput generator, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagsProvider.TagLookup<Block>> blockTags, ExistingFileHelper helper) {
        super(generator, lookupProvider, blockTags, Underland.MODID, helper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
    }

    @Override
    public String getName() {
        return "Underland Item Tags";
    }
}