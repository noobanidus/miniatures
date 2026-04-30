package noobanidus.mods.miniatures.common.api;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.Level;
import noobanidus.mods.miniatures.common.config.ConfigManager;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.Optional;

public interface IMiniaturesAPI {

  MinecraftServer getServer ();

  @Nullable
  default ServerLevel getOverworld() {
    MinecraftServer server = getServer();
    if (server == null) {
      return null;
    }
    return server.getLevel(Level.OVERWORLD);
  }

  // From configuration
  default boolean shouldSkipNullCheck () {
    return ConfigManager.shouldSkipNullCheck();
  }
  default boolean getHostile () {
    return ConfigManager.getHostile();
  }
  default boolean getImmune() {
    return ConfigManager.getImmune();
  }
  default boolean getDestroysBlocks() {
    return ConfigManager.getDestroysBlocks();
  }
  default boolean getBreaksBlocks() {
    return ConfigManager.getBreaksBlocks();
  }
  default boolean getDoesPickup() {
      return ConfigManager.getDoesPickup();
  }
  default boolean getOwnerRider() {
    return ConfigManager.getOwnerRider();
  }
  default double getDistractionValue() {
    return ConfigManager.getDistractionValue();
  }
  default int getBaseRunDelay() {
    return ConfigManager.getBaseRunDelay();
  }
  default int getRandomRunDelay() {
    return ConfigManager.getRandomRunDelay();
  }

  EntityDataSerializer<Optional<ResolvableProfile>> getGameProfileSerializer ();

  boolean canEntityDestroy(Level level, BlockPos blockPos, Mob entity);

  Path getGameDir();

  void sendValidatePacket(ServerPlayer player);

  default boolean isMini (Entity entity) {
    return entity.getType().is(MiniTags.Entity.MINI);
  }
}
