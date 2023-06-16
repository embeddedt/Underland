package org.embeddedt.underland.dimensions;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.embeddedt.underland.Underland;

public class Dimensions {
    public static final ResourceKey<Level> UNDERLAND = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(Underland.MODID, "underland"));
}
