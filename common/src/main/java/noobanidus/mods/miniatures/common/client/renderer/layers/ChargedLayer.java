package noobanidus.mods.miniatures.common.client.renderer.layers;

import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.resources.ResourceLocation;
import noobanidus.mods.miniatures.common.client.ModelHolder;
import noobanidus.mods.miniatures.common.client.model.MiniRenderModel;
import noobanidus.mods.miniatures.common.client.renderer.state.MiniRenderState;

@SuppressWarnings("unchecked")
public class ChargedLayer<M extends MiniRenderModel> extends EnergySwirlLayer<MiniRenderState, M> {
  private static final ResourceLocation POWER_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/creeper/creeper_armor.png");

  public ChargedLayer(RenderLayerParent<MiniRenderState, M> p_i50947_1_) {
    super(p_i50947_1_);
  }

  protected float xOffset(float p_225634_1_) {
    return p_225634_1_ * 0.01F;
  }

  protected ResourceLocation getTextureLocation() {
    return POWER_LOCATION;
  }

  protected M model() {
    return (M) ModelHolder.chargedMiniMe;
  }

  @Override
  protected boolean isPowered(MiniRenderState p_360505_) {
    return p_360505_.isPowered;
  }
}