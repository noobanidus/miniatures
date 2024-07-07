package noobanidus.mods.miniatures.network;

import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import noobanidus.mods.miniatures.client.ClientValidate;

public class ClientPayloadHandler {
  private static final ClientPayloadHandler INSTANCE = new ClientPayloadHandler();

  public static ClientPayloadHandler getInstance() {
    return INSTANCE;
  }

  public void handleData(final ClientValidatePacket data, final PlayPayloadContext context) {
    context.workHandler().submitAsync(ClientValidate::validate)
            .exceptionally(e -> {
              // Handle exception
              context.packetHandler().disconnect(Component.translatable("miniatures.networking.client_validate.failed", e.getMessage()));
              return null;
            });
  }
}
