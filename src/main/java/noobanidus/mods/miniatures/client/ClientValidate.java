package noobanidus.mods.miniatures.client;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.AbortableIterationConsumer;
import net.minecraft.world.level.entity.EntityTypeTest;
import noobanidus.mods.miniatures.Miniatures;
import noobanidus.mods.miniatures.entity.MiniMeEntity;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

public class ClientValidate {
  public static void validate() {
    Minecraft instance = Minecraft.getInstance();
    if (instance == null || instance.player == null) {
      Miniatures.LOG.error("Invalid instance or no player instance found.");
      return;
    }
    if (instance.player.level() instanceof ClientLevel level) {
      level.entityStorage.getEntityGetter().get(EntityTypeTest.forClass(MiniMeEntity.class), e -> {
        Optional<GameProfile> profile = e.getGameProfile();
        if (profile.isPresent()) {
          GameProfile prof = profile.get();
          if (prof.getId() == null || !StringUtils.isNotBlank(prof.getName())) {
            Miniatures.LOG.warn("Incomplete profile for {}: {}", e, prof);
          }
        } else {
          Miniatures.LOG.warn("No profile for {}", e);
        }
        return AbortableIterationConsumer.Continuation.CONTINUE;
      });
    }
  }
}
