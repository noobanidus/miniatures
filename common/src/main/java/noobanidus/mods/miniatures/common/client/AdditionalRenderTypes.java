package noobanidus.mods.miniatures.common.client;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.TriState;

import java.util.function.Function;

public class AdditionalRenderTypes {
  // TODO: REGISTER THESE
  public static final RenderPipeline GLOWING_PIPELINE =
      RenderPipeline.builder(RenderPipelines.ENTITY_SNIPPET)
          .withLocation("pipeline/entity_translucent")
          .withShaderDefine("ALPHA_CUTOUT", 0.02F)
          .withSampler("Sampler1")
          .withBlend(BlendFunction.GLINT)
          .withCull(false)
          .build();

  public static final RenderPipeline OTHER_GLOWING_PIPELINE =
      RenderPipeline.builder(RenderPipelines.ENTITY_SNIPPET)
          .withLocation("pipeline/entity_translucent")
          .withShaderDefine("ALPHA_CUTOUT", 0.02F)
          .withSampler("Sampler1")
          .withBlend(BlendFunction.LIGHTNING)
          .withCull(false)
          .build();

  private static final Function<ResourceLocation, RenderType> GLOWING = Util.memoize((pLocation) -> RenderType.create("spirit_entity", 256, false, true, GLOWING_PIPELINE, RenderType.CompositeState.builder()
      .setTextureState(new RenderStateShard.TextureStateShard(pLocation, TriState.FALSE, false)).setLightmapState(RenderType.LIGHTMAP)
      .setOverlayState(RenderType.NO_OVERLAY)
      .createCompositeState(false)));

  public static RenderType getGlowing (ResourceLocation location) {
    return GLOWING.apply(location);
  }

  private static final Function<ResourceLocation, RenderType> OTHER_GLOWING = Util.memoize((pLocation) -> RenderType.create("spirit_entity2", 256, false, true, OTHER_GLOWING_PIPELINE, RenderType.CompositeState.builder()
      .setTextureState(new RenderStateShard.TextureStateShard(pLocation, TriState.FALSE, false)).setLightmapState(RenderType.LIGHTMAP)
      .setOverlayState(RenderType.NO_OVERLAY)
      .createCompositeState(false)));

  public static RenderType getOtherGlowing (ResourceLocation location) {
    return OTHER_GLOWING.apply(location);
  }
}
