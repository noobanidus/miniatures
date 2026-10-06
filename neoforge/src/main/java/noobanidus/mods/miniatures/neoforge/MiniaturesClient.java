package noobanidus.mods.miniatures.neoforge;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import noobanidus.mods.miniatures.common.api.MiniaturesAPI;

@Mod(value = MiniaturesAPI.MODID, dist = Dist.CLIENT)
public class MiniaturesClient {
  public MiniaturesClient(ModContainer container, IEventBus modBus) {

  }
}
