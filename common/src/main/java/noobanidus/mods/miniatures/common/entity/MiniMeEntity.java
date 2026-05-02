package noobanidus.mods.miniatures.common.entity;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.yggdrasil.ProfileResult;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.Util;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.Services;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.StringUtil;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import noobanidus.mods.miniatures.common.api.MiniTags;
import noobanidus.mods.miniatures.common.api.MiniaturesAPI;
import noobanidus.mods.miniatures.common.api.Modifiers;
import noobanidus.mods.miniatures.common.entity.ai.MiniBreakBlockGoal;
import noobanidus.mods.miniatures.common.entity.ai.MiniMeleeAttackGoal;
import noobanidus.mods.miniatures.common.entity.ai.PickupPlayerGoal;
import noobanidus.mods.miniatures.common.util.NoobUtil;
import noobanidus.mods.miniatures.common.util.NullProfileCache;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

public class MiniMeEntity extends Monster {
  private static final EntityDataAccessor<Optional<ResolvableProfile>> RESOLVABLE_PROFILE = SynchedEntityData.defineId(MiniMeEntity.class, MiniaturesAPI.getGameProfileSerializer());

  public static final EntityDataAccessor<Integer> AGGRO = SynchedEntityData.defineId(MiniMeEntity.class, EntityDataSerializers.INT);
  public static final EntityDataAccessor<Byte> NOOB = SynchedEntityData.defineId(MiniMeEntity.class, EntityDataSerializers.BYTE);

  private ServerBossEvent bossInfo;

  @Nullable
  private static LoadingCache<String, CompletableFuture<Optional<GameProfile>>> gameProfileCacheByName;
  @Nullable
  private static LoadingCache<UUID, CompletableFuture<Optional<GameProfile>>> gameProfileCacheById;
  private int pickupCooldown = 0;
  private boolean wasRidden = false;
  protected boolean adult = false;

  private boolean healthBoosted = false;
  private boolean attackBoosted = false;

  private boolean isBeingLoaded = false;
  private CompletableFuture<?> currentFuture = null;

  static CompletableFuture<Optional<GameProfile>> fetchProfileByName(String name, Services services) {
    return services.profileCache()
        .getAsync(name)
        .exceptionally(
            throwable -> {
              MiniaturesAPI.LOG.error("Failed to get profile for {}", name, throwable);
              return Optional.ofNullable(null);
            }
        )
        .thenCompose(
            optionalProfile -> {
              LoadingCache<UUID, CompletableFuture<Optional<GameProfile>>> loadingcache = gameProfileCacheById;
              if (optionalProfile.isEmpty()) {
                NullProfileCache.cacheNull(name, null);
              }
              return loadingcache != null && optionalProfile.isPresent()
                  ? loadingcache.getUnchecked(optionalProfile.get().getId())
                  .thenApply(p_339543_ -> p_339543_.or(() -> optionalProfile))
                  : CompletableFuture.completedFuture(Optional.empty());
            }
        );
  }

  static CompletableFuture<Optional<GameProfile>> fetchProfileById(UUID id, Services services, BooleanSupplier cacheUninitialized) {
    return CompletableFuture.supplyAsync(() -> {
      if (NullProfileCache.isCachedNull(null, id)) {
        return Optional.empty();
      }
      if (cacheUninitialized.getAsBoolean()) {
        return Optional.empty();
      } else {
        ProfileResult profileresult = services.sessionService().fetchProfile(id, true);
        if (profileresult == null) {
          NullProfileCache.cacheNull(null, id);
          return Optional.empty();
        } else {
          return Optional.of(profileresult).map(ProfileResult::profile);
        }
      }
    }, Util.backgroundExecutor());
  }

  public static void setup(final Services services) {
    final BooleanSupplier booleanSupplier = () -> gameProfileCacheById == null;
    gameProfileCacheByName = CacheBuilder.newBuilder().expireAfterAccess(Duration.ofHours(6))
        .maximumSize(256L)
        .build(new CacheLoader<>() {
          @Override
          public CompletableFuture<Optional<GameProfile>> load(String key) {
            return fetchProfileByName(key, services);
          }
        });
    gameProfileCacheById = CacheBuilder.newBuilder().expireAfterAccess(Duration.ofHours(6)).maximumSize(256L)
        .build(new CacheLoader<>() {
          @Override
          public CompletableFuture<Optional<GameProfile>> load(UUID key) {
            return fetchProfileById(key, services, booleanSupplier);
          }
        });
  }

