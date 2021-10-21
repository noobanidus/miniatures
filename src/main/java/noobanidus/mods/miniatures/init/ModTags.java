package noobanidus.mods.miniatures.init;

import com.tterrag.registrate.providers.ProviderType;
import net.minecraft.block.Blocks;
import net.minecraft.tags.BlockTags;
import noobanidus.mods.miniatures.MiniTags;

import static noobanidus.mods.miniatures.Miniatures.REGISTRATE;

public class ModTags {
  static {
    REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, (p) -> {
      p.tag(MiniTags.Blocks.BREAK_BLOCKS).add(Blocks.TORCH);
      p.tag(MiniTags.Blocks.BREAK_BLOCKS).addTag(BlockTags.FLOWERS);
    });
    REGISTRATE.addDataGenerator(ProviderType.ENTITY_TAGS, (p) -> {
      p.tag(MiniTags.Entity.MOB_ATTACK_BLACKLIST).add(ModEntities.MAXIME.get(), ModEntities.MINIME.get(), ModEntities.ME.get());
    });
  }

  public static void load () {
  }
}
