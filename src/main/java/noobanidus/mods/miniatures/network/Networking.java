package noobanidus.mods.miniatures.network;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;
import noobanidus.mods.miniatures.Miniatures;

public class Networking {
  public static void setupPackets(final RegisterPayloadHandlerEvent event) {
    final IPayloadRegistrar registrar = event.registrar(Miniatures.MODID);

    registrar.play(ClientValidatePacket.ID, ClientValidatePacket::new, handler -> handler
            .client(ClientPayloadHandler.getInstance()::handleData));
  }

  public static void send(ServerPlayer player) {
    if (!(player instanceof FakePlayer))
      player.connection.send(new ClientValidatePacket());
  }
}
