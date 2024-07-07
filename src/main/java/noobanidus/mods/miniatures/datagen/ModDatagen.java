package noobanidus.mods.miniatures.datagen;


import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import noobanidus.mods.miniatures.datagen.assets.ModBlockstateProvider;
import noobanidus.mods.miniatures.datagen.assets.ModLanguageProvider;
import noobanidus.mods.miniatures.datagen.data.ModBlockTagsProvider;
import noobanidus.mods.miniatures.datagen.data.ModEntityTypeTagsProvider;
import noobanidus.mods.miniatures.datagen.data.ModLootTableProvider;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModDatagen {
  @SubscribeEvent
  public static void gatherData(GatherDataEvent event) {
    DataGenerator generator = event.getGenerator();
    PackOutput packOutput = generator.getPackOutput();
    CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
    ExistingFileHelper helper = event.getExistingFileHelper();

    if (event.includeServer()) {
      generator.addProvider(true, new ModLootTableProvider(packOutput));
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
