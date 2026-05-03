package noobanidus.mods.miniatures.fabric.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import noobanidus.mods.miniatures.common.api.MiniaturesAPI;
import noobanidus.mods.miniatures.common.block.SensorTorchBlock;

public class ModBlocks {
  public static final BlockBehaviour.Properties PROPERTIES = BlockBehaviour.Properties.ofFullCopy(Blocks.TORCH);

  public static SensorTorchBlock SENSOR_TORCH_BLOCK;

  public static void register() {
    SENSOR_TORCH_BLOCK = (SensorTorchBlock) Blocks.register(ResourceKey.create(Registries.BLOCK, MiniaturesAPI.rl("sensor_torch")), SensorTorchBlock::new, PROPERTIES);
  }
}
