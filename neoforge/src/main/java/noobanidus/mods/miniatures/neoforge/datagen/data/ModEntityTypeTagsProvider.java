package noobanidus.mods.miniatures.neoforge.datagen.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import noobanidus.mods.miniatures.common.api.MiniTags;
import noobanidus.mods.miniatures.common.api.MiniaturesAPI;
import noobanidus.mods.miniatures.neoforge.init.ModEntities;

import java.util.concurrent.CompletableFuture;

public class ModEntityTypeTagsProvider extends EntityTypeTagsProvider {
  public ModEntityTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
    super(output, lookupProvider, MiniaturesAPI.MODID);
  }

  @Override
  protected void addTags(HolderLookup.Provider provider) {
    tag(MiniTags.Entity.MOB_ATTACK_BLACKLIST).add(MiniaturesAPI.MAXIME, MiniaturesAPI.MINIME, MiniaturesAPI.ME);
    tag(MiniTags.Entity.MINI).add(MiniaturesAPI.MINIME, MiniaturesAPI.ME, MiniaturesAPI.MAXIME);
  }
}
