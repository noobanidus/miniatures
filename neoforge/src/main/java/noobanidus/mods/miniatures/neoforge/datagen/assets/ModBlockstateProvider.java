package noobanidus.mods.miniatures.neoforge.datagen.assets;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import noobanidus.mods.miniatures.common.api.MiniaturesAPI;
import noobanidus.mods.miniatures.common.block.SensorTorchBlock;
import noobanidus.mods.miniatures.neoforge.init.ModBlocks;

import java.util.stream.Stream;

public class ModBlockstateProvider extends ModelProvider {
  public ModBlockstateProvider(PackOutput p_388260_) {
    super(p_388260_, MiniaturesAPI.MODID);
  }

  @Override
  protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
    var torchBlock = ModBlocks.SENSOR_TORCH_BLOCK.value();

    TextureMapping texturemapping = TextureMapping.torch(torchBlock);
    var torchModel = BlockModelGenerators.plainVariant(ModelTemplates.TORCH.create(torchBlock, texturemapping, blockModels.modelOutput));
    blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(torchBlock)
        .with(PropertyDispatch.initial(SensorTorchBlock.TRIGGERED).select(true, torchModel).select(false, torchModel)));
  }

  @Override
  protected Stream<? extends Holder<Block>> getKnownBlocks() {
    return Stream.of(ModBlocks.SENSOR_TORCH_BLOCK);
  }

  @Override
  protected Stream<? extends Holder<Item>> getKnownItems() {
    return Stream.of();
  }
}
