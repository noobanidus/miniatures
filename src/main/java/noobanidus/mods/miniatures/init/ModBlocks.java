package noobanidus.mods.miniatures.init;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import noobanidus.mods.miniatures.Miniatures;
import noobanidus.mods.miniatures.block.SensorTorchBlock;

public class ModBlocks {
  public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Miniatures.MODID);
  public static final DeferredBlock<SensorTorchBlock> SENSOR_TORCH_BLOCK = BLOCKS.register("sensor_torch", () ->
          new SensorTorchBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TORCH)));

  public static void load(IEventBus eventBus) {
    BLOCKS.register(eventBus);
  }
}
