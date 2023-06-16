package org.embeddedt.underland.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;
import org.embeddedt.underland.Underland;

import java.util.function.Consumer;

public class Recipes extends RecipeProvider {
    public Recipes(PackOutput generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, Underland.TELEPORTER_ITEM.get())
                .pattern("#&#")
                .pattern("&%&")
                .pattern("#&#")
                .define('#', Items.STONE)
                .define('&', ItemTags.STONE_TOOL_MATERIALS)
                .define('%', Items.NETHER_STAR)
                .unlockedBy("criteria", RecipeProvider.has(Items.NETHER_STAR))
                .save(consumer);
    }
}
