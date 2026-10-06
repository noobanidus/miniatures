package noobanidus.mods.miniatures.common.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.ProfileResolver;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.StringUtil;
import net.minecraft.util.Util;
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
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import noobanidus.mods.miniatures.common.api.MiniTags;
import noobanidus.mods.miniatures.common.api.MiniaturesAPI;
import noobanidus.mods.miniatures.common.api.Modifiers;
import noobanidus.mods.miniatures.common.entity.ai.MiniBreakBlockGoal;
import noobanidus.mods.miniatures.common.entity.ai.MiniMeleeAttackGoal;
import noobanidus.mods.miniatures.common.entity.ai.PickupPlayerGoal;
import noobanidus.mods.miniatures.common.util.NoobUtil;
import org.apache.commons.lang3.function.Consumers;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class MiniMeEntity extends Monster {
  private static final EntityDataAccessor<ResolvableProfile> RESOLVABLE_PROFILE = SynchedEntityData.defineId(MiniMeEntity.class, EntityDataSerializers.RESOLVABLE_PROFILE);

  public static final EntityDataAccessor<Integer> AGGRO = SynchedEntityData.defineId(MiniMeEntity.class, EntityDataSerializers.INT);
  public static final EntityDataAccessor<Integer> NOOB = SynchedEntityData.defineId(MiniMeEntity.class, EntityDataSerializers.INT);

  private ServerBossEvent bossInfo;

  private int pickupCooldown = 0;
  private boolean wasRidden = false;
  protected boolean adult = false;

  private boolean healthBoosted = false;
  private boolean attackBoosted = false;

  private boolean isBeingLoaded = false;
  private CompletableFuture<?> currentFuture = null;


  public MiniMeEntity(EntityType<? extends MiniMeEntity> type, Level world) {
    super(type, world);
    setPersistenceRequired();
  }

  public ResolvableProfile getResolvableProfile() {
    return entityData.get(RESOLVABLE_PROFILE);
  }

  public void setProfile(ResolvableProfile profile) {
    entityData.set(RESOLVABLE_PROFILE, profile);
    if (level() instanceof ServerLevel level) {
      resolveOnServer(level, profile);
    }
  }

  public static void loadProfile(MinecraftServer server, String name) {
    loadProfile(ResolvableProfile.createUnresolved(name), server, server.services.profileResolver(), Consumers.nop());
  }

  public static CompletableFuture<?> loadProfile(ResolvableProfile profile, MinecraftServer server, ProfileResolver resolver, Consumer<ResolvableProfile> callback) {
    if (!(profile instanceof ResolvableProfile.Dynamic)) {
      return null;
    }

    return CompletableFuture
        .supplyAsync(() -> profile.resolveProfile(resolver).join(), Util.backgroundExecutor())
        .thenAcceptAsync(gameProfile -> {
          if (!gameProfile.id().equals(Util.NIL_UUID) && gameProfile.properties()
              .containsKey("textures")) {
            callback.accept(ResolvableProfile.createResolved(gameProfile));
          }
        }, server);

  }

  private void resolveOnServer(ServerLevel level, ResolvableProfile profile) {
    if (currentFuture != null) {
      currentFuture.cancel(false);
    }
    MinecraftServer server = level.getServer();
    ProfileResolver resolver = server.services().profileResolver();
    currentFuture = loadProfile(profile, server, resolver, (newProfile) -> {
      if (!isRemoved()) {
        entityData.set(RESOLVABLE_PROFILE, newProfile);
      }
    });
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
    arg.define(RESOLVABLE_PROFILE, ResolvableProfile.createUnresolved("steve"));
    arg.define(AGGRO, -1);
    arg.define(NOOB, -1);

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
    entityData.set(NOOB, variant);
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
    setProfile(ResolvableProfile.createUnresolved(name.toLowerCase(Locale.ROOT)));
  }

  public void setGameProfileById(UUID id) {
    setProfile(ResolvableProfile.createUnresolved(id));
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
    if (level().isClientSide()) {
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

  private boolean profileFromSave = false;

  @Override
  public void setCustomName(@Nullable Component name) {
    super.setCustomName(name);
    if (name == null) {
      return;
    }

    if (bossInfo != null) {
      bossInfo.setName(name);
    }

    if (isBeingLoaded && profileFromSave) {
      return;
    }

    String username = name.getString().toLowerCase(Locale.ROOT);
    if (!StringUtil.isValidPlayerName(username)) {
      MiniaturesAPI.LOG.debug("Custom name '{}' is not a valid player name; keeping current profile", username);
      return;
    }

    ResolvableProfile current = getResolvableProfile();
    boolean sameName = current.name().map(username::equalsIgnoreCase).orElse(false);
    if (sameName && !(current instanceof ResolvableProfile.Dynamic)) {
      return;
    }

    setProfile(ResolvableProfile.createUnresolved(username));
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
  public void addAdditionalSaveData(ValueOutput output) {
    super.addAdditionalSaveData(output);

    output.store("resolvableProfile", ResolvableProfile.CODEC, getResolvableProfile());

    output.putInt("Noob", entityData.get(NOOB));

    output.putInt("pickupCooldown", pickupCooldown);
    if (healthBoosted) {
      AttributeInstance health = this.getAttribute(Attributes.MAX_HEALTH);
      if (health != null) {
        AttributeModifier mod = health.getModifier(Modifiers.HEALTH_INCREASE);
        if (mod != null) {
          output.putDouble("HealthAddition", mod.amount());
          output.putBoolean("HealthWasBoosted", true);
        }
      }
    }
    if (attackBoosted) {
      AttributeInstance attack = this.getAttribute(Attributes.ATTACK_DAMAGE);
      if (attack != null) {
        AttributeModifier mod = attack.getModifier(Modifiers.ATTACK_DAMAGE_INCREASE);
        if (mod != null) {
          output.putDouble("AttackAddition", mod.amount());
        }
      }
    }

    output.putInt("Hostile", entityData.get(AGGRO));

    if (bossInfo != null) {
      output.store("BossBar", MiniBossEvent.CODEC, new MiniBossEvent(bossInfo));
    }
  }

  @Override
  public void readAdditionalSaveData(ValueInput tag) {
    super.readAdditionalSaveData(tag);
    this.pickupCooldown = tag.getIntOr("pickupCooldown", 0);
    var noobVariant = tag.getIntOr("Noob", -1);
    if (noobVariant == -1) {
      noobVariant = random.nextInt(10);
    }
    this.setNoobVariant(noobVariant);
    this.setAggro(tag.getIntOr("Hostile", 0));
  }

  @Override
  public void load(ValueInput compound) {
    this.isBeingLoaded = true;

    ResolvableProfile saved = compound.read("resolvableProfile", ResolvableProfile.CODEC).orElse(null);
    this.profileFromSave = saved != null;

    if (saved != null) {
      setProfile(saved);
    } else {

    }


    ResolvableProfile currentProfile = getResolvableProfile();

    UUID ownerUuid = compound.read("owner", UUIDUtil.CODEC).orElse(null);

    String owner = compound.getStringOr("owner", "");

    if (ownerUuid != null) {
      setGameProfileById(ownerUuid);
    } else if (!owner.isEmpty()) {
      setGameProfileByName(owner);
    }

    super.load(compound);

    this.isBeingLoaded = false;

    double attackAddition = compound.getDoubleOr("AttackAddition", 0.0);
    if (attackAddition != 0.0) {
      AttributeInstance attack = this.getAttribute(Attributes.ATTACK_DAMAGE);
      if (attack != null) {
        if (attack.getModifier(Modifiers.ATTACK_DAMAGE_INCREASE) != null) {
          attack.removeModifier(Modifiers.ATTACK_DAMAGE_INCREASE);
        }
        attack.addPermanentModifier(new AttributeModifier(Modifiers.ATTACK_DAMAGE_INCREASE, attackAddition, AttributeModifier.Operation.ADD_VALUE));
        attackBoosted = true;
      }
    }

    double healthAddition = compound.getDoubleOr("HealthAddition", 0.0);
    if (healthAddition != 0.0) {
      AttributeInstance health = this.getAttribute(Attributes.MAX_HEALTH);
      if (health != null) {
        if (health.getModifier(Modifiers.HEALTH_INCREASE) != null) {
          health.removeModifier(Modifiers.HEALTH_INCREASE);
        }
        health.addPermanentModifier(new AttributeModifier(Modifiers.HEALTH_INCREASE, healthAddition, AttributeModifier.Operation.ADD_VALUE));
        if (compound.getBooleanOr("HealthWasBoosted", true)) {
          this.heal((float) healthAddition);
        }
        healthBoosted = true;
      }
    }

    this.bossInfo = compound.read("BossBar", MiniBossEvent.CODEC).map(MiniBossEvent::event).orElse(null);
  }

  public record MiniBossEvent(
      UUID id,
      Component name,
      BossEvent.BossBarColor color,
      BossEvent.BossBarOverlay overlay
  ) {
    public static final Codec<MiniBossEvent> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            UUIDUtil.CODEC.fieldOf("Id").forGetter(MiniBossEvent::id),
            ComponentSerialization.CODEC.fieldOf("Name").forGetter(MiniBossEvent::name),
            BossEvent.BossBarColor.CODEC.optionalFieldOf("Color", BossEvent.BossBarColor.WHITE)
                .forGetter(MiniBossEvent::color),
            BossEvent.BossBarOverlay.CODEC.optionalFieldOf("Overlay", BossEvent.BossBarOverlay.PROGRESS)
                .forGetter(MiniBossEvent::overlay)
        ).apply(instance, MiniBossEvent::new)
    );

    public MiniBossEvent(ServerBossEvent bossEvent) {
      this(bossEvent.getId(), bossEvent.getName(), bossEvent.getColor(), bossEvent.getOverlay());
    }

    public ServerBossEvent event() {
      return new ServerBossEvent(id, name, color, overlay);
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

  public boolean isPowered() {
    return getNoobVariant() == 5;
  }
}
