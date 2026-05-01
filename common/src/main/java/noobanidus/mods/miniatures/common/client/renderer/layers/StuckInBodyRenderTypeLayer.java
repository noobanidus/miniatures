package noobanidus.mods.miniatures.common.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.StuckInBodyLayer;
import net.minecraft.client.renderer.entity.state.MiniRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import noobanidus.mods.miniatures.common.client.model.MiniRenderModel;


public abstract class StuckInBodyRenderTypeLayer<M extends MiniRenderModel> extends RenderLayer<MiniRenderState, M> {
  private final Model model;
  private final ResourceLocation texture;
  private final StuckInBodyLayer.PlacementStyle placementStyle;

  public StuckInBodyRenderTypeLayer(LivingEntityRenderer<?, MiniRenderState, M> arg, Model model, ResourceLocation layer, StuckInBodyLayer.PlacementStyle style) {
    super(arg);
    this.model = model;
    this.texture = layer;
    this.placementStyle = style;
  }

  protected abstract int numStuck(MiniRenderState p_365314_);

  private void renderStuckItem(PoseStack p_117566_, MultiBufferSource p_117567_, int p_117568_, float p_117570_, float p_117571_, float p_117572_) {
    float f = Mth.sqrt(p_117570_ * p_117570_ + p_117572_ * p_117572_);
    float f1 = (float) (Math.atan2(p_117570_, p_117572_) * 180.0F / (float) Math.PI);
    float f2 = (float) (Math.atan2(p_117571_, f) * 180.0F / (float) Math.PI);
    p_117566_.mulPose(Axis.YP.rotationDegrees(f1 - 90.0F));
    p_117566_.mulPose(Axis.ZP.rotationDegrees(f2));
    this.model.renderToBuffer(p_117566_, p_117567_.getBuffer(this.model.renderType(this.texture)), p_117568_, OverlayTexture.NO_OVERLAY);
  }

  public void render(PoseStack p_117575_, MultiBufferSource p_117576_, int p_117577_, MiniRenderState p_363391_, float p_117579_, float p_117580_) {
    int i = this.numStuck(p_363391_);
    if (i > 0) {
      RandomSource randomsource = RandomSource.create(p_363391_.id);

      for (int j = 0; j < i; j++) {
        p_117575_.pushPose();
        ModelPart modelpart = this.getParentModel().getRandomBodyPart(randomsource);
        ModelPart.Cube modelpart$cube = modelpart.getRandomCube(randomsource);
        modelpart.translateAndRotate(p_117575_);
        float f = randomsource.nextFloat();
        float f1 = randomsource.nextFloat();
        float f2 = randomsource.nextFloat();
        if (this.placementStyle == StuckInBodyLayer.PlacementStyle.ON_SURFACE) {
          int k = randomsource.nextInt(3);
          switch (k) {
            case 0:
              f = snapToFace(f);
              break;
            case 1:
              f1 = snapToFace(f1);
              break;
            default:
              f2 = snapToFace(f2);
          }
        }

        p_117575_.translate(
            Mth.lerp(f, modelpart$cube.minX, modelpart$cube.maxX) / 16.0F,
            Mth.lerp(f1, modelpart$cube.minY, modelpart$cube.maxY) / 16.0F,
            Mth.lerp(f2, modelpart$cube.minZ, modelpart$cube.maxZ) / 16.0F
        );
        this.renderStuckItem(p_117575_, p_117576_, p_117577_, -(f * 2.0F - 1.0F), -(f1 * 2.0F - 1.0F), -(f2 * 2.0F - 1.0F));
        p_117575_.popPose();
      }
    }
  }

  private static float snapToFace(float p_361108_) {
    return p_361108_ > 0.5F ? 1.0F : 0.5F;
  }
}
