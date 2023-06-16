package org.embeddedt.underland.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.embeddedt.underland.Underland;

public class BlockStates extends BlockStateProvider {
    public BlockStates(PackOutput gen, ExistingFileHelper helper) {
        super(gen, Underland.MODID, helper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(Underland.TELEPORTER.get());
    }
}
