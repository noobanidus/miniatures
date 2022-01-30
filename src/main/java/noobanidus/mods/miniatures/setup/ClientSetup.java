package noobanidus.mods.miniatures.setup;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import noobanidus.mods.miniatures.Miniatures;
import noobanidus.mods.miniatures.client.ModelHolder;
import noobanidus.mods.miniatures.client.model.MiniMeModel;
import noobanidus.mods.miniatures.client.model.PlayerRenderModel;
import noobanidus.mods.miniatures.client.renderer.entity.MaxiMeRenderer;
import noobanidus.mods.miniatures.client.renderer.entity.MiniMeRenderer;
import noobanidus.mods.miniatures.init.ModBlocks;
import noobanidus.mods.miniatures.init.ModEntities;

@OnlyIn(Dist.CLIENT)
public class ClientSetup {
  public static final ModelLayerLocation MINI_ME = new ModelLayerLocation(new ResourceLocation(Miniatures.MODID, "mini_me"), "main");
  public static final ModelLayerLocation MINI_ME_ARMOR = new ModelLayerLocation(new ResourceLocation(Miniatures.MODID, "mini_me"), "outer_armor");
  public static final ModelLayerLocation MINI_ME_SLIM = new ModelLayerLocation(new ResourceLocation(Miniatures.MODID, "mini_me_slim"), "main");
  public static final ModelLayerLocation CHARGED_MINI_ME = new ModelLayerLocation(new ResourceLocation(Miniatures.MODID, "charged_mini_me"), "main");

  public static void init(FMLClientSetupEvent event) {
    // TODO:
    event.enqueueWork(() -> {
      RenderType rendertype = RenderType.cutoutMipped();
      ItemBlockRenderTypes.setRenderLayer(ModBlocks.SENSOR_TORCH_BLOCK.get(), rendertype);
    });
  }


  public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
    event.registerEntityRenderer(ModEntities.MINIME.get(), MiniMeRenderer::new);
    event.registerEntityRenderer(ModEntities.MAXIME.get(), MaxiMeRenderer::new);
    event.registerEntityRenderer(ModEntities.ME.get(), MaxiMeRenderer::new);
  }

  public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
    LayerDefinition armor = LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(1.02F), 0.0F), 64, 32);

    event.registerLayerDefinition(MINI_ME, () -> LayerDefinition.create(PlayerRenderModel.createMesh(CubeDeformation.NONE, false), 64, 64));
    event.registerLayerDefinition(MINI_ME_ARMOR, () -> armor);
    event.registerLayerDefinition(MINI_ME_SLIM, () -> LayerDefinition.create(PlayerRenderModel.createMesh(CubeDeformation.NONE, true), 64, 64));
    event.registerLayerDefinition(CHARGED_MINI_ME, () -> LayerDefinition.create(PlayerRenderModel.createMesh(new CubeDeformation(1.0F), false), 64, 64));
  }
}
