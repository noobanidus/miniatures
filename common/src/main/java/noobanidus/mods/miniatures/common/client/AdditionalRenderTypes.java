package noobanidus.mods.miniatures.common.client;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.renderer.BindGroupLayouts;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;

import java.util.function.Function;

public class AdditionalRenderTypes {
  public static final RenderPipeline GLOWING_PIPELINE =
      RenderPipeline.builder(RenderPipelines.ENTITY_SNIPPET)
          .withLocation("pipeline/miniatures_glowing")
          .withShaderDefine("ALPHA_CUTOUT", 0.02F)
          .withColorTargetState(new ColorTargetState(BlendFunction.GLINT))
          .withCull(false)
          .build();

  public static final RenderPipeline OTHER_GLOWING_PIPELINE =
      RenderPipeline.builder(RenderPipelines.ENTITY_SNIPPET)
          .withLocation("pipeline/miniatures_other_glowing")
          .withShaderDefine("ALPHA_CUTOUT", 0.02F)
          .withColorTargetState(new ColorTargetState(BlendFunction.LIGHTNING))
          .withCull(false)
          .build();

  private static final Function<Identifier, RenderType> GLOWING = Util.memoize((texture) -> {
    RenderSetup state = RenderSetup.builder(GLOWING_PIPELINE)
        .withTexture("Sampler0", texture)
        .useLightmap()
        .affectsCrumbling()
        .setOutline(RenderSetup.OutlineProperty.AFFECTS_OUTLINE)
        .createRenderSetup();

    return RenderType.create("spirit_entity", state);
  });

  public static RenderType getGlowing(Identifier location) {
    return GLOWING.apply(location);
  }

  private static final Function<Identifier, RenderType> OTHER_GLOWING = Util.memoize((texture) -> {
    RenderSetup state = RenderSetup.builder(OTHER_GLOWING_PIPELINE)
        .withTexture("Sampler0", texture)
        .useLightmap()
        .affectsCrumbling()
        .setOutline(RenderSetup.OutlineProperty.AFFECTS_OUTLINE)
        .createRenderSetup();

    return RenderType.create("spirit_entity2", state);
  });

  public static RenderType getOtherGlowing(Identifier location) {
    return OTHER_GLOWING.apply(location);
  }
}
