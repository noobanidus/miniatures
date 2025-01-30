package noobanidus.mods.miniatures.common.client.model;

import net.minecraft.client.model.geom.ModelPart;
import noobanidus.mods.miniatures.common.client.AdditionalRenderTypes;
import noobanidus.mods.miniatures.common.entity.MiniMeEntity;

public class GhostlyMiniMeModel<E extends MiniMeEntity> extends MiniMeModel<E> {
  public GhostlyMiniMeModel(ModelPart root, boolean slim) {
    super(AdditionalRenderTypes::getLightning, root, slim);
  }
}
