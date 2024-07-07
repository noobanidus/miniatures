package noobanidus.mods.miniatures.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import noobanidus.mods.miniatures.Miniatures;

public record ClientValidatePacket() implements CustomPacketPayload {
  public static final ResourceLocation ID = new ResourceLocation(Miniatures.MODID, "client_validate");

  public ClientValidatePacket(FriendlyByteBuf buf) {
    this();
  }

  public void write(FriendlyByteBuf buf) {
  }

  @Override
  public ResourceLocation id() {
    return ID;
  }
}
