package noobanidus.mods.miniatures.util;

import com.google.common.collect.ImmutableSet;
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
import java.util.*;

public class ProfileCache extends WorldSavedData {
  private final Set<String> cached = new HashSet<>();

  private static ProfileCache INSTANCE = null;

  private static final String IDENTIFIER = "MiniaturesProfileCache";

  public ProfileCache() {
    super(IDENTIFIER);
  }

  private static ServerWorld getServerWorld() {
    return ServerLifecycleHooks.getCurrentServer().getLevel(World.OVERWORLD);
  }

  private static void save () {
    ServerWorld world = getServerWorld();
    world.getDataStorage().save();
  }

  public static ProfileCache getInstance() {
    if (INSTANCE == null) {
      DimensionSavedDataManager manager = getServerWorld().getDataStorage();
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
  public void load(CompoundNBT pCompound) {
    cached.clear();
    ListNBT names = pCompound.getList("names", Constants.NBT.TAG_STRING);
    for (INBT nbt : names) {
      cached.add(nbt.getAsString());
    }
  }

  @Override
  public CompoundNBT save(CompoundNBT pCompound) {
    ListNBT names = new ListNBT();
    for (String name : cached) {
      names.add(StringNBT.valueOf(name));
    }
    pCompound.put("names", names);
    return pCompound;
  }
}
