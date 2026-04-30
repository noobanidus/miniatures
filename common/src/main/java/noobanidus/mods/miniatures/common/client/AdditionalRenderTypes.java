package noobanidus.mods.miniatures.common.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

// TODO: CACHE THESE IMPORTANT
public class AdditionalRenderTypes {
  public AdditionalRenderTypes(String p_i225992_1_, VertexFormat p_i225992_2_, VertexFormat.Mode p_i225992_3_, int p_i225992_4_, boolean p_i225992_5_, boolean p_i225992_6_, Runnable p_i225992_7_, Runnable p_i225992_8_) {
    super(p_i225992_1_, p_i225992_2_, p_i225992_3_, p_i225992_4_, p_i225992_5_, p_i225992_6_, p_i225992_7_, p_i225992_8_);
  }

  private static Function<ResourceLocation, RenderType> GLOWING = Util.memoize((pLocation) -> RenderType.create("spirit_entity", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, CompositeState.builder()
      .setShaderState(RENDERTYPE_ENTITY_TRANSLUCENT_SHADER)
      .setTextureState(new TextureStateShard(pLocation, false, false)).setCullState(NO_CULL).setLightmapState(LIGHTMAP)
      .setTransparencyState(RenderStateShard.GLINT_TRANSPARENCY).setOverlayState(NO_OVERLAY)
      .createCompositeState(false)));

  public static RenderType getGlowing (ResourceLocation location) {
    return GLOWING.apply(location);

  }

  private static Function<ResourceLocation, RenderType> OTHER_GLOWING = Util.memoize((pLocation) -> create("spirit_entity", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, CompositeState.builder()
      .setShaderState(RENDERTYPE_ENTITY_TRANSLUCENT_SHADER)
      .setTextureState(new TextureStateShard(pLocation, false, false)).setCullState(NO_CULL).setLightmapState(LIGHTMAP)
      .setTransparencyState(RenderStateShard.LIGHTNING_TRANSPARENCY).setOverlayState(NO_OVERLAY)
      .createCompositeState(false)));

  public static RenderType getOtherGlowing (ResourceLocation location) {
    return OTHER_GLOWING.apply(location);
  }
}
