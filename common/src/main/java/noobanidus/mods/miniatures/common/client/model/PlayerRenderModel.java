package noobanidus.mods.miniatures.common.client.model;

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
import java.util.function.Function;

public class PlayerRenderModel<T extends LivingEntity> extends HumanoidModel<T> {
  private final List<ModelPart> parts;
  public final ModelPart leftSleeve;
  public final ModelPart rightSleeve;
  public final ModelPart leftPants;
  public final ModelPart rightPants;
  public final ModelPart jacket;
  private final ModelPart cloak;
  private final ModelPart ear;
  protected final boolean slim;

  public PlayerRenderModel(Function<ResourceLocation, RenderType> renderType, ModelPart arg, boolean bl) {
    super(arg, renderType);
    this.slim = bl;
    this.ear = arg.getChild("ear");
    this.cloak = arg.getChild("cloak");
    this.leftSleeve = arg.getChild("left_sleeve");
    this.rightSleeve = arg.getChild("right_sleeve");
    this.leftPants = arg.getChild("left_pants");
    this.rightPants = arg.getChild("right_pants");
    this.jacket = arg.getChild("jacket");
    this.parts = arg.getAllParts().filter((argx) -> !argx.isEmpty()).collect(ImmutableList.toImmutableList());
  }

  public static MeshDefinition createMesh(CubeDeformation arg, boolean bl) {
    MeshDefinition meshDefinition = HumanoidModel.createMesh(arg, 0.0F);
    PartDefinition partDefinition = meshDefinition.getRoot();
    partDefinition.addOrReplaceChild("ear", CubeListBuilder.create().texOffs(24, 0).addBox(-3.0F, -6.0F, -1.0F, 6.0F, 6.0F, 1.0F, arg), PartPose.ZERO);
    partDefinition.addOrReplaceChild("cloak", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, 0.0F, -1.0F, 10.0F, 16.0F, 1.0F, arg, 1.0F, 0.5F), PartPose.offset(0.0F, 0.0F, 0.0F));
    float f = 0.25F;
    if (bl) {
      partDefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(32, 48).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, arg), PartPose.offset(5.0F, 2.5F, 0.0F));
      partDefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, arg), PartPose.offset(-5.0F, 2.5F, 0.0F));
      partDefinition.addOrReplaceChild("left_sleeve", CubeListBuilder.create().texOffs(48, 48).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, arg.extend(0.25F)), PartPose.offset(5.0F, 2.5F, 0.0F));
      partDefinition.addOrReplaceChild("right_sleeve", CubeListBuilder.create().texOffs(40, 32).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, arg.extend(0.25F)), PartPose.offset(-5.0F, 2.5F, 0.0F));
    } else {
      partDefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(32, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, arg), PartPose.offset(5.0F, 2.0F, 0.0F));
      partDefinition.addOrReplaceChild("left_sleeve", CubeListBuilder.create().texOffs(48, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, arg.extend(0.25F)), PartPose.offset(5.0F, 2.0F, 0.0F));
      partDefinition.addOrReplaceChild("right_sleeve", CubeListBuilder.create().texOffs(40, 32).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, arg.extend(0.25F)), PartPose.offset(-5.0F, 2.0F, 0.0F));
    }

    partDefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(16, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, arg), PartPose.offset(1.9F, 12.0F, 0.0F));
    partDefinition.addOrReplaceChild("left_pants", CubeListBuilder.create().texOffs(0, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, arg.extend(0.25F)), PartPose.offset(1.9F, 12.0F, 0.0F));
    partDefinition.addOrReplaceChild("right_pants", CubeListBuilder.create().texOffs(0, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, arg.extend(0.25F)), PartPose.offset(-1.9F, 12.0F, 0.0F));
    partDefinition.addOrReplaceChild("jacket", CubeListBuilder.create().texOffs(16, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, arg.extend(0.25F)), PartPose.ZERO);
    return meshDefinition;
  }

  protected Iterable<ModelPart> bodyParts() {
    return Iterables.concat(super.bodyParts(), ImmutableList.of(this.leftPants, this.rightPants, this.leftSleeve, this.rightSleeve, this.jacket));
  }

  public void renderEars(PoseStack arg, VertexConsumer arg2, int i, int j) {
    this.ear.copyFrom(this.head);
    this.ear.x = 0.0F;
    this.ear.y = 0.0F;
    this.ear.render(arg, arg2, i, j);
  }

  public void renderCloak(PoseStack arg, VertexConsumer arg2, int i, int j) {
    this.cloak.render(arg, arg2, i, j);
  }

  public void setupAnim(T arg, float f, float g, float h, float i, float j) {
    super.setupAnim(arg, f, g, h, i, j);
    this.leftPants.copyFrom(this.leftLeg);
    this.rightPants.copyFrom(this.rightLeg);
    this.leftSleeve.copyFrom(this.leftArm);
    this.rightSleeve.copyFrom(this.rightArm);
    this.jacket.copyFrom(this.body);
    if (arg.getItemBySlot(EquipmentSlot.CHEST).isEmpty()) {
      if (arg.isCrouching()) {
        this.cloak.z = 1.4F;
        this.cloak.y = 1.85F;
      } else {
        this.cloak.z = 0.0F;
        this.cloak.y = 0.0F;
      }
    } else if (arg.isCrouching()) {
      this.cloak.z = 0.3F;
      this.cloak.y = 0.8F;
    } else {
      this.cloak.z = -1.1F;
      this.cloak.y = -0.85F;
    }
  }

  public void setAllVisible(boolean bl) {
    super.setAllVisible(bl);
    this.leftSleeve.visible = bl;
    this.rightSleeve.visible = bl;
    this.leftPants.visible = bl;
    this.rightPants.visible = bl;
    this.jacket.visible = bl;
    this.cloak.visible = bl;
    this.ear.visible = bl;
  }

  public void translateToHand(HumanoidArm arg, PoseStack arg2) {
    ModelPart modelPart = this.getArm(arg);
    if (this.slim) {
      float f = 0.5F * (float)(arg == HumanoidArm.RIGHT ? 1 : -1);
      modelPart.x += f;
      modelPart.translateAndRotate(arg2);
      modelPart.x -= f;
    } else {
      modelPart.translateAndRotate(arg2);
    }

  }

  public ModelPart getRandomModelPart(RandomSource arg) {
    return this.parts.get(arg.nextInt(this.parts.size()));
  }
}

