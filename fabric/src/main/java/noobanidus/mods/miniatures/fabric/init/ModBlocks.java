package noobanidus.mods.miniatures.fabric.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import noobanidus.mods.miniatures.common.api.MiniaturesAPI;
import noobanidus.mods.miniatures.common.block.SensorTorchBlock;

public class ModBlocks {
  public static final SensorTorchBlock SENSOR_TORCH_BLOCK = new SensorTorchBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TORCH));

  public static void register() {
    Blocks.register(ResourceKey.create(Registries.BLOCK, MiniaturesAPI.rl("sensor_torch")), SENSOR_TORCH_BLOCK);

  }
}
