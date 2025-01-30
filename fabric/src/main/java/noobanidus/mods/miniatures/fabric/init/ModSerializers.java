package noobanidus.mods.miniatures.fabric.init;

import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.item.component.ResolvableProfile;

import java.util.Optional;

public class ModSerializers {
  public static EntityDataSerializer<Optional<ResolvableProfile>> RESOLVABLE_PROFILE = EntityDataSerializer.forValueType(ResolvableProfile.STREAM_CODEC.apply(ByteBufCodecs::optional));
}
