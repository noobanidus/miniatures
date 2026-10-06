package noobanidus.mods.miniatures.common.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import noobanidus.mods.miniatures.common.client.model.MiniMeModel;
import noobanidus.mods.miniatures.common.client.renderer.state.MiniRenderState;

public class MiniItemInHandLayer extends ItemInHandLayer<MiniRenderState, MiniMeModel> {
  private static final float X_ROT_MIN = (float) (-Math.PI / 6);
  private static final float X_ROT_MAX = (float) (Math.PI / 2);

  public MiniItemInHandLayer(RenderLayerParent<MiniRenderState, MiniMeModel> renderer) {
    super(renderer);
  }

  @Override
  protected void submitArmWithItem(
      MiniRenderState state, ItemStackRenderState item, ItemStack itemStack, HumanoidArm arm, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords
  ) {
    if (!item.isEmpty()) {
      InteractionHand currentHand = arm == state.mainArm ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
      if (state.isUsingItem && state.useItemHand == currentHand && state.attackTime < 1.0E-5F && !state.heldOnHead.isEmpty()) {
        this.renderItemHeldToEye(state, arm, poseStack, submitNodeCollector, lightCoords);
      } else {
        super.submitArmWithItem(state, item, itemStack, arm, poseStack, submitNodeCollector, lightCoords);
      }
    }
  }

  private void renderItemHeldToEye(MiniRenderState state, HumanoidArm arm, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords) {
    poseStack.pushPose();
    this.getParentModel().root().translateAndRotate(poseStack);
    ModelPart head = this.getParentModel().getHead();
    float previousXRot = head.xRot;
    head.xRot = Mth.clamp(head.xRot, (float) (-Math.PI / 6), (float) (Math.PI / 2));
    head.translateAndRotate(poseStack);
    head.xRot = previousXRot;
    CustomHeadLayer.translateToHead(poseStack, CustomHeadLayer.Transforms.DEFAULT);
    boolean isLeftHand = arm == HumanoidArm.LEFT;
    poseStack.translate((isLeftHand ? -2.5F : 2.5F) / 16.0F, -0.0625F, 0.0F);
    state.heldOnHead.submit(poseStack, submitNodeCollector, lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
    poseStack.popPose();
  }
}

