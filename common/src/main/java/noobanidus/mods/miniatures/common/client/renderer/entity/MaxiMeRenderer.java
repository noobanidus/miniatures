package noobanidus.mods.miniatures.common.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
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
import noobanidus.mods.miniatures.common.api.client.Layers;
import noobanidus.mods.miniatures.common.client.ModelHolder;
import noobanidus.mods.miniatures.common.client.model.MiniMeModel;
import noobanidus.mods.miniatures.common.client.renderer.layers.ArrowRenderTypeLayer;
import noobanidus.mods.miniatures.common.client.renderer.layers.BeeStingerRenderTypeLayer;
import noobanidus.mods.miniatures.common.entity.MiniMeEntity;

public class MaxiMeRenderer extends HumanoidMobRenderer<MiniMeEntity, MiniMeModel<MiniMeEntity>> {
  private static final ResourceLocation TEXTURE_STEVE = ResourceLocation.withDefaultNamespace("textures/entity/player/wide/steve.png");
  public boolean isSlim = false;

  public MaxiMeRenderer(EntityRendererProvider.Context context) {
    super(context, new MiniMeModel<>(context.bakeLayer(Layers.MINI_ME), false), 0.5f);
    ModelHolder.init(context);
    this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
    this.addLayer(new ArrowRenderTypeLayer<>(context, this));
    this.addLayer(new CustomHeadLayer<>(this, context.getModelSet(), context.getItemInHandRenderer()));
    this.addLayer(new ElytraLayer<>(this, context.getModelSet()));
    this.addLayer(new BeeStingerRenderTypeLayer<>(this));
    this.addLayer(new HumanoidArmorLayer<>(this, new HumanoidModel<>(context.bakeLayer(Layers.MINI_ME_ARMOR)), new HumanoidModel<>(context.bakeLayer(Layers.MINI_ME_ARMOR)), context.getModelManager()));
  }

  @Override
  public ResourceLocation getTextureLocation(MiniMeEntity entity) {
    return entity.getGameProfile()
        .map(MiniMeRenderer::getSkin)
        .orElse(TEXTURE_STEVE);
  }


  @Override
  public void render(MiniMeEntity miniMeEntity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn) {
    SkinManager skinmanager = Minecraft.getInstance().getSkinManager();
    if (miniMeEntity.getGameProfile().isPresent()) {
      if (isSlim != skinmanager.getInsecureSkin(miniMeEntity.getGameProfile().get().gameProfile()).model().id().equals("slim"))
        isSlim = !isSlim;
    }
    this.model = isSlim ? ModelHolder.miniMeSlim : ModelHolder.miniMe;
    int noob = miniMeEntity.getNoobVariant();
    if (noob == 3) {
      packedLightIn = 15728880;
      this.model = ModelHolder.ghostlyMiniMe;
      if (miniMeEntity.isSlim() && this.model != ModelHolder.ghostlyMiniMeSlim) {
        this.model = ModelHolder.ghostlyMiniMeSlim;
      }
    } else if (noob == 4) {
      packedLightIn = 15728880;
      this.model = ModelHolder.glowingMiniMe;
      if (miniMeEntity.isSlim() && this.model != ModelHolder.glowingMiniMeSlim) {
        this.model = ModelHolder.glowingMiniMeSlim;
      }
    }
    super.render(miniMeEntity, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
  }

  protected void scale(MiniMeEntity miniMeEntity, PoseStack poseStack, float partialTickTime) {
    poseStack.scale(3.5375F, 3.5375F, 3.5375F);
  }
}
