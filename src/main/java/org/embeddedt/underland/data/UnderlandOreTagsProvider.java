package org.embeddedt.underland.data;

import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.embeddedt.underland.Underland;

import java.util.Collection;

public class UnderlandOreTagsProvider extends TagsProvider<PlacedFeature> {
    private static final TagKey<PlacedFeature> ORES = TagKey.create(Registry.PLACED_FEATURE_REGISTRY, new ResourceLocation(Underland.MODID, "ores"));
    private final Collection<ResourceLocation> targets;
    public UnderlandOreTagsProvider(DataGenerator generator, ExistingFileHelper helper, Collection<ResourceLocation> targets) {
        super(generator, BuiltinRegistries.PLACED_FEATURE, Underland.MODID, helper);
        this.targets = targets;
    }

    @Override
    protected void addTags() {
        var appender = this.tag(ORES);
        for(ResourceLocation location : targets) {
            appender.add(ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, location));
        }
    }
}
