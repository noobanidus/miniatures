package noobanidus.mods.miniatures.client;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import noobanidus.mods.miniatures.client.model.GhostlyMiniMeModel;
import noobanidus.mods.miniatures.client.model.GlowingMiniMeModel;
import noobanidus.mods.miniatures.client.model.MiniMeModel;
import noobanidus.mods.miniatures.setup.ClientSetup;

public class ModelHolder {
  public static MiniMeModel miniMe;
  public static MiniMeModel miniMeSlim;
  public static MiniMeModel miniMeGhost;
  public static MiniMeModel miniMeGhostSlim;
  public static MiniMeModel miniMeGlow;
  public static MiniMeModel miniMeGlowSlim;
  public static MiniMeModel chargedMiniMe;

  public static void init (EntityRendererProvider.Context context) {
    miniMe = new MiniMeModel(context.bakeLayer(ClientSetup.MINI_ME), false);
    miniMeSlim = new MiniMeModel(context.bakeLayer(ClientSetup.MINI_ME_SLIM), true);
    miniMeGhost = new MiniMeModel(context.bakeLayer(ClientSetup.MINI_ME), false);
    miniMeGhostSlim =new MiniMeModel(context.bakeLayer(ClientSetup.MINI_ME_SLIM), true);
    miniMeGlow = new MiniMeModel(context.bakeLayer(ClientSetup.MINI_ME), false);
    miniMeGlowSlim = new MiniMeModel(context.bakeLayer(ClientSetup.MINI_ME_SLIM), true);
    chargedMiniMe =new MiniMeModel(context.bakeLayer(ClientSetup.CHARGED_MINI_ME), false);
  }
}
