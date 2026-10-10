package noobanidus.mods.miniatures.common.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.AbortableIterationConsumer;
import net.minecraft.util.Continuation;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.entity.EntityTypeTest;
import noobanidus.mods.miniatures.common.api.MiniaturesAPI;
import noobanidus.mods.miniatures.common.entity.MiniMeEntity;

import java.util.Optional;

public class ClientValidate {
  public static void validate() {
    Minecraft instance = Minecraft.getInstance();
    if (instance == null || instance.player == null) {
      MiniaturesAPI.LOG.error("Invalid instance or no player instance found.");
      return;
    }
    if (instance.player.level() instanceof ClientLevel level) {
      level.entityStorage.getEntityGetter().get(EntityTypeTest.forClass(MiniMeEntity.class), e -> {
        ResolvableProfile innerProfile = e.getResolvableProfile();
        if (!(innerProfile instanceof ResolvableProfile.Dynamic)) {
          MiniaturesAPI.LOG.warn("Unresolved profile for {}: {}", e, innerProfile);
        }
        return Continuation.CONTINUE;
      });
    }
  }
}
