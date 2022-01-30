package noobanidus.mods.miniatures.entity;

import com.mojang.authlib.GameProfile;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class MeEntity extends MiniMeEntity {
  public MeEntity(EntityType<? extends MiniMeEntity> type, Level world) {
    super(type, world);
    this.adult = true;
  }

  public MeEntity(EntityType<? extends MiniMeEntity> type, Level world, GameProfile owner) {
    super(type, world, owner);
    this.adult = true;
  }
}
