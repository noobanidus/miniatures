package noobanidus.mods.miniatures.common.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.PlayerSkinRenderCache;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.WingsLayer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.numbers.StyledFormat;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.PlayerModelType;
import net.minecraft.world.entity.player.PlayerSkin;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.DisplaySlot;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.ReadOnlyScoreInfo;
import net.minecraft.world.scores.Scoreboard;
import noobanidus.mods.miniatures.common.client.ModelHolder;
import noobanidus.mods.miniatures.common.client.model.MiniMeModel;
import noobanidus.mods.miniatures.common.client.model.MiniRenderModel;
import noobanidus.mods.miniatures.common.client.renderer.layers.*;
import noobanidus.mods.miniatures.common.client.renderer.state.MiniRenderState;
import noobanidus.mods.miniatures.common.entity.MaxiMeEntity;
import noobanidus.mods.miniatures.common.entity.MiniMeEntity;

public class MiniMeRenderer extends LivingEntityRenderer<MiniMeEntity, MiniRenderState, MiniMeModel> {
  public boolean isSlim = false;
  private final PlayerSkinRenderCache cache;

  public MiniMeRenderer(EntityRendererProvider.Context context) {
    super(context, new MiniMeModel(context.bakeLayer(ModelLayers.PLAYER), false), 0.25F);
    ModelHolder.init(context);
    this.addLayer(
        new DynamicHumanoidArmorLayer<>(
            this,
            ArmorModelSet.bake(
                ModelLayers.PLAYER_SLIM_ARMOR, context.getModelSet(), part -> new MiniRenderModel(RenderTypes::entityCutout, part, true)
            ),
            context.getEquipmentRenderer(),
            true
        )
    );
    this.addLayer(
        new DynamicHumanoidArmorLayer<>(
            this,
            ArmorModelSet.bake(
                ModelLayers.PLAYER_ARMOR, context.getModelSet(), part -> new MiniRenderModel(RenderTypes::entityCutout, part, false)
            ),
            context.getEquipmentRenderer(),
            false
        )
    );
    this.addLayer(new MiniItemInHandLayer(this));
    this.addLayer(new ChargedLayer<>(this));
    this.addLayer(new ArrowRenderTypeLayer<>(this, context));
    this.addLayer(new CustomHeadLayer<>(this, context.getModelSet(), context.getPlayerSkinRenderCache()));
    this.addLayer(new WingsLayer<>(this, context.getModelSet(), context.getEquipmentRenderer()));
    this.addLayer(new BeeStingerRenderTypeLayer<>(this, context));
    this.cache = context.getPlayerSkinRenderCache();
  }

  @Override
  public Vec3 getRenderOffset(MiniRenderState p_360756_) {
    Vec3 vec3 = super.getRenderOffset(p_360756_);
    return p_360756_.isCrouching ? vec3.add(0.0, p_360756_.scale * -2.0F / 16.0, 0.0) : vec3;
  }

  @Override
  public MiniRenderState createRenderState() {
    return new MiniRenderState();
  }

  @Override
  public Identifier getTextureLocation(MiniRenderState entity) {
    return entity.skin.body().texturePath();
  }

  @Override
  public void submit(MiniRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
    this.model = ModelHolder.miniMe;
    boolean shouldBeSlim = state.skin.model() == PlayerModelType.SLIM;
    if (isSlim != shouldBeSlim) {
      isSlim = !isSlim;
    }
    this.model = isSlim ? ModelHolder.miniMeSlim : ModelHolder.miniMe;
    int noob = state.noobVariant;
    if (noob == 3) {
      state.lightCoords = 15728880;
      this.model = ModelHolder.ghostlyMiniMe;
      if (isSlim && this.model != ModelHolder.ghostlyMiniMeSlim) {
        this.model = ModelHolder.ghostlyMiniMeSlim;
      }
    } else if (noob == 4) {
      state.lightCoords = 15728880;
      this.model = ModelHolder.glowingMiniMe;
      if (isSlim && this.model != ModelHolder.glowingMiniMeSlim) {
        this.model = ModelHolder.glowingMiniMeSlim;
      }
    }

    poseStack.pushPose();
    if (state.hasPose(Pose.SLEEPING)) {
      Direction bedOrientation = state.bedOrientation;
      if (bedOrientation != null) {
        float headOffset = state.eyeHeight - 0.1F;
        poseStack.translate(-bedOrientation.getStepX() * headOffset, 0.0F, -bedOrientation.getStepZ() * headOffset);
      }
    }

    float scale = state.scale;
    poseStack.scale(scale, scale, scale);
    this.setupRotations(state, poseStack, state.bodyRot, scale);
    poseStack.scale(-1.0F, -1.0F, 1.0F);
    this.scale(state, poseStack);
    poseStack.translate(0.0F, -1.501F, 0.0F);
    boolean isBodyVisible = this.isBodyVisible(state);
    boolean forceTransparent = !isBodyVisible && !state.isInvisibleToPlayer;
    RenderType renderType = this.getRenderType(state, isBodyVisible, forceTransparent, state.appearsGlowing());
    if (renderType != null) {
      int overlayCoords = getOverlayCoords(state, this.getWhiteOverlayProgress(state));
      int baseColor = forceTransparent ? 654311423 : -1;
      int tintedColor = ARGB.multiply(baseColor, this.getModelTint(state));
      submitNodeCollector.submitModel(
          this.model, state, poseStack, renderType, state.lightCoords, overlayCoords, tintedColor, null, state.outlineColor, null
      );
    }

    if (this.shouldRenderLayers(state) && !this.layers.isEmpty()) {
      this.model.setupAnim(state);

      for (RenderLayer<MiniRenderState, MiniMeModel> layer : this.layers) {
        layer.submit(poseStack, submitNodeCollector, state.lightCoords, state, state.yRot, state.xRot);
      }
    }

    poseStack.popPose();
    if (state.leashStates != null) {
      for (EntityRenderState.LeashState leashState : state.leashStates) {
        submitNodeCollector.submitLeash(poseStack, leashState);
      }
    }

    this.submitNameDisplay(state, poseStack, submitNodeCollector, camera);
  }

