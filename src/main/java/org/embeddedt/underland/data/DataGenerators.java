package org.embeddedt.underland.data;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.JsonCodecProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.embeddedt.underland.Underland;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = Underland.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    record DefaultOre(BlockState targetState, int size, List<PlacementModifier> modifiers) {

    }

    private static final ImmutableMap<String, DefaultOre> ORE_MAP = ImmutableMap.<String, DefaultOre>builder()
            .put("coal_ore", new DefaultOre(Blocks.DEEPSLATE_COAL_ORE.defaultBlockState(), 16, ImmutableList.of(
                    CountPlacement.of(32),
                    HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(10), VerticalAnchor.belowTop(10))
            )))
            .put("iron_ore", new DefaultOre(Blocks.DEEPSLATE_IRON_ORE.defaultBlockState(), 16, ImmutableList.of(
                    CountPlacement.of(24),
                    HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(10), VerticalAnchor.belowTop(10))
            )))
            .put("lapis_ore", new DefaultOre(Blocks.DEEPSLATE_LAPIS_ORE.defaultBlockState(), 8, ImmutableList.of(
                    CountPlacement.of(12),
                    HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(10), VerticalAnchor.belowTop(10))
            )))
            .put("redstone_ore", new DefaultOre(Blocks.DEEPSLATE_REDSTONE_ORE.defaultBlockState(), 12, ImmutableList.of(
                    CountPlacement.of(12),
                    HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(10), VerticalAnchor.belowTop(10))
            )))
            .put("gold_ore", new DefaultOre(Blocks.DEEPSLATE_GOLD_ORE.defaultBlockState(), 8, ImmutableList.of(
                    CountPlacement.of(8),
                    HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(10), VerticalAnchor.belowTop(10))
            )))
            .put("diamond_ore", new DefaultOre(Blocks.DEEPSLATE_DIAMOND_ORE.defaultBlockState(), 8, ImmutableList.of(
                    CountPlacement.of(4),
                    HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(10), VerticalAnchor.belowTop(10))
            )))
            .put("emerald_ore", new DefaultOre(Blocks.DEEPSLATE_EMERALD_ORE.defaultBlockState(), 4, ImmutableList.of(
                    CountPlacement.of(2),
                    HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(10), VerticalAnchor.belowTop(10))
            )))
            .put("infested_deepslate", new DefaultOre(Blocks.INFESTED_DEEPSLATE.defaultBlockState(), 24, ImmutableList.of(
                    CountPlacement.of(48),
                    HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(10), VerticalAnchor.belowTop(10))
            )))
            .build();

    private static HolderLookup.Provider append(HolderLookup.Provider original, RegistrySetBuilder builder) {
        return builder.buildPatch(RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY), original);
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        generator.addProvider(event.includeServer(), new Recipes(generator.getPackOutput()));
        BlockTags blockTags = new BlockTags(generator.getPackOutput(), event.getLookupProvider(), event.getExistingFileHelper());
        generator.addProvider(event.includeServer(), blockTags);
        generator.addProvider(event.includeServer(), new Advancements(generator.getPackOutput(), event.getLookupProvider(), event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new ItemTags(generator.getPackOutput(), event.getLookupProvider(), blockTags.contentsGetter(), event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new BlockStates(generator.getPackOutput(), event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new ItemModels(generator.getPackOutput(), event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new LanguageProvider(generator.getPackOutput(), "en_us"));

        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        RegistrySetBuilder builder = new RegistrySetBuilder();
        builder.add(Registries.CONFIGURED_FEATURE, context -> {
            for(var entry : ORE_MAP.entrySet()) {
                ResourceLocation key = new ResourceLocation(Underland.MODID, entry.getKey());
                context.register(ResourceKey.create(Registries.CONFIGURED_FEATURE, key), new ConfiguredFeature<>(Feature.ORE, simpleOre(entry.getValue().targetState(), entry.getValue().size())));
            }
        });
        builder.add(Registries.PLACED_FEATURE, context -> {
            var configuredLookup = context.lookup(Registries.CONFIGURED_FEATURE);
            for(var entry : ORE_MAP.entrySet()) {
                ResourceLocation key = new ResourceLocation(Underland.MODID, entry.getKey());
                context.register(ResourceKey.create(Registries.PLACED_FEATURE, key), new PlacedFeature(configuredLookup.getOrThrow(ResourceKey.create(Registries.CONFIGURED_FEATURE, key)), entry.getValue().modifiers()));
            }
        });
        builder.add(Registries.DAMAGE_TYPE, context -> {
            context.register(Underland.DARKNESS, new DamageType("darkness", 0.1f));
        });
        generator.addProvider(event.includeServer(), new DatapackBuiltinEntriesProvider(generator.getPackOutput(), event.getLookupProvider(), builder, Set.of(Underland.MODID)));
        CompletableFuture<HolderLookup.Provider> withDynProvider = event.getLookupProvider().thenApply(r -> append(r, builder));
        generator.addProvider(event.includeServer(), new UnderlandOreTagsProvider(generator.getPackOutput(), existingFileHelper, withDynProvider, ORE_MAP.keySet()));
        generator.addProvider(event.includeServer(), new TagsProvider<>(generator.getPackOutput(), Registries.DAMAGE_TYPE, withDynProvider, Underland.MODID, event.getExistingFileHelper()) {
            @Override
            protected void addTags(HolderLookup.Provider provider) {
                this.tag(DamageTypeTags.BYPASSES_ARMOR).add(Underland.DARKNESS);
            }
        });
    }

    public static OreConfiguration simpleOre(BlockState desiredOre, int size) {
        return new OreConfiguration(
                ImmutableList.of(OreConfiguration.target(new TagMatchTest(net.minecraft.tags.BlockTags.DEEPSLATE_ORE_REPLACEABLES), desiredOre)),
                size,
                0f);
    }
}