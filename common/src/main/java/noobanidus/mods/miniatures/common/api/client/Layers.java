package noobanidus.mods.miniatures.common.api.client;

import net.minecraft.client.model.geom.ModelLayerLocation;
import noobanidus.mods.miniatures.common.api.MiniaturesAPI;

public class Layers {
  public static final ModelLayerLocation MINI_ME = new ModelLayerLocation(MiniaturesAPI.rl("mini_me"), "main");
  public static final ModelLayerLocation MINI_ME_ARMOR = new ModelLayerLocation(MiniaturesAPI.rl("mini_me"), "outer_armor");
  public static final ModelLayerLocation MINI_ME_SLIM = new ModelLayerLocation(MiniaturesAPI.rl("mini_me_slim"), "main");
  public static final ModelLayerLocation CHARGED_MINI_ME = new ModelLayerLocation(MiniaturesAPI.rl("charged_mini_me"), "main");
  public static final ModelLayerLocation GLOWING_MINI_ME = new ModelLayerLocation(MiniaturesAPI.rl("glowing_mini_me"), "main");
  public static final ModelLayerLocation GLOWING_MINI_ME_SLIM = new ModelLayerLocation(MiniaturesAPI.rl("glowing_mini_me_slim"), "main");
  public static final ModelLayerLocation GHOSTLY_MINI_ME = new ModelLayerLocation(MiniaturesAPI.rl("ghostly_mini_me"), "main");
  public static final ModelLayerLocation GHOSTLY_MINI_ME_SLIM = new ModelLayerLocation(MiniaturesAPI.rl("ghostly_mini_me_slim"), "main");
}
