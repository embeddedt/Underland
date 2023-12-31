package org.embeddedt.underland.advancement;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.embeddedt.underland.Underland;

public class PlacedLightEmittingBlockTrigger extends SimpleCriterionTrigger<PlacedLightEmittingBlockTrigger.TriggerInstance> {
    private static final ResourceLocation ID = new ResourceLocation(Underland.MODID, "placed_light_emitting_block");

    @Override
    protected TriggerInstance createInstance(JsonObject pJson, ContextAwarePredicate pPlayer, DeserializationContext pContext) {
        return new TriggerInstance(pPlayer);
    }

    public void trigger(ServerPlayer pPlayer, BlockPos pPos, BlockState blockstate) {
        this.trigger(pPlayer, (p_59481_) -> {
            return p_59481_.matches(blockstate, pPos, pPlayer.serverLevel());
        });
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        public TriggerInstance(ContextAwarePredicate pPlayer) {
            super(ID, pPlayer);
        }

        public static TriggerInstance placedLightBlock(ContextAwarePredicate pPlayer) {
            return new TriggerInstance(pPlayer);
        }

        public boolean matches(BlockState pState, BlockPos pPos, ServerLevel pLevel) {
            return pState.getLightEmission(pLevel, pPos) > 0;
        }
    }
}
