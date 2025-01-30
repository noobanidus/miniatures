package noobanidus.mods.miniatures.common.entity.ai;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.pathfinder.Path;
import noobanidus.mods.miniatures.common.api.MiniaturesAPI;
import noobanidus.mods.miniatures.common.entity.MiniMeEntity;

import java.util.EnumSet;
import java.util.List;

public class PickupPlayerGoal extends Goal {

  private final MiniMeEntity minime;
  private Path path;
  private Player targetPlayer;

  public PickupPlayerGoal(MiniMeEntity entity) {
    this.minime = entity;
    this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
  }

  @Override
  public boolean canUse() {
    if (!MiniaturesAPI.getDoesPickup() || this.minime.getHostile()) {
      return false;
    }
    if (minime.getPickupCooldown() > 0) {
      return false;
    }
    if (path != null) {
      return false;
    }
    if (targetPlayer == null || targetPlayer.isAlive()) {
      List<Player> players = minime.level().getEntitiesOfClass(Player.class, minime.getBoundingBox().inflate(10));
      for (Player player : players) {
        if (player.isPassenger()) {
          continue;
        }
        if (!MiniaturesAPI.getOwnerRider() || canRidePlayer(player)) {
          targetPlayer = player;
          path = minime.getNavigation().createPath(targetPlayer, 0);
          return path != null;
        }
      }
    }
    return false;
  }

  @Override
  public void stop() {
    path = null;
    targetPlayer = null;
    minime.getNavigation().stop();
  }

  @Override
  public boolean canContinueToUse() {
    return targetPlayer != null && !targetPlayer.isPassenger() && MiniaturesAPI.getDoesPickup() && !minime.getHostile() && minime.isAlive() && path != null && (!MiniaturesAPI.getOwnerRider() || canRidePlayer(targetPlayer));
  }

  @Override
  public void start() {
    minime.getNavigation().moveTo(this.path, 1);
  }

  @Override
  public void tick() {
    super.tick();
    if (!minime.level().isClientSide && (!MiniaturesAPI.getOwnerRider() || canRidePlayer(targetPlayer))) {
      if (minime.distanceTo(targetPlayer) < 1.5) {
        targetPlayer.startRiding(minime);
      }
    }
  }

  private boolean canRidePlayer(Player player) {
    if (!MiniaturesAPI.getOwnerRider()) {
      return true;
    }
    final ResolvableProfile owner = minime.getGameProfile().orElse(null);
    return owner != null && player != null && player.getGameProfile().getId().equals(owner.gameProfile().getId());
  }
}
