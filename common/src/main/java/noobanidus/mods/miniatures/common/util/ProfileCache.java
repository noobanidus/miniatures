package noobanidus.mods.miniatures.common.util;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.minecraft.world.level.storage.DimensionDataStorage;
import noobanidus.mods.miniatures.common.api.MiniaturesAPI;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ProfileCache extends SavedData {
  public static Codec<ProfileCache> CODEC = RecordCodecBuilder.create(instance ->
      instance.group(Codec.STRING.listOf().xmap(HashSet::new, ArrayList::new).fieldOf("cached")
          .forGetter(o -> o.cached)).apply(instance, ProfileCache::new));
  private static final String IDENTIFIER = "MiniaturesProfileCache";
  public static final SavedDataType<ProfileCache> TYPE = new SavedDataType<>(IDENTIFIER, ProfileCache::new, CODEC, null);

  private final HashSet<String> cached;

  private static ProfileCache INSTANCE = null;

  public ProfileCache() {
    this.cached = new HashSet<>();
  }

  public ProfileCache(HashSet<String> cached) {
    this.cached = cached;
  }

  private static ServerLevel getServerWorld() {
    return MiniaturesAPI.getOverworld();
  }

  private static void save() {
    ServerLevel world = getServerWorld();
    world.getDataStorage().scheduleSave();
  }

  public static ProfileCache getInstance() {
    if (INSTANCE == null) {
      DimensionDataStorage manager = getServerWorld().getDataStorage();
      INSTANCE = manager.computeIfAbsent(TYPE);
    }

    return INSTANCE;
  }

  protected void internalCache(String name) {
    cached.add(name);
  }

  public static void cache(@Nullable String name) {
    ProfileCache instance = getInstance();
    if (instance == null) {
      MiniaturesAPI.LOG.error("Could not acquire ProfileCache. Miniature loading may become laggy.");
      return;
    }

    instance.internalCache(name);
    instance.setDirty();
    save();
  }

  public static void cache(Collection<String> names) {
    ProfileCache instance = getInstance();
    if (instance == null) {
      MiniaturesAPI.LOG.error("Could not acquire ProfileCache. Miniature loading may become laggy.");
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
      MiniaturesAPI.LOG.error("Could not acquire ProfileCache. Unable to clear it.");
      return;
    }

    instance.internalClear();
    instance.setDirty();
    save();
  }

  public static Set<String> cache() {
    ProfileCache instance = getInstance();
    if (instance == null) {
      MiniaturesAPI.LOG.error("Could not acquire ProfileCache. Miniature loading may become laggy.");
      return Collections.emptySet();
    }

    return ImmutableSet.copyOf(instance.cached);
  }
}
