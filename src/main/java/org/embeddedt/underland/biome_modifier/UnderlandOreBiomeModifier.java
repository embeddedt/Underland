package org.embeddedt.underland.biome_modifier;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import org.embeddedt.underland.Underland;

public record UnderlandOreBiomeModifier(Holder<Biome> targetBiome, HolderSet<PlacedFeature> oreFeatures) implements BiomeModifier {
    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if(phase == Phase.ADD && biome.equals(targetBiome)) {
            for(Holder<PlacedFeature> feature : oreFeatures) {
                builder.getGenerationSettings().getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES).add(feature);
            }
        }
    }

    @Override
    public Codec<? extends BiomeModifier> codec() {
        return Underland.UNDERLAND_ORE_CODEC.get();
    }
}
