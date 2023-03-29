package noobanidus.mods.miniatures.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraftforge.server.ServerLifecycleHooks;

public class SensorTorchBlock extends TorchBlock {
  public static final BooleanProperty TRIGGERED = BooleanProperty.create("trigger");

  public SensorTorchBlock(Properties pProperties) {
    super(pProperties, ParticleTypes.FLAME);
    this.registerDefaultState(this.defaultBlockState().setValue(TRIGGERED, false));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
    pBuilder.add(TRIGGERED);
  }

  @Override
  @SuppressWarnings("deprecated")
  public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
    if (!pLevel.isClientSide() && !pState.getValue(TRIGGERED)) {
      ServerLifecycleHooks.getCurrentServer().getPlayerList().broadcastSystemMessage(Component.literal("First to cross the line was: ").append(pEntity.getName()), false);
      pLevel.setBlock(pPos, pState.setValue(TRIGGERED, true), 3);
    }
  }
}
