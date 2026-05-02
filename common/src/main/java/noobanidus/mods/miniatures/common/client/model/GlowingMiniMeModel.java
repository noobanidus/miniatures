package noobanidus.mods.miniatures.common.client.model;

import net.minecraft.client.model.geom.ModelPart;
import noobanidus.mods.miniatures.common.client.AdditionalRenderTypes;
import noobanidus.mods.miniatures.common.entity.MiniMeEntity;

public class GlowingMiniMeModel extends MiniMeModel {
  public GlowingMiniMeModel(ModelPart part, boolean slim) {
    super(AdditionalRenderTypes::getGlowing, part, slim);
  }
}
