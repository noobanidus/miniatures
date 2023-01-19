package noobanidus.mods.miniatures.network;

import net.minecraft.server.level.ServerPlayer;
import noobanidus.libs.noobutil.network.PacketHandler;
import noobanidus.mods.miniatures.Miniatures;

public class Networking extends PacketHandler {
  public static Networking INSTANCE = new Networking();

  public Networking() {
    super(Miniatures.MODID);
  }

  @Override
  public void registerMessages() {
    registerMessage(ClientValidatePacket.class, ClientValidatePacket::encode, ClientValidatePacket::new, ClientValidatePacket::handle);
  }

  public static void send(ServerPlayer player) {
    INSTANCE.sendToInternal(new ClientValidatePacket(), player);
  }
}
