package noobanidus.mods.miniatures.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.util.Mth;
import noobanidus.mods.miniatures.client.model.PlayerRenderModel;

import java.util.Random;

public abstract class StuckInBodyRenderTypeLayer<T extends LivingEntity, M extends PlayerRenderModel<T>> extends RenderLayer<T, M> {
  public StuckInBodyRenderTypeLayer(LivingEntityRenderer<T, M> p_i226041_1_) {
    super(p_i226041_1_);
  }

  protected abstract int numStuck(T p_225631_1_);

  protected abstract void renderStuckItem(PoseStack p_225632_1_, MultiBufferSource p_225632_2_, int p_225632_3_, Entity p_225632_4_, float p_225632_5_, float p_225632_6_, float p_225632_7_, float p_225632_8_);

  public void render(PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
    int i = this.numStuck(entitylivingbaseIn);
    Random random = new Random((long) entitylivingbaseIn.getId());
    if (i > 0) {
      for (int j = 0; j < i; ++j) {
        poseStack.pushPose();
        ModelPart modelpart = this.getParentModel().getRandomModelRenderer(random);
        ModelPart.Cube modelpart$cube = modelpart.getRandomCube(random);
        modelpart.translateAndRotate(poseStack);
        float f = random.nextFloat();
        float f1 = random.nextFloat();
        float f2 = random.nextFloat();
        float f3 = Mth.lerp(f, modelpart$cube.minX, modelpart$cube.maxX) / 16.0F;
        float f4 = Mth.lerp(f1, modelpart$cube.minY, modelpart$cube.maxY) / 16.0F;
        float f5 = Mth.lerp(f2, modelpart$cube.minZ, modelpart$cube.maxZ) / 16.0F;
        poseStack.translate((double) f3, (double) f4, (double) f5);
        f = -1.0F * (f * 2.0F - 1.0F);
        f1 = -1.0F * (f1 * 2.0F - 1.0F);
        f2 = -1.0F * (f2 * 2.0F - 1.0F);
        this.renderStuckItem(poseStack, bufferIn, packedLightIn, entitylivingbaseIn, f, f1, f2, partialTicks);
        poseStack.popPose();
      }

    }
  }
}
