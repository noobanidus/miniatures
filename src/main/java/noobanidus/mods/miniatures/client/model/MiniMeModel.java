package noobanidus.mods.miniatures.client.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import noobanidus.mods.miniatures.client.AdditionalRenderTypes;
import noobanidus.mods.miniatures.entity.MiniMeEntity;

import java.util.function.Function;

public class MiniMeModel<E extends MiniMeEntity> extends PlayerRenderModel<E> {

  private final boolean arms;

  public MiniMeModel(float modelSize, boolean arms) {
    this(AdditionalRenderTypes::entityTranslucent, modelSize, arms);
  }

  public MiniMeModel(Function<ResourceLocation, RenderType> renderTypeIn, float modelSize, boolean smallArmsIn) {
    this(renderTypeIn, modelSize, 0.0F, 64, 64, smallArmsIn);
  }

  public MiniMeModel(Function<ResourceLocation, RenderType> renderTypeIn, float modelSize, float yOffsetIn, int textureWidthIn, int textureHeightIn, boolean smallArmsIn) {
    super(renderTypeIn, modelSize, yOffsetIn, textureWidthIn, textureHeightIn, smallArmsIn);
    this.arms = smallArmsIn;
  }

  public boolean isArms() {
    return arms;
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
    this.bipedBodyWear.copyFrom(this.body);
    this.bipedLeftArmwear.copyFrom(this.leftArm);
    this.bipedRightArmwear.copyFrom(this.rightArm);
    this.bipedLeftLegwear.copyFrom(this.leftLeg);
    this.bipedRightLegwear.copyFrom(this.rightLeg);
  }

  @Override
  protected Iterable<ModelRenderer> headParts() {
    return ImmutableList.of(this.head, this.hat);
  }


}