  public static void clear() {
    gameProfileCacheById = null;
    gameProfileCacheByName = null;
  }

  public MiniMeEntity(EntityType<? extends MiniMeEntity> type, Level world) {
    super(type, world);
    setPersistenceRequired();
  }

  public Optional<ResolvableProfile> getGameProfile() {
    return entityData.get(RESOLVABLE_PROFILE);
  }

  public boolean getHostile() {
    int aggro = getAggro();
    if (aggro == -1) {
      return MiniaturesAPI.getHostile();
    }
    return aggro == 1;
  }

  @Override
  public boolean isPreventingPlayerRest(ServerLevel p_376906_, Player p_33036_) {
    return false;
  }

  @Override
  public boolean displayFireAnimation() {
    if (getNoobVariant() == 2) {
      return true;
    }
    return super.displayFireAnimation();
  }

  @Override
  protected void defineSynchedData(SynchedEntityData.Builder arg) {
    super.defineSynchedData(arg);
    arg.define(RESOLVABLE_PROFILE, Optional.empty());
    arg.define(AGGRO, -1);
    arg.define(NOOB, (byte) random.nextInt(20));

    // 0: Upside down
    // 1: Floating
    // 2: On Fire
    // 3: Ghost
    // 4: Glow
    // 5: Charged
    // 6: dexter
    // 7: sinister
    // 8: backwards
  }

  @Override
  public float getAgeScale() {
    return 1f;
  }

  public int getNoobVariant() {
    if (!NoobUtil.isNoob(this)) {
      return -1;
    }
    return entityData.get(NOOB);
  }

  public void setNoobVariant(int variant) {
    entityData.set(NOOB, (byte) variant);
  }

  public int getAggro() {
    return entityData.get(AGGRO);
  }

  public void setAggro(int aggro) {
    entityData.set(AGGRO, aggro);
  }

