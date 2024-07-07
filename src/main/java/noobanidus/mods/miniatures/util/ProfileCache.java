package noobanidus.mods.miniatures.util;

import com.google.common.collect.ImmutableSet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import noobanidus.mods.miniatures.Miniatures;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ProfileCache extends SavedData {
  private final Set<String> cached = new HashSet<>();

  private static ProfileCache INSTANCE = null;

  private static final String IDENTIFIER = "MiniaturesProfileCache";

  public ProfileCache() {
  }

  private static ServerLevel getServerWorld() {
    return ServerLifecycleHooks.getCurrentServer().getLevel(Level.OVERWORLD);
  }

  private static void save() {
    ServerLevel world = getServerWorld();
    world.getDataStorage().save();
  }

  public static ProfileCache getInstance() {
    if (INSTANCE == null) {
      DimensionDataStorage manager = getServerWorld().getDataStorage();
      INSTANCE = manager.computeIfAbsent(new SavedData.Factory<>(ProfileCache::new, ProfileCache::new), IDENTIFIER);
    }

    return INSTANCE;
  }

  protected void internalCache(String name) {
    cached.add(name);
  }

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

  public static void cache(Collection<String> names) {
    ProfileCache instance = getInstance();
    if (instance == null) {
      Miniatures.LOG.error("Could not acquire ProfileCache. Miniature loading may become laggy.");
      return;
    }

    instance.cached.addAll(names);
    instance.setDirty();
    save();
  }

  protected void internalClear() {
    cached.clear();
  }

  public static void clear() {
    ProfileCache instance = getInstance();
    if (instance == null) {
      Miniatures.LOG.error("Could not acquire ProfileCache. Unable to clear it.");
      return;
    }

    instance.internalClear();
    instance.setDirty();
    save();
  }

  public static Set<String> cache() {
    ProfileCache instance = getInstance();
    if (instance == null) {
      Miniatures.LOG.error("Could not acquire ProfileCache. Miniature loading may become laggy.");
      return Collections.emptySet();
    }

    return ImmutableSet.copyOf(instance.cached);
  }

  public ProfileCache(CompoundTag pCompound) {
    cached.clear();
    ListTag names = pCompound.getList("names", Tag.TAG_STRING);
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
