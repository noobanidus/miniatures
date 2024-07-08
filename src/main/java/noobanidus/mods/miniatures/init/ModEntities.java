package noobanidus.mods.miniatures.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import noobanidus.mods.miniatures.Miniatures;
import noobanidus.mods.miniatures.entity.MaxiMeEntity;
import noobanidus.mods.miniatures.entity.MeEntity;
import noobanidus.mods.miniatures.entity.MiniMeEntity;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Miniatures.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntities {
  public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, Miniatures.MODID);

  public static final Supplier<EntityType<MiniMeEntity>> MINIME = ENTITY_TYPES.register("minime",
          () -> EntityType.Builder.<MiniMeEntity>of(MiniMeEntity::new, MobCategory.CREATURE)
                  .sized(0.3f, 1.1F).clientTrackingRange(8).setShouldReceiveVelocityUpdates(true).setUpdateInterval(4)
                  .build("minime"));

  public static final Supplier<EntityType<MeEntity>> ME = ENTITY_TYPES.register("me",
          () -> EntityType.Builder.<MeEntity>of(MeEntity::new, MobCategory.CREATURE)
                  .sized(0.6F, 2F).clientTrackingRange(8).setShouldReceiveVelocityUpdates(true).setUpdateInterval(4)
                  .build("me"));

  public static final Supplier<EntityType<MaxiMeEntity>> MAXIME = ENTITY_TYPES.register("maxime",
          () -> EntityType.Builder.<MaxiMeEntity>of(MaxiMeEntity::new, MobCategory.CREATURE)
                  .sized(2.3f, 7).clientTrackingRange(16).setShouldReceiveVelocityUpdates(true).setUpdateInterval(4)
                  .build("maxime"));

  public static void load(IEventBus eventBus) {
    ENTITY_TYPES.register(eventBus);
  }

  @SubscribeEvent
  public static void registerAttributes(EntityAttributeCreationEvent event) {
    event.put(ModEntities.MINIME.get(), MiniMeEntity.attributes().build());
    event.put(ModEntities.MAXIME.get(), MaxiMeEntity.attributes().build());
    event.put(ModEntities.ME.get(), MiniMeEntity.attributes().build());
  }
}
