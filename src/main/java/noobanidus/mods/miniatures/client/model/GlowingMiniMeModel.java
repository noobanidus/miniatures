package noobanidus.mods.miniatures.client.model;

import net.minecraft.client.model.geom.ModelPart;
import noobanidus.mods.miniatures.client.AdditionalRenderTypes;
import noobanidus.mods.miniatures.entity.MiniMeEntity;

public class GlowingMiniMeModel<E extends MiniMeEntity> extends MiniMeModel<E> {
  public GlowingMiniMeModel(ModelPart part, boolean slim) {
    super(AdditionalRenderTypes::getGlowing, part, slim);
  }
}
