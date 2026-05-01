package noobanidus.mods.miniatures.common.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.component.ResolvableProfile;
import noobanidus.mods.miniatures.common.api.client.Layers;
import noobanidus.mods.miniatures.common.client.ModelHolder;
import noobanidus.mods.miniatures.common.client.model.MiniMeModel;
import noobanidus.mods.miniatures.common.client.renderer.layers.ArrowRenderTypeLayer;
import noobanidus.mods.miniatures.common.client.renderer.layers.BeeStingerRenderTypeLayer;
import noobanidus.mods.miniatures.common.client.renderer.layers.ChargedLayer;
import noobanidus.mods.miniatures.common.client.renderer.state.MiniRenderState;
import noobanidus.mods.miniatures.common.entity.MiniMeEntity;
import noobanidus.mods.miniatures.common.util.NoobUtil;

// TODO:
public class MiniMeRenderer extends HumanoidMobRenderer<MiniMeEntity, MiniRenderState, MiniMeModel> {
  private static final ResourceLocation TEXTURE_STEVE = ResourceLocation.withDefaultNamespace("textures/entity/player/wide/steve.png");
  public boolean isSlim = false;

  @SuppressWarnings("unchecked")
  public MiniMeRenderer(EntityRendererProvider.Context context) {
    super(context, new MiniMeModel<>(context.bakeLayer(Layers.MINI_ME), false), 0.5f);
    ModelHolder.init(context);
    this.addLayer(new ItemInHandLayer<>(this));
    this.addLayer(new ArrowRenderTypeLayer<>(context, this));
    this.addLayer(new CustomHeadLayer<>(this, context.getModelSet(), context.getItemInHandRenderer()));
    this.addLayer(new ElytraLayer<>(this, context.getModelSet()));
    this.addLayer(new BeeStingerRenderTypeLayer<>(this));
    this.addLayer(new HumanoidArmorLayer<>(this, new HumanoidModel<>(context.bakeLayer(Layers.MINI_ME_ARMOR)), new HumanoidModel<>(context.bakeLayer(Layers.MINI_ME_ARMOR)), context.getModelManager()));
    this.addLayer(new ChargedLayer<>(this));
  }

  @Override
  public MiniRenderState createRenderState() {
    return null;
  }

  @Override
  public ResourceLocation getTextureLocation(MiniRenderState entity) {
    return entity.getGameProfile()
        .map(MiniMeRenderer::getSkin)
        .orElse(TEXTURE_STEVE);
  }

  public static ResourceLocation getSkin(ResolvableProfile resolvableProfile) {
    SkinManager skinmanager = Minecraft.getInstance().getSkinManager();
    if (resolvableProfile != null) {
      return skinmanager.getInsecureSkin(resolvableProfile.gameProfile()).texture();
    } else {
      return TEXTURE_STEVE;
    }
  }

  @Override
  public void render(MiniRenderState miniMeEntity, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn) {
    this.model = ModelHolder.miniMe;
    SkinManager skinmanager = Minecraft.getInstance().getSkinManager();
    if (miniMeEntity.getGameProfile().isPresent()) {
      if (isSlim != skinmanager.getInsecureSkin(miniMeEntity.getGameProfile().get().gameProfile()).model().id()
          .equals("slim"))
        isSlim = !isSlim;
    }
    this.model = isSlim ? ModelHolder.miniMeSlim : ModelHolder.miniMe;
    int noob = miniMeEntity.getNoobVariant();
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
    super.render(miniMeEntity, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
  }

  protected void scale(MiniRenderState miniMeEntity, PoseStack poseStack, float partialTickTime) {
    float scale = (NoobUtil.isNoob(miniMeEntity) ? 1.0975f : 0.9375f) * miniMeEntity.getAgeScale();
    poseStack.scale(scale, scale, scale);
  }

  protected void setupRotations(MiniRenderState miniMeEntity, PoseStack poseStack, float f, float g) {
    super.setupRotations(miniMeEntity, poseStack, f, g);
    // TODO: Move this into the extract state
    int noob = miniMeEntity.getNoobVariant();
    if (noob == 0) {
      poseStack.translate(0.0D, miniMeEntity.getBbHeight() + 0.25F, 0.0D);
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
}
