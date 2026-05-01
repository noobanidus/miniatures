package noobanidus.mods.miniatures.common.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import noobanidus.mods.miniatures.common.client.model.MiniRenderModel;

public class BeeStingerRenderTypeLayer<T extends LivingEntity, M extends MiniRenderModel<T>> extends StuckInBodyRenderTypeLayer<T, M> {
  private static final ResourceLocation BEE_STINGER_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/bee/bee_stinger.png");

  public BeeStingerRenderTypeLayer(LivingEntityRenderer<T, M> arg) {
    super(arg);
  }

  protected int numStuck(T arg) {
    return arg.getStingerCount();
  }

  protected void renderStuckItem(PoseStack arg, MultiBufferSource arg2, int i, Entity arg3, float f, float g, float h, float j) {
    float k = Mth.sqrt(f * f + h * h);
    float l = (float)(Math.atan2((double)f, (double)h) * (double)(180F / (float)Math.PI));
    float m = (float)(Math.atan2((double)g, (double)k) * (double)(180F / (float)Math.PI));
    arg.translate(0.0F, 0.0F, 0.0F);
    arg.mulPose(Axis.YP.rotationDegrees(l - 90.0F));
    arg.mulPose(Axis.ZP.rotationDegrees(m));
    float n = 0.0F;
    float o = 0.125F;
    float p = 0.0F;
    float q = 0.0625F;
    float r = 0.03125F;
    arg.mulPose(Axis.XP.rotationDegrees(45.0F));
    arg.scale(0.03125F, 0.03125F, 0.03125F);
    arg.translate(2.5F, 0.0F, 0.0F);
    VertexConsumer vertexConsumer = arg2.getBuffer(RenderType.entityCutoutNoCull(BEE_STINGER_LOCATION));

    for(int s = 0; s < 4; ++s) {
      arg.mulPose(Axis.XP.rotationDegrees(90.0F));
      PoseStack.Pose pose = arg.last();
      vertex(vertexConsumer, pose, -4.5F, -1, 0.0F, 0.0F, i);
      vertex(vertexConsumer, pose, 4.5F, -1, 0.125F, 0.0F, i);
      vertex(vertexConsumer, pose, 4.5F, 1, 0.125F, 0.0625F, i);
      vertex(vertexConsumer, pose, -4.5F, 1, 0.0F, 0.0625F, i);
    }

  }

  private static void vertex(VertexConsumer arg, PoseStack.Pose arg2, float f, int i, float g, float h, int j) {
    arg.addVertex(arg2, f, (float)i, 0.0F).setColor(-1).setUv(g, h).setOverlay(OverlayTexture.NO_OVERLAY).setLight(j).setNormal(arg2, 0.0F, 1.0F, 0.0F);
  }
}
