package noobanidus.mods.miniatures.client;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import noobanidus.mods.miniatures.client.model.MiniMeModel;
import noobanidus.mods.miniatures.entity.MiniMeEntity;
import noobanidus.mods.miniatures.setup.ClientSetup;

public class ModelHolder {
  public static MiniMeModel<MiniMeEntity> miniMe;
  public static MiniMeModel<MiniMeEntity> miniMeSlim;
  public static MiniMeModel<MiniMeEntity> chargedMiniMe;

  public static void init(EntityRendererProvider.Context context) {
    miniMe = new MiniMeModel<>(context.bakeLayer(ClientSetup.MINI_ME), false);
    miniMeSlim = new MiniMeModel<>(context.bakeLayer(ClientSetup.MINI_ME_SLIM), true);
    chargedMiniMe = new MiniMeModel<>(context.bakeLayer(ClientSetup.CHARGED_MINI_ME), false);
  }
}
