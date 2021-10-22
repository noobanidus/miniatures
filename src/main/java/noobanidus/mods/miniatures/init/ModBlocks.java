package noobanidus.mods.miniatures.init;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.LootTable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import noobanidus.mods.miniatures.Miniatures;
import noobanidus.mods.miniatures.block.SensorTorchBlock;
import noobanidus.mods.miniatures.entity.MaxiMeEntity;
import noobanidus.mods.miniatures.entity.MeEntity;
import noobanidus.mods.miniatures.entity.MiniMeEntity;

import static noobanidus.mods.miniatures.Miniatures.REGISTRATE;

public class ModBlocks {
  public static final RegistryEntry<SensorTorchBlock> SENSOR_TORCH_BLOCK = REGISTRATE.block("sensor_torch", SensorTorchBlock::new)
      .properties(o -> AbstractBlock.Properties.copy(Blocks.TORCH))
      .blockstate((ctx, p) -> p.getVariantBuilder(ctx.getEntry()).forAllStates(state -> ConfiguredModel.builder().modelFile(p.models().getExistingFile(new ResourceLocation("minecraft", "block/torch"))).build()))
      .register();

  public static void load() {
  }
}
