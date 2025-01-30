package noobanidus.mods.miniatures.neoforge.events;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityEvent;
import net.neoforged.neoforge.event.entity.EntityMountEvent;
import noobanidus.mods.miniatures.common.api.MiniaturesAPI;
import noobanidus.mods.miniatures.common.entity.MiniMeEntity;
import noobanidus.mods.miniatures.neoforge.init.ModEntities;

@EventBusSubscriber(modid = MiniaturesAPI.MODID)
public class EntityEvents {
  // TODO: Convert to entity tag
  private static boolean isMiniature(Entity entity) {
    EntityType<?> type = entity.getType();
    return type == ModEntities.MINIME.get() || type == ModEntities.MAXIME.get() || type == ModEntities.ME.get();
  }

/*  @SubscribeEvent
  public static void onSizeChange(EntityEvent.Size event) {
    if (event.getEntity().isAddedToLevel()) {
      if (isMiniature(event.getEntity())) {
        MiniMeEntity mini = (MiniMeEntity) event.getEntity();
        EntityDimensions oldSize = event.getOldSize();
        event.setNewSize(EntityDimensions.scalable(oldSize.width() * mini.getMiniScale(), oldSize.height() * mini.getMiniScale()));
      }
    }
  }*/
}
