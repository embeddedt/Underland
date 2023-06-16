package org.embeddedt.underland.data;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.JsonCodecProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.embeddedt.underland.Underland;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        generator.addProvider(event.includeServer(), new Recipes(generator));
        BlockTags blockTags = new BlockTags(generator, event.getExistingFileHelper());
        generator.addProvider(event.includeServer(), blockTags);
        generator.addProvider(event.includeServer(), new Advancements(generator, event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new ItemTags(generator, blockTags, event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new BlockStates(generator, event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new ItemModels(generator, event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new LanguageProvider(generator, "en_us"));

        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        RegistryAccess registryAccess = RegistryAccess.builtinCopy();
        RegistryOps<JsonElement> registryOps = RegistryOps.create(JsonOps.INSTANCE, registryAccess);

        Map<ResourceLocation, ConfiguredFeature<?, ?>> configuredFeatureMap = new HashMap<>();
        Map<ResourceLocation, PlacedFeature> placedFeatureMap = new HashMap<>();

        Registry<ConfiguredFeature<?, ?>> configuredFeatureRegistry = registryAccess.registryOrThrow(Registry.CONFIGURED_FEATURE_REGISTRY);

        for(var entry : ORE_MAP.entrySet()) {
            ResourceLocation key = new ResourceLocation(Underland.MODID, entry.getKey());
            configuredFeatureMap.put(key, new ConfiguredFeature<>(Feature.ORE, simpleOre(entry.getValue().targetState(), entry.getValue().size())));
            Holder<ConfiguredFeature<?, ?>> holder = configuredFeatureRegistry.getOrCreateHolderOrThrow(ResourceKey.create(Registry.CONFIGURED_FEATURE_REGISTRY, key));
            placedFeatureMap.put(key, new PlacedFeature(holder, entry.getValue().modifiers()));
        }

        generator.addProvider(event.includeServer(), JsonCodecProvider.forDatapackRegistry(
                generator, existingFileHelper, Underland.MODID, registryOps, Registry.CONFIGURED_FEATURE_REGISTRY, configuredFeatureMap));

        generator.addProvider(event.includeServer(), JsonCodecProvider.forDatapackRegistry(
                generator, existingFileHelper, Underland.MODID, registryOps, Registry.PLACED_FEATURE_REGISTRY, placedFeatureMap));

        generator.addProvider(event.includeServer(), new UnderlandOreTagsProvider(generator, existingFileHelper, placedFeatureMap.keySet()));
    }

    public static OreConfiguration simpleOre(BlockState desiredOre, int size) {
        return new OreConfiguration(
                ImmutableList.of(OreConfiguration.target(new TagMatchTest(net.minecraft.tags.BlockTags.DEEPSLATE_ORE_REPLACEABLES), desiredOre)),
                size,
                0f);
    }
}