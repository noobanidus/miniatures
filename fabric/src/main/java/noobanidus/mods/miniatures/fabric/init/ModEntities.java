package noobanidus.mods.miniatures.fabric.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import noobanidus.mods.miniatures.common.api.MiniaturesAPI;
import noobanidus.mods.miniatures.common.entity.MaxiMeEntity;
import noobanidus.mods.miniatures.common.entity.MeEntity;
import noobanidus.mods.miniatures.common.entity.MiniMeEntity;

public class ModEntities {
  public static final EntityType<MiniMeEntity> MINIME = EntityType.Builder.of(MiniMeEntity::new, MobCategory.CREATURE)
      .sized(0.37f, 1.1f).eyeHeight(0.93f).alwaysUpdateVelocity(true).clientTrackingRange(8).updateInterval(4).build();

  public static final EntityType<MeEntity> ME = EntityType.Builder.of(MeEntity::new, MobCategory.CREATURE)
      .sized(0.6f, 2f).alwaysUpdateVelocity(true).clientTrackingRange(8).updateInterval(4).build();

  public static final EntityType<MaxiMeEntity> MAXIME = EntityType.Builder.of(MaxiMeEntity::new, MobCategory.CREATURE)
      .sized(2.3f, 7f).eyeHeight(6.25f).alwaysUpdateVelocity(true).clientTrackingRange(16).updateInterval(4).build();

  public static void register() {
    Registry.register(BuiltInRegistries.ENTITY_TYPE, ResourceKey.create(Registries.ENTITY_TYPE, MiniaturesAPI.rl("minime")), MINIME);
    Registry.register(BuiltInRegistries.ENTITY_TYPE, ResourceKey.create(Registries.ENTITY_TYPE, MiniaturesAPI.rl("maxime")), MAXIME);
    Registry.register(BuiltInRegistries.ENTITY_TYPE, ResourceKey.create(Registries.ENTITY_TYPE, MiniaturesAPI.rl("me")), ME);
  }
}
