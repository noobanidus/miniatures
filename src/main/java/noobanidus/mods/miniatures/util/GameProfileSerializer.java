package noobanidus.mods.miniatures.util;

import com.google.common.base.Strings;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import net.minecraft.network.FriendlyByteBuf;

import java.util.UUID;

public class GameProfileSerializer {
  public static void write(GameProfile o, FriendlyByteBuf output) {
    final UUID uuid = o.getId();
    if (uuid == null) {
      output.writeBoolean(false);
    } else {
      output.writeBoolean(true);
      output.writeUUID(uuid);
    }
    output.writeUtf(Strings.nullToEmpty(o.getName()));
    final PropertyMap properties = o.getProperties();
    output.writeVarInt(properties.size());
    for (Property p : properties.values()) {
      output.writeUtf(p.getName());
      output.writeUtf(p.getValue());

      final String signature = p.getSignature();
      if (signature != null) {
        output.writeBoolean(true);
        output.writeUtf(signature);
      } else {
        output.writeBoolean(false);
      }
    }
  }

  public static GameProfile read(FriendlyByteBuf input) {
    boolean hasUuid = input.readBoolean();
    UUID uuid = hasUuid ? input.readUUID() : null;
    final String name = input.readUtf(Short.MAX_VALUE);
    GameProfile result = new GameProfile(uuid, name);
    int propertyCount = input.readVarInt();

    final PropertyMap properties = result.getProperties();
    for (int i = 0; i < propertyCount; ++i) {
      String key = input.readUtf(Short.MAX_VALUE);
      String value = input.readUtf(Short.MAX_VALUE);
      if (input.readBoolean()) {
        String signature = input.readUtf(Short.MAX_VALUE);
        properties.put(key, new Property(key, value, signature));
      } else {
        properties.put(key, new Property(key, value));
      }

    }

    return result;
  }
}
