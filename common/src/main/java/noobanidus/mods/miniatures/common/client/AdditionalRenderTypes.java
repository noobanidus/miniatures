package noobanidus.mods.miniatures.common.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

// TODO: CACHE THESE IMPORTANT
public class AdditionalRenderTypes {
  private static Function<ResourceLocation, RenderType> GLOWING = Util.memoize((pLocation) -> RenderType.create("spirit_entity", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder()
      .setTextureState(new RenderStateShard.TextureStateShard(pLocation, false, false)).setCullState(RenderType.NO_CULL).setLightmapState(RenderType.LIGHTMAP)
      .setTransparencyState(RenderStateShard.GLINT_TRANSPARENCY).setOverlayState(OverlayTexture.NO_OVERLAY)
      .createCompositeState(false)));

  public static RenderType getGlowing (ResourceLocation location) {
    return GLOWING.apply(location);

  }

  private static Function<ResourceLocation, RenderType> OTHER_GLOWING = Util.memoize((pLocation) -> RenderType.create("spirit_entity", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder()
      .setShaderState(RENDERTYPE_ENTITY_TRANSLUCENT_SHADER)
      .setTextureState(new TextureStateShard(pLocation, false, false)).setCullState(NO_CULL).setLightmapState(LIGHTMAP)
      .setTransparencyState(RenderStateShard.LIGHTNING_TRANSPARENCY).setOverlayState(NO_OVERLAY)
      .createCompositeState(false)));

  public static RenderType getOtherGlowing (ResourceLocation location) {
    return OTHER_GLOWING.apply(location);
  }
}
