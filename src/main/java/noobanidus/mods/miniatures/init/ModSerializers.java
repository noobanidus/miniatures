package noobanidus.mods.miniatures.init;

import com.mojang.authlib.GameProfile;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import noobanidus.mods.miniatures.Miniatures;

import java.util.Optional;
import java.util.function.Supplier;

public class ModSerializers {
  private static final DeferredRegister<EntityDataSerializer<?>> REGISTRY = DeferredRegister.create(NeoForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, Miniatures.MODID);

  public static final Supplier<EntityDataSerializer<Optional<GameProfile>>> OPTIONAL_GAME_PROFILE = REGISTRY.register("game_profile",
          GameProfileSerializer::new);

  public static class GameProfileSerializer implements EntityDataSerializer<Optional<GameProfile>> {
    public void write(FriendlyByteBuf friendlyByteBuf, Optional<GameProfile> optionalGameProfile) {
      friendlyByteBuf.writeBoolean(optionalGameProfile.isPresent());
      if (optionalGameProfile.isPresent()) {
        friendlyByteBuf.writeNbt(NbtUtils.writeGameProfile(new CompoundTag(), optionalGameProfile.get()));
      }
    }

    public Optional<GameProfile> read(FriendlyByteBuf friendlyByteBuf) {
      return !friendlyByteBuf.readBoolean() ? Optional.empty() : Optional.of(NbtUtils.readGameProfile(friendlyByteBuf.readNbt()));
    }

    public Optional<GameProfile> copy(Optional<GameProfile> optionalGameProfile) {
      return optionalGameProfile;
    }
  }

  public static void load(IEventBus bus) {
    REGISTRY.register(bus);
  }
}
