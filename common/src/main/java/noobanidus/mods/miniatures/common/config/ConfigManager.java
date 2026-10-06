package noobanidus.mods.miniatures.common.config;

import com.teamresourceful.resourcefulconfig.api.annotations.Comment;
import com.teamresourceful.resourcefulconfig.api.annotations.Config;
import com.teamresourceful.resourcefulconfig.api.annotations.ConfigEntry;
import com.teamresourceful.resourcefulconfig.api.annotations.ConfigInfo;
import com.teamresourceful.resourcefulconfig.api.annotations.ConfigOption;
import com.teamresourceful.resourcefulconfig.api.loader.Configurator;
import noobanidus.mods.miniatures.common.api.MiniaturesAPI;

@Config("miniatures-common")
@ConfigInfo(
    title = "Miniatures",
    titleTranslation = "miniatures.config.title",
    description = "Options relating to miniatures",
    descriptionTranslation = "miniatures.config.desc"
)
public final class ConfigManager {
  private static Configurator configurator;

  public static Configurator getConfigurator() {
    if (configurator == null) {
      configurator = new Configurator(MiniaturesAPI.MODID);
    }
    return configurator;
  }

  public static void register() {
    getConfigurator().register(ConfigManager.class);
  }

  @ConfigEntry(id = "hostile", translation = "miniatures.config.hostile")
  @Comment(value = "Whether or not miniatures are hostile to players. [default: false]", translation = "miniatures.config.hostile.desc")
  public static boolean hostile = false;

  @ConfigEntry(id = "non_player_immune", translation = "miniatures.config.non_player_immune")
  @Comment(value = "Whether or not miniatures are immune to damage that does not originate from a player. [default: true]", translation = "miniatures.config.non_player_immune.desc")
  public static boolean nonPlayerImmune = true;

  @ConfigEntry(id = "breaks_blocks", translation = "miniatures.config.breaks_blocks")
  @Comment(value = "Whether or not miniatures will break blocks in the default tag (miniatures:break_blocks). [default: true]", translation = "miniatures.config.breaks_blocks.desc")
  public static boolean breaksBlocks = true;

  @ConfigEntry(id = "distraction_chance", translation = "miniatures.config.distraction_chance")
  @Comment(value = "The percentage chance per tick that a miniature will get distracted from breaking a block (0 for no distraction). [default: 0.05]", translation = "miniatures.config.distraction_chance.desc")
  @ConfigOption.Range(min = 0, max = Double.MAX_VALUE)
  public static double distractionChance = 0.05;

  @ConfigEntry(id = "base_run_delay", translation = "miniatures.config.base_run_delay")
  @Comment(value = "The minimum delay in ticks before a miniature begins running to a block. [default: 200]", translation = "miniatures.config.base_run_delay.desc")
  @ConfigOption.Range(min = 0, max = Integer.MAX_VALUE)
  public static int baseRunDelay = 200;

  @ConfigEntry(id = "random_run_delay", translation = "miniatures.config.random_run_delay")
  @Comment(value = "The maximum value (0 to value-1) added to the run delay. [default: 200]", translation = "miniatures.config.random_run_delay.desc")
  @ConfigOption.Range(min = 0, max = Integer.MAX_VALUE)
  public static int randomRunDelay = 200;

  @ConfigEntry(id = "destroys_blocks", translation = "miniatures.config.destroys_blocks")
  @Comment(value = "Whether blocks in the default tag (miniatures:break_blocks) are destroyed (true) or dropped when broken (false). [default: false]", translation = "miniatures.config.destroys_blocks.desc")
  public static boolean destroysBlocks = false;

  @ConfigEntry(id = "pickup_goal", translation = "miniatures.config.pickup_goal")
  @Comment(value = "Whether or not non-hostile miniatures will try to pick up players. [default: true]", translation = "miniatures.config.pickup_goal.desc")
  public static boolean pickupGoal = true;

  @ConfigEntry(id = "owner_rider", translation = "miniatures.config.owner_rider")
  @Comment(value = "If true, a miniature will only try to pick up its owner; if false, it will pick up any player. [default: false]", translation = "miniatures.config.owner_rider.desc")
  public static boolean ownerRider = false;

  @ConfigEntry(id = "skip_null_check", translation = "miniatures.config.skip_null_check")
  @Comment(value = "If true, the null profile cache is not consulted, which may cause lag when miniatures with non-existent skins are spawned. [default: false]", translation = "miniatures.config.skip_null_check.desc")
  public static boolean skipNullCheck = false;

  public static boolean getHostile() { return hostile; }
  public static boolean getImmune() { return nonPlayerImmune; }
  public static boolean getDestroysBlocks() { return destroysBlocks; }
  public static boolean getBreaksBlocks() { return breaksBlocks; }
  public static boolean getDoesPickup() { return pickupGoal; }
  public static boolean getOwnerRider() { return ownerRider; }
  public static int getRandomRunDelay() { return randomRunDelay; }
  public static int getBaseRunDelay() { return baseRunDelay; }
  public static double getDistractionValue() { return distractionChance; }
  public static boolean shouldSkipNullCheck() { return skipNullCheck; }
}
