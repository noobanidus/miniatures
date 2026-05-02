package noobanidus.mods.miniatures.common.client.renderer.layers;

import net.minecraft.client.model.ArrowModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.TippableArrowRenderer;
import net.minecraft.client.renderer.entity.layers.StuckInBodyLayer;
import noobanidus.mods.miniatures.common.client.model.MiniRenderModel;
import noobanidus.mods.miniatures.common.client.renderer.state.MiniRenderState;

// TODO:?
public class ArrowRenderTypeLayer<M extends MiniRenderModel> extends StuckInBodyRenderTypeLayer<M> {
  public ArrowRenderTypeLayer(LivingEntityRenderer<?, MiniRenderState, M> p_174466_, EntityRendererProvider.Context p_174465_) {
    super(
        p_174466_,
        new ArrowModel(p_174465_.bakeLayer(ModelLayers.ARROW)),
        TippableArrowRenderer.NORMAL_ARROW_LOCATION,
        StuckInBodyLayer.PlacementStyle.IN_CUBE
    );
  }

  @Override
  protected int numStuck(MiniRenderState p_365413_) {
    return p_365413_.arrowCount;
  }
}
