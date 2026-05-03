package noobanidus.mods.miniatures.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.RenderType;
import noobanidus.mods.miniatures.common.api.client.Layers;
import noobanidus.mods.miniatures.common.client.AdditionalRenderTypes;
import noobanidus.mods.miniatures.common.client.model.MiniRenderModel;
import noobanidus.mods.miniatures.common.client.renderer.entity.MiniMeRenderer;
import noobanidus.mods.miniatures.fabric.init.ModBlocks;
import noobanidus.mods.miniatures.fabric.init.ModEntities;
import noobanidus.mods.miniatures.fabric.network.NetworkingInit;

@Environment(EnvType.CLIENT)
public class MiniaturesClient implements ClientModInitializer {
  @Override
  public void onInitializeClient() {
    BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SENSOR_TORCH_BLOCK, RenderType.cutoutMipped());

    EntityRendererRegistry.register(ModEntities.ME, MiniMeRenderer::new);
    EntityRendererRegistry.register(ModEntities.MINIME, MiniMeRenderer::new);
    EntityRendererRegistry.register(ModEntities.MAXIME, MiniMeRenderer::new); // TODO: Prayge

    RenderPipelines.register(AdditionalRenderTypes.GLOWING_PIPELINE);
    RenderPipelines.register(AdditionalRenderTypes.OTHER_GLOWING_PIPELINE);

    LayerDefinition armor = LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(1.02f), 0.0f), 64, 32);

    EntityModelLayerRegistry.registerModelLayer(Layers.MINI_ME, () -> LayerDefinition.create(MiniRenderModel.createMesh(CubeDeformation.NONE, false), 64, 64));
    EntityModelLayerRegistry.registerModelLayer(Layers.MINI_ME_ARMOR, () -> armor);
    EntityModelLayerRegistry.registerModelLayer(Layers.MINI_ME_SLIM, () -> LayerDefinition.create(MiniRenderModel.createMesh(CubeDeformation.NONE, true), 64, 64));
    EntityModelLayerRegistry.registerModelLayer(Layers.CHARGED_MINI_ME, () -> LayerDefinition.create(MiniRenderModel.createMesh(new CubeDeformation(1.0F), false), 64, 64));
    EntityModelLayerRegistry.registerModelLayer(Layers.GLOWING_MINI_ME, () -> LayerDefinition.create(MiniRenderModel.createMesh(CubeDeformation.NONE, false), 64, 64));
    EntityModelLayerRegistry.registerModelLayer(Layers.GLOWING_MINI_ME_SLIM, () -> LayerDefinition.create(MiniRenderModel.createMesh(CubeDeformation.NONE, true), 64, 64));
    EntityModelLayerRegistry.registerModelLayer(Layers.GHOSTLY_MINI_ME, () -> LayerDefinition.create(MiniRenderModel.createMesh(CubeDeformation.NONE, false), 64, 64));
    EntityModelLayerRegistry.registerModelLayer(Layers.GHOSTLY_MINI_ME_SLIM, () -> LayerDefinition.create(MiniRenderModel.createMesh(CubeDeformation.NONE, true), 64, 64));

    NetworkingInit.registerClientNetwork();
  }
}