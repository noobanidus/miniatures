package noobanidus.mods.miniatures.entity;

import com.google.common.collect.Iterables;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.properties.Property;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.GameProfileCache;
import net.minecraft.util.StringUtil;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import noobanidus.libs.noobutil.type.LazySupplier;
import noobanidus.mods.miniatures.MiniTags;
import noobanidus.mods.miniatures.config.ConfigManager;
import noobanidus.mods.miniatures.entity.ai.MiniBreakBlockGoal;
import noobanidus.mods.miniatures.entity.ai.MiniMeleeAttackGoal;
import noobanidus.mods.miniatures.entity.ai.PickupPlayerGoal;
import noobanidus.mods.miniatures.init.ModModifiers;
import noobanidus.mods.miniatures.init.ModSerializers;
import noobanidus.mods.miniatures.util.NoobUtil;
import noobanidus.mods.miniatures.util.NullProfileCache;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

@OnlyIn(
    value = Dist.CLIENT,
    _interface = PowerableMob.class
)
public class MiniMeEntity extends Monster implements PowerableMob {
  @SuppressWarnings("unchecked")
  private static final LazySupplier<EntityDataAccessor<Optional<GameProfile>>> GAMEPROFILE = new LazySupplier<>(() -> (EntityDataAccessor<Optional<GameProfile>>) SynchedEntityData.defineId(MiniMeEntity.class, ModSerializers.OPTIONAL_GAME_PROFILE.get()));
  public static final EntityDataAccessor<Integer> AGGRO = SynchedEntityData.defineId(MiniMeEntity.class, EntityDataSerializers.INT);
  public static final EntityDataAccessor<Byte> NOOB = SynchedEntityData.defineId(MiniMeEntity.class, EntityDataSerializers.BYTE);
  public static final EntityDataAccessor<Float> SCALE = SynchedEntityData.defineId(MiniMeEntity.class, EntityDataSerializers.FLOAT);

  private ServerBossEvent bossInfo;

  private static GameProfileCache profileCache;
  private static MinecraftSessionService sessionService;
  private int pickupCooldown = 0;
  private boolean wasRidden = false;
  protected boolean adult = false;

  private boolean healthBoosted = false;
  private boolean attackBoosted = false;
  private int scaleChanged = -1;
  private boolean isSlim;

  private String owner;
  private UUID ownerId;

  // TODO:
  @Nullable
  public static GameProfile updateGameProfile(@Nullable GameProfile input) {
    if (input != null && !StringUtil.isNullOrEmpty(input.getName())) {
      if (NullProfileCache.isCachedNull(input.getName(), null)) {
        return input;
      }

      if (input.isComplete() && input.getProperties().containsKey("textures")) {
        return input;
      } else if (profileCache != null && sessionService != null) {
        Optional<GameProfile> gameprofile = profileCache.get(input.getName());
        if (!gameprofile.isPresent()) {
          NullProfileCache.cacheNull(input.getName(), input.getId());
          return input;
        } else {
          GameProfile profile = gameprofile.get();
          Property property = Iterables.getFirst(profile.getProperties().get("textures"), null);
          if (property == null) {
            //Miniatures.LOG.info("Refilling cache for gameprofile: " + profile);
            profile = sessionService.fillProfileProperties(profile, true);
          }

          if (!profile.isComplete()) {
            NullProfileCache.cacheNull(profile.getName(), profile.getId());
          }

          return profile;
        }
      } else {
        return input;
      }
    } else {
      return input;
    }
  }

  public static void setProfileCache(GameProfileCache profileCache) {
    MiniMeEntity.profileCache = profileCache;
  }

  public static void setSessionService(MinecraftSessionService sessionService) {
    MiniMeEntity.sessionService = sessionService;
  }

  public MiniMeEntity(EntityType<? extends MiniMeEntity> type, Level world) {
    super(type, world);
    setPersistenceRequired();
  }

  public boolean getHostile() {
    int aggro = getAggro();
    if (aggro == -1) {
      return ConfigManager.getHostile();
    }
    return aggro == 1;
  }

