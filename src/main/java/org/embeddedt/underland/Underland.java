package org.embeddedt.underland;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.embeddedt.underland.advancement.DarknessDamageTrigger;
import org.embeddedt.underland.advancement.PlacedLightEmittingBlockTrigger;
import org.embeddedt.underland.biome_modifier.UnderlandOreBiomeModifier;
import org.embeddedt.underland.blocks.TeleporterBlock;
import org.embeddedt.underland.blocks.TeleporterBlockEntity;
import org.embeddedt.underland.config.UnderlandConfig;
import org.embeddedt.underland.entity.ShadowEntity;

@Mod(Underland.MODID)
public class Underland {
    public static final String MODID = "underland";

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, MODID);

    public static final String TAB_NAME = "underland";


    public static final RegistryObject<Block> TELEPORTER = BLOCKS.register("teleporter", () -> new TeleporterBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(3.0F)));

    public static final RegistryObject<BlockEntityType<TeleporterBlockEntity>> TELEPORTER_BE = BLOCK_ENTITIES.register("teleporter", () -> BlockEntityType.Builder.of(TeleporterBlockEntity::new, TELEPORTER.get()).build(null));

    public static final RegistryObject<Item> TELEPORTER_ITEM = ITEMS.register("teleporter", () -> new BlockItem(TELEPORTER.get(), new Item.Properties()));

    public static final RegistryObject<EntityType<ShadowEntity>> SHADOW = ENTITIES.register("shadow", () -> EntityType.Builder.of(ShadowEntity::new, MobCategory.MONSTER)
            .sized(0.6f, 1.95f)
            .clientTrackingRange(8)
            .setShouldReceiveVelocityUpdates(false)
            .build("shadow"));

    public static final RegistryObject<Item> SHADOW_EGG = ITEMS.register("shadow_spawn_egg", () -> new ForgeSpawnEggItem(SHADOW, 0xff0000, 0x00ff00, new Item.Properties()));

    public static RegistryObject<Codec<UnderlandOreBiomeModifier>> UNDERLAND_ORE_CODEC = Underland.BIOME_MODIFIER_SERIALIZERS.register("underland_ore", () ->
            RecordCodecBuilder.create(builder -> builder.group(
                    // declare fields
                    Biome.CODEC.fieldOf("biome").forGetter(UnderlandOreBiomeModifier::targetBiome),
                    PlacedFeature.LIST_CODEC.fieldOf("features").forGetter(UnderlandOreBiomeModifier::oreFeatures)
                    // declare constructor
            ).apply(builder, UnderlandOreBiomeModifier::new)));

    public static final RegistryObject<CreativeModeTab> ITEM_GROUP = CREATIVE_TABS.register(TAB_NAME, () -> CreativeModeTab.builder()
            .icon(() -> TELEPORTER_ITEM.get().getDefaultInstance())
            .displayItems((flags, output) -> {
                output.accept(TELEPORTER_ITEM.get());
                output.accept(SHADOW_EGG.get());
            })
            .build());

    public static DarknessDamageTrigger DARKNESS_DAMAGE_TRIGGER;
    public static PlacedLightEmittingBlockTrigger PLACED_LIGHT_EMITTING_BLOCK_TRIGGER;

    public static ResourceKey<DamageType> DARKNESS = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(Underland.MODID, "darkness"));

    public Underland() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
        BIOME_MODIFIER_SERIALIZERS.register(modEventBus);
        ENTITIES.register(modEventBus);
        CREATIVE_TABS.register(modEventBus);

        modEventBus.register(this);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, UnderlandConfig.COMMON_CONFIG);
    }

    @SubscribeEvent
    public void onAttributeCreate(EntityAttributeCreationEvent event) {
        event.put(SHADOW.get(), ShadowEntity.prepareAttributes().build());
    }

    @SubscribeEvent
    public void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            CriteriaTriggers.register(DARKNESS_DAMAGE_TRIGGER = new DarknessDamageTrigger());
            CriteriaTriggers.register(PLACED_LIGHT_EMITTING_BLOCK_TRIGGER = new PlacedLightEmittingBlockTrigger());
        });
    }
}
