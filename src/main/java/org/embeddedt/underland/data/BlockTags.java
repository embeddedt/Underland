package org.embeddedt.underland.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.embeddedt.underland.Underland;

import java.util.concurrent.CompletableFuture;

public class BlockTags extends BlockTagsProvider {

    public BlockTags(PackOutput generator, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper helper) {
        super(generator, lookupProvider, Underland.MODID, helper);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        tag(net.minecraft.tags.BlockTags.MINEABLE_WITH_PICKAXE)
                .add(Underland.TELEPORTER.get());
        tag(net.minecraft.tags.BlockTags.NEEDS_STONE_TOOL)
                .add(Underland.TELEPORTER.get());
    }

    @Override
    public String getName() {
        return "Underland Block Tags";
    }
}