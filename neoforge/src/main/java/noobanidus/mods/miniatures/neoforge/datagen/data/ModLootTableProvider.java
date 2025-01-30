package noobanidus.mods.miniatures.neoforge.datagen.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.neoforge.registries.DeferredHolder;
import noobanidus.mods.miniatures.neoforge.init.ModBlocks;
import noobanidus.mods.miniatures.neoforge.init.ModEntities;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class ModLootTableProvider extends LootTableProvider {

  public ModLootTableProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> future) {
    super(packOutput, Set.of(), List.of(
            new SubProviderEntry(ModBlockLoot::new, LootContextParamSets.BLOCK),
            new SubProviderEntry(ModEntityLoot::new, LootContextParamSets.ENTITY)
    ), future);
  }

  private static class ModBlockLoot extends BlockLootSubProvider {

    protected ModBlockLoot(HolderLookup.Provider provider) {
      super(Set.of(), FeatureFlags.REGISTRY.allFlags(), provider);
    }

    @Override
    protected void generate() {
      this.dropOther(ModBlocks.SENSOR_TORCH_BLOCK.get(), Items.AIR);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
      return (Iterable<Block>) ModBlocks.BLOCKS.getEntries().stream().map((holder) -> (Block) holder.value())::iterator;
    }
  }

  private static class ModEntityLoot extends EntityLootSubProvider {
    protected ModEntityLoot(HolderLookup.Provider provider) {
      super(FeatureFlags.REGISTRY.allFlags(), provider);
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
