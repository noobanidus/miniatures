package noobanidus.mods.miniatures.neoforge.datagen.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.SingleRegistryBootstrap;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.neoforge.registries.DeferredHolder;
import noobanidus.mods.miniatures.common.api.MiniaturesAPI;
import noobanidus.mods.miniatures.neoforge.init.ModEntities;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class ModLootTableProvider {
  public static SingleRegistryBootstrap<LootTable> create() {
    return new LootTableProvider(Set.of(MiniaturesAPI.MAXIME_LOOT, MiniaturesAPI.MINIME_LOOT, MiniaturesAPI.ME_LOOT), List.of(new LootTableProvider.SubProviderEntry(ModEntityLoot::new, LootContextParamSets.ENTITY)));
  }

/*  private static class ModBlockLoot extends BlockLootSubProvider {

    protected ModBlockLoot(HolderLookup.Provider provider) {
      super(Set.of(), FeatureFlags.REGISTRY.allFlags(), provider);
    }

    @Override
    protected void generate() {
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
      return List.of(ModBlocks.SENSOR_TORCH_BLOCK.value());
    }
  }*/

  private static class ModEntityLoot extends EntityLootSubProvider {
    protected ModEntityLoot(Context context) {
      super(FeatureFlags.REGISTRY.allFlags(), context);
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
