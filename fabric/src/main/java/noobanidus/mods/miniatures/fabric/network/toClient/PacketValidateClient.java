package noobanidus.mods.miniatures.fabric.network.toClient;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import noobanidus.mods.miniatures.common.api.MiniaturesAPI;

public class PacketValidateClient implements CustomPacketPayload {
  public static final PacketValidateClient INSTANCE = new PacketValidateClient();

  public static final CustomPacketPayload.Type<PacketValidateClient> TYPE = new CustomPacketPayload.Type<>(MiniaturesAPI.rl("validate_client"));

  public static final StreamCodec<ByteBuf, PacketValidateClient> STREAM_CODEC = StreamCodec.unit(INSTANCE);

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
