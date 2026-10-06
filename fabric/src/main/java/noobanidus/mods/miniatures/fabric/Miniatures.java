package noobanidus.mods.miniatures.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.server.MinecraftServer;
import noobanidus.mods.miniatures.common.api.MiniaturesAPI;
import noobanidus.mods.miniatures.common.commands.CommandMiniatures;
import noobanidus.mods.miniatures.common.config.ConfigManager;
import noobanidus.mods.miniatures.common.entity.MaxiMeEntity;
import noobanidus.mods.miniatures.common.entity.MeEntity;
import noobanidus.mods.miniatures.common.entity.MiniMeEntity;
import noobanidus.mods.miniatures.fabric.impl.FabricMiniaturesAPIImpl;
import noobanidus.mods.miniatures.fabric.init.ModBlocks;
import noobanidus.mods.miniatures.fabric.init.ModEntities;
import noobanidus.mods.miniatures.fabric.network.toClient.PacketValidateClient;

public class Miniatures implements ModInitializer {
  public static MinecraftServer serverInstance;

  @Override
  public void onInitialize(){
    MiniaturesAPI.INSTANCE = new FabricMiniaturesAPIImpl();
    ModBlocks.register();
    ModEntities.register();

    ConfigManager.getConfigurator().register(ConfigManager.class);

    PayloadTypeRegistry.clientboundPlay().register(PacketValidateClient.TYPE, PacketValidateClient.STREAM_CODEC);

    FabricDefaultAttributeRegistry.register(ModEntities.ME, MeEntity.attributes());
    FabricDefaultAttributeRegistry.register(ModEntities.MINIME, MiniMeEntity.attributes());
    FabricDefaultAttributeRegistry.register(ModEntities.MAXIME, MaxiMeEntity.attributes());

    ServerLifecycleEvents.SERVER_STARTING.register( server -> {
      serverInstance = server;
    });

    ServerLifecycleEvents.SERVER_STOPPED.register( server -> {
      serverInstance = null;
    });

    CommandRegistrationCallback.EVENT.register((dispatcher, reg, env) -> {
      CommandMiniatures.register(dispatcher);
    });
  }
}
