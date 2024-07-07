package noobanidus.mods.miniatures;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.players.GameProfileCache;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import noobanidus.mods.miniatures.commands.CommandMiniatures;
import noobanidus.mods.miniatures.config.ConfigManager;
import noobanidus.mods.miniatures.entity.MiniMeEntity;
import noobanidus.mods.miniatures.init.ModBlocks;
import noobanidus.mods.miniatures.init.ModEntities;
import noobanidus.mods.miniatures.init.ModSerializers;
import noobanidus.mods.miniatures.network.Networking;
import noobanidus.mods.miniatures.setup.ClientInit;
import noobanidus.mods.miniatures.setup.CommonSetup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("miniatures")
public class Miniatures {
  public static final Logger LOG = LogManager.getLogger();
  public static final String MODID = "miniatures";

  public Miniatures(IEventBus modBus) {
    ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigManager.COMMON_CONFIG);
    ConfigManager.loadConfig(ConfigManager.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve(Miniatures.MODID + "-common.toml"));
    modBus.addListener(CommonSetup::init);
    modBus.addListener(Networking::setupPackets);

    ModEntities.load(modBus);
    ModBlocks.load(modBus);

    ModSerializers.load(modBus);

    if (FMLEnvironment.dist.isClient()) {
      ClientInit.init(modBus);
    }

    NeoForge.EVENT_BUS.addListener(this::onServerAboutToStart);
    NeoForge.EVENT_BUS.addListener(this::onCommandsLoad);
  }

  public void onServerAboutToStart(ServerAboutToStartEvent event) {
    MinecraftServer server = event.getServer();
    MiniMeEntity.setMainThreadExecutor(server);
    MiniMeEntity.setProfileCache(server.getProfileCache());
    MiniMeEntity.setSessionService(server.getSessionService());
    GameProfileCache.setUsesAuthentication(server.usesAuthentication());
  }

  public void onCommandsLoad(RegisterCommandsEvent event) {
    CommandMiniatures.register(event.getDispatcher());
  }
}
