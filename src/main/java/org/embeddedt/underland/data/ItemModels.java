package org.embeddedt.underland.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.embeddedt.underland.Underland;

public class ItemModels extends ItemModelProvider {

    public ItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Underland.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        withExistingParent(Underland.TELEPORTER.getId().getPath(), modLoc("block/teleporter"));
        withExistingParent(Underland.SHADOW_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
    }
}
