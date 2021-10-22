package noobanidus.mods.miniatures.client.renderer.layers;

import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.EnergyLayer;
import net.minecraft.entity.IChargeableMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import noobanidus.mods.miniatures.client.ModelHolder;
import noobanidus.mods.miniatures.client.model.PlayerRenderModel;

public class ChargedLayer<T extends LivingEntity & IChargeableMob, M extends PlayerRenderModel<T>> extends EnergyLayer<T, M> {
  private static final ResourceLocation POWER_LOCATION = new ResourceLocation("textures/entity/creeper/creeper_armor.png");

  public ChargedLayer(IEntityRenderer<T, M> p_i50947_1_) {
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
}