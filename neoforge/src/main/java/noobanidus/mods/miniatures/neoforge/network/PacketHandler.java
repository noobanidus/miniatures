package noobanidus.mods.miniatures.neoforge.network;

/* Shamelessly crib from Mekanism until it works
 * Original source: https://github.com/mekanism/Mekanism/blob/1.21.x/src/main/java/mekanism/common/network/BasePacketHandler.java
 * */

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import noobanidus.mods.miniatures.common.api.MiniaturesAPI;

public class PacketHandler {

  public PacketHandler(IEventBus modEventBus) {
    modEventBus.addListener(RegisterPayloadHandlersEvent.class, event -> {
      PayloadRegistrar registrar = event.registrar(MiniaturesAPI.NETWORK_VERSION);
      registerClientToServer(new PacketRegistrar(registrar, true));
      registerServerToClient(new PacketRegistrar(registrar, false));
    });
  }

  protected void registerClientToServer(PacketRegistrar registrar) {
  }

  protected void registerServerToClient(PacketRegistrar registrar) {
    registrar.play(ClientboundValidateCachePacket.TYPE, ClientboundValidateCachePacket.CODEC);
  }

  protected record PacketRegistrar(PayloadRegistrar registrar, boolean toServer) {

    public <MSG extends INeoForgePacket> void play(CustomPacketPayload.Type<MSG> type, StreamCodec<? super RegistryFriendlyByteBuf, MSG> reader) {
      if (toServer) {
        registrar.playToServer(type, reader, INeoForgePacket::handle);
      } else {
        registrar.playToClient(type, reader, INeoForgePacket::handle);
      }
    }
  }
}
