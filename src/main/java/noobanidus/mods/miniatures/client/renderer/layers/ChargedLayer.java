package noobanidus.mods.miniatures.client.renderer.layers;

import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PowerableMob;
import noobanidus.mods.miniatures.client.ModelHolder;
import noobanidus.mods.miniatures.client.model.PlayerRenderModel;

public class ChargedLayer<T extends LivingEntity & PowerableMob, M extends PlayerRenderModel<T>> extends EnergySwirlLayer<T, M> {
  private static final ResourceLocation POWER_LOCATION = new ResourceLocation("textures/entity/creeper/creeper_armor.png");

  public ChargedLayer(RenderLayerParent<T, M> p_i50947_1_) {
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