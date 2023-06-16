package org.embeddedt.underland.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.embeddedt.underland.Underland;

public class BlockStates extends BlockStateProvider {
    public BlockStates(DataGenerator gen, ExistingFileHelper helper) {
        super(gen, Underland.MODID, helper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(Underland.TELEPORTER.get());
    }
}
