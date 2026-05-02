package noobanidus.mods.miniatures.common.client;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import noobanidus.mods.miniatures.common.api.client.Layers;
import noobanidus.mods.miniatures.common.client.model.GhostlyMiniMeModel;
import noobanidus.mods.miniatures.common.client.model.GlowingMiniMeModel;
import noobanidus.mods.miniatures.common.client.model.MiniMeModel;
import noobanidus.mods.miniatures.common.entity.MiniMeEntity;

public class ModelHolder {
  public static MiniMeModel miniMe;
  public static MiniMeModel miniMeSlim;
  public static MiniMeModel chargedMiniMe;
  public static MiniMeModel ghostlyMiniMe;
  public static MiniMeModel ghostlyMiniMeSlim;
  public static MiniMeModel glowingMiniMe;
  public static MiniMeModel glowingMiniMeSlim;

  public static void init(EntityRendererProvider.Context context) {
    miniMe = new MiniMeModel(context.bakeLayer(Layers.MINI_ME), false);
    miniMeSlim = new MiniMeModel(context.bakeLayer(Layers.MINI_ME_SLIM), true);
    chargedMiniMe = new MiniMeModel(context.bakeLayer(Layers.CHARGED_MINI_ME), false);
    ghostlyMiniMe = new GhostlyMiniMeModel(context.bakeLayer(Layers.GHOSTLY_MINI_ME), false);
    ghostlyMiniMeSlim = new GhostlyMiniMeModel(context.bakeLayer(Layers.GHOSTLY_MINI_ME_SLIM), true);
    glowingMiniMe = new GlowingMiniMeModel(context.bakeLayer(Layers.GLOWING_MINI_ME), false);
    glowingMiniMeSlim = new GlowingMiniMeModel(context.bakeLayer(Layers.GLOWING_MINI_ME_SLIM), true);
  }
}
