package org.embeddedt.underland.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.embeddedt.underland.Underland;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class UnderlandOreTagsProvider extends TagsProvider<PlacedFeature> {
    private static final TagKey<PlacedFeature> ORES = TagKey.create(Registries.PLACED_FEATURE, new ResourceLocation(Underland.MODID, "ores"));
    private final Collection<String> targets;
    public UnderlandOreTagsProvider(PackOutput generator, ExistingFileHelper helper, CompletableFuture<HolderLookup.Provider> lookupProvider, Collection<String> targets) {
        super(generator, Registries.PLACED_FEATURE, lookupProvider, Underland.MODID, helper);
        this.targets = targets;
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        var appender = this.tag(ORES);
        for(String oreKey : targets) {
            appender.add(ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(Underland.MODID, oreKey)));
        }
    }
}