  @Override
  protected void scale(MiniRenderState miniMeEntity, PoseStack poseStack) {
    float scale = miniMeEntity.isMaxi ? 3.5375f : (miniMeEntity.noobVariant != -1 ? 1.0975f : 0.9375f) * miniMeEntity.ageScale;
    poseStack.scale(scale, scale, scale);
  }

  protected void setupRotations(MiniRenderState miniMeEntity, PoseStack poseStack, float f, float g) {
    super.setupRotations(miniMeEntity, poseStack, f, g);
    int noob = miniMeEntity.noobVariant;
    if (noob == 0) {
      poseStack.translate(0.0D, miniMeEntity.bbHeight, 0.0D);
      poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
    } else if (noob == 1) {
      poseStack.translate(0.0D, 0.35F, 0.0D);
    } else if (noob == 6) {
      poseStack.mulPose(Axis.YP.rotationDegrees(90.0f));
    } else if (noob == 7) {
      poseStack.mulPose(Axis.YP.rotationDegrees(-90.0f));
    } else if (noob == 8) {
      poseStack.mulPose(Axis.YP.rotationDegrees(180.0f));
    }
  }

  @Override
  protected boolean shouldShowName(MiniMeEntity entity, double distanceToCameraSq) {
    return super.shouldShowName(entity, distanceToCameraSq)
        && (entity.shouldShowName() || entity.hasCustomName() && entity == this.entityRenderDispatcher.crosshairPickEntity);
  }

  @Override
  protected float getShadowRadius(MiniRenderState state) {
    return super.getShadowRadius(state) * state.ageScale;
  }

  @Override
  public void extractRenderState(MiniMeEntity entity, MiniRenderState state, float someFloatValue) {
    super.extractRenderState(entity, state, someFloatValue);
    state.isPowered = entity.isPowered();
    state.bbHeight = entity.getBbHeight() + 0.25;
    state.noobVariant = entity.getNoobVariant();
    HumanoidMobRenderer.extractHumanoidRenderState(entity, state, someFloatValue, this.itemModelResolver);
    state.isMaxi = entity instanceof MaxiMeEntity;
    state.skin = getSkin(entity); //entity.getSkin();
    state.arrowCount = entity.getArrowCount();
    state.stingerCount = entity.getStingerCount();
    if (state.distanceToCameraSq < 100.0) {
      Scoreboard scoreboard = entity.level().getScoreboard();
      Objective objective = scoreboard.getDisplayObjective(DisplaySlot.BELOW_NAME);
      if (objective != null) {
        ReadOnlyScoreInfo readonlyscoreinfo = scoreboard.getPlayerScoreInfo(entity, objective);
        Component component = ReadOnlyScoreInfo.safeFormatValue(readonlyscoreinfo, objective.numberFormatOrDefault(StyledFormat.NO_STYLE));
        state.scoreText = Component.empty().append(component).append(CommonComponents.SPACE)
            .append(objective.getDisplayName());
      } else {
        state.scoreText = null;
      }
    } else {
      state.scoreText = null;
    }

    state.id = entity.getId();
/*        state.name = entity.getGameProfile().ifPresent(o ->
            if (o.)).flatMap(GameProfile::getName).orElse("Minime");*/
    state.heldOnHead.clear();
    if (state.isUsingItem) {
      ItemStack itemstack = entity.getItemInHand(state.useItemHand);
      if (itemstack.is(Items.SPYGLASS)) {
        this.itemModelResolver.updateForLiving(state.heldOnHead, itemstack, ItemDisplayContext.HEAD, entity);
      }
    }
  }

  public PlayerSkin getSkin(MiniMeEntity entity) {
    return cache.getOrDefault(entity.getResolvableProfile()).playerSkin();
  }
}
