package noobanidus.mods.miniatures.client;

import mezz.jei.util.StringUtil;
import net.minecraft.util.StringUtils;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class NullProfileCache {
  private static final Set<String> cachedNull = new HashSet<>();
  private static final Set<UUID> cachedNullUUID = new HashSet<>();

  public static boolean isCachedNull(String name) {
    return cachedNull.contains(name);
  }

  public static boolean isCachedNull(UUID uuid) {
    return cachedNullUUID.contains(uuid);
  }

  public static boolean isCachedNull (@Nullable String name, @Nullable UUID uuid) {
    if (StringUtils.isNullOrEmpty(name) && uuid == null) {
      throw new NullPointerException("Both name and uuid cannot be null in `isCachedNull` check");
    }

    if (!StringUtils.isNullOrEmpty(name)) {
      if (isCachedNull(name)) {
        return true;
      }
    }

    if (uuid != null) {
      return isCachedNull(uuid);
    }

    return false;
  }

  public static void cacheNull(String name) {
    cachedNull.add(name);
  }

  public static void cacheNull (UUID uuid) {
    cachedNullUUID.add(uuid);
  }

  public static void cacheNull (@Nullable String name, @Nullable UUID id) {
    if (!StringUtils.isNullOrEmpty(name)) {
      cacheNull(name);
    }

    if (id != null) {
      cacheNull(id);
    }
  }
}
