package org.embeddedt.underland.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.embeddedt.underland.Underland;
import org.embeddedt.underland.dimensions.Dimensions;

import java.util.Objects;

public class TeleporterBlockEntity extends BlockEntity {
    private ResourceLocation targetDimension;
    public TeleporterBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(Underland.TELEPORTER_BE.get(), pPos, pBlockState);
        targetDimension = Dimensions.UNDERLAND.location();
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putString("TargetDim", targetDimension.toString());
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        if(pTag.contains("TargetDim", Tag.TAG_STRING)) {
            targetDimension = new ResourceLocation(pTag.getString("TargetDim"));
        }
    }

    public ResourceLocation getTargetDimension() {
        return targetDimension;
    }

    public void setTargetDimension(ResourceLocation targetDimension) {
        Objects.requireNonNull(targetDimension);
        this.targetDimension = targetDimension;
        this.setChanged();
    }
}
