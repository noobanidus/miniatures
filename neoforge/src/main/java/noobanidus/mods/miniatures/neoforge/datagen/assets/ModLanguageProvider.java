package noobanidus.mods.miniatures.neoforge.datagen.assets;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import noobanidus.mods.miniatures.common.api.MiniaturesAPI;
import noobanidus.mods.miniatures.neoforge.init.ModBlocks;
import noobanidus.mods.miniatures.neoforge.init.ModEntities;

public class ModLanguageProvider extends LanguageProvider {
  private final String locale;

  public ModLanguageProvider(PackOutput output, String locale) {
    super(output, MiniaturesAPI.MODID, locale);
    this.locale = locale;
  }

  @Override
  protected void addTranslations() {
    addBlock(ModBlocks.SENSOR_TORCH_BLOCK, "Sensor Torch");
    addEntityType(ModEntities.MAXIME, "Maxime");
    addEntityType(ModEntities.ME, "Me");
    addEntityType(ModEntities.MINIME, "Minime");

    add("miniatures.networking.client_validate.failed", "Failed to validate client data: %s");

    add("miniatures.config.title", "Miniatures");
    add("miniatures.config.desc", "Configuration options for Miniatures.");

    addConfig("hostile", "Hostility", "If true, miniatures are automatically hostile to players. [default: false]");
    addConfig("non_player_immune", "Non-Player Damage Immunity", "If true, miniatures are immune to any damage whose source is not directly (or indirectly) a player [default: true]");
    addConfig("breaks_blocks", "Block breaking", "If true, miniatures will attempt to break blocks that are tagged as such. [default: true]");
    addConfig("distraction_chance", "Distraction chance", "Chance per tick that a miniature will be distracted from breaking a block. [default 0.05]");
    addConfig("base_run_delay", "Run delay", "The minimum delay in ticks before a miniature will begin running towards a block that it can break.");
    addConfig("random_run_delay", "Random run delay", "The maximum value that can be added to the run delay.");
    addConfig("destroys_blocks", "Destroys blocks", "Whether or not miniatures will destroy (break without dropping) blocks in the default block break tag.");
    addConfig("pickup_goal", "Pick-up goal", "Whether or not miniatures will attempt to pick up players.");
    addConfig("owner_rider", "Owner Rider", "If true, miniatures will only attempt to pick up their equivalent player owner.");
    addConfig("skip_null_check", "Skip null check", "If true, the null profile cache isn't consulted, which means skin loading may be delayed.");
  }

  protected void addConfig (String section, String title, String description) {
    add("miniatures.config." + section, title);
    add("miniatures.config." + section + ".desc", description);
  }

  // Generate upside-down if the locale is en_ud
  @Override
  public void add(String key, String value) {
    if (locale.equalsIgnoreCase("en_ud"))
      super.add(key, toUpsideDown(value));
    else
      super.add(key, value);
  }

  private static final String NORMAL_CHARS =
          /* lowercase */ "abcdefghijklmn\u00F1opqrstuvwxyz" +
          /* uppercase */ "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
          /*  numbers  */ "0123456789" +
          /*  special  */ "_,;.?!/\\'";
  private static final String UPSIDE_DOWN_CHARS =
          /* lowercase */ "\u0250q\u0254p\u01DD\u025Fb\u0265\u0131\u0638\u029E\u05DF\u026Fuuodb\u0279s\u0287n\u028C\u028Dx\u028Ez" +
          /* uppercase */ "\u2C6F\u15FA\u0186\u15E1\u018E\u2132\u2141HI\u017F\u029E\uA780WNO\u0500\u1F49\u1D1AS\u27D8\u2229\u039BMX\u028EZ" +
          /*  numbers  */ "0\u0196\u1105\u0190\u3123\u03DB9\u312586" +
          /*  special  */ "\u203E'\u061B\u02D9\u00BF\u00A1/\\,";

  private String toUpsideDown(String normal) {
    char[] ud = new char[normal.length()];
    for (int i = 0; i < normal.length(); i++) {
      char c = normal.charAt(i);
      if (c == '%') {
        String fmtArg = "";
        while (Character.isDigit(c) || c == '%' || c == '$' || c == 's' || c == 'd') {
          fmtArg += c;
          i++;
          c = i == normal.length() ? 0 : normal.charAt(i);
        }
        i--;
        for (int j = 0; j < fmtArg.length(); j++) {
          ud[normal.length() - 1 - i + j] = fmtArg.charAt(j);
        }
        continue;
      }
      int lookup = NORMAL_CHARS.indexOf(c);
      if (lookup >= 0) {
        c = UPSIDE_DOWN_CHARS.charAt(lookup);
      }
      ud[normal.length() - 1 - i] = c;
    }
    return new String(ud);
  }
}
