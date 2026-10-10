package noobanidus.mods.miniatures.neoforge.datagen;


import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.advancements.AdvancementProvider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import noobanidus.mods.miniatures.common.api.MiniaturesAPI;
import noobanidus.mods.miniatures.neoforge.datagen.assets.ModBlockstateProvider;
import noobanidus.mods.miniatures.neoforge.datagen.assets.ModLanguageProvider;
import noobanidus.mods.miniatures.neoforge.datagen.data.ModBlockTagsProvider;
import noobanidus.mods.miniatures.neoforge.datagen.data.ModEntityTypeTagsProvider;
import noobanidus.mods.miniatures.neoforge.datagen.data.ModLootTableProvider;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid= MiniaturesAPI.MODID)
public class ModDatagen {
  @SubscribeEvent
  public static void gatherData(GatherDataEvent.Client event) {
    DataGenerator generator = event.getGenerator();
    PackOutput packOutput = generator.getPackOutput();
    CompletableFuture<HolderLookup.Provider> worldLookupProvider = event.getWorldLookupProvider();

    generator.addProvider(true, new ModBlockTagsProvider(packOutput, worldLookupProvider));
    generator.addProvider(true, new ModEntityTypeTagsProvider(packOutput, worldLookupProvider));
    generator.addProvider(true, new ModBlockstateProvider(packOutput));
    generator.addProvider(true, new ModLanguageProvider(packOutput, "en_us"));
    generator.addProvider(true, new ModLanguageProvider(packOutput, "en_ud"));
    generator.addProvider(true, DatapackBuiltinEntriesProvider.forReloadableLayer(packOutput, "Miniatures Datapacks", event.getWorldLookupProvider(), event.getReloadableLookupProvider(), new RegistrySetBuilder().add(Registries.LOOT_TABLE, ModLootTableProvider.create()), Set.of("miniatures")));
  }
}
