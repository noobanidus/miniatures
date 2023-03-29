package noobanidus.mods.miniatures.client.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class PlayerRenderModel<T extends LivingEntity> extends HumanoidModel<T> {
  private static final String EAR = "ear";
  private static final String CLOAK = "cloak";
  private static final String LEFT_SLEEVE = "left_sleeve";
  private static final String RIGHT_SLEEVE = "right_sleeve";
  private static final String LEFT_PANTS = "left_pants";
  private static final String RIGHT_PANTS = "right_pants";
  private final List<ModelPart> parts;
  public final ModelPart leftSleeve;
  public final ModelPart rightSleeve;
  public final ModelPart leftPants;
  public final ModelPart rightPants;
  public final ModelPart jacket;
  private final ModelPart cloak;
  private final ModelPart ear;
  protected final boolean slim;

  public PlayerRenderModel(Function<ResourceLocation, RenderType> renderTypeIn, ModelPart root, boolean slim) {
    super(root, renderTypeIn);
    this.slim = slim;
    this.ear = root.getChild("ear");
    this.cloak = root.getChild("cloak");
    this.leftSleeve = root.getChild("left_sleeve");
    this.rightSleeve = root.getChild("right_sleeve");
    this.leftPants = root.getChild("left_pants");
    this.rightPants = root.getChild("right_pants");
    this.jacket = root.getChild("jacket");
    this.parts = root.getAllParts().filter((part) -> {
      return !part.isEmpty();
    }).collect(ImmutableList.toImmutableList());
  }

  public static MeshDefinition createMesh(CubeDeformation cubeDeformation, boolean slim) {
    MeshDefinition meshdefinition = HumanoidModel.createMesh(cubeDeformation, 0.0F);
    PartDefinition partdefinition = meshdefinition.getRoot();
    partdefinition.addOrReplaceChild("ear", CubeListBuilder.create().texOffs(24, 0).addBox(-3.0F, -6.0F, -1.0F, 6.0F, 6.0F, 1.0F, cubeDeformation), PartPose.ZERO);
    partdefinition.addOrReplaceChild("cloak", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, 0.0F, -1.0F, 10.0F, 16.0F, 1.0F, cubeDeformation, 1.0F, 0.5F), PartPose.offset(0.0F, 0.0F, 0.0F));
    float f = 0.25F;
    if (slim) {
      partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(32, 48).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, cubeDeformation), PartPose.offset(5.0F, 2.5F, 0.0F));
      partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, cubeDeformation), PartPose.offset(-5.0F, 2.5F, 0.0F));
      partdefinition.addOrReplaceChild("left_sleeve", CubeListBuilder.create().texOffs(48, 48).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, cubeDeformation.extend(0.25F)), PartPose.offset(5.0F, 2.5F, 0.0F));
      partdefinition.addOrReplaceChild("right_sleeve", CubeListBuilder.create().texOffs(40, 32).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, cubeDeformation.extend(0.25F)), PartPose.offset(-5.0F, 2.5F, 0.0F));
    } else {
      partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(32, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, cubeDeformation), PartPose.offset(5.0F, 2.0F, 0.0F));
      partdefinition.addOrReplaceChild("left_sleeve", CubeListBuilder.create().texOffs(48, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, cubeDeformation.extend(0.25F)), PartPose.offset(5.0F, 2.0F, 0.0F));
      partdefinition.addOrReplaceChild("right_sleeve", CubeListBuilder.create().texOffs(40, 32).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, cubeDeformation.extend(0.25F)), PartPose.offset(-5.0F, 2.0F, 0.0F));
    }

    partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(16, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, cubeDeformation), PartPose.offset(1.9F, 12.0F, 0.0F));
    partdefinition.addOrReplaceChild("left_pants", CubeListBuilder.create().texOffs(0, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, cubeDeformation.extend(0.25F)), PartPose.offset(1.9F, 12.0F, 0.0F));
    partdefinition.addOrReplaceChild("right_pants", CubeListBuilder.create().texOffs(0, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, cubeDeformation.extend(0.25F)), PartPose.offset(-1.9F, 12.0F, 0.0F));
    partdefinition.addOrReplaceChild("jacket", CubeListBuilder.create().texOffs(16, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, cubeDeformation.extend(0.25F)), PartPose.ZERO);
    return meshdefinition;
  }

  protected Iterable<ModelPart> bodyParts() {
    return Iterables.concat(super.bodyParts(), ImmutableList.of(this.leftPants, this.rightPants, this.leftSleeve, this.rightSleeve, this.jacket));
  }

  public void renderEars(PoseStack poseStack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn) {
    this.ear.copyFrom(this.head);
    this.ear.x = 0.0F;
    this.ear.y = 0.0F;
    this.ear.render(poseStack, bufferIn, packedLightIn, packedOverlayIn);
  }

  public void renderCloak(PoseStack poseStack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn) {
    this.cloak.render(poseStack, bufferIn, packedLightIn, packedOverlayIn);
  }

  /**
   * Sets this entity's model rotation angles
   */
  public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    super.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    this.leftPants.copyFrom(this.leftLeg);
    this.rightPants.copyFrom(this.rightLeg);
    this.leftSleeve.copyFrom(this.leftArm);
    this.rightSleeve.copyFrom(this.rightArm);
    this.jacket.copyFrom(this.body);
    if (entityIn.getItemBySlot(EquipmentSlot.CHEST).isEmpty()) {
      if (entityIn.isCrouching()) {
        this.cloak.z = 1.4F;
        this.cloak.y = 1.85F;
      } else {
        this.cloak.z = 0.0F;
        this.cloak.y = 0.0F;
      }
    } else if (entityIn.isCrouching()) {
      this.cloak.z = 0.3F;
      this.cloak.y = 0.8F;
    } else {
      this.cloak.z = -1.1F;
      this.cloak.y = -0.85F;
    }

  }

  public void setAllVisible(boolean visible) {
    super.setAllVisible(visible);
    this.leftSleeve.visible = visible;
    this.rightSleeve.visible = visible;
    this.leftPants.visible = visible;
    this.rightPants.visible = visible;
    this.jacket.visible = visible;
    this.cloak.visible = visible;
    this.ear.visible = visible;
  }

  public void translateToHand(HumanoidArm sideIn, PoseStack poseStack) {
    ModelPart modelpart = this.getArm(sideIn);
    if (this.slim) {
      float f = 0.5F * (float) (sideIn == HumanoidArm.RIGHT ? 1 : -1);
      modelpart.x += f;
      modelpart.translateAndRotate(poseStack);
      modelpart.x -= f;
    } else {
      modelpart.translateAndRotate(poseStack);
    }

  }

  public ModelPart getRandomModelRenderer(RandomSource randomIn) {
    return this.parts.get(randomIn.nextInt(this.parts.size()));
  }
}
