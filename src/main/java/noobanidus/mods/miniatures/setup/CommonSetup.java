package noobanidus.mods.miniatures.setup;

import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import noobanidus.mods.miniatures.init.ModSerializers;

public class CommonSetup {
  public static void init(FMLCommonSetupEvent event) {
    event.enqueueWork(() -> {
    });
  }
}
