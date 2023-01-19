package noobanidus.mods.miniatures.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import noobanidus.mods.miniatures.client.ClientValidate;

import java.util.function.Supplier;

public class ClientValidatePacket {
  public ClientValidatePacket() {
  }

  public ClientValidatePacket(FriendlyByteBuf buf) {
  }

  public void encode(FriendlyByteBuf buf) {
  }

  public void handle(Supplier<NetworkEvent.Context> context) {
    context.get().enqueueWork(() -> handle(this, context));
    context.get().setPacketHandled(true);
  }

  private static void handle(ClientValidatePacket packet, Supplier<NetworkEvent.Context> context) {
    ClientValidate.validate();
  }
}
