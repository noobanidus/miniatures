package noobanidus.mods.miniatures.neoforge.datagen.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import noobanidus.mods.miniatures.common.api.MiniTags;
import noobanidus.mods.miniatures.common.api.MiniaturesAPI;
import noobanidus.mods.miniatures.neoforge.init.ModBlocks;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends BlockTagsProvider {
  public ModBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper fileHelper) {
    super(output, lookupProvider, MiniaturesAPI.MODID, fileHelper);
  }

  @Override
  protected void addTags(HolderLookup.Provider provider) {
    tag(MiniTags.Blocks.BREAK_BLOCKS).add(Blocks.TORCH, ModBlocks.SENSOR_TORCH_BLOCK.get());
    tag(MiniTags.Blocks.BREAK_BLOCKS).addTag(BlockTags.FLOWERS);
  }
}
