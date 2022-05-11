package com.t2pellet.strawgolem.entity.ai;

import com.t2pellet.strawgolem.StrawgolemCommon;
import com.t2pellet.strawgolem.config.StrawgolemConfig;
import com.t2pellet.strawgolem.entity.EntityStrawGolem;
import com.t2pellet.strawgolem.network.GreedyPacket;
import com.t2pellet.strawgolem.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

public class GolemTemptGoal extends TemptGoal {

    private static final double SPEED = 0.8D;

    private final EntityStrawGolem strawGolem;

    public GolemTemptGoal(EntityStrawGolem creatureIn) {
        super(creatureIn, SPEED, Ingredient.of(Items.APPLE), false);
        strawGolem = creatureIn;
    }

    @Override
    public boolean canUse() {
        return !strawGolem.getHunger().isHungry() && !strawGolem.isHarvesting() && super.canUse();
    }

    @Override
    public void start() {
        super.start();
        if (StrawgolemConfig.Miscellaneous.isSoundsEnabled()) strawGolem.playSound(EntityStrawGolem.GOLEM_INTERESTED, 1.0F, 1.0F);
        Services.PACKETS.sendInRange(new GreedyPacket(strawGolem, true), strawGolem, 25.0F);
    }

    @Override
    public void tick() {
        this.mob.getLookControl().setLookAt(this.player, (float)(this.mob.getXRot() + 20), (float)this.mob.getYHeadRot());
        if (this.mob.distanceToSqr(this.player) < 6.25D) {
            this.mob.getNavigation().stop();
        } else {
            this.mob.getNavigation().moveTo(this.player, SPEED * strawGolem.getHunger().getPercentage());
        }
        if (StrawgolemConfig.Tether.isTetherEnabled() && StrawgolemConfig.Tether.doesTemptResetTether()) {
            BlockPos golemPos = strawGolem.blockPosition();
            Level golemWorld = strawGolem.level;
            double d = strawGolem.getTether().distanceTo(golemWorld, golemPos);
            if (d > StrawgolemConfig.Tether.getTetherMaxRange()) {
                StrawgolemCommon.LOG.debug(strawGolem.getId() + " setting new anchor " + golemPos);
                strawGolem.getTether().set(golemWorld, golemPos);
            }
        }
    }

    @Override
    public void stop() {
        super.stop();
        Services.PACKETS.sendInRange(new GreedyPacket(strawGolem, false), strawGolem, 25.0F);
    }
}