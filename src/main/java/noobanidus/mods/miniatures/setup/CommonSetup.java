package noobanidus.mods.miniatures.setup;

import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import noobanidus.mods.miniatures.init.ModSerializers;
import noobanidus.mods.miniatures.network.Networking;

public class CommonSetup {
  public static void init(FMLCommonSetupEvent event) {
    event.enqueueWork(() -> {
      Networking.INSTANCE.registerMessages();
    });
  }
}
