package noobanidus.mods.miniatures.common.client.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import noobanidus.mods.miniatures.common.client.renderer.state.MiniRenderState;

import java.util.function.Function;

public class MiniMeModel extends MiniRenderModel {

  public MiniMeModel(ModelPart root, boolean slim) {
    super(RenderType::entityTranslucent, root, slim);
  }

  public MiniMeModel(Function<ResourceLocation, RenderType> renderTypeIn, ModelPart root, boolean slim) {
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
    this.hat.copyFrom(this.head);
    this.jacket.copyFrom(this.body);
    this.leftSleeve.copyFrom(this.leftArm);
    this.rightSleeve.copyFrom(this.rightArm);
    this.leftPants.copyFrom(this.leftLeg);
    this.rightPants.copyFrom(this.rightLeg);
  }
}
