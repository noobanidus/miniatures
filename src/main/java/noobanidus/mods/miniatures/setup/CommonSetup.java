package noobanidus.mods.miniatures.setup;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import noobanidus.mods.miniatures.network.Networking;

public class CommonSetup {
  public static void init(FMLCommonSetupEvent event) {
    event.enqueueWork(() -> {
      Networking.init();
    });
  }
}
