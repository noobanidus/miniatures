package noobanidus.mods.miniatures.common.client.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import noobanidus.mods.miniatures.common.client.renderer.state.MiniRenderState;

import java.util.function.Function;

public class MiniMeModel extends MiniRenderModel {

  public MiniMeModel(ModelPart root, boolean slim) {
    super(RenderTypes::entityTranslucent, root, slim);
  }

  public MiniMeModel(Function<Identifier, RenderType> renderTypeIn, ModelPart root, boolean slim) {
    super(renderTypeIn, root, slim);
  }

  @Override
  public void setupAnim(MiniRenderState entityIn) {
    super.setupAnim(entityIn);
    int noob = entityIn.noobVariant;
    if (noob == 1) {
      this.leftLeg.xRot = 0.0f;
      this.leftLeg.zRot = 0.0f;
      this.rightLeg.xRot = 0.0f;
      this.rightLeg.zRot = 0.0f;
    }
    if (entityIn.isPassenger) {
      this.leftArm.xRot = -3f;
      this.rightArm.xRot = -3f;
      this.leftArm.zRot = 0.3f;
      this.rightArm.zRot = -0.3f;
    }
  }
}
