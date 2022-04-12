package noobanidus.mods.miniatures.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import noobanidus.mods.miniatures.client.model.PlayerRenderModel;

public class BeeStingerRenderTypeLayer<T extends LivingEntity, M extends PlayerRenderModel<T>> extends StuckInBodyRenderTypeLayer<T, M> {
  private static final ResourceLocation BEE_STINGER_LOCATION = new ResourceLocation("textures/entity/bee/bee_stinger.png");

  public BeeStingerRenderTypeLayer(LivingEntityRenderer<T, M> p_i226036_1_) {
    super(p_i226036_1_);
  }

  protected int numStuck(T p_225631_1_) {
    return p_225631_1_.getStingerCount();
  }

  protected void renderStuckItem(PoseStack p_225632_1_, MultiBufferSource p_225632_2_, int p_225632_3_, Entity p_225632_4_, float p_225632_5_, float p_225632_6_, float p_225632_7_, float p_225632_8_) {
    float f = Mth.sqrt(p_225632_5_ * p_225632_5_ + p_225632_7_ * p_225632_7_);
    float f1 = (float) (Math.atan2(p_225632_5_, p_225632_7_) * (double) (180F / (float) Math.PI));
    float f2 = (float) (Math.atan2(p_225632_6_, f) * (double) (180F / (float) Math.PI));
    p_225632_1_.translate(0.0D, 0.0D, 0.0D);
    p_225632_1_.mulPose(Vector3f.YP.rotationDegrees(f1 - 90.0F));
    p_225632_1_.mulPose(Vector3f.ZP.rotationDegrees(f2));
    float f3 = 0.0F;
    float f4 = 0.125F;
    float f5 = 0.0F;
    float f6 = 0.0625F;
    float f7 = 0.03125F;
    p_225632_1_.mulPose(Vector3f.XP.rotationDegrees(45.0F));
    p_225632_1_.scale(0.03125F, 0.03125F, 0.03125F);
    p_225632_1_.translate(2.5D, 0.0D, 0.0D);
    VertexConsumer vertexconsumer = p_225632_2_.getBuffer(RenderType.entityCutoutNoCull(BEE_STINGER_LOCATION));

    for (int i = 0; i < 4; ++i) {
      p_225632_1_.mulPose(Vector3f.XP.rotationDegrees(90.0F));
      PoseStack.Pose posestack$entry = p_225632_1_.last();
      Matrix4f matrix4f = posestack$entry.pose();
      Matrix3f matrix3f = posestack$entry.normal();
      vertex(vertexconsumer, matrix4f, matrix3f, -4.5F, -1, 0.0F, 0.0F, p_225632_3_);
      vertex(vertexconsumer, matrix4f, matrix3f, 4.5F, -1, 0.125F, 0.0F, p_225632_3_);
      vertex(vertexconsumer, matrix4f, matrix3f, 4.5F, 1, 0.125F, 0.0625F, p_225632_3_);
      vertex(vertexconsumer, matrix4f, matrix3f, -4.5F, 1, 0.0F, 0.0625F, p_225632_3_);
    }

  }

  private static void vertex(VertexConsumer p_229132_0_, Matrix4f p_229132_1_, Matrix3f p_229132_2_, float p_229132_3_, int p_229132_4_, float p_229132_5_, float p_229132_6_, int p_229132_7_) {
    p_229132_0_.vertex(p_229132_1_, p_229132_3_, (float) p_229132_4_, 0.0F).color(255, 255, 255, 255).uv(p_229132_5_, p_229132_6_).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(p_229132_7_).normal(p_229132_2_, 0.0F, 1.0F, 0.0F).endVertex();
  }
}
