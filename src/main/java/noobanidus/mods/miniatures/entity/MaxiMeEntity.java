package noobanidus.mods.miniatures.entity;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.world.World;
import noobanidus.mods.miniatures.config.ConfigManager;

public class MaxiMeEntity extends MiniMeEntity {
  public MaxiMeEntity(EntityType<? extends MiniMeEntity> type, World world) {
    super(type, world);
    setPersistenceRequired();
  }

  public static AttributeModifierMap.MutableAttribute attributes() {
    return MobEntity.createMobAttributes().add(Attributes.MAX_HEALTH, ConfigManager.getMaxHealth()).add(Attributes.MOVEMENT_SPEED, ConfigManager.getMovementSpeed()).add(Attributes.ATTACK_DAMAGE, ConfigManager.getAttackDamage()).add(Attributes.ARMOR, ConfigManager.getArmorValue());
  }

  public MaxiMeEntity(EntityType<? extends MiniMeEntity> type, World world, GameProfile owner) {
    super(type, world, owner);
  }

  @Override
  public double getMyRidingOffset() {
    return 0;
  }

  @Override
  public boolean isBaby() {
    return false;
  }

  @Override
  protected float getStandingEyeHeight(Pose pPose, EntitySize pSize) {
    return 6.25f;
  }
}
