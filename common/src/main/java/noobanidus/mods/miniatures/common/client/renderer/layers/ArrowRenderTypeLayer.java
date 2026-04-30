package noobanidus.mods.miniatures.common.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import noobanidus.mods.miniatures.common.client.model.PlayerRenderModel;

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

	@Override
	protected void renderStuckItem(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, Entity entity, float f, float g, float h, float j) {
		float k = Mth.sqrt(f * f + h * h);
		Arrow arrow = new Arrow(entity.level(), entity.getX(), entity.getY(), entity.getZ(), ItemStack.EMPTY, null);
		arrow.setYRot((float)(Math.atan2((double)f, (double)h) * 180.0F / (float)Math.PI));
		arrow.setXRot((float)(Math.atan2((double)g, (double)k) * 180.0F / (float)Math.PI));
		arrow.yRotO = arrow.getYRot();
		arrow.xRotO = arrow.getXRot();
		this.dispatcher.render(arrow, 0.0, 0.0, 0.0, 0.0F, j, poseStack, multiBufferSource, i);
	}
}
