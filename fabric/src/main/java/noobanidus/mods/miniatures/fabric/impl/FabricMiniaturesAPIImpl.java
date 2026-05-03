package noobanidus.mods.miniatures.fabric.impl;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import noobanidus.mods.miniatures.common.api.IMiniaturesAPI;
import noobanidus.mods.miniatures.fabric.Miniatures;
import noobanidus.mods.miniatures.fabric.init.ModSerializers;
import noobanidus.mods.miniatures.fabric.network.toClient.PacketValidateClient;

import java.nio.file.Path;
import java.util.Optional;

public class FabricMiniaturesAPIImpl implements IMiniaturesAPI {
  @Override
  public MinecraftServer getServer() {
    return Miniatures.serverInstance;
  }

  @Override
  public EntityDataSerializer<Optional<ResolvableProfile>> getGameProfileSerializer() {
    return ModSerializers.RESOLVABLE_PROFILE;
  }

  @Override
  public boolean canEntityDestroy(ServerLevel level, BlockPos blockPos, Mob entity) {
    return level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
  }

  @Override
  public Path getGameDir() {
    return FabricLoader.getInstance().getGameDir();
  }

  @Override
  public void sendValidatePacket(ServerPlayer player) {
    ServerPlayNetworking.send(player, new PacketValidateClient());
  }
}
