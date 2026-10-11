package noobanidus.mods.miniatures.common.client.model;

import net.minecraft.client.model.geom.ModelPart;
import noobanidus.mods.miniatures.common.client.AdditionalRenderTypes;

public class SubtractiveMiniMeModel extends MiniMeModel {
  public SubtractiveMiniMeModel(ModelPart root, boolean slim) {
    super(AdditionalRenderTypes::getSubtractive, root, slim);
  }
}
