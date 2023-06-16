package org.embeddedt.underland.advancement;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.embeddedt.underland.Underland;

public class DarknessDamageTrigger extends SimpleCriterionTrigger<DarknessDamageTrigger.TriggerInstance>  {
    private static final ResourceLocation ID = new ResourceLocation(Underland.MODID, "darkness_damage");

    public void trigger(ServerPlayer pPlayer) {
        this.trigger(pPlayer, (p) -> true);
    }

    @Override
    protected DarknessDamageTrigger.TriggerInstance createInstance(JsonObject pJson, EntityPredicate.Composite pPlayer, DeserializationContext pContext) {
        return new TriggerInstance(pPlayer);
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        public TriggerInstance(EntityPredicate.Composite pPlayer) {
            super(ID, pPlayer);
        }

        public static TriggerInstance tookDamage(EntityPredicate.Composite pPlayer) {
            return new TriggerInstance(pPlayer);
        }
    }
}
