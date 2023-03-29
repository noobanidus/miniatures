package noobanidus.mods.miniatures.init;

import com.mojang.authlib.GameProfile;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import noobanidus.mods.miniatures.Miniatures;

import javax.annotation.Nonnull;
import java.util.Optional;

public class ModSerializers {
  private static final DeferredRegister<EntityDataSerializer<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, Miniatures.MODID);

  public static final RegistryObject<EntityDataSerializer<?>> OPTIONAL_GAME_PROFILE = REGISTRY.register("game_profile", () -> new GameProfileSerializer());

  public static class GameProfileSerializer implements EntityDataSerializer<Optional<GameProfile>> {
     @Override
    public void write(@Nonnull FriendlyByteBuf packetBuffer, @Nonnull Optional<GameProfile> gameProfile) {
      if (gameProfile.isPresent()) {
        packetBuffer.writeBoolean(true);
        packetBuffer.writeNbt(NbtUtils.writeGameProfile(new CompoundTag(), gameProfile.get()));
      } else {
        packetBuffer.writeBoolean(false);
      }
    }

    @Override
    @Nonnull
    public Optional<GameProfile> read(@Nonnull FriendlyByteBuf packetBuffer) {
      if (packetBuffer.readBoolean()) {
        CompoundTag tag = packetBuffer.readNbt();
        if (tag != null) {
          GameProfile profile = NbtUtils.readGameProfile(tag);
          if (profile != null) {
            return Optional.of(profile);
          }
        }
      }
      return Optional.empty();
    }

    @Override
    @Nonnull
    public Optional<GameProfile> copy(@Nonnull Optional<GameProfile> gameProfile) {
      return gameProfile;
    }
  }

  public static void load(IEventBus bus) {
    REGISTRY.register(bus);
  }
}
