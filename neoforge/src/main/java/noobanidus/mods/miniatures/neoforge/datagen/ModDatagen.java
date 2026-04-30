package noobanidus.mods.miniatures.neoforge.datagen;


import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import noobanidus.mods.miniatures.neoforge.datagen.assets.ModBlockstateProvider;
import noobanidus.mods.miniatures.neoforge.datagen.assets.ModLanguageProvider;
import noobanidus.mods.miniatures.neoforge.datagen.data.ModBlockTagsProvider;
import noobanidus.mods.miniatures.neoforge.datagen.data.ModEntityTypeTagsProvider;
import noobanidus.mods.miniatures.neoforge.datagen.data.ModLootTableProvider;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class ModDatagen {
  @SubscribeEvent
  public static void gatherData(GatherDataEvent event) {
    DataGenerator generator = event.getGenerator();
    PackOutput packOutput = generator.getPackOutput();
    CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
    ExistingFileHelper helper = event.getExistingFileHelper();

    if (event.includeServer()) {
      generator.addProvider(true, new ModLootTableProvider(packOutput, lookupProvider));
      generator.addProvider(true, new ModBlockTagsProvider(packOutput, lookupProvider, helper));
      generator.addProvider(true, new ModEntityTypeTagsProvider(packOutput, lookupProvider, helper));
    }
    if (event.includeClient()) {
      generator.addProvider(true, new ModBlockstateProvider(packOutput, helper));
      generator.addProvider(true, new ModLanguageProvider(packOutput, "en_us"));
      generator.addProvider(true, new ModLanguageProvider(packOutput, "en_ud"));
    }
  }
}
