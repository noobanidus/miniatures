package noobanidus.mods.miniatures.neoforge.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import noobanidus.mods.miniatures.common.client.ClientValidate;
import noobanidus.mods.miniatures.common.api.MiniaturesAPI;

public record ClientboundValidateCachePacket() implements INeoForgePacket {
  private static final ClientboundValidateCachePacket INSTANCE = new ClientboundValidateCachePacket();
  public static final Type<ClientboundValidateCachePacket> TYPE = new Type<>(MiniaturesAPI.rl("client_bound_validate_cache"));
  public static final StreamCodec<ByteBuf, ClientboundValidateCachePacket> CODEC = StreamCodec.unit(INSTANCE);

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }


  @Override
  public void handle(IPayloadContext context) {
    ClientValidate.validate();
  }
}
