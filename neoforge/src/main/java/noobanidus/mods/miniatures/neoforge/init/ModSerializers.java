package noobanidus.mods.miniatures.neoforge.init;

import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.item.component.ResolvableProfile;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import noobanidus.mods.miniatures.common.api.MiniaturesAPI;

import java.util.Optional;
import java.util.function.Supplier;

public class ModSerializers {
  private static final DeferredRegister<EntityDataSerializer<?>> REGISTRY = DeferredRegister.create(NeoForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, MiniaturesAPI.MODID);

  public static final Supplier<EntityDataSerializer<Optional<ResolvableProfile>>> OPTIONAL_RESOLVABLE_PROFILE = REGISTRY.register("game_profile", () -> EntityDataSerializer.forValueType(ResolvableProfile.STREAM_CODEC.apply(ByteBufCodecs::optional)));

  public static void load(IEventBus bus) {
    REGISTRY.register(bus);
  }
}
