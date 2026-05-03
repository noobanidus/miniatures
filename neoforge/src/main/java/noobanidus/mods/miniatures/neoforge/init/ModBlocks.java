package noobanidus.mods.miniatures.neoforge.init;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import noobanidus.mods.miniatures.common.block.SensorTorchBlock;
import noobanidus.mods.miniatures.common.api.MiniaturesAPI;

public class ModBlocks {
  public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MiniaturesAPI.MODID);
  public static final DeferredBlock<SensorTorchBlock> SENSOR_TORCH_BLOCK = BLOCKS.register("sensor_torch", () ->
          new SensorTorchBlock(MiniaturesAPI.SENSOR_TORCH_PROPERTIES));

  public static void load(IEventBus eventBus) {
    BLOCKS.register(eventBus);
  }
}
