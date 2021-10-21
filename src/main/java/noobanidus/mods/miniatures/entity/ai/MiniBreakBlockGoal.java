package noobanidus.mods.miniatures.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.Tags;
import noobanidus.mods.miniatures.config.ConfigManager;

import javax.annotation.Nullable;
import java.util.Random;

public class MiniBreakBlockGoal extends MoveToBlockGoal {
   private final Tags.IOptionalNamedTag<Block> block;
   private final MobEntity entity;
   private int breakingTime;

   public MiniBreakBlockGoal(Tags.IOptionalNamedTag<Block> blockIn, CreatureEntity creature, double speed, int yMax) {
      super(creature, speed, 24, yMax);
      this.block = blockIn;
      this.entity = creature;
      this.nextStartTick = this.nextStartTick(this.mob);
   }

   /**
    * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
    * method as well.
    */
   public boolean canUse() {
      if (!ConfigManager.getBreaksBlocks()) {
         return false;
      }
      if (!net.minecraftforge.common.ForgeHooks.canEntityDestroy(this.entity.level, this.blockPos, this.entity)) {
         return false;
      } else if (this.nextStartTick > 0) {
         --this.nextStartTick;
         return false;
      } else if (this.tryFindBlock()) {
         this.nextStartTick = 20;
         return true;
      } else {
         this.nextStartTick = this.nextStartTick(this.mob);
         return false;
      }
   }

   @Override
   public boolean canContinueToUse() {
      return !ConfigManager.getBreaksBlocks() && (ConfigManager.getDistractionValue() != 0 && this.mob.getRandom().nextDouble() > ConfigManager.getDistractionValue()) && super.canContinueToUse();
   }

   private boolean tryFindBlock() {
      return this.blockPos != null && this.isValidTarget(this.mob.level, this.blockPos) || this.findNearestBlock();
   }

   /**
    * Reset the task's internal state. Called when this task is interrupted by another one
    */
   public void stop() {
      super.stop();
      this.entity.fallDistance = 1.0F;
   }

   /**
    * Execute a one shot task or start executing a continuous task
    */
   public void start() {
      super.start();
      this.breakingTime = 0;
   }

   public void playBreakingSound(IWorld worldIn, BlockPos pos) {
   }

   public void playBrokenSound(World worldIn, BlockPos pos) {
   }

   @Override
   public double acceptedDistance() {
      return 2.5;
   }

   /**
    * Keep ticking a continuous task that has already been started
    */
   public void tick() {
      super.tick();
      World world = this.entity.level;
      BlockPos blockpos = this.entity.blockPosition();
      BlockPos blockpos1 = this.findTarget(blockpos, world);
      Random random = this.entity.getRandom();
      if (this.isReachedTarget() && blockpos1 != null) {
         if (this.breakingTime > 0) {
            Vector3d vector3d = this.entity.getDeltaMovement();
            this.entity.setDeltaMovement(vector3d.x, 0.3D, vector3d.z);
            if (!world.isClientSide) {
               double d0 = 0.08D;
               ((ServerWorld)world).sendParticles(new ItemParticleData(ParticleTypes.ITEM, new ItemStack(Items.EGG)), (double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.7D, (double)blockpos1.getZ() + 0.5D, 3, ((double)random.nextFloat() - 0.5D) * 0.08D, ((double)random.nextFloat() - 0.5D) * 0.08D, ((double)random.nextFloat() - 0.5D) * 0.08D, (double)0.15F);
            }
         }

         if (this.breakingTime % 2 == 0) {
            Vector3d vector3d1 = this.entity.getDeltaMovement();
            this.entity.setDeltaMovement(vector3d1.x, -0.3D, vector3d1.z);
            if (this.breakingTime % 6 == 0) {
               this.playBreakingSound(world, this.blockPos);
            }
         }

         if (this.breakingTime > 60) {
            world.destroyBlock(blockpos1, !ConfigManager.getDestroysBlocks(), this.mob);
            if (!world.isClientSide) {
               for(int i = 0; i < 20; ++i) {
                  double d3 = random.nextGaussian() * 0.02D;
                  double d1 = random.nextGaussian() * 0.02D;
                  double d2 = random.nextGaussian() * 0.02D;
                  ((ServerWorld)world).sendParticles(ParticleTypes.POOF, (double)blockpos1.getX() + 0.5D, (double)blockpos1.getY(), (double)blockpos1.getZ() + 0.5D, 1, d3, d1, d2, (double)0.15F);
               }

               this.playBrokenSound(world, blockpos1);
            }
         }

         ++this.breakingTime;
      }

   }

   @Nullable
   private BlockPos findTarget(BlockPos pos, IBlockReader worldIn) {
      if (worldIn.getBlockState(pos).is(this.block)) {
         return pos;
      } else {
         BlockPos[] ablockpos = new BlockPos[]{pos.below(), pos.west(), pos.east(), pos.north(), pos.south(), pos.below().below()};

         for(BlockPos blockpos : ablockpos) {
            if (worldIn.getBlockState(blockpos).is(this.block)) {
               return blockpos;
            }
         }

         return null;
      }
   }

   /**
    * Return true to set given position as destination
    */
   protected boolean isValidTarget(IWorldReader worldIn, BlockPos pos) {
      IChunk ichunk = worldIn.getChunk(pos.getX() >> 4, pos.getZ() >> 4, ChunkStatus.FULL, false);
      if (ichunk == null) {
         return false;
      } else {
         return ichunk.getBlockState(pos).is(this.block) && ichunk.getBlockState(pos.above()).isAir() && ichunk.getBlockState(pos.above(2)).isAir();
      }
   }

   @Override
   protected int nextStartTick(CreatureEntity pCreature) {
      return ConfigManager.getBaseRunDelay() + pCreature.getRandom().nextInt(ConfigManager.getRandomRunDelay());
   }
}
