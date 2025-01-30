package noobanidus.mods.miniatures.neoforge.setup;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import noobanidus.mods.miniatures.common.api.MiniaturesAPI;

@EventBusSubscriber(modid = MiniaturesAPI.MODID, bus = EventBusSubscriber.Bus.MOD)
public class CommonSetup {
  @SubscribeEvent
  public static void init(FMLCommonSetupEvent event) {
    event.enqueueWork(() -> {

    });
  }
}
