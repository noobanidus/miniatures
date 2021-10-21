package noobanidus.mods.miniatures;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

public class MiniTags {
  public static class Entity extends MiniTags {
    public static Tags.IOptionalNamedTag<EntityType<?>> MOB_ATTACK_BLACKLIST = compatTag("minecolonies", "mob_attack_blacklist");

    static Tags.IOptionalNamedTag<EntityType<?>> modTag(String name) {
      return EntityTypeTags.createOptional(new ResourceLocation(Miniatures.MODID, name));
    }

    static Tags.IOptionalNamedTag<EntityType<?>> compatTag(String name) {
      return compatTag("forge", name);
    }

    static Tags.IOptionalNamedTag<EntityType<?>> compatTag(String namespace, String name) {
      return EntityTypeTags.createOptional(new ResourceLocation(namespace, name));
    }
  }

  public static class Blocks extends MiniTags {
    public static Tags.IOptionalNamedTag<Block> BREAK_BLOCKS = modTag("break_blocks");

    static Tags.IOptionalNamedTag<Block> modTag(String name) {
      return BlockTags.createOptional(new ResourceLocation(Miniatures.MODID, name));
    }

    static Tags.IOptionalNamedTag<Block> compatTag(String name) {
      return BlockTags.createOptional(new ResourceLocation("forge", name));
    }
  }
}
