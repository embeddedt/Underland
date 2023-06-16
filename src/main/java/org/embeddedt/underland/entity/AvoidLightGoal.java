package org.embeddedt.underland.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class AvoidLightGoal extends Goal {
    private final PathfinderMob mob;
    @Nullable
    protected Path path;
    protected final PathNavigation pathNav;

    public AvoidLightGoal(PathfinderMob mob) {
        this.mob = mob;
        this.pathNav = mob.getNavigation();
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }
    @Override
    public boolean canUse() {
        int brightness = this.mob.level.getBrightness(LightLayer.BLOCK, new BlockPos(this.mob.position()));
        if(brightness == 0)
            return false;
        else {
            Vec3 vec3 = DefaultRandomPos.getPosAway(this.mob, 16, 7, this.mob.position());
            if(vec3 == null)
                return false;
            else if(this.mob.level.getBrightness(LightLayer.BLOCK, new BlockPos(vec3)) > brightness)
                return false;
            else {
                this.path = this.pathNav.createPath(vec3.x, vec3.y, vec3.z, 0);
                return this.path != null;
            }
        }
    }

    public boolean canContinueToUse() {
        return !this.pathNav.isDone();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void start() {
        this.pathNav.moveTo(this.path, 0.5);
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void stop() {
    }
}
