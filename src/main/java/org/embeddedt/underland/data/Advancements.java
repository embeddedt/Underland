package org.embeddedt.underland.data;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.ChangeDimensionTrigger;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.embeddedt.underland.Underland;
import org.embeddedt.underland.advancement.DarknessDamageTrigger;
import org.embeddedt.underland.advancement.PlacedLightEmittingBlockTrigger;
import org.embeddedt.underland.dimensions.Dimensions;

import java.util.function.Consumer;

public class Advancements extends AdvancementProvider {
    public Advancements(DataGenerator generatorIn, ExistingFileHelper fileHelperIn) {
        super(generatorIn, fileHelperIn);
    }

    @Override
    protected void registerAdvancements(Consumer<Advancement> consumer, ExistingFileHelper fileHelper) {
        Advancement root = Advancement.Builder.advancement()
                .display(Underland.TELEPORTER.get(), Component.translatable("advancements.underland.root.title"), Component.translatable("advancements.underland.root.description"), new ResourceLocation("textures/gui/advancements/backgrounds/stone.png"), FrameType.TASK, true, true, false)
                .addCriterion("entered_underland", ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(Dimensions.UNDERLAND))
                .save(consumer, new ResourceLocation(Underland.MODID, "underland/root"), fileHelper);
        Advancement.Builder.advancement().parent(root).display(Blocks.TORCH, Component.translatable("advancements.underland.light.title"), Component.translatable("advancements.underland.light.description"), null, FrameType.TASK, true, true, false)
                .addCriterion("placed_light_block", PlacedLightEmittingBlockTrigger.TriggerInstance.placedLightBlock(EntityPredicate.Composite.ANY))
                .save(consumer, new ResourceLocation(Underland.MODID, "underland/light"), fileHelper);
        Advancement.Builder.advancement().parent(root).display(Items.DIAMOND_SWORD, Component.translatable("advancements.underland.damage.title"), Component.translatable("advancements.underland.damage.description"), null, FrameType.TASK, true, true, false)
                .addCriterion("took_darkness_damage", DarknessDamageTrigger.TriggerInstance.tookDamage(EntityPredicate.Composite.ANY))
                .save(consumer, new ResourceLocation(Underland.MODID, "underland/damage"), fileHelper);
    }
}
