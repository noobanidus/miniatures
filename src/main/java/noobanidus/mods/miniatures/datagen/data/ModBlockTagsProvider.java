package noobanidus.mods.miniatures.datagen.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import noobanidus.mods.miniatures.MiniTags;
import noobanidus.mods.miniatures.Miniatures;
import noobanidus.mods.miniatures.init.ModBlocks;
import noobanidus.mods.miniatures.init.ModEntities;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends BlockTagsProvider {
  public ModBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper fileHelper) {
    super(output, lookupProvider, Miniatures.MODID, fileHelper);
  }

  @Override
  protected void addTags(HolderLookup.Provider provider) {
    tag(MiniTags.Blocks.BREAK_BLOCKS).add(Blocks.TORCH, ModBlocks.SENSOR_TORCH_BLOCK.get());
    tag(MiniTags.Blocks.BREAK_BLOCKS).addTag(BlockTags.FLOWERS);
  }
}
