package noobanidus.mods.miniatures.common.client.model;

import net.minecraft.client.model.geom.ModelPart;
import noobanidus.mods.miniatures.common.client.AdditionalRenderTypes;
import noobanidus.mods.miniatures.common.entity.MiniMeEntity;

public class GhostlyMiniMeModel extends MiniMeModel {
  public GhostlyMiniMeModel(ModelPart root, boolean slim) {
    super(AdditionalRenderTypes::getOtherGlowing, root, slim);
  }
}
