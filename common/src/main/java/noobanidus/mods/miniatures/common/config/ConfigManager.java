package noobanidus.mods.miniatures.common.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ConfigManager {
  private static final ModConfigSpec.Builder COMMON_BUILDER = new ModConfigSpec.Builder();

  public static final ModConfigSpec COMMON_CONFIG;

  private static final ModConfigSpec.BooleanValue HOSTILE;
  private static final ModConfigSpec.BooleanValue IMMUNE;
  private static final ModConfigSpec.BooleanValue DESTROYS_BLOCKS;
  private static final ModConfigSpec.BooleanValue BREAKS_BLOCKS;
  private static final ModConfigSpec.BooleanValue PICKUP_GOAL;
  private static final ModConfigSpec.BooleanValue OWNER_RIDER;
  private static final ModConfigSpec.DoubleValue DISTRACTION_CHANCE;
  private static final ModConfigSpec.IntValue BASE_RUN_DELAY;
  private static final ModConfigSpec.IntValue RANDOM_RUN_DELAY;
  private static final ModConfigSpec.BooleanValue SKIP_NULL_CHECK;

  static {
    COMMON_BUILDER.comment("options relating to miniatures").push("miniatures");
    HOSTILE = COMMON_BUILDER.comment("whether or not miniatures are hostile to players").define("hostile", false);
    IMMUNE = COMMON_BUILDER.comment("whether or not miniatures are immune to damage that does not originate from a player").define("non_player_immune", true);
    BREAKS_BLOCKS = COMMON_BUILDER.comment("whether or not the miniatures will break blocks in the default tag (miniatures:break_blocks)").define("breaks_blocks", true);
    DISTRACTION_CHANCE = COMMON_BUILDER.comment("the percentage chance per tick that a miniature will get distracted from breaking a block (0 for no distraction)").defineInRange("distraction_chance", 0.05, 0, Double.MAX_VALUE);
    BASE_RUN_DELAY = COMMON_BUILDER.comment("the minimum delay in ticks before a miniature begins running to a block (200)").defineInRange("base_run_delay", 200, 0, Integer.MAX_VALUE);
    RANDOM_RUN_DELAY = COMMON_BUILDER.comment("the maximum value (0 to value-1) added to the run delay (200)").defineInRange("random_run_delay", 200, 0, Integer.MAX_VALUE);
    DESTROYS_BLOCKS = COMMON_BUILDER.comment("whether blocks in the default tag (miniatures:break_blocks) will be destroyed (true) or instead dropped when broken (false)").define("destroys_blocks", false);
    PICKUP_GOAL = COMMON_BUILDER.comment("whether or not non-hostile miniatures will try to pick up players").define("pickup_goal", true);
    OWNER_RIDER = COMMON_BUILDER.comment("whether or not only the owner entity of the miniature will attempt to pick up a player, or whether it will pick up any player").define("owner_rider", false);
    SKIP_NULL_CHECK = COMMON_BUILDER.comment("whether or the null profile cache should be consulted; setting this to false may cause lag when miniatures with non-existent skins are spawned").define("skip_null_check", false);
    COMMON_BUILDER.pop();
    COMMON_CONFIG = COMMON_BUILDER.build();
  }

  public static boolean getHostile() {
    return HOSTILE.get();
  }

  public static boolean getImmune() {
    return IMMUNE.get();
  }

  public static boolean getDestroysBlocks() {
    return DESTROYS_BLOCKS.get();
  }

  public static boolean getBreaksBlocks() {
    return BREAKS_BLOCKS.get();
  }

  public static boolean getDoesPickup() {
    return PICKUP_GOAL.get();
  }

  public static boolean getOwnerRider() {
    return OWNER_RIDER.get();
  }

  public static int getRandomRunDelay() {
    return RANDOM_RUN_DELAY.get();
  }

  public static int getBaseRunDelay() {
    return BASE_RUN_DELAY.get();
  }

  public static double getDistractionValue() {
    return DISTRACTION_CHANCE.get();
  }

  public static boolean shouldSkipNullCheck() {
    return SKIP_NULL_CHECK.get();
  }
}
