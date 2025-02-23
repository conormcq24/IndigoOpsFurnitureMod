package indigoops.indigooperationsfurnituremod.block.blocklogic;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public class TableBlock extends Block {

    // Enum to track which quarter of the table this block represents
    public enum TablePart implements StringIdentifiable {
        TOP_LEFT("top_left"),
        TOP_RIGHT("top_right"),
        BOTTOM_LEFT("bottom_left"),
        BOTTOM_RIGHT("bottom_right");

        private final String name;

        TablePart(String name) {
            this.name = name;
        }

        @Override
        public String asString() {
            return this.name;
        }
    }


    public static final EnumProperty<TablePart> PART = EnumProperty.of("part", TablePart.class);

    public TableBlock(Settings settings) {
        super(settings.nonOpaque());
        setDefaultState(getStateManager().getDefaultState().with(PART, TablePart.TOP_LEFT));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(PART);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos pos = ctx.getBlockPos();
        World world = ctx.getWorld();

        // Check if we have space for all four blocks
        if (canPlaceTable(world, pos)) {
            return getDefaultState().with(PART, TablePart.BOTTOM_LEFT);
        }

        return null;
    }

    private boolean canPlaceTable(World world, BlockPos pos) {
        return world.getBlockState(pos).isAir()
                && world.getBlockState(pos.east()).isAir()
                && world.getBlockState(pos.north()).isAir()
                && world.getBlockState(pos.north().east()).isAir();
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (!world.isClient) {
            // Place all four parts of the table
            world.setBlockState(pos, state.with(PART, TablePart.BOTTOM_LEFT), Block.NOTIFY_ALL);
            world.setBlockState(pos.east(), state.with(PART, TablePart.BOTTOM_RIGHT), Block.NOTIFY_ALL);
            world.setBlockState(pos.north(), state.with(PART, TablePart.TOP_LEFT), Block.NOTIFY_ALL);
            world.setBlockState(pos.north().east(), state.with(PART, TablePart.TOP_RIGHT), Block.NOTIFY_ALL);
        }
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState,
                                                WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        TablePart part = state.get(PART);

        // Avoid checking before the table is fully placed
        if (world.getBlockState(pos).getBlock() != this) {
            return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
        }

        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    private boolean isTablePart(WorldAccess world, BlockPos pos, TablePart part) {
        BlockState state = world.getBlockState(pos);
        return state.isOf(this) && state.get(PART) == part;
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient && player.isCreative()) {
            TablePart part = state.get(PART);

            // Get the northwest corner position
            BlockPos bottomLeftPos = switch (part) {
                case BOTTOM_LEFT -> pos;
                case BOTTOM_RIGHT -> pos.west();
                case TOP_LEFT -> pos.south();
                case TOP_RIGHT -> pos.south().west();
            };

            // Break all parts
            breakPart(world, bottomLeftPos);
            breakPart(world, bottomLeftPos.east());
            breakPart(world, bottomLeftPos.north());
            breakPart(world, bottomLeftPos.north().east());
        }

        return super.onBreak(world, pos, state, player);
    }

    private void breakPart(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        if (state.isOf(this)) {
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL | Block.SKIP_DROPS);
        }
    }
}