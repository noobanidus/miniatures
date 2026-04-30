package noobanidus.mods.miniatures.neoforge.datagen.assets;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import noobanidus.mods.miniatures.common.api.MiniaturesAPI;
import noobanidus.mods.miniatures.neoforge.init.ModBlocks;

public class ModBlockstateProvider extends BlockStateProvider {
  public ModBlockstateProvider(PackOutput output, ExistingFileHelper fileHelper) {
    super(output, MiniaturesAPI.MODID, fileHelper);
  }

  @Override
  protected void registerStatesAndModels() {
    getVariantBuilder(ModBlocks.SENSOR_TORCH_BLOCK.get())
            .forAllStates(state -> ConfiguredModel.builder()
                    .modelFile(models().getExistingFile(ResourceLocation.withDefaultNamespace("block/torch"))).build()
            );
  }
}
