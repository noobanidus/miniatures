package noobanidus.mods.miniatures.common.client.renderer.layers;

import net.minecraft.client.model.animal.bee.BeeStingerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.StuckInBodyLayer;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;
import noobanidus.mods.miniatures.common.client.model.MiniRenderModel;
import noobanidus.mods.miniatures.common.client.renderer.state.MiniRenderState;

public class BeeStingerRenderTypeLayer<M extends MiniRenderModel> extends StuckInBodyRenderTypeLayer<M, Unit> {
  private static final Identifier BEE_STINGER_LOCATION = Identifier.withDefaultNamespace("textures/entity/bee/bee_stinger.png");

  public BeeStingerRenderTypeLayer(LivingEntityRenderer<?, MiniRenderState, M> renderer, EntityRendererProvider.Context p_361959_) {
    super(renderer, new BeeStingerModel(p_361959_.bakeLayer(ModelLayers.BEE_STINGER)), Unit.INSTANCE, BEE_STINGER_LOCATION, StuckInBodyLayer.PlacementStyle.ON_SURFACE);
  }

  @Override
  protected int numStuck(MiniRenderState p_362747_) {
    return p_362747_.stingerCount;
  }
}
