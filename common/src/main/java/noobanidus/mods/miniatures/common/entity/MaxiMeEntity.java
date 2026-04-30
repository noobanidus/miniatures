package noobanidus.mods.miniatures.common.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.Level;

public class MaxiMeEntity extends MiniMeEntity {
  public MaxiMeEntity(EntityType<? extends MiniMeEntity> type, Level world) {
    super(type, world);
    setPersistenceRequired();
  }

  public static AttributeSupplier.Builder attributes() {
    return MiniMeEntity.attributes();
  }

/*
  @Override
  protected float ridingOffset(Entity entity) {
    return 0;
  }
*/

  @Override
  public boolean isBaby() {
    return false;
  }

/*  @Override
  protected float getStandingEyeHeight(Pose pPose, EntityDimensions pSize) {
    return 6.25f;
  }*/
}