  // TODO: Properly handle the relative offset for huge entities? And tiny.
  @Override
  public double getPassengersRidingOffset() {
    return super.getPassengersRidingOffset();
  }

  @Override
  public boolean isPreventingPlayerRest(Player p_230292_1_) {
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
  protected void defineSynchedData() {
    super.defineSynchedData();
    this.entityData.define(GAMEPROFILE.get(), Optional.empty());
    this.entityData.define(AGGRO, -1);
    this.entityData.define(NOOB, (byte) random.nextInt(20));
    this.entityData.define(SCALE, 1f);

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

  public int getNoobVariant() {
    if (!NoobUtil.isNoob(this)) {
      return -1;
    }
    return entityData.get(NOOB);
  }

  public void setNoobVariant(int variant) {
    entityData.set(NOOB, (byte) variant);
  }

  public float getMiniScale() {
    return entityData.get(SCALE);
  }

  public void setMiniScale(float scale) {
    entityData.set(SCALE, scale);
  }

  public void setSlim(boolean slim) {
    this.isSlim = slim;
  }

  public boolean isSlim() {
    return this.isSlim;
  }

  public int getAggro() {
    return entityData.get(AGGRO);
  }

  public void setAggro(int aggro) {
    entityData.set(AGGRO, aggro);
  }

  public static AttributeSupplier.Builder attributes() {
    return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, ConfigManager.getMaxHealth()).add(Attributes.MOVEMENT_SPEED, ConfigManager.getMovementSpeed()).add(Attributes.ATTACK_DAMAGE, ConfigManager.getAttackDamage()).add(Attributes.ARMOR, ConfigManager.getArmorValue());
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

  public Optional<GameProfile> getGameProfile() {
    return entityData.get(GAMEPROFILE.get());
  }

  public void setGameProfile(String name) {
    setGameProfile(new GameProfile(null, name));
  }

  public void setGameProfile(UUID id) {
    setGameProfile(new GameProfile(id, null));
  }

  public void setGameProfile(GameProfile playerProfile) {
    if (NullProfileCache.isCachedNull(playerProfile.getName(), playerProfile.getId())) {
      return;
    }

    GameProfile profile = updateGameProfile(playerProfile);
    if (profile != null) {
      setGameProfileInternal(profile);
    } else {
      setGameProfileInternal(null);
      NullProfileCache.cacheNull(playerProfile.getName(), playerProfile.getId());
    }
  }

  protected void setGameProfileInternal(GameProfile playerProfile) {
    if (playerProfile != null) {
      owner = playerProfile.getName();
      ownerId = playerProfile.getId();
      entityData.set(GAMEPROFILE.get(), Optional.of(playerProfile));
    } else {
      entityData.set(GAMEPROFILE.get(), Optional.empty());
    }
  }

  @Override
  protected PathNavigation createNavigation(Level worldIn) {
    GroundPathNavigation navigator = new GroundPathNavigation(this, worldIn);
    setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
    navigator.setCanFloat(true);
    return navigator;
  }

  @Override
  public boolean hurt(DamageSource source, float amount) {
    if (ConfigManager.getImmune() && !(source.getEntity() instanceof Player) && source != DamageSource.OUT_OF_WORLD) {
      return false;
    }
    return super.hurt(source, amount);
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
    if (level.isClientSide) {
      int noob = getNoobVariant();
      if (tickCount % 4 == 0 && noob == 1) {
        level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, getX(), getY() + 0.3, getZ(), 0, 0, 0);
      }
    }
    if (scaleChanged == -1) {
      scaleChanged = tickCount + 20;
    } else if (this.tickCount > scaleChanged && scaleChanged != 0) {
      this.refreshDimensions();
      scaleChanged = 0;
    }
    if (bossInfo != null) {
      this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
    }
  }

  @Override
  public double getMyRidingOffset() {
    return super.getMyRidingOffset();
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

    if (name != null && this.bossInfo != null) {
      this.bossInfo.setName(name);
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

    if (owner != null) {
      compound.putString("owner", owner);
    }
    if (ownerId != null) {
      compound.putUUID("OwnerUUID", ownerId);
    }

    getGameProfile().ifPresent(profile -> {
      compound.put("gameProfile", NbtUtils.writeGameProfile(new CompoundTag(), profile));
      // TODO: Are these necessary?
      if (owner == null) {
        compound.putString("owner", profile.getName());
      }
      if (ownerId == null) {
        compound.putUUID("OwnerUUID", profile.getId());
      }
    });

    compound.putByte("Noob", (byte) getNoobVariant());
    compound.putFloat("Scale", getMiniScale());

    compound.putInt("pickupCooldown", pickupCooldown);
    if (healthBoosted) {
      AttributeInstance health = this.getAttribute(Attributes.MAX_HEALTH);
      if (health != null) {
        AttributeModifier mod = health.getModifier(ModModifiers.HEALTH_INCREASE);
        if (mod != null) {
          compound.putDouble("HealthAddition", mod.getAmount());
          compound.putBoolean("HealthWasBoosted", true);
        }
      }
    }
    if (attackBoosted) {
      AttributeInstance attack = this.getAttribute(Attributes.ATTACK_DAMAGE);
      if (attack != null) {
        AttributeModifier mod = attack.getModifier(ModModifiers.ATTACK_DAMAGE_INCREASE);
        if (mod != null) {
          compound.putDouble("AttackAddition", mod.getAmount());
        }
      }
    }

    compound.putInt("Hostile", getAggro());

    if (bossInfo != null) {
      compound.putBoolean("BossBar", true);
      compound.putString("BossBarColor", bossInfo.getColor().getName());
      compound.putString("BossBarOverlay", bossInfo.getOverlay().getName());
    }
  }

  @Override
  public void readAdditionalSaveData(CompoundTag tag) {
    super.readAdditionalSaveData(tag);
    this.pickupCooldown = tag.getInt("pickupCooldown");
    if (tag.contains("Noob")) {
      this.setNoobVariant(tag.getByte("Noob"));
    }
    if (tag.contains("Scale")) {
      this.setMiniScale(tag.getFloat("Scale"));
    }
    if (tag.contains("Hostile")) {
      this.setAggro(tag.getInt("Hostile"));
    }
  }

  @Override
  public void load(CompoundTag compound) {
    super.load(compound);

    GameProfile incomingProfile = null;
    String incomingOwner = null;
    UUID incomingUuid = null;

    if (compound.contains("gameProfile")) {
      incomingProfile = NbtUtils.readGameProfile(compound.getCompound("gameProfile"));
    }

    if (incomingProfile == null) {
      if (compound.contains("owner", Tag.TAG_STRING)) {
        incomingOwner = compound.getString("owner");
        incomingProfile = new GameProfile(null, incomingOwner);
      } else if (compound.hasUUID("OwnerUUID")) {
        incomingUuid = compound.getUUID("OwnerUUID");
        incomingProfile = new GameProfile(incomingUuid, null);
      }
    }

    GameProfile currentProfile = getGameProfile().orElse(null);

    if (incomingProfile != null && currentProfile != null && ((incomingProfile.getId() != null && !incomingProfile.getId().equals(currentProfile.getId())) || (incomingProfile.getName() != null && !incomingProfile.getName().equals(currentProfile.getName())))) {
      setGameProfile(incomingProfile);
    } else if (incomingProfile != null && currentProfile == null) {
      setGameProfile(incomingProfile);
    }

    if (compound.contains("NameTag", Tag.TAG_STRING)) {
      entityData.set(DATA_CUSTOM_NAME, Optional.of(Component.literal(compound.getString("NameTag"))));
    }
    if (compound.contains("AttackAddition")) {
      AttributeInstance attack = this.getAttribute(Attributes.ATTACK_DAMAGE);
      if (attack != null) {
        double value = 0.0;
        if (compound.contains("AttackAddition", Tag.TAG_FLOAT)) {
          value = compound.getFloat("AttackAddition");
        } else if (compound.contains("AttackAddition", Tag.TAG_INT)) {
          value = compound.getInt("AttackAddition");
        } else if (compound.contains("AttackAddition", Tag.TAG_DOUBLE)) {
          value = compound.getDouble("AttackAddition");
        }
        if (value != 0.0) {
          if (attack.getModifier(ModModifiers.ATTACK_DAMAGE_INCREASE) != null) {
            attack.removeModifier(ModModifiers.ATTACK_DAMAGE_INCREASE);
          }
          attack.addPermanentModifier(new AttributeModifier(ModModifiers.ATTACK_DAMAGE_INCREASE, "Sublimiter Torture Mechanism", value, AttributeModifier.Operation.ADDITION));
          attackBoosted = true;
        }
      }
    }
    if (compound.contains("HealthAddition")) {
      AttributeInstance health = this.getAttribute(Attributes.MAX_HEALTH);
      if (health != null) {
        double value = 0.0;
        if (compound.contains("HealthAddition", Tag.TAG_FLOAT)) {
          value = compound.getFloat("HealthAddition");
        } else if (compound.contains("HealthAddition", Tag.TAG_INT)) {
          value = compound.getInt("HealthAddition");
        } else if (compound.contains("HealthAddition", Tag.TAG_DOUBLE)) {
          value = compound.getDouble("HealthAddition");
        }
        if (value != 0.0) {
          if (health.getModifier(ModModifiers.HEALTH_INCREASE) != null) {
            health.removeModifier(ModModifiers.HEALTH_INCREASE);
          }
          health.addPermanentModifier(new AttributeModifier(ModModifiers.HEALTH_INCREASE, "Sublimiter Torture Mechanism", value, AttributeModifier.Operation.ADDITION));
          if (!compound.contains("HealthWasBoosted") || !compound.getBoolean("HealthWasBoosted")) {
            this.heal((float) value);
          }
          healthBoosted = true;
        }
      }
    }
    if (compound.contains("BossBar", Tag.TAG_BYTE) && compound.getBoolean("BossBar")) {
      BossEvent.BossBarColor bossInfoColor = BossEvent.BossBarColor.WHITE;
      BossEvent.BossBarOverlay bossInfoOverlay = BossEvent.BossBarOverlay.PROGRESS;
      if (compound.contains("BossBarColor", Tag.TAG_STRING)) {
        bossInfoColor = BossEvent.BossBarColor.byName(compound.getString("BossBarColor").toLowerCase());
      }
      if (compound.contains("BossBarOverlay", Tag.TAG_STRING)) {
        bossInfoOverlay = BossEvent.BossBarOverlay.byName(compound.getString("BossBarOverlay").toLowerCase());
      }
      Component name = Component.literal("Unknown Mini");
      if (getGameProfile().isPresent()) {
        name = ComponentUtils.getDisplayName(getGameProfile().get());
      }

      bossInfo = new ServerBossEvent(name, bossInfoColor, bossInfoOverlay);
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

  @Override
  public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
    if (GAMEPROFILE.get().equals(key) && this.level.isClientSide()) {
      this.getGameProfile().ifPresent(gameprofile -> {
        if (gameprofile.isComplete()) {
          Minecraft.getInstance().getSkinManager().registerSkins(gameprofile, (textureType, textureLocation, profileTexture) -> {
            if (textureType.equals(MinecraftProfileTexture.Type.SKIN)) {
              String metadata = profileTexture.getMetadata("model");
              this.setSlim(metadata != null && metadata.equals("slim"));
            }
          }, true);
        }
      });
    }
  }

  @Override
  protected float getStandingEyeHeight(Pose pPose, EntityDimensions pSize) {
    return pSize.height * 0.93f;
  }

  @Override
  public float getScale() {
    return 1.0f;
  }

  @Override
  public boolean isPowered() {
    return getNoobVariant() == 5;
  }
}
