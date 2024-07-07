package noobanidus.mods.miniatures.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import noobanidus.mods.miniatures.Miniatures;

public class Networking {
  private static final String PROTOCOL_VERSION = "1";
  public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
          new ResourceLocation(Miniatures.MODID, "main"),
          () -> PROTOCOL_VERSION,
          PROTOCOL_VERSION::equals,
          PROTOCOL_VERSION::equals
  );

  private static int id = 0;

  public static void init() {
    CHANNEL.registerMessage(id++, ClientValidatePacket.class, ClientValidatePacket::encode, ClientValidatePacket::new, ClientValidatePacket::handle);
  }

  public static void send(ServerPlayer player) {
    if (!(player instanceof FakePlayer))
      CHANNEL.sendTo(new ClientValidatePacket(), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
  }
}
