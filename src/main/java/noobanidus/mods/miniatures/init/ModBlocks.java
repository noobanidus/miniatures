package noobanidus.mods.miniatures.init;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import noobanidus.mods.miniatures.Miniatures;
import noobanidus.mods.miniatures.block.SensorTorchBlock;

public class ModBlocks {
  public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Miniatures.MODID);
  public static final RegistryObject<SensorTorchBlock> SENSOR_TORCH_BLOCK = BLOCKS.register("sensor_torch", () ->
          new SensorTorchBlock(BlockBehaviour.Properties.copy(Blocks.TORCH)));

  public static void load(IEventBus eventBus) {
    BLOCKS.register(eventBus);
  }
}
