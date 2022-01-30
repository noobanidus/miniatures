package noobanidus.mods.miniatures.init;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import noobanidus.mods.miniatures.Miniatures;
import noobanidus.mods.miniatures.entity.MaxiMeEntity;
import noobanidus.mods.miniatures.entity.MeEntity;
import noobanidus.mods.miniatures.entity.MiniMeEntity;

import static noobanidus.mods.miniatures.Miniatures.REGISTRATE;

@Mod.EventBusSubscriber(modid= Miniatures.MODID, bus= Mod.EventBusSubscriber.Bus.MOD)
public class ModEntities {
  public static RegistryEntry<EntityType<MiniMeEntity>> MINIME = REGISTRATE.<MiniMeEntity>entity("minime", MiniMeEntity::new, MobCategory.CREATURE)
      .loot((p, e) -> p.add(e, LootTable.lootTable()))
      .properties(o -> o.sized(0.3f, 1.1F).clientTrackingRange(8).setShouldReceiveVelocityUpdates(true).setUpdateInterval(4))
      .register();

  public static RegistryEntry<EntityType<MeEntity>> ME = REGISTRATE.<MeEntity>entity("me", MeEntity::new, MobCategory.CREATURE)
      .loot((p, e) -> p.add(e, LootTable.lootTable()))
      .properties(o -> o.sized(0.6F, 2F).clientTrackingRange(8).setShouldReceiveVelocityUpdates(true).setUpdateInterval(4))
      .register();

  public static RegistryEntry<EntityType<MaxiMeEntity>> MAXIME = REGISTRATE.<MaxiMeEntity>entity("maxime", MaxiMeEntity::new, MobCategory.CREATURE)
      .loot((p, e) -> p.add(e, LootTable.lootTable()))
      .properties(o -> o.sized(2.3f, 7).clientTrackingRange(16).setShouldReceiveVelocityUpdates(true).setUpdateInterval(4))
      .register();

  public static void load() {
  }

  @SubscribeEvent
  public static void registerAttributes(EntityAttributeCreationEvent event) {
    event.put(ModEntities.MINIME.get(), MiniMeEntity.attributes().build());
    event.put(ModEntities.MAXIME.get(), MaxiMeEntity.attributes().build());
    event.put(ModEntities.ME.get(), MiniMeEntity.attributes().build());
  }
}
