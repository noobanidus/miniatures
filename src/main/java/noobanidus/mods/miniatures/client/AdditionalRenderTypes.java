package noobanidus.mods.miniatures.client;

import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.resources.ResourceLocation;

import net.minecraft.client.renderer.RenderStateShard.OffsetTexturingStateShard;
import net.minecraft.client.renderer.RenderStateShard.TextureStateShard;
import net.minecraft.client.renderer.RenderType.CompositeState;

// TODO?
public class AdditionalRenderTypes extends RenderType {
  public AdditionalRenderTypes(String p_i225992_1_, VertexFormat p_i225992_2_, VertexFormat.Mode p_i225992_3_, int p_i225992_4_, boolean p_i225992_5_, boolean p_i225992_6_, Runnable p_i225992_7_, Runnable p_i225992_8_) {
    super(p_i225992_1_, p_i225992_2_, p_i225992_3_, p_i225992_4_, p_i225992_5_, p_i225992_6_, p_i225992_7_, p_i225992_8_);
  }

  public static RenderType getFullbrightLayer(ResourceLocation pLocation, float pU, float pV) {
    return create("fullbright_entity", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, CompositeState.builder().setShaderState(RENDERTYPE_ENTITY_TRANSLUCENT_SHADER).setTextureState(new TextureStateShard(pLocation, false, false)).setTexturingState(new OffsetTexturingStateShard(pU, pV)).setCullState(NO_CULL).setLightmapState(LIGHTMAP).setTransparencyState(RenderStateShard.NO_TRANSPARENCY).setOverlayState(NO_OVERLAY).createCompositeState(false));
  }

  public static RenderType getGlowing(ResourceLocation pLocation) {
    return create("spirit_entity", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, CompositeState.builder().setShaderState(RENDERTYPE_ENTITY_TRANSLUCENT_SHADER).setTextureState(new TextureStateShard(pLocation, false, false)).setCullState(NO_CULL).setLightmapState(LIGHTMAP).setTransparencyState(RenderStateShard.GLINT_TRANSPARENCY).setOverlayState(NO_OVERLAY).createCompositeState(false));
  }

  public static RenderType getLightning(ResourceLocation pLocation) {
    return create("spirit_entity", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, CompositeState.builder().setShaderState(RENDERTYPE_ENTITY_TRANSLUCENT_SHADER).setTextureState(new TextureStateShard(pLocation, false, false)).setCullState(NO_CULL).setLightmapState(LIGHTMAP).setTransparencyState(RenderStateShard.ADDITIVE_TRANSPARENCY).setOverlayState(NO_OVERLAY).createCompositeState(false));
  }
}
