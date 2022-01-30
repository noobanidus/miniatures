package noobanidus.mods.miniatures.util;

import com.google.common.collect.ImmutableSet;
import net.minecraft.nbt.*;
import net.minecraft.util.StringUtils;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import noobanidus.mods.miniatures.Miniatures;

import javax.annotation.Nullable;
import java.util.*;

public class ProfileCache extends SavedData {
  private final Set<String> cached = new HashSet<>();

  private static ProfileCache INSTANCE = null;

  private static final String IDENTIFIER = "MiniaturesProfileCache";

  public ProfileCache() {
    super(IDENTIFIER);
  }

  private static ServerLevel getServerWorld() {
    return ServerLifecycleHooks.getCurrentServer().getLevel(Level.OVERWORLD);
  }

  private static void save () {
    ServerLevel world = getServerWorld();
    world.getDataStorage().save();
  }

  public static ProfileCache getInstance() {
    if (INSTANCE == null) {
      DimensionDataStorage manager = getServerWorld().getDataStorage();
      INSTANCE = manager.computeIfAbsent(ProfileCache::new, IDENTIFIER);
    }

    return INSTANCE;
  }

  protected void internalCache (String name) { cached.add(name); }

  public static void cache(@Nullable String name) {
    ProfileCache instance = getInstance();
    if (instance == null) {
      Miniatures.LOG.error("Could not acquire ProfileCache. Miniature loading may become laggy.");
      return;
    }

    instance.internalCache(name);
    instance.setDirty();
    save();
  }

  public static void cache (Collection<String> names) {
    ProfileCache instance = getInstance();
    if (instance == null) {
      Miniatures.LOG.error("Could not acquire ProfileCache. Miniature loading may become laggy.");
      return;
    }

    instance.cached.addAll(names);
    instance.setDirty();
    save();
  }

  protected void internalClear () {
    cached.clear();
  }

  public static void clear () {
    ProfileCache instance = getInstance();
    if (instance == null) {
      Miniatures.LOG.error("Could not acquire ProfileCache. Unable to clear it.");
      return;
    }

    instance.internalClear();
    instance.setDirty();
    save();
  }

  public static Set<String> cache () {
    ProfileCache instance = getInstance();
    if (instance == null) {
      Miniatures.LOG.error("Could not acquire ProfileCache. Miniature loading may become laggy.");
      return Collections.emptySet();
    }

    return ImmutableSet.copyOf(instance.cached);
  }

  @Override
  public void load(CompoundTag pCompound) {
    cached.clear();
    ListTag names = pCompound.getList("names", Constants.NBT.TAG_STRING);
    for (Tag nbt : names) {
      cached.add(nbt.getAsString());
    }
  }

  @Override
  public CompoundTag save(CompoundTag pCompound) {
    ListTag names = new ListTag();
    for (String name : cached) {
      names.add(StringTag.valueOf(name));
    }
    pCompound.put("names", names);
    return pCompound;
  }
}
