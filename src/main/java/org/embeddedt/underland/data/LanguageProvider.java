package org.embeddedt.underland.data;

import net.minecraft.data.DataGenerator;
import org.embeddedt.underland.Underland;

public class LanguageProvider extends net.minecraftforge.common.data.LanguageProvider {

    public LanguageProvider(DataGenerator gen, String locale) {
        super(gen, Underland.MODID, locale);
    }

    @Override
    protected void addTranslations() {
        add("itemGroup." + Underland.TAB_NAME, "Underland");
        add(Underland.TELEPORTER.get(), "Teleporter");
        add(Underland.SHADOW.get(), "Shadow");
        add(Underland.SHADOW_EGG.get(), "Shadow Spawn Egg");
        add("death.attack." + Underland.DARKNESS.getMsgId(), "%1$s spent too long in darkness");
        add("death.attack." + Underland.DARKNESS.getMsgId() + ".player", "%1$s spent too long in the darkness of %2$s");
        add("advancements.underland.root.title", "It's Dark In Here...");
        add("advancements.underland.root.description", "Enter the Underland");
        add("advancements.underland.damage.title", "Watch Your Step");
        add("advancements.underland.damage.description", "Take damage from standing in darkness too long");
        add("advancements.underland.light.title", "Drive Out The Darkness");
        add("advancements.underland.light.description", "Place your first light source in the Underland");
        add("underland.teleporter_out_of_range", "Teleporter is too high or too low");
    }
}

