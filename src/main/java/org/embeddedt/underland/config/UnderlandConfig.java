package org.embeddedt.underland.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class UnderlandConfig {
    private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
    public static ForgeConfigSpec COMMON_CONFIG;

    public static ForgeConfigSpec.DoubleValue DAMAGE_MULTIPLIER;
    public static ForgeConfigSpec.DoubleValue HEALTH_MULTIPLIER;
    public static ForgeConfigSpec.BooleanValue TELEPORTER_TORCHES;
    public static ForgeConfigSpec.BooleanValue ADDITIONAL_DIMENSION_TELEPORT;
    public static ForgeConfigSpec.IntValue DARKNESS_DAMAGE;
    public static ForgeConfigSpec.IntValue DARKNESS_DAMAGE_TIMER;

    static {
        ADDITIONAL_DIMENSION_TELEPORT = COMMON_BUILDER
                .comment("Whether players can teleport between the Underland and dimensions that aren't the Overworld")
                .define("additional_dimension_teleportation", true);
        DAMAGE_MULTIPLIER = COMMON_BUILDER
                .comment("Entities that spawn in this dimension have their attack damage multiplied by this amount")
                .defineInRange("damage_multiplier", 2, 0.5, Double.MAX_VALUE);
        HEALTH_MULTIPLIER = COMMON_BUILDER
                .comment("Entities that spawn in this dimension have their health multiplied by this amount")
                .defineInRange("health_multiplier", 2, 0.5, Double.MAX_VALUE);
        TELEPORTER_TORCHES = COMMON_BUILDER
                .comment("Whether the teleporter should spawn torches on its platform")
                .define("teleporter_torches", true);
        DARKNESS_DAMAGE = COMMON_BUILDER
                .comment("Amount of damage to take from being in darkness in the Underland. Default is a full heart.")
                .defineInRange("darkness_damage", 2, 0, 20);
        DARKNESS_DAMAGE_TIMER = COMMON_BUILDER
                .comment("Number of seconds between taking darkness damage")
                .defineInRange("darkness_damage_timer", 5, 1, 10);
        COMMON_CONFIG = COMMON_BUILDER.build();
    }

}
