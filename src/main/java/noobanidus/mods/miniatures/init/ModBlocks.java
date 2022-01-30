package noobanidus.mods.miniatures.init;

import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import noobanidus.mods.miniatures.block.SensorTorchBlock;

import static noobanidus.mods.miniatures.Miniatures.REGISTRATE;

public class ModBlocks {
  public static final BlockEntry<SensorTorchBlock> SENSOR_TORCH_BLOCK = REGISTRATE.block("sensor_torch", SensorTorchBlock::new)
      .properties(o -> BlockBehaviour.Properties.copy(Blocks.TORCH))
      .blockstate((ctx, p) -> p.getVariantBuilder(ctx.getEntry()).forAllStates(state -> ConfiguredModel.builder().modelFile(p.models().getExistingFile(new ResourceLocation("minecraft", "block/torch"))).build()))
      .register();

  public static void load() {
  }
}
