package noobanidus.mods.miniatures.common.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.Util;
import net.minecraft.core.UUIDUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.minecraft.world.level.storage.DimensionDataStorage;
import noobanidus.mods.miniatures.common.api.MiniaturesAPI;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class NullProfileCache extends SavedData {
  public static final Codec<NullProfileCache> CODEC = RecordCodecBuilder.create(instance ->
      instance.group(
          Codec.list(Codec.STRING).xmap(HashSet::new, ArrayList::new).fieldOf("cachedNull")
              .forGetter(o -> o.cachedNull),
          UUIDUtil.CODEC_SET.fieldOf("cachedNullUUID").forGetter(o -> o.cachedNullUUID)
      ).apply(instance, NullProfileCache::new));

  private static final String IDENTIFIER = "MiniaturesNullProfileCache";
  public static final SavedDataType<NullProfileCache> TYPE = new SavedDataType<>(IDENTIFIER, NullProfileCache::new, CODEC, null);

  private final HashSet<String> cachedNull;
  private final Set<UUID> cachedNullUUID;

  private static NullProfileCache INSTANCE = null;

  public NullProfileCache() {
    this.cachedNull = new HashSet<>();
    this.cachedNullUUID = new HashSet<>();
  }

  public NullProfileCache(HashSet<String> cachedNull, Set<UUID> cachedUuids) {
    this.cachedNull = cachedNull;
    this.cachedNullUUID = cachedUuids;
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
      INSTANCE = manager.computeIfAbsent(TYPE);
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
}
