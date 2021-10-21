package noobanidus.mods.miniatures.entity.ai;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.world.World;
import noobanidus.mods.miniatures.config.ConfigManager;
import noobanidus.mods.miniatures.entity.MiniMeEntity;

import java.util.EnumSet;
import java.util.List;

public class PickupPlayerGoal extends Goal {

  private MiniMeEntity minime;
  private Path path;
  private PlayerEntity targetPlayer;

  public PickupPlayerGoal(MiniMeEntity entity) {
    this.minime = entity;
    this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
  }

  @Override
  public boolean canUse() {
    if (!ConfigManager.getDoesPickup() || this.minime.getHostile()) {
      return false;
    }
    if (minime.getPickupCooldown() > 0) {
      return false;
    }
    if (path != null) {
      return false;
    }
    if (targetPlayer == null || targetPlayer.isAlive()) {
      List<PlayerEntity> players = minime.level.getEntitiesOfClass(PlayerEntity.class, minime.getBoundingBox().inflate(10));
      for (PlayerEntity player : players) {
        if (player.isPassenger()) {
          continue;
        }
        if (!ConfigManager.getOwnerRider() || canRidePlayer(player)) {
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
    return targetPlayer != null && !targetPlayer.isPassenger() && ConfigManager.getDoesPickup() && !minime.getHostile() && minime.isAlive() && path != null && (!ConfigManager.getOwnerRider() || canRidePlayer(targetPlayer));
  }

  @Override
  public void start() {
    minime.getNavigation().moveTo(this.path, 1);
  }

  @Override
  public void tick() {
    super.tick();
    if (!minime.level.isClientSide && (!ConfigManager.getOwnerRider() || canRidePlayer(targetPlayer))) {
      if (minime.distanceTo(targetPlayer) < 1.5) {
        targetPlayer.startRiding(minime);
      }
    }
  }

  private boolean canRidePlayer(PlayerEntity player) {
    if (!ConfigManager.getOwnerRider()) {
      return true;
    }
    final GameProfile owner = minime.getGameProfile().orElse(null);
    return owner != null && player != null && player.getGameProfile().getId().equals(owner.getId());
  }
}
