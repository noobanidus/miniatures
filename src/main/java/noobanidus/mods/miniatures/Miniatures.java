package noobanidus.mods.miniatures;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import noobanidus.libs.noobutil.registrate.CustomRegistrate;
import noobanidus.mods.miniatures.commands.CommandCache;
import noobanidus.mods.miniatures.config.ConfigManager;
import noobanidus.mods.miniatures.entity.MiniMeEntity;
import noobanidus.mods.miniatures.init.*;
import noobanidus.mods.miniatures.setup.ClientInit;
import noobanidus.mods.miniatures.setup.CommonSetup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("miniatures")
public class Miniatures {
  public static final Logger LOG = LogManager.getLogger();
  public static final String MODID = "miniatures";

  public static CustomRegistrate REGISTRATE;

  public Miniatures() {
    ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigManager.COMMON_CONFIG);
    ConfigManager.loadConfig(ConfigManager.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve(Miniatures.MODID + "-common.toml"));
    IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
    modBus.addListener(CommonSetup::init);

    DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientInit::init);

    MinecraftForge.EVENT_BUS.addListener(this::onServerAboutToStart);
    MinecraftForge.EVENT_BUS.addListener(this::onCommandsLoad);

    REGISTRATE = CustomRegistrate.create(MODID);
    ModEntities.load();
    ModTags.load();
    ModSerializers.load();
    ModBlocks.load();
  }

  public void onServerAboutToStart(FMLServerAboutToStartEvent event) {
    MinecraftServer server = event.getServer();
    MiniMeEntity.setProfileCache(server.getProfileCache());
    MiniMeEntity.setSessionService(server.getSessionService());
    PlayerProfileCache.setUsesAuthentication(server.usesAuthentication());
  }

  public void onCommandsLoad (RegisterCommandsEvent event) {
    CommandCache.register(event.getDispatcher());
  }
}
