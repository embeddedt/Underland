package org.embeddedt.underland.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.embeddedt.underland.Underland;

public class BlockTags extends BlockTagsProvider {

    public BlockTags(DataGenerator generator, ExistingFileHelper helper) {
        super(generator, Underland.MODID, helper);
    }

    @Override
    protected void addTags() {
        tag(net.minecraft.tags.BlockTags.MINEABLE_WITH_PICKAXE)
                .add(Underland.TELEPORTER.get());
        tag(net.minecraft.tags.BlockTags.NEEDS_STONE_TOOL)
                .add(Underland.TELEPORTER.get());
    }

    @Override
    public String getName() {
        return "Underland Tags";
    }
}