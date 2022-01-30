package noobanidus.mods.miniatures.client.model;

import net.minecraft.client.model.geom.ModelPart;
import noobanidus.mods.miniatures.client.AdditionalRenderTypes;
import noobanidus.mods.miniatures.entity.MiniMeEntity;

public class GhostlyMiniMeModel<E extends MiniMeEntity> extends MiniMeModel<E> {
  public GhostlyMiniMeModel(ModelPart root, boolean slim) {
    super(AdditionalRenderTypes::getLightning, root, slim);
  }
}
