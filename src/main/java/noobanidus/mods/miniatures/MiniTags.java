package noobanidus.mods.miniatures;

import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.entity.EntityType;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.Tags;

public class MiniTags {
  public static class Entity extends MiniTags {
    public static TagKey<EntityType<?>> MOB_ATTACK_BLACKLIST = compatTag("minecolonies", "mob_attack_blacklist");

    static TagKey<EntityType<?>> modTag(String name) {
      return TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(Miniatures.MODID, name));
    }

    static TagKey<EntityType<?>> compatTag(String namespace, String name) {
      return TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(namespace, name));
    }
  }

  public static class Blocks extends MiniTags {
    public static TagKey<Block> BREAK_BLOCKS = modTag("break_blocks");

    static TagKey<Block> modTag(String name) {
      return BlockTags.create(new ResourceLocation(Miniatures.MODID, name));
    }

    static TagKey<Block> compatTag(String name) {
      return BlockTags.create(new ResourceLocation("forge", name));
    }
  }
}
