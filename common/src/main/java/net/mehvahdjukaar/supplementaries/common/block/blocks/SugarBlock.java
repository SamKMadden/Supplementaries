package net.mehvahdjukaar.supplementaries.common.block.blocks;


import net.mehvahdjukaar.supplementaries.reg.ModParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class SugarBlock extends FallingBlock {

    public SugarBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public void onLand(Level level, BlockPos pos, BlockState blockState, BlockState blockState2, FallingBlockEntity fallingBlock) {
        if (isWater(blockState2)) {


            //level.addDestroyBlockEffect(blockPos, blockState);

            level.destroyBlock(pos, false);
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (touchesLiquid(level, pos)) {
            level.blockEvent(pos, state.getBlock(), 1, 0);
        } else super.tick(state, level, pos, random);
    }

    @Override
    public boolean triggerEvent(BlockState state, Level level, BlockPos pos, int id, int param) {
        if (id == 1) {
            if (level.isClientSide) {
                this.dissolve(level, pos);
            }
            if (shouldTurnToWater(level, pos)) {
                level.setBlockAndUpdate(pos, Blocks.WATER.defaultBlockState());
            } else level.removeBlock(pos, false);
            return true;
        }
        return super.triggerEvent(state, level, pos, id, param);
    }

    private boolean shouldTurnToWater(Level level, BlockPos pos) {
        BlockPos.MutableBlockPos mutableBlockPos = pos.mutable();
        int count = 0;
        for (Direction direction : Direction.values()) {
            if (direction != Direction.DOWN) {
                mutableBlockPos.setWithOffset(pos, direction);
                var s = level.getBlockState(mutableBlockPos);
                if((direction == Direction.UP && isWater(s) || s.getBlock() == Blocks.WATER)){
                    count++;
                }
                if (count >= 2) return true;
            }
        }
        return false;
    }

    private boolean touchesLiquid(BlockGetter level, BlockPos pos) {
        boolean bl = false;
        BlockPos.MutableBlockPos mutableBlockPos = pos.mutable();
        BlockState blockState = level.getBlockState(mutableBlockPos);
        if (isWater(blockState)) return true;
        for (Direction direction : Direction.values()) {
            mutableBlockPos.setWithOffset(pos, direction);
            blockState = level.getBlockState(mutableBlockPos);
            if (isWater(blockState) && !blockState.isFaceSturdy(level, pos, direction.getOpposite())) {
                bl = true;
                break;
            }
        }
        return bl;
    }

    private boolean isWater(BlockState state) {
        return state.getFluidState().is(FluidTags.WATER);
    }


    public void dissolve(Level level, BlockPos pos) {

        double d2 = 0.25;
        int d = 0, e = 0, f = 0;

        int amount = 4;
        for (int ax = 0; ax < amount; ++ax) {
            for (int ay = 0; ay < amount; ++ay) {
                for (int az = 0; az < amount; ++az) {
                    double s = ((double) ax + 0.5) / (double) amount;
                    double t = ((double) ay + 0.5) / (double) amount;
                    double u = ((double) az + 0.5) / (double) amount;
                    double px = s + d;
                    double py = t + e;
                    double pz = u + f;
                    level.addParticle(ModParticles.SUGAR_PARTICLE.get(),
                            (double) pos.getX() + px, (double) pos.getY() + py, (double) pos.getZ() + pz,
                            s - 0.5, 0, u - 0.5);
                }
            }
        }

    }

    @Override
    public int getDustColor(BlockState state, BlockGetter level, BlockPos pos) {
        return state.getMapColor(level, pos).col;
    }
}