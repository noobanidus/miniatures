package noobanidus.mods.miniatures.neoforge.impl;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.Level;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import noobanidus.mods.miniatures.common.api.IMiniaturesAPI;
import noobanidus.mods.miniatures.neoforge.config.ConfigManager;
import noobanidus.mods.miniatures.neoforge.init.ModSerializers;
import noobanidus.mods.miniatures.neoforge.network.ClientboundValidateCachePacket;

import java.nio.file.Path;
import java.util.Optional;

public class NeoForgeMiniaturesAPIImpl implements IMiniaturesAPI {
  @Override
  public MinecraftServer getServer() {
    return ServerLifecycleHooks.getCurrentServer();
  }

  @Override
  public boolean shouldSkipNullCheck() {
    return ConfigManager.shouldSkipNullCheck();
  }

  @Override
  public boolean getHostile() {
    return ConfigManager.getHostile();
  }

  @Override
  public boolean getImmune() {
    return ConfigManager.getImmune();
  }

  @Override
  public boolean getDestroysBlocks() {
    return ConfigManager.getDestroysBlocks();
  }

  @Override
  public boolean getBreaksBlocks() {
    return ConfigManager.getDestroysBlocks();
  }

  @Override
  public boolean getDoesPickup() {
    return ConfigManager.getDoesPickup();
  }

  @Override
  public boolean getOwnerRider() {
    return ConfigManager.getOwnerRider();
  }

  @Override
  public double getDistractionValue() {
    return ConfigManager.getDistractionValue();
  }

  @Override
  public int getBaseRunDelay() {
    return ConfigManager.getBaseRunDelay();
  }

  @Override
  public int getRandomRunDelay() {
    return ConfigManager.getRandomRunDelay();
  }

  @Override
  public EntityDataSerializer<Optional<ResolvableProfile>> getGameProfileSerializer() {
    return ModSerializers.OPTIONAL_RESOLVABLE_PROFILE.get();
  }

  @Override
  public boolean canEntityDestroy(Level level, BlockPos blockPos, Mob entity) {
    return CommonHooks.canEntityDestroy(level, blockPos, entity);
  }

  @Override
  public Path getGameDir() {
    return FMLPaths.GAMEDIR.get();
  }

  @Override
  public void sendValidatePacket(ServerPlayer player) {
    PacketDistributor.sendToPlayer(player, new ClientboundValidateCachePacket());
  }
}
