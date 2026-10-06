package noobanidus.mods.miniatures.neoforge.setup;

import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import noobanidus.mods.miniatures.common.commands.CommandMiniatures;
import noobanidus.mods.miniatures.common.api.MiniaturesAPI;
import noobanidus.mods.miniatures.common.entity.MiniMeEntity;

@EventBusSubscriber(modid = MiniaturesAPI.MODID)
public class NeoForgeEvents {
  @SubscribeEvent
  public static void onCommandsLoad(RegisterCommandsEvent event) {
    CommandMiniatures.register(event.getDispatcher());
  }
}
