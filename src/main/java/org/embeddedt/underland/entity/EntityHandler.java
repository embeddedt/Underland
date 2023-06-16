package org.embeddedt.underland.entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.VanillaGameEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.embeddedt.underland.Underland;
import org.embeddedt.underland.advancement.DarknessDamageTrigger;
import org.embeddedt.underland.config.UnderlandConfig;
import org.embeddedt.underland.dimensions.Dimensions;

import java.util.UUID;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EntityHandler {
    private static final UUID HEALTH_BOOST_UUID = UUID.fromString("564ddc1d-5513-4f0f-963a-a62009b0b6d6");
    private static final UUID DAMAGE_BOOST_UUID = UUID.fromString("9412b6b8-9419-4e26-bce0-146e1ecae9cc");

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onEntitySpawn(EntityJoinLevelEvent event) {
        if(event.isCanceled())
            return;
        if(event.getLevel() instanceof ServerLevel sLevel) {
            if(!event.getEntity().getPersistentData().contains("UnderlandChecked")) {
                event.getEntity().getPersistentData().putBoolean("UnderlandChecked", true);
                if(event.getEntity() instanceof Mob entity && sLevel.dimension() == Dimensions.UNDERLAND) {
                    AttributeModifier health = new AttributeModifier(HEALTH_BOOST_UUID, "Underland health boost", UnderlandConfig.HEALTH_MULTIPLIER.get() - 1, AttributeModifier.Operation.MULTIPLY_TOTAL);
                    AttributeModifier damage = new AttributeModifier(DAMAGE_BOOST_UUID, "Underland damage boost", UnderlandConfig.DAMAGE_MULTIPLIER.get() - 1, AttributeModifier.Operation.MULTIPLY_TOTAL);
                    AttributeInstance healthAttribute = entity.getAttribute(Attributes.MAX_HEALTH);
                    AttributeInstance damageAttribute = entity.getAttribute(Attributes.ATTACK_DAMAGE);
                    // detect already having buffed the entity
                    if(healthAttribute == null || damageAttribute == null || healthAttribute.hasModifier(health))
                        return;

                    healthAttribute.removeModifier(health);
                    healthAttribute.addPermanentModifier(health);
                    damageAttribute.removeModifier(damage);
                    damageAttribute.addPermanentModifier(damage);
                    entity.setHealth(entity.getMaxHealth());
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingUpdate(TickEvent.PlayerTickEvent event) {
        int desiredDamage = UnderlandConfig.DARKNESS_DAMAGE.get();
        if(desiredDamage == 0 || event.player.level().isClientSide || event.player.isCreative() || event.player.level().dimension() != Dimensions.UNDERLAND)
            return;
        if(event.player.getLightLevelDependentMagicValue() <= 0.05f && (event.player.tickCount % (UnderlandConfig.DARKNESS_DAMAGE_TIMER.get() * 20)) == 0) {
            event.player.hurt(new DamageSource(event.player.level().holderLookup(Registries.DAMAGE_TYPE).getOrThrow(Underland.DARKNESS)), desiredDamage);
            Underland.DARKNESS_DAMAGE_TRIGGER.trigger((ServerPlayer)event.player);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        if(event.getEntity() instanceof ServerPlayer player) {
            Underland.PLACED_LIGHT_EMITTING_BLOCK_TRIGGER.trigger(player, event.getPos(), event.getPlacedBlock());
        }
    }
}
