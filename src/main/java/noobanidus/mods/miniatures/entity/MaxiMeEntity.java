package noobanidus.mods.miniatures.entity;

import com.mojang.authlib.GameProfile;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import noobanidus.mods.miniatures.config.ConfigManager;

public class MaxiMeEntity extends MiniMeEntity {
  public MaxiMeEntity(EntityType<? extends MiniMeEntity> type, Level world) {
    super(type, world);
    setPersistenceRequired();
  }

  public static AttributeSupplier.Builder attributes() {
    return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, ConfigManager.getMaxHealth()).add(Attributes.MOVEMENT_SPEED, ConfigManager.getMovementSpeed()).add(Attributes.ATTACK_DAMAGE, ConfigManager.getAttackDamage()).add(Attributes.ARMOR, ConfigManager.getArmorValue());
  }

  public MaxiMeEntity(EntityType<? extends MiniMeEntity> type, Level world, GameProfile owner) {
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
  protected float getStandingEyeHeight(Pose pPose, EntityDimensions pSize) {
    return 6.25f;
  }
}
