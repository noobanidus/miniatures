package noobanidus.mods.miniatures.setup;

import net.neoforged.bus.api.IEventBus;

public class ClientInit {
  public static void init(IEventBus modBus) {
    modBus.addListener(ClientSetup::init);
    modBus.addListener(ClientSetup::registerEntityRenders);
    modBus.addListener(ClientSetup::registerLayerDefinitions);
  }
}
