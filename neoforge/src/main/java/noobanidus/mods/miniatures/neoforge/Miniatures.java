package noobanidus.mods.miniatures.neoforge;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import noobanidus.mods.miniatures.common.api.MiniaturesAPI;
import noobanidus.mods.miniatures.neoforge.config.ConfigManager;
import noobanidus.mods.miniatures.neoforge.init.ModBlocks;
import noobanidus.mods.miniatures.neoforge.init.ModEntities;
import noobanidus.mods.miniatures.neoforge.init.ModSerializers;
import noobanidus.mods.miniatures.neoforge.impl.NeoForgeMiniaturesAPIImpl;

@Mod(MiniaturesAPI.MODID)
public class Miniatures {

  public Miniatures(ModContainer container, IEventBus modBus) {
    MiniaturesAPI.INSTANCE = new NeoForgeMiniaturesAPIImpl();
    container.registerConfig(ModConfig.Type.COMMON, ConfigManager.COMMON_CONFIG);

    ModEntities.register(modBus);
    ModBlocks.load(modBus);
    ModSerializers.load(modBus);
  }
}
