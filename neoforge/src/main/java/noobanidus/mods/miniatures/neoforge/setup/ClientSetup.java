package noobanidus.mods.miniatures.neoforge.setup;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterRenderPipelinesEvent;
import noobanidus.mods.miniatures.common.api.client.Layers;
import noobanidus.mods.miniatures.common.client.AdditionalRenderTypes;
import noobanidus.mods.miniatures.common.client.model.MiniRenderModel;
import noobanidus.mods.miniatures.common.client.renderer.entity.MiniMeRenderer;
import noobanidus.mods.miniatures.common.api.MiniaturesAPI;
import noobanidus.mods.miniatures.neoforge.init.ModBlocks;
import noobanidus.mods.miniatures.neoforge.init.ModEntities;

@EventBusSubscriber(modid = MiniaturesAPI.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ClientSetup {

  @SubscribeEvent
  public static void init(FMLClientSetupEvent event) {
    // TODO:
    event.enqueueWork(() -> {
      RenderType rendertype = RenderType.cutoutMipped();
      //noinspection deprecation
      ItemBlockRenderTypes.setRenderLayer(ModBlocks.SENSOR_TORCH_BLOCK.get(), rendertype);
    });
  }

  @SubscribeEvent
  public static void registerPipelines (RegisterRenderPipelinesEvent event) {
    event.registerPipeline(AdditionalRenderTypes.GLOWING_PIPELINE);
    event.registerPipeline(AdditionalRenderTypes.OTHER_GLOWING_PIPELINE);
  }

  @SubscribeEvent
  public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
    event.registerEntityRenderer(ModEntities.MINIME.get(), MiniMeRenderer::new);
    event.registerEntityRenderer(ModEntities.MAXIME.get(), MiniMeRenderer::new);
    event.registerEntityRenderer(ModEntities.ME.get(), MiniMeRenderer::new);
  }

  @SubscribeEvent
  public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
    LayerDefinition armor = LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(1.02F), 0.0F), 64, 32);

    event.registerLayerDefinition(Layers.MINI_ME, () -> LayerDefinition.create(MiniRenderModel.createMesh(CubeDeformation.NONE, false), 64, 64));
    event.registerLayerDefinition(Layers.MINI_ME_ARMOR, () -> armor);
    event.registerLayerDefinition(Layers.MINI_ME_SLIM, () -> LayerDefinition.create(MiniRenderModel.createMesh(CubeDeformation.NONE, true), 64, 64));
    event.registerLayerDefinition(Layers.CHARGED_MINI_ME, () -> LayerDefinition.create(MiniRenderModel.createMesh(new CubeDeformation(1.0F), false), 64, 64));
    event.registerLayerDefinition(Layers.GLOWING_MINI_ME, () -> LayerDefinition.create(MiniRenderModel.createMesh(CubeDeformation.NONE, false), 64, 64));
    event.registerLayerDefinition(Layers.GLOWING_MINI_ME_SLIM, () -> LayerDefinition.create(MiniRenderModel.createMesh(CubeDeformation.NONE, true), 64, 64));
    event.registerLayerDefinition(Layers.GHOSTLY_MINI_ME, () -> LayerDefinition.create(MiniRenderModel.createMesh(CubeDeformation.NONE, false), 64, 64));
    event.registerLayerDefinition(Layers.GHOSTLY_MINI_ME_SLIM, () -> LayerDefinition.create(MiniRenderModel.createMesh(CubeDeformation.NONE, true), 64, 64));
  }
}
