package noobanidus.mods.miniatures.common.api;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.util.Optional;

public class MiniaturesAPI {
  public static final Logger LOG = LogManager.getLogger();
  public static final String MODID = "miniatures";
  public static final String NETWORK_VERSION = "miniatures-v1.21.1-0";

  public static final ResourceKey<EntityType<?>> MINIME = ResourceKey.create(Registries.ENTITY_TYPE, MiniaturesAPI.rl("minime"));
  public static final ResourceKey<EntityType<?>> ME = ResourceKey.create(Registries.ENTITY_TYPE, MiniaturesAPI.rl("me"));
  public static final ResourceKey<EntityType<?>> MAXIME = ResourceKey.create(Registries.ENTITY_TYPE, MiniaturesAPI.rl("maxime"));


  public static IMiniaturesAPI INSTANCE;

  public static MinecraftServer getServer() {
    return INSTANCE.getServer();
  }

  public static ServerLevel getOverworld() {
    return INSTANCE.getOverworld();
  }

  public static boolean shouldSkipNullCheck () {
    return INSTANCE.shouldSkipNullCheck();
  }

  public static boolean getHostile () {
    return INSTANCE.getHostile();
  }

  public static boolean getImmune () {
    return INSTANCE.getImmune();
  }

  public static boolean getDestroysBlocks () {
    return INSTANCE.getDestroysBlocks();
  }

  public static boolean getBreaksBlocks () {
    return INSTANCE.getBreaksBlocks();
  }

  public static boolean getDoesPickup () {
    return INSTANCE.getDoesPickup();
  }

  public static boolean getOwnerRider () {
    return INSTANCE.getOwnerRider();
  }

  public static double getDistractionValue () {
    return INSTANCE.getDistractionValue();
  }

  public static int getBaseRunDelay () {
    return INSTANCE.getBaseRunDelay();
  }

  public static int getRandomRunDelay () {
    return INSTANCE.getRandomRunDelay();
  }




  public static EntityDataSerializer<Optional<ResolvableProfile>> getGameProfileSerializer() {
    return INSTANCE.getGameProfileSerializer();
  }

  public static ResourceLocation rl(String path) {
    return ResourceLocation.fromNamespaceAndPath(MODID, path);
  }

  public static boolean canEntityDestroy(ServerLevel level, BlockPos blockPos, Mob entity) {
    return INSTANCE.canEntityDestroy(level, blockPos, entity);
  }

  public static Path getGameDir () {
    return INSTANCE.getGameDir();
  }

  public static void sendValidatePacket (ServerPlayer player) {
    INSTANCE.sendValidatePacket(player);
  }
}
