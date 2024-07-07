package noobanidus.mods.miniatures.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.StringUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraftforge.server.ServerLifecycleHooks;
import noobanidus.mods.miniatures.Miniatures;
import noobanidus.mods.miniatures.config.ConfigManager;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class NullProfileCache extends SavedData {
  private final Set<String> cachedNull = new HashSet<>();
  private final Set<UUID> cachedNullUUID = new HashSet<>();

  private static NullProfileCache INSTANCE = null;

  private static final String IDENTIFIER = "MiniaturesNullProfileCache";

  public NullProfileCache() {
  }

  private static ServerLevel getServerWorld() {
    return ServerLifecycleHooks.getCurrentServer().getLevel(Level.OVERWORLD);
  }

  private static void save() {
    ServerLevel world = getServerWorld();
    world.getDataStorage().save();
  }

  public static NullProfileCache getInstance() {
    if (INSTANCE == null) {
      DimensionDataStorage manager = getServerWorld().getDataStorage();
      INSTANCE = manager.computeIfAbsent(NullProfileCache::new, NullProfileCache::new, IDENTIFIER);
    }

    return INSTANCE;
  }

  protected boolean internalIsCachedNull(String name) {
    return cachedNull.contains(name);
  }

  protected boolean internalIsCachedNull(UUID uuid) {
    return cachedNullUUID.contains(uuid);
  }

  public static boolean isCachedNull(@Nullable String name, @Nullable UUID uuid) {
    if (ConfigManager.shouldSkipNullCheck()) {
      return false;
    }

    NullProfileCache instance = getInstance();
    if (instance == null) {
      Miniatures.LOG.error("Could not acquire NullProfileCache. Miniature loading may become laggy.");
      return false;
    }

    return instance.internalIsCachedNull(name, uuid);
  }

  protected boolean internalIsCachedNull(@Nullable String name, @Nullable UUID uuid) {
    if (StringUtil.isNullOrEmpty(name) && uuid == null) {
      throw new NullPointerException("Both name and uuid cannot be null in `isCachedNull` check");
    }

    if (!StringUtil.isNullOrEmpty(name)) {
      if (internalIsCachedNull(name)) {
        return true;
      }
    }

    if (uuid != null) {
      return internalIsCachedNull(uuid);
    }

    return false;
  }

  protected void internalCacheNull(String name) {
    cachedNull.add(name);
  }

  protected void internalCacheNull(UUID uuid) {
    cachedNullUUID.add(uuid);
  }

  public static void cacheNull(@Nullable String name, @Nullable UUID id) {
    NullProfileCache instance = getInstance();
    if (instance == null) {
      Miniatures.LOG.error("Could not acquire NullProfileCache. Miniature loading may become laggy.");
      return;
    }

    //Miniatures.LOG.info("Null profile detected! Name {}, UUID {}", name == null ? "<null>" : name, id == null ? "<null>" : id.toString());

    instance.internalCacheNull(name, id);
    instance.setDirty();
    save();
  }

  protected void internalCacheNull(@Nullable String name, @Nullable UUID id) {
    if (!StringUtil.isNullOrEmpty(name)) {
      internalCacheNull(name);
    }

    if (id != null) {
      internalCacheNull(id);
    }
  }

  public NullProfileCache(CompoundTag pCompound) {
    cachedNull.clear();
    cachedNullUUID.clear();
    ListTag uuids = pCompound.getList("uuids", Tag.TAG_INT_ARRAY);
    for (Tag nbt : uuids) {
      cachedNullUUID.add(NbtUtils.loadUUID(nbt));
    }
    ListTag names = pCompound.getList("names", Tag.TAG_STRING);
    for (Tag nbt : names) {
      cachedNull.add(nbt.getAsString());
    }
  }

  @Override
  public CompoundTag save(CompoundTag pCompound) {
    ListTag uuids = new ListTag();
    for (UUID uuid : cachedNullUUID) {
      uuids.add(NbtUtils.createUUID(uuid));
    }
    ListTag names = new ListTag();
    for (String name : cachedNull) {
      names.add(StringTag.valueOf(name));
    }
    pCompound.put("uuids", uuids);
    pCompound.put("names", names);
    return pCompound;
  }
}
