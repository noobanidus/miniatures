package noobanidus.mods.miniatures.common.api;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.Level;
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
  boolean shouldSkipNullCheck ();
  boolean getHostile ();
  boolean getImmune();
  boolean getDestroysBlocks();
  boolean getBreaksBlocks();
  boolean getDoesPickup();
  boolean getOwnerRider();
  double getDistractionValue();
  int getBaseRunDelay();
  int getRandomRunDelay();

  EntityDataSerializer<Optional<ResolvableProfile>> getGameProfileSerializer ();

  boolean canEntityDestroy(Level level, BlockPos blockPos, Mob entity);

  Path getGameDir();

  void sendValidatePacket(ServerPlayer player);
}
