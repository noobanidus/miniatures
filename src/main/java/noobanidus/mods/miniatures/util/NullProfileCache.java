package noobanidus.mods.miniatures.util;

import net.minecraft.nbt.*;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import noobanidus.mods.miniatures.Miniatures;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class NullProfileCache extends WorldSavedData {
  private final Set<String> cachedNull = new HashSet<>();
  private final Set<UUID> cachedNullUUID = new HashSet<>();

  private static NullProfileCache INSTANCE = null;

  private static final String IDENTIFIER = "MiniaturesNullProfileCache";

  public NullProfileCache() {
    super(IDENTIFIER);
  }

  private static ServerWorld getServerWorld() {
    return ServerLifecycleHooks.getCurrentServer().getLevel(World.OVERWORLD);
  }

  private static void save () {
    ServerWorld world = getServerWorld();
    world.getDataStorage().save();
  }

  public static NullProfileCache getInstance() {
    if (INSTANCE == null) {
      DimensionSavedDataManager manager = getServerWorld().getDataStorage();
      INSTANCE = manager.computeIfAbsent(NullProfileCache::new, IDENTIFIER);
    }

    return INSTANCE;
  }

  protected boolean internalIsCachedNull(String name) {
    return cachedNull.contains(name);
  }

  protected boolean internalIsCachedNull(UUID uuid) {
    return cachedNullUUID.contains(uuid);
  }

  public static boolean isCachedNull (@Nullable String name, @Nullable UUID uuid) {
    NullProfileCache instance = getInstance();
    if (instance == null) {
      Miniatures.LOG.error("Could not acquire NullProfileCache. Miniature loading may become laggy.");
      return false;
    }

    return instance.internalIsCachedNull(name, uuid);
  }

  protected boolean internalIsCachedNull(@Nullable String name, @Nullable UUID uuid) {
    if (StringUtils.isNullOrEmpty(name) && uuid == null) {
      throw new NullPointerException("Both name and uuid cannot be null in `isCachedNull` check");
    }

    if (!StringUtils.isNullOrEmpty(name)) {
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

    instance.internalCacheNull(name, id);
    instance.setDirty();
    save();
  }

  protected void internalCacheNull(@Nullable String name, @Nullable UUID id) {
    if (!StringUtils.isNullOrEmpty(name)) {
      internalCacheNull(name);
    }

    if (id != null) {
      internalCacheNull(id);
    }
  }

  @Override
  public void load(CompoundNBT pCompound) {
    cachedNull.clear();
    cachedNullUUID.clear();
    ListNBT uuids = pCompound.getList("uuids", Constants.NBT.TAG_INT_ARRAY);
    for (INBT nbt : uuids) {
      cachedNullUUID.add(NBTUtil.loadUUID(nbt));
    }
    ListNBT names = pCompound.getList("names", Constants.NBT.TAG_STRING);
    for (INBT nbt : names) {
      cachedNull.add(nbt.getAsString());
    }
  }

  @Override
  public CompoundNBT save(CompoundNBT pCompound) {
    ListNBT uuids = new ListNBT();
    for (UUID uuid : cachedNullUUID) {
      uuids.add(NBTUtil.createUUID(uuid));
    }
    ListNBT names = new ListNBT();
    for (String name : cachedNull) {
      names.add(StringNBT.valueOf(name));
    }
    pCompound.put("uuids", uuids);
    pCompound.put("names", names);
    return pCompound;
  }
}
