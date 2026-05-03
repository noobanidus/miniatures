package noobanidus.mods.miniatures.neoforge.impl;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.Level;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import noobanidus.mods.miniatures.common.api.IMiniaturesAPI;
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
  public EntityDataSerializer<Optional<ResolvableProfile>> getGameProfileSerializer() {
    return ModSerializers.OPTIONAL_RESOLVABLE_PROFILE.get();
  }

  @Override
  public boolean canEntityDestroy(ServerLevel level, BlockPos blockPos, Mob entity) {
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
