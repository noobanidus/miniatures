package noobanidus.mods.miniatures.common.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.StuckInBodyLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import noobanidus.mods.miniatures.common.client.model.MiniRenderModel;
import noobanidus.mods.miniatures.common.client.renderer.state.MiniRenderState;

public abstract class StuckInBodyRenderTypeLayer<M extends MiniRenderModel, S> extends RenderLayer<MiniRenderState, M> {
  private final Model<S> model;
  private final S modelState;
  private final Identifier texture;
  private final StuckInBodyLayer.PlacementStyle placementStyle;

  public StuckInBodyRenderTypeLayer(LivingEntityRenderer<?, MiniRenderState, M> arg,  Model<S> model, S state, Identifier layer, StuckInBodyLayer.PlacementStyle style) {
    super(arg);
    this.model = model;
    this.texture = layer;
    this.modelState = state;
    this.placementStyle = style;
  }

  protected abstract int numStuck(MiniRenderState p_365314_);


  private void submitStuckItem(
      PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, float directionX, float directionY, float directionZ, int outlineColor
  ) {
    float directionXZ = Mth.sqrt(directionX * directionX + directionZ * directionZ);
    float yRot = (float)(Math.atan2(directionX, directionZ) * 180.0F / (float)Math.PI);
    float xRot = (float)(Math.atan2(directionY, directionXZ) * 180.0F / (float)Math.PI);
    poseStack.mulPose(Axis.YP.rotationDegrees(yRot - 90.0F));
    poseStack.mulPose(Axis.ZP.rotationDegrees(xRot));
    submitNodeCollector.submitModel(this.model, this.modelState, poseStack, this.texture, lightCoords, OverlayTexture.NO_OVERLAY, outlineColor, null);
  }

  public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, MiniRenderState state, float yRot, float xRot) {
    int count = this.numStuck(state);
    if (count > 0) {
      RandomSource random = RandomSource.createThreadLocalInstance(state.id);

      for (int i = 0; i < count; i++) {
        poseStack.pushPose();
        ModelPart modelPart = this.getParentModel().getRandomBodyPart(random);
        ModelPart.Cube cube = modelPart.getRandomCube(random);
        modelPart.translateAndRotate(poseStack);
        float midX = random.nextFloat();
        float midY = random.nextFloat();
        float midZ = random.nextFloat();
        if (this.placementStyle == StuckInBodyLayer.PlacementStyle.ON_SURFACE) {
          int plane = random.nextInt(3);
          switch (plane) {
            case 0:
              midX = snapToFace(midX);
              break;
            case 1:
              midY = snapToFace(midY);
              break;
            default:
              midZ = snapToFace(midZ);
          }
        }

        poseStack.translate(
            Mth.lerp(midX, cube.minX, cube.maxX) / 16.0F, Mth.lerp(midY, cube.minY, cube.maxY) / 16.0F, Mth.lerp(midZ, cube.minZ, cube.maxZ) / 16.0F
        );
        this.submitStuckItem(
            poseStack, submitNodeCollector, lightCoords, -(midX * 2.0F - 1.0F), -(midY * 2.0F - 1.0F), -(midZ * 2.0F - 1.0F), state.outlineColor
        );
        poseStack.popPose();
      }
    }
  }

  private static float snapToFace(float value) {
    return value > 0.5F ? 1.0F : 0.5F;
  }
}
