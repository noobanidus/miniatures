package noobanidus.mods.miniatures.fabric.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import noobanidus.mods.miniatures.common.client.ClientValidate;
import noobanidus.mods.miniatures.fabric.network.toClient.PacketValidateClient;

public class NetworkingInit {
  public static void registerClientNetwork() {
    ClientPlayNetworking.registerGlobalReceiver(PacketValidateClient.TYPE, (payload, context) -> {
      context.client().execute(() -> {
        ClientValidate.validate();
      });
    });
  }
}
