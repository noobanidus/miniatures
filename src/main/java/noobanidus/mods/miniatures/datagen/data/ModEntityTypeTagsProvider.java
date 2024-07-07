package noobanidus.mods.miniatures.datagen.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import noobanidus.mods.miniatures.MiniTags;
import noobanidus.mods.miniatures.Miniatures;
import noobanidus.mods.miniatures.init.ModEntities;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModEntityTypeTagsProvider extends EntityTypeTagsProvider {
  public ModEntityTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper fileHelper) {
    super(output, lookupProvider, Miniatures.MODID, fileHelper);
  }

  @Override
  protected void addTags(HolderLookup.Provider provider) {
    tag(MiniTags.Entity.MOB_ATTACK_BLACKLIST).add(ModEntities.MAXIME.get(), ModEntities.MINIME.get(), ModEntities.ME.get());
  }
}
