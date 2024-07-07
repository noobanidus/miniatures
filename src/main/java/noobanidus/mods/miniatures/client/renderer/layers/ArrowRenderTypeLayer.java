package noobanidus.mods.miniatures.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.Items;
import noobanidus.mods.miniatures.client.model.PlayerRenderModel;

// TODO:?
public class ArrowRenderTypeLayer<T extends LivingEntity, M extends PlayerRenderModel<T>> extends StuckInBodyRenderTypeLayer<T, M> {
  private final EntityRenderDispatcher dispatcher;

  public ArrowRenderTypeLayer(EntityRendererProvider.Context context, LivingEntityRenderer<T, M> rendererIn) {
    super(rendererIn);
    this.dispatcher = context.getEntityRenderDispatcher();
  }

  protected int numStuck(T p_225631_1_) {
    return p_225631_1_.getArrowCount();
  }

  protected void renderStuckItem(PoseStack poseStack, MultiBufferSource bufferSource, int p_225632_3_, Entity entity, float p_225632_5_, float p_225632_6_, float p_225632_7_, float p_225632_8_) {
    float f = Mth.sqrt(p_225632_5_ * p_225632_5_ + p_225632_7_ * p_225632_7_);
    Arrow arrow = new Arrow(entity.level(), entity.getX(), entity.getY(), entity.getZ(), Items.ARROW.getDefaultInstance());
    arrow.setYRot((float) (Math.atan2(p_225632_5_, p_225632_7_) * (double) (180F / (float) Math.PI)));
    arrow.setXRot((float) (Math.atan2(p_225632_6_, f) * (double) (180F / (float) Math.PI)));
    arrow.yRotO = arrow.getYRot();
    arrow.xRotO = arrow.getXRot();
    this.dispatcher.render(arrow, 0.0D, 0.0D, 0.0D, 0.0F, p_225632_8_, poseStack, bufferSource, p_225632_3_);
  }
}
