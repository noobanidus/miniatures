package noobanidus.mods.miniatures.common.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.PlayerItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.WingsLayer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.numbers.StyledFormat;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.scores.DisplaySlot;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.ReadOnlyScoreInfo;
import net.minecraft.world.scores.Scoreboard;
import noobanidus.mods.miniatures.common.client.ModelHolder;
import noobanidus.mods.miniatures.common.client.model.MiniMeModel;
import noobanidus.mods.miniatures.common.client.renderer.layers.ArrowRenderTypeLayer;
import noobanidus.mods.miniatures.common.client.renderer.layers.BeeStingerRenderTypeLayer;
import noobanidus.mods.miniatures.common.client.renderer.layers.ChargedLayer;
import noobanidus.mods.miniatures.common.client.renderer.layers.DynamicHumanoidArmorLayer;
import noobanidus.mods.miniatures.common.client.renderer.state.MiniRenderState;
import noobanidus.mods.miniatures.common.entity.MiniMeEntity;

public class MiniMeRenderer extends LivingEntityRenderer<MiniMeEntity, MiniRenderState, MiniMeModel> {
  private static final ResourceLocation TEXTURE_STEVE = ResourceLocation.withDefaultNamespace("textures/entity/player/wide/steve.png");
  public boolean isSlim = false;

  public MiniMeRenderer(EntityRendererProvider.Context context) {
    super(context, new MiniMeModel(context.bakeLayer(ModelLayers.PLAYER), false), 0.5F);
    ModelHolder.init(context);
    this.addLayer(
        new DynamicHumanoidArmorLayer<>(
            this,
            new HumanoidArmorModel<>(context.bakeLayer(ModelLayers.PLAYER_SLIM_INNER_ARMOR)),
            new HumanoidArmorModel<>(context.bakeLayer(ModelLayers.PLAYER_SLIM_OUTER_ARMOR)),
            context.getEquipmentRenderer(),
            true
        )
    );
    this.addLayer(
        new DynamicHumanoidArmorLayer<>(
            this,
            new HumanoidArmorModel<>(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)),
            new HumanoidArmorModel<>(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)),
            context.getEquipmentRenderer(),
            false
        )
    );

    this.addLayer(new PlayerItemInHandLayer<>(this));
    this.addLayer(new ChargedLayer<>(this));
    this.addLayer(new ArrowRenderTypeLayer<>(this, context));
    this.addLayer(new CustomHeadLayer<>(this, context.getModelSet()));
    this.addLayer(new WingsLayer<>(this, context.getModelSet(), context.getEquipmentRenderer()));
    this.addLayer(new BeeStingerRenderTypeLayer<>(this, context));
  }

  @Override
  public MiniRenderState createRenderState() {
    return new MiniRenderState();
  }

  @Override
  public ResourceLocation getTextureLocation(MiniRenderState entity) {
    return entity.skin.texture();
  }

  @Override
  public void render(MiniRenderState miniMeEntity, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn) {
    this.model = ModelHolder.miniMe;
    boolean shouldBeSlim = miniMeEntity.skin.model() == PlayerSkin.Model.SLIM;
    if (isSlim != shouldBeSlim) {
      isSlim = !isSlim;
    }
    this.model = isSlim ? ModelHolder.miniMeSlim : ModelHolder.miniMe;
    int noob = miniMeEntity.noobVariant;
    if (noob == 3) {
      packedLightIn = 15728880;
      this.model = ModelHolder.ghostlyMiniMe;
      if (isSlim && this.model != ModelHolder.ghostlyMiniMeSlim) {
        this.model = ModelHolder.ghostlyMiniMeSlim;
      }
    } else if (noob == 4) {
      packedLightIn = 15728880;
      this.model = ModelHolder.glowingMiniMe;
      if (isSlim && this.model != ModelHolder.glowingMiniMeSlim) {
        this.model = ModelHolder.glowingMiniMeSlim;
      }
    }
    super.render(miniMeEntity, poseStack, bufferIn, packedLightIn);
  }

  @Override
  protected void scale(MiniRenderState miniMeEntity, PoseStack poseStack) {
    float scale = miniMeEntity.noobVariant != -1 ? 1.0975f : 0.9375f;
    poseStack.scale(scale, scale, scale);
  }

  protected void setupRotations(MiniRenderState miniMeEntity, PoseStack poseStack, float f, float g) {
    super.setupRotations(miniMeEntity, poseStack, f, g);
    int noob = miniMeEntity.noobVariant;
    if (noob == 0) {
      /*      poseStack.translate(0.0D, miniMeEntity.bbHeight, 0.0D);*/
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
    // TODO: Copy player rendering stuff
  }

  @Override
  public void extractRenderState(MiniMeEntity entity, MiniRenderState state, float someFloatValue) {
    super.extractRenderState(entity, state, someFloatValue);
    HumanoidMobRenderer.extractHumanoidRenderState(entity, state, someFloatValue, this.itemModelResolver);
/*        state.leftArmPose = PlayerRenderer.getArmPose(entity, HumanoidArm.LEFT);
        state.rightArmPose = PlayerRenderer.getArmPose(entity, HumanoidArm.RIGHT);*/
    state.skin = getSkin(entity); //entity.getSkin();
    state.arrowCount = entity.getArrowCount();
    state.stingerCount = entity.getStingerCount();
    state.useItemRemainingTicks = entity.getUseItemRemainingTicks();
    state.swinging = entity.swinging;
    state.isSpectator = entity.isSpectator();
    state.showHat = true; //entity.isModelPartShown(PlayerModelPart.HAT);
    state.showJacket = true; //entity.isModelPartShown(PlayerModelPart.JACKET);
    state.showLeftPants = true; //entity.isModelPartShown(PlayerModelPart.LEFT_PANTS_LEG);
    state.showRightPants = true; //entity.isModelPartShown(PlayerModelPart.RIGHT_PANTS_LEG);
    state.showLeftSleeve = true; //entity.isModelPartShown(PlayerModelPart.LEFT_SLEEVE);
    state.showRightSleeve = true; //entity.isModelPartShown(PlayerModelPart.RIGHT_SLEEVE);
    state.showCape = true; //entity.isModelPartShown(PlayerModelPart.CAPE);
/*    PlayerRenderer.extractFlightData(entity, state, entity);
    PlayerRenderer.extractCapeState(entity, state, entity);*/
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

    state.parrotOnLeftShoulder = null; /*getParrotOnShoulder(entity, true);*/
    state.parrotOnRightShoulder = null; //getParrotOnShoulder(entity, false);
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

  public static PlayerSkin getSkin(MiniMeEntity entity) {
    return entity.getGameProfile().map(resolvableProfile -> Minecraft.getInstance().getSkinManager()
        .getInsecureSkin(resolvableProfile.gameProfile())).orElseGet(DefaultPlayerSkin::getDefaultSkin);
  }
}
