package noobanidus.mods.miniatures.events;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.EntityEvent;
import net.neoforged.neoforge.event.entity.EntityMountEvent;
import noobanidus.mods.miniatures.Miniatures;
import noobanidus.mods.miniatures.entity.MiniMeEntity;
import noobanidus.mods.miniatures.init.ModEntities;

@Mod.EventBusSubscriber(modid = Miniatures.MODID)
public class EntityEvents {
  private static boolean isMiniature(Entity entity) {
    EntityType type = entity.getType();
    return type == ModEntities.MINIME.get() || type == ModEntities.MAXIME.get() || type == ModEntities.ME.get();
  }

  @SubscribeEvent
  public static void onSizeChange(EntityEvent.Size event) {
    if (event.getEntity().isAddedToWorld()) {
      if (isMiniature(event.getEntity())) {
        MiniMeEntity mini = (MiniMeEntity) event.getEntity();
        EntityDimensions oldSize = event.getOldSize();
        event.setNewSize(EntityDimensions.scalable(oldSize.width * mini.getMiniScale(), oldSize.height * mini.getMiniScale()));
        event.setNewEyeHeight(event.getOldEyeHeight() * mini.getMiniScale());
      }
    }
  }

  @SubscribeEvent
  public static void onEntityDismount(EntityMountEvent event) {
    if (event.isDismounting()) {
      Entity mounted = event.getEntityBeingMounted();
      if (isMiniature(event.getEntityBeingMounted())) {
        MiniMeEntity mini = (MiniMeEntity) mounted;
        mini.setPickupCooldown(mini.level().random.nextInt(800) + 600);
      }
    }
  }
}
