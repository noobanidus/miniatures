package noobanidus.mods.miniatures.client.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import noobanidus.mods.miniatures.client.AdditionalRenderTypes;
import noobanidus.mods.miniatures.entity.MiniMeEntity;

import java.util.function.Function;

public class MiniMeModel<E extends MiniMeEntity> extends PlayerRenderModel<E> {

  public MiniMeModel(ModelPart root, boolean slim) {
    super(AdditionalRenderTypes::entityTranslucent, root, slim);
  }

  public MiniMeModel(Function<ResourceLocation, RenderType> renderTypeIn, ModelPart root, boolean slim) {
    super(renderTypeIn, root, slim);
  }

  public boolean isSlim() {
    return slim;
  }

  @Override
  public void setupAnim(E entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    super.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    int noob = entityIn.getNoobVariant();
    if (noob == 1) {
      this.leftLeg.xRot = 0.0f;
      this.leftLeg.zRot = 0.0f;
      this.rightLeg.xRot = 0.0f;
      this.rightLeg.zRot = 0.0f;
    }
    if (entityIn.isVehicle()) {
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

  @Override
  protected Iterable<ModelPart> headParts() {
    return ImmutableList.of(this.head, this.hat);
  }


}
