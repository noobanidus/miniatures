package noobanidus.mods.miniatures.neoforge.setup;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.players.GameProfileCache;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import noobanidus.mods.miniatures.common.commands.CommandMiniatures;
import noobanidus.mods.miniatures.common.api.MiniaturesAPI;
import noobanidus.mods.miniatures.common.entity.MiniMeEntity;

@EventBusSubscriber(modid = MiniaturesAPI.MODID, bus = EventBusSubscriber.Bus.GAME)
public class NeoForgeEvents {

  @SubscribeEvent
  public static void onServerAboutToStart(ServerAboutToStartEvent event) {
    MinecraftServer server = event.getServer();
    MiniMeEntity.setup(server.services);
    GameProfileCache.setUsesAuthentication(server.usesAuthentication());
  }

  @SubscribeEvent
  public static void onServerAboutToStop (ServerStoppedEvent event) {
    MiniMeEntity.clear();
  }

  @SubscribeEvent
  public static void onCommandsLoad(RegisterCommandsEvent event) {
    CommandMiniatures.register(event.getDispatcher());
  }
}
