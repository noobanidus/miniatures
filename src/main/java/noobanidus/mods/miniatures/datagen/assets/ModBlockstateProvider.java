package noobanidus.mods.miniatures.datagen.assets;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;
import noobanidus.mods.miniatures.Miniatures;
import noobanidus.mods.miniatures.init.ModBlocks;

public class ModBlockstateProvider extends BlockStateProvider {
  public ModBlockstateProvider(PackOutput output, ExistingFileHelper fileHelper) {
    super(output, Miniatures.MODID, fileHelper);
  }

  @Override
  protected void registerStatesAndModels() {
    getVariantBuilder(ModBlocks.SENSOR_TORCH_BLOCK.get())
            .forAllStates(state -> ConfiguredModel.builder()
                    .modelFile(models().getExistingFile(new ResourceLocation("minecraft", "block/torch"))).build()
            );
  }
}
