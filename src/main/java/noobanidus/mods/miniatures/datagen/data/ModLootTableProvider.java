package noobanidus.mods.miniatures.datagen.data;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.neoforge.registries.DeferredHolder;
import noobanidus.mods.miniatures.init.ModBlocks;
import noobanidus.mods.miniatures.init.ModEntities;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class ModLootTableProvider extends LootTableProvider {

  public ModLootTableProvider(PackOutput packOutput) {
    super(packOutput, Set.of(), List.of(
            new SubProviderEntry(ModBlockLoot::new, LootContextParamSets.BLOCK),
            new SubProviderEntry(ModEntityLoot::new, LootContextParamSets.ENTITY)
    ));
  }

  @Override
  protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationtracker) {
    map.forEach((name, table) -> table.validate(validationtracker));
  }

  private static class ModBlockLoot extends BlockLootSubProvider {

    protected ModBlockLoot() {
      super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
      this.dropOther(ModBlocks.SENSOR_TORCH_BLOCK.get(), Items.AIR);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
      return (Iterable<Block>) ModBlocks.BLOCKS.getEntries().stream().map((holder) -> (Block)holder.value())::iterator;
    }
  }

  private static class ModEntityLoot extends EntityLootSubProvider {
    protected ModEntityLoot() {
      super(FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    public void generate() {
      this.add(ModEntities.MAXIME.get(), LootTable.lootTable());
      this.add(ModEntities.ME.get(), LootTable.lootTable());
      this.add(ModEntities.MINIME.get(), LootTable.lootTable());
    }

    @Override
    protected Stream<EntityType<?>> getKnownEntityTypes() {
      return ModEntities.ENTITY_TYPES.getEntries().stream().map(DeferredHolder::value);
    }
  }
}
