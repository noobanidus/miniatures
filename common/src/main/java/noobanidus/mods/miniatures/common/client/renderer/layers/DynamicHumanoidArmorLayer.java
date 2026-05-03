package noobanidus.mods.miniatures.common.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.resources.PlayerSkin;
import noobanidus.mods.miniatures.common.client.renderer.state.MiniRenderState;

public class DynamicHumanoidArmorLayer<M extends HumanoidModel<MiniRenderState>, A extends HumanoidModel<MiniRenderState>> extends HumanoidArmorLayer<MiniRenderState, M, A> {
  private final boolean isSlim;

  public DynamicHumanoidArmorLayer(RenderLayerParent<MiniRenderState, M> p_267286_, A p_267110_, A p_267150_, EquipmentLayerRenderer p_371362_, boolean isSlim) {
    super(p_267286_, p_267110_, p_267150_, p_371362_);
    this.isSlim = isSlim;
  }

  public DynamicHumanoidArmorLayer(RenderLayerParent<MiniRenderState, M> p_360748_, A p_361913_, A p_362555_, A p_362321_, A p_362768_, EquipmentLayerRenderer p_371733_, boolean isSlim) {
    super(p_360748_, p_361913_, p_362555_, p_362321_, p_362768_, p_371733_);
    this.isSlim = isSlim;
  }

  @Override
  public void render(PoseStack p_117096_, MultiBufferSource p_117097_, int p_117098_, MiniRenderState state, float p_117100_, float p_117101_) {
    if ((state.skin.model() == PlayerSkin.Model.SLIM && isSlim) || (state.skin.model() == PlayerSkin.Model.WIDE && !isSlim)) {
      super.render(p_117096_, p_117097_, p_117098_, state, p_117100_, p_117101_);
    }
  }
}
