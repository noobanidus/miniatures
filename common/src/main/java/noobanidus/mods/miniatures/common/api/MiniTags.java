package noobanidus.mods.miniatures.common.api;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;

public class MiniTags {
  public static class Entity extends MiniTags {
    public static TagKey<EntityType<?>> MOB_ATTACK_BLACKLIST = compatTag("minecolonies", "mob_attack_blacklist");

    static TagKey<EntityType<?>> compatTag(String namespace, String name) {
      return TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(namespace, name));
    }
  }

  public static class Blocks extends MiniTags {
    public static TagKey<Block> BREAK_BLOCKS = modTag("break_blocks");

    static TagKey<Block> modTag(String name) {
      return TagKey.create(Registries.BLOCK, MiniaturesAPI.rl(name));
    }
  }
}
