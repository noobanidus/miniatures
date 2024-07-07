package noobanidus.mods.miniatures.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class MeEntity extends MiniMeEntity {
  public MeEntity(EntityType<? extends MiniMeEntity> type, Level world) {
    super(type, world);
    this.adult = true;
  }
}
