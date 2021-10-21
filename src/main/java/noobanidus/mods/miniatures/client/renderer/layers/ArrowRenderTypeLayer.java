package noobanidus.mods.miniatures.client.renderer.layers;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import noobanidus.mods.miniatures.client.model.PlayerRenderModel;

@OnlyIn(Dist.CLIENT)
public class ArrowRenderTypeLayer<T extends LivingEntity, M extends PlayerRenderModel<T>> extends StuckInBodyRenderTypeLayer<T, M> {
  private final EntityRendererManager dispatcher;
  private ArrowEntity arrow;

  public ArrowRenderTypeLayer(LivingRenderer<T, M> rendererIn) {
    super(rendererIn);
    this.dispatcher = rendererIn.getDispatcher();
  }

  protected int numStuck(T p_225631_1_) {
    return p_225631_1_.getArrowCount();
  }

  protected void renderStuckItem(MatrixStack p_225632_1_, IRenderTypeBuffer p_225632_2_, int p_225632_3_, Entity p_225632_4_, float p_225632_5_, float p_225632_6_, float p_225632_7_, float p_225632_8_) {
    float f = MathHelper.sqrt(p_225632_5_ * p_225632_5_ + p_225632_7_ * p_225632_7_);
    this.arrow = new ArrowEntity(p_225632_4_.level, p_225632_4_.getX(), p_225632_4_.getY(), p_225632_4_.getZ());
    this.arrow.yRot = (float) (Math.atan2((double) p_225632_5_, (double) p_225632_7_) * (double) (180F / (float) Math.PI));
    this.arrow.xRot = (float) (Math.atan2((double) p_225632_6_, (double) f) * (double) (180F / (float) Math.PI));
    this.arrow.yRotO = this.arrow.yRot;
    this.arrow.xRotO = this.arrow.xRot;
    this.dispatcher.render(this.arrow, 0.0D, 0.0D, 0.0D, 0.0F, p_225632_8_, p_225632_1_, p_225632_2_, p_225632_3_);
  }
}
