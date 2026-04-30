package noobanidus.mods.miniatures.common.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import noobanidus.mods.miniatures.common.client.model.PlayerRenderModel;


public abstract class StuckInBodyRenderTypeLayer<T extends LivingEntity, M extends PlayerRenderModel<T>> extends RenderLayer<T, M> {
  public StuckInBodyRenderTypeLayer(LivingEntityRenderer<T, M> arg) {
    super(arg);
  }

  protected abstract int numStuck(T arg);

  protected abstract void renderStuckItem(PoseStack arg, MultiBufferSource arg2, int i, Entity arg3, float f, float g, float h, float j);

  public void render(PoseStack arg, MultiBufferSource arg2, int i, T arg3, float f, float g, float h, float j, float k, float l) {
    int m = this.numStuck(arg3);
    RandomSource randomSource = RandomSource.create((long)arg3.getId());
    if (m > 0) {
      for(int n = 0; n < m; ++n) {
        arg.pushPose();
        ModelPart modelPart = ((PlayerRenderModel<?>)this.getParentModel()).getRandomModelPart(randomSource);
        ModelPart.Cube cube = modelPart.getRandomCube(randomSource);
        modelPart.translateAndRotate(arg);
        float o = randomSource.nextFloat();
        float p = randomSource.nextFloat();
        float q = randomSource.nextFloat();
        float r = Mth.lerp(o, cube.minX, cube.maxX) / 16.0F;
        float s = Mth.lerp(p, cube.minY, cube.maxY) / 16.0F;
        float t = Mth.lerp(q, cube.minZ, cube.maxZ) / 16.0F;
        arg.translate(r, s, t);
        o = -1.0F * (o * 2.0F - 1.0F);
        p = -1.0F * (p * 2.0F - 1.0F);
        q = -1.0F * (q * 2.0F - 1.0F);
        this.renderStuckItem(arg, arg2, i, arg3, o, p, q, h);
        arg.popPose();
      }

    }
  }
}
