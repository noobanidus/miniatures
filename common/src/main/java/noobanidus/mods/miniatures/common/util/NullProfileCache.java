package noobanidus.mods.miniatures.common.util;

import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import noobanidus.mods.miniatures.common.api.MiniaturesAPI;
import org.jetbrains.annotations.Nullable;

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
    return MiniaturesAPI.getOverworld();
  }

  private static void save() {
    ServerLevel world = getServerWorld();
    world.getDataStorage().scheduleSave();
  }

  public static NullProfileCache getInstance() {
    if (INSTANCE == null) {
      DimensionDataStorage manager = getServerWorld().getDataStorage();
      INSTANCE = manager.computeIfAbsent(new SavedData.Factory<>(NullProfileCache::new, NullProfileCache::new, null), IDENTIFIER);
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
    if (MiniaturesAPI.shouldSkipNullCheck()) {
      return false;
    }

    NullProfileCache instance = getInstance();
    if (instance == null) {
      MiniaturesAPI.LOG.error("Could not acquire NullProfileCache. Miniature loading may become laggy.");
      return false;
    }

    return instance.internalIsCachedNull(name, uuid);
  }

  protected boolean internalIsCachedNull(@Nullable String name, @Nullable UUID uuid) {
    if (name == null && uuid == null) {
      throw new NullPointerException("Both name and uuid cannot be null in `isCachedNull` check");
    }

    if (name != null && !name.isEmpty()) {
      if (internalIsCachedNull(name)) {
        return true;
      }
    }

    if (uuid != null && !uuid.equals(Util.NIL_UUID)) {
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
      MiniaturesAPI.LOG.error("Could not acquire NullProfileCache. Miniature loading may become laggy.");
      return;
    }

    //Miniatures.LOG.info("Null profile detected! Name {}, UUID {}", name == null ? "<null>" : name, id == null ? "<null>" : id.toString());

    instance.internalCacheNull(name, id);
    instance.setDirty();
    save();
  }

  protected void internalCacheNull(@Nullable String name, @Nullable UUID id) {
    if (name != null && !name.isEmpty()) {
      internalCacheNull(name);
    }

    if (id != null && !id.equals(Util.NIL_UUID)) {
      internalCacheNull(id);
    }
  }

  public NullProfileCache(CompoundTag pCompound, HolderLookup.Provider provider) {
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
  public CompoundTag save(CompoundTag pCompound, HolderLookup.Provider lookup) {
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