  public static AttributeSupplier.Builder attributes() {
    return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 16)
        .add(Attributes.MOVEMENT_SPEED, 0.3)
        .add(Attributes.ATTACK_DAMAGE, 2.0)
        .add(Attributes.ARMOR, 0);
  }

  @Override
  protected void registerGoals() {
    this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    this.goalSelector.addGoal(1, new MiniMeleeAttackGoal(this, 1.0d, false));
    this.goalSelector.addGoal(2, new FloatGoal(this));
    this.goalSelector.addGoal(3, new MiniBreakBlockGoal(MiniTags.Blocks.BREAK_BLOCKS, this, 1, 3));
    this.goalSelector.addGoal(4, new PickupPlayerGoal(this));
    this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
    this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
    this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
  }

  public void setGameProfileByName(String name) {
    if (NullProfileCache.isCachedNull(name, null)) {
      return;
    }

    if (currentFuture == null || (currentFuture.isCancelled() || currentFuture.isDone())) {
      String username = name.toLowerCase(Locale.ROOT);
      if (!NullProfileCache.isCachedNull(username, null)) {
        currentFuture = fetchGameProfile(username).thenAccept(
            profile -> entityData.set(RESOLVABLE_PROFILE, Optional.of(new ResolvableProfile(profile.orElse(new GameProfile(Util.NIL_UUID, username))))));
      }
    }
  }

  public void setGameProfileById(UUID id) {
    if (id == null) {
      return;
    }
    if (NullProfileCache.isCachedNull(null, id)) {
      return;
    }

    if (currentFuture == null || (currentFuture.isCancelled() || currentFuture.isDone())) {
      currentFuture = fetchGameProfile(id).thenAccept(
          profile -> entityData.set(RESOLVABLE_PROFILE, Optional.of(new ResolvableProfile(profile.orElse(new GameProfile(id, ""))))));
    }
  }

  @Override
  protected PathNavigation createNavigation(Level worldIn) {
    GroundPathNavigation navigator = new GroundPathNavigation(this, worldIn);
    setPathfindingMalus(PathType.WATER, -1.0F);
    navigator.setCanFloat(true);
    return navigator;
  }

  @Override
  public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
    if (MiniaturesAPI.getImmune() && !(source.getEntity() instanceof Player) && !source.is(DamageTypeTags.BYPASSES_RESISTANCE)) {
      return false;
    }
    return super.hurtServer(level, source, amount);
  }

  @Override
  protected void removePassenger(Entity entity) {
    super.removePassenger(entity);

    this.setPickupCooldown(this.getRandom().nextInt(800) + 600);
  }

  @Override
  public void tick() {
    super.tick();
    if (pickupCooldown > 0) pickupCooldown--;
    if (wasRidden && !isVehicle()) {
      wasRidden = false;
    } else if (isVehicle()) {
      wasRidden = true;
    }
    if (level().isClientSide) {
      int noob = getNoobVariant();
      if (tickCount % 4 == 0 && noob == 1) {
        level().addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, getX(), getY() + 0.3, getZ(), 0, 0, 0);
      }
    }
    if (bossInfo != null) {
      this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
    }
  }

  @Override
  public Vec3 getDismountLocationForPassenger(LivingEntity livingEntity) {
    return new Vec3(this.getX(), this.getBoundingBox().minY, this.getZ());
  }

  public int getPickupCooldown() {
    return pickupCooldown;
  }

  public void setPickupCooldown(int cooldown) {
    pickupCooldown = cooldown;
  }

  @Override
  public void setCustomName(@Nullable Component name) {
    super.setCustomName(name);

    if (name != null) {
      if (this.bossInfo != null) {
        this.bossInfo.setName(name);
      }

      Optional<ResolvableProfile> opt = getGameProfile();
      if ((isBeingLoaded && opt.isEmpty()) || (isBeingLoaded && opt.isEmpty() && currentFuture == null) || (isBeingLoaded && opt.isEmpty() && currentFuture != null && currentFuture.isDone()) || (isBeingLoaded && currentFuture != null && currentFuture.isCancelled()) || !isBeingLoaded) {
        String username = name.getString().toLowerCase(Locale.ROOT);
        if (StringUtil.isValidPlayerName(username) && !NullProfileCache.isCachedNull(username, null)) {
          currentFuture = fetchGameProfile(username).thenAccept(
              profile -> entityData.set(RESOLVABLE_PROFILE, Optional.of(new ResolvableProfile(profile.orElse(new GameProfile(Util.NIL_UUID, username))))));
        } else {
          MiniaturesAPI.LOG.error("Null profile detected from setCustomName! Name {} is null.", username);
        }
      }
    }
  }

  @Override
  public boolean removeWhenFarAway(double distance) {
    return false;
  }

  @Override
  public boolean isBaby() {
    return !adult;
  }

  @Override
  public void addAdditionalSaveData(CompoundTag compound) {
    super.addAdditionalSaveData(compound);

    compound.putBoolean("gameProfileExists", entityData.get(RESOLVABLE_PROFILE).isPresent());
    if (getGameProfile().isPresent()) {
      ResolvableProfile.CODEC.encodeStart(NbtOps.INSTANCE, entityData.get(RESOLVABLE_PROFILE).get())
          .resultOrPartial(MiniaturesAPI.LOG::error)
          .ifPresent(profile -> compound.put("gameProfile", profile));
    }

    compound.putByte("Noob", entityData.get(NOOB));

    compound.putInt("pickupCooldown", pickupCooldown);
    if (healthBoosted) {
      AttributeInstance health = this.getAttribute(Attributes.MAX_HEALTH);
      if (health != null) {
        AttributeModifier mod = health.getModifier(Modifiers.HEALTH_INCREASE);
        if (mod != null) {
          compound.putDouble("HealthAddition", mod.amount());
          compound.putBoolean("HealthWasBoosted", true);
        }
      }
    }
    if (attackBoosted) {
      AttributeInstance attack = this.getAttribute(Attributes.ATTACK_DAMAGE);
      if (attack != null) {
        AttributeModifier mod = attack.getModifier(Modifiers.ATTACK_DAMAGE_INCREASE);
        if (mod != null) {
          compound.putDouble("AttackAddition", mod.amount());
        }
      }
    }

    compound.putInt("Hostile", entityData.get(AGGRO));

    if (bossInfo != null) {
      compound.store("BossBar", MiniBossEvent.CODEC, new MiniBossEvent(bossInfo));
    }
  }

  @Override
  public void readAdditionalSaveData(CompoundTag tag) {
    super.readAdditionalSaveData(tag);
    this.pickupCooldown = tag.getIntOr("pickupCooldown", 0);
    if (tag.contains("Noob")) {
      this.setNoobVariant(tag.getIntOr("Noob", 0));
    }
    if (tag.contains("Hostile")) {
      this.setAggro(tag.getIntOr("Hostile", 0));
    }
  }

  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  private static <T> boolean compareOptional(Optional<T> a, Optional<T> b, Predicate<T> isEmptyOrNil) {
    if (a.isPresent() && b.isPresent()) {
      return isEmptyOrNil.test(a.get()) && isEmptyOrNil.test(b.get());
    } else {
      return a.isEmpty() && b.isEmpty();
    }
  }

  @Override
  public void load(CompoundTag compound) {
    this.isBeingLoaded = true;

    ResolvableProfile incomingProfile = null;

    boolean profileExists = compound.getBooleanOr("gameProfileExists", false);
    if (profileExists) {
      incomingProfile = ResolvableProfile.CODEC.parse(NbtOps.INSTANCE, compound.get("gameProfile"))
          .resultOrPartial(o -> MiniaturesAPI.LOG.error("Failed to parse game profile: {}", o)).orElse(null);
    }

    ResolvableProfile currentProfile = getGameProfile().orElse(null);

    if (incomingProfile != null && currentProfile != null && (!compareOptional(incomingProfile.name(), currentProfile.name(), String::isBlank) || !compareOptional(incomingProfile.id(), currentProfile.id(), Util.NIL_UUID::equals))) {
      // Different profile than currently set
      if (!incomingProfile.isResolved()) {
        currentFuture = incomingProfile.resolve()
            .thenAccept(profile -> entityData.set(RESOLVABLE_PROFILE, Optional.of(profile)));
      } else {
        entityData.set(RESOLVABLE_PROFILE, Optional.of(incomingProfile));
      }
    } else if (incomingProfile != null && currentProfile == null) {
      // No current profile
      if (!incomingProfile.isResolved()) {
        currentFuture = incomingProfile.resolve()
            .thenAccept(profile -> entityData.set(RESOLVABLE_PROFILE, Optional.of(profile)));
      } else {
        entityData.set(RESOLVABLE_PROFILE, Optional.of(incomingProfile));
      }
    } else if (incomingProfile == null && currentProfile != null) {
      if (!currentProfile.isResolved()) {
        currentFuture = currentProfile.resolve()
            .thenAccept(profile -> entityData.set(RESOLVABLE_PROFILE, Optional.of(profile)));
      }
    } else if (incomingProfile == null) {
      if (compound.contains("owner")) {

        setGameProfileByName(compound.getString("owner").orElseThrow());
      } else if (compound.contains("OwnerUUID")) {
        setGameProfileById(compound.read("owner", UUIDUtil.CODEC).orElse(null));
      }
    }

    super.load(compound);

    if (compound.contains("NameTag")) {
      entityData.set(DATA_CUSTOM_NAME, Optional.of(Component.literal(compound.getStringOr("NameTag", ""))));
    }
    if (compound.contains("AttackAddition")) {
      var tag = compound.get("AttackAddition");
      AttributeInstance attack = this.getAttribute(Attributes.ATTACK_DAMAGE);
      if (attack != null && tag != null) {
        double value = tag.asDouble().orElse(0.0);
        if (value != 0.0) {
          if (attack.getModifier(Modifiers.ATTACK_DAMAGE_INCREASE) != null) {
            attack.removeModifier(Modifiers.ATTACK_DAMAGE_INCREASE);
          }
          attack.addPermanentModifier(new AttributeModifier(Modifiers.ATTACK_DAMAGE_INCREASE, value, AttributeModifier.Operation.ADD_VALUE));
          attackBoosted = true;
        }
      }
    }
    if (compound.contains("HealthAddition")) {
      AttributeInstance health = this.getAttribute(Attributes.MAX_HEALTH);
      var tag = compound.get("HealthAddition");
      if (health != null && tag != null) {
        double value = tag.asDouble().orElse(0.0);
        if (value != 0.0) {
          if (health.getModifier(Modifiers.HEALTH_INCREASE) != null) {
            health.removeModifier(Modifiers.HEALTH_INCREASE);
          }
          health.addPermanentModifier(new AttributeModifier(Modifiers.HEALTH_INCREASE, value, AttributeModifier.Operation.ADD_VALUE));
          // TODO: Check this logic
          if (!compound.contains("HealthWasBoosted") || !compound.getBooleanOr("HealthWasBoosted", true)) {
            this.heal((float) value);
          }
          healthBoosted = true;
        }
      }
    }
    if (compound.contains("BossBar")) {
      this.bossInfo = compound.read("BossBar", MiniBossEvent.CODEC).map(MiniBossEvent::event).orElse(null);
    }
    this.isBeingLoaded = false;
  }

  public record MiniBossEvent(
      Component name,
      BossEvent.BossBarColor color,
      BossEvent.BossBarOverlay overlay
  ) {
    public static final Codec<MiniBossEvent> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            ComponentSerialization.CODEC.fieldOf("Name").forGetter(MiniBossEvent::name),
            BossEvent.BossBarColor.CODEC.optionalFieldOf("Color", BossEvent.BossBarColor.WHITE)
                .forGetter(MiniBossEvent::color),
            BossEvent.BossBarOverlay.CODEC.optionalFieldOf("Overlay", BossEvent.BossBarOverlay.PROGRESS)
                .forGetter(MiniBossEvent::overlay)
        ).apply(instance, MiniBossEvent::new)
    );

    public MiniBossEvent (ServerBossEvent bossEvent) {
      this(bossEvent.getName(), bossEvent.getColor(), bossEvent.getOverlay());
    }

    public ServerBossEvent event () {
      return new ServerBossEvent(name, color, overlay);
    }
  }


  public static Component getDisplayName(@SuppressWarnings("OptionalUsedAsFieldOrParameterType") Optional<ResolvableProfile> incomingProfile) {
    if (incomingProfile.isEmpty()) {
      return Component.literal("(unknown)");
    }
    ResolvableProfile profile = incomingProfile.get();
    if (profile.name().isPresent()) {
      return Component.literal(profile.name().get());
    } else {
      if (profile.id().isPresent()) {
        return Component.literal(profile.id().get().toString());
      } else {
        return Component.literal("(unknown)");
      }
    }
  }

  @Override
  public void startSeenByPlayer(ServerPlayer player) {
    super.startSeenByPlayer(player);
    if (bossInfo != null) {
      this.bossInfo.addPlayer(player);
    }
  }

  @Override
  public void stopSeenByPlayer(ServerPlayer player) {
    super.stopSeenByPlayer(player);
    if (bossInfo != null) {
      this.bossInfo.removePlayer(player);
    }
  }

/*  @Override
  public boolean isPowered() {
    return getNoobVariant() == 5;
  }*/

  public static CompletableFuture<Optional<GameProfile>> fetchGameProfile(String profileName) {
    if (!StringUtil.isValidPlayerName(profileName)) {
      return CompletableFuture.completedFuture(Optional.empty());
    }
    LoadingCache<String, CompletableFuture<Optional<GameProfile>>> loadingcache = gameProfileCacheByName;
    return loadingcache != null && StringUtil.isValidPlayerName(profileName)
        ? loadingcache.getUnchecked(profileName)
        : CompletableFuture.completedFuture(Optional.empty());
  }

  public static CompletableFuture<Optional<GameProfile>> fetchGameProfile(UUID profileUuid) {
    LoadingCache<UUID, CompletableFuture<Optional<GameProfile>>> loadingcache = gameProfileCacheById;
    return loadingcache != null ? loadingcache.getUnchecked(profileUuid) : CompletableFuture.completedFuture(Optional.empty());
  }
}
