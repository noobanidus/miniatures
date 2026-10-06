package noobanidus.mods.miniatures.common.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.world.entity.player.PlayerModelType;
import noobanidus.mods.miniatures.common.client.renderer.state.MiniRenderState;

public class DynamicHumanoidArmorLayer<M extends HumanoidModel<MiniRenderState>, A extends HumanoidModel<MiniRenderState>> extends HumanoidArmorLayer<MiniRenderState, M, A> {
  private final boolean isSlim;

  public DynamicHumanoidArmorLayer(RenderLayerParent<MiniRenderState, M> renderer, ArmorModelSet<A> modelSet, EquipmentLayerRenderer layerRenderer, boolean isSlim) {
    super(renderer, modelSet, modelSet, layerRenderer);
    this.isSlim = isSlim;
  }

  @Override
  public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, MiniRenderState state, float yRot, float xRot) {
    if ((state.skin.model() == PlayerModelType.SLIM && isSlim) || (state.skin.model() == PlayerModelType.WIDE && !isSlim)) {
      super.submit(poseStack, submitNodeCollector, lightCoords, state, yRot, xRot);
    }
  }
}
