package noobanidus.mods.miniatures.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.TorchBlock;
import net.minecraft.entity.Entity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class SensorTorchBlock extends TorchBlock {
  public static final BooleanProperty TRIGGERED = BooleanProperty.create("trigger");

  public SensorTorchBlock(Properties pProperties) {
    super(pProperties, ParticleTypes.FLAME);
    this.registerDefaultState(this.defaultBlockState().setValue(TRIGGERED, false));
  }

  @Override
  protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> pBuilder) {
    pBuilder.add(TRIGGERED);
  }

  @Override
  public void entityInside(BlockState pState, World pLevel, BlockPos pPos, Entity pEntity) {
    if (!pLevel.isClientSide() && !pState.getValue(TRIGGERED)) {
      ServerLifecycleHooks.getCurrentServer().getPlayerList().broadcastMessage(new StringTextComponent("First to cross the line was: ").append(pEntity.getName()), ChatType.CHAT, Util.NIL_UUID);
      pLevel.setBlock(pPos, pState.setValue(TRIGGERED, true), 3);
    }
  }
}
