package noobanidus.mods.miniatures.neoforge.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import noobanidus.mods.miniatures.common.api.MiniaturesAPI;
import noobanidus.mods.miniatures.common.entity.MaxiMeEntity;
import noobanidus.mods.miniatures.common.entity.MeEntity;
import noobanidus.mods.miniatures.common.entity.MiniMeEntity;

import java.util.function.Supplier;

@EventBusSubscriber(modid = MiniaturesAPI.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModEntities {
  public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, MiniaturesAPI.MODID);

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

  public static void register(IEventBus eventBus) {
    ENTITY_TYPES.register(eventBus);
  }

  @SubscribeEvent
  public static void registerAttributes(EntityAttributeCreationEvent event) {
    event.put(ModEntities.MINIME.get(), MiniMeEntity.attributes().build());
    event.put(ModEntities.MAXIME.get(), MaxiMeEntity.attributes().build());
    event.put(ModEntities.ME.get(), MiniMeEntity.attributes().build());
  }
}
