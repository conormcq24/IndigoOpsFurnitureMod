package indigoops.indigooperationsfurnituremod.block.blocklogic;

import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public class TableBlock extends Block {
    // Enum to track what type of wood the table is composed of for texturing purposes
    public enum TableWood implements StringIdentifiable {
        ACACIA("acacia"),
        BIRCH("birch"),
        CHERRY("cherry"),
        CRIMSON("crimson"),
        DARKOAK("dark_oak"),
        JUNGLE("jungle"),
        MANGROVE("mangrove"),
        OAK("oak"),
        SPRUCE("spruce"),
        WARPED("warped");

        private final String woodType;

        TableWood(String woodType) { this.woodType = woodType; }

        @Override
        public String asString() { return this.woodType; }

    }
    // Enum to track if the table has a cloth and what color it is if it does, for texturing purposes
    public enum TableCloth implements StringIdentifiable {
        NONE("none"),
        WHITE("white"),
        LIGHTGRAY("light_gray"),
        GRAY("gray"),
        BLACK("black"),
        BROWN("brown"),
        RED("red"),
        ORANGE("orange"),
        YELLOW("yellow"),
        LIME("lime"),
        GREEN("green"),
        CYAN("cyan"),
        LIGHTBLUE("light_blue"),
        BLUE("blue"),
        PURPLE("purple"),
        MAGENTA("magenta"),
        PINK("pink");

        private final String color;

        TableCloth(String color) { this.color = color; }

        @Override
        public String asString() { return this.color; }

    }

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
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;

    public TableBlock(Settings settings) {
        super(settings.nonOpaque());
        setDefaultState(getStateManager().getDefaultState().with(PART, TablePart.TOP_LEFT).with(FACING, Direction.NORTH));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(PART, FACING);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos pos = ctx.getBlockPos();
        World world = ctx.getWorld();
        Direction facing = ctx.getHorizontalPlayerFacing();

        // Check if we have space for all four blocks using the facing direction
        if (canPlaceTable(world, pos, facing)) {
            return getDefaultState().with(PART, TablePart.BOTTOM_LEFT).with(FACING, facing);
        }

        return null;
    }

    private boolean canPlaceTable(World world, BlockPos pos, Direction facing) {
        // Check if the four parts of the table can be placed based on the facing direction
        switch (facing) {
            case NORTH:
                return world.getBlockState(pos).isAir()
                        && world.getBlockState(pos.east()).isAir()
                        && world.getBlockState(pos.north()).isAir()
                        && world.getBlockState(pos.north().east()).isAir();
            case EAST:
                return world.getBlockState(pos).isAir()
                        && world.getBlockState(pos.south()).isAir()
                        && world.getBlockState(pos.east()).isAir()
                        && world.getBlockState(pos.south().east()).isAir();
            case SOUTH:
                return world.getBlockState(pos).isAir()
                        && world.getBlockState(pos.west()).isAir()
                        && world.getBlockState(pos.south()).isAir()
                        && world.getBlockState(pos.south().west()).isAir();
            case WEST:
                return world.getBlockState(pos).isAir()
                        && world.getBlockState(pos.north()).isAir()
                        && world.getBlockState(pos.west()).isAir()
                        && world.getBlockState(pos.north().west()).isAir();
            default:
                return false;
        }
    }


    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (!world.isClient) {
            Direction facing = state.get(FACING); // Get the player's facing direction

            // Rotate the table parts based on the facing direction
            switch (facing) {
                case NORTH:
                    // Facing north: Place the table as normal
                    world.setBlockState(pos, state.with(PART, TablePart.BOTTOM_LEFT), Block.NOTIFY_ALL);
                    world.setBlockState(pos.east(), state.with(PART, TablePart.BOTTOM_RIGHT), Block.NOTIFY_ALL);
                    world.setBlockState(pos.north(), state.with(PART, TablePart.TOP_LEFT), Block.NOTIFY_ALL);
                    world.setBlockState(pos.north().east(), state.with(PART, TablePart.TOP_RIGHT), Block.NOTIFY_ALL);
                    break;
                case EAST:
                    // Facing east: Rotate the table 90 degrees
                    world.setBlockState(pos, state.with(PART, TablePart.BOTTOM_LEFT), Block.NOTIFY_ALL);
                    world.setBlockState(pos.south(), state.with(PART, TablePart.BOTTOM_RIGHT), Block.NOTIFY_ALL);
                    world.setBlockState(pos.east(), state.with(PART, TablePart.TOP_LEFT), Block.NOTIFY_ALL);
                    world.setBlockState(pos.south().east(), state.with(PART, TablePart.TOP_RIGHT), Block.NOTIFY_ALL);
                    break;
                case SOUTH:
                    // Facing south: Rotate the table 180 degrees
                    world.setBlockState(pos, state.with(PART, TablePart.BOTTOM_LEFT), Block.NOTIFY_ALL);
                    world.setBlockState(pos.west(), state.with(PART, TablePart.BOTTOM_RIGHT), Block.NOTIFY_ALL);
                    world.setBlockState(pos.south(), state.with(PART, TablePart.TOP_LEFT), Block.NOTIFY_ALL);
                    world.setBlockState(pos.south().west(), state.with(PART, TablePart.TOP_RIGHT), Block.NOTIFY_ALL);
                    break;
                case WEST:
                    // Facing west: Rotate the table 270 degrees
                    world.setBlockState(pos, state.with(PART, TablePart.BOTTOM_LEFT), Block.NOTIFY_ALL);
                    world.setBlockState(pos.north(), state.with(PART, TablePart.BOTTOM_RIGHT), Block.NOTIFY_ALL);
                    world.setBlockState(pos.west(), state.with(PART, TablePart.TOP_LEFT), Block.NOTIFY_ALL);
                    world.setBlockState(pos.north().west(), state.with(PART, TablePart.TOP_RIGHT), Block.NOTIFY_ALL);
                    break;
            }
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

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient && player.isCreative()) {
            TablePart part = state.get(PART);
            Direction facing = state.get(FACING);

            // Define the array of BlockPos for all four parts of the table, based on the facing direction and part
            BlockPos[] positionsToBreak = getPositionsForFacing(part, pos, facing);

            // Loop through all the positions to break the corresponding table parts
            for (BlockPos blockPos : positionsToBreak) {
                breakPart(world, blockPos);
            }
        }

        return super.onBreak(world, pos, state, player);
    }

    private BlockPos[] getPositionsForFacing(TablePart part, BlockPos pos, Direction facing) {
        switch (facing) {
            case NORTH:
                return getPositionsForFacingNorth(part, pos);
            case EAST:
                return getPositionsForFacingEast(part, pos);
            case SOUTH:
                return getPositionsForFacingSouth(part, pos);
            case WEST:
                return getPositionsForFacingWest(part, pos);
            default:
                return new BlockPos[] { pos }; // default case for any unknown directions
        }
    }

    private BlockPos[] getPositionsForFacingNorth(TablePart part, BlockPos pos) {
        switch (part) {
            case BOTTOM_LEFT:
                return new BlockPos[] { pos, pos.east(), pos.north(), pos.north().east() };
            case BOTTOM_RIGHT:
                return new BlockPos[] { pos, pos.west(), pos.north(), pos.north().west() };
            case TOP_LEFT:
                return new BlockPos[] { pos, pos.east(), pos.south(), pos.south().east() };
            case TOP_RIGHT:
                return new BlockPos[] { pos, pos.west(), pos.south(), pos.south().west() };
            default:
                return new BlockPos[] { pos };
        }
    }

    private BlockPos[] getPositionsForFacingEast(TablePart part, BlockPos pos) {
        switch (part) {
            case BOTTOM_LEFT:
                return new BlockPos[] { pos, pos.south(), pos.east(), pos.south().east() };
            case BOTTOM_RIGHT:
                return new BlockPos[] { pos, pos.north(), pos.east(), pos.north().east() };
            case TOP_LEFT:
                return new BlockPos[] { pos, pos.south(), pos.west(), pos.south().west() };
            case TOP_RIGHT:
                return new BlockPos[] { pos, pos.north(), pos.west(), pos.north().west() };
            default:
                return new BlockPos[] { pos };
        }
    }

    private BlockPos[] getPositionsForFacingSouth(TablePart part, BlockPos pos) {
        switch (part) {
            case BOTTOM_LEFT:
                return new BlockPos[] { pos, pos.west(), pos.south(), pos.south().west() };
            case BOTTOM_RIGHT:
                return new BlockPos[] { pos, pos.east(), pos.south(), pos.south().east() };
            case TOP_LEFT:
                return new BlockPos[] { pos, pos.west(), pos.north(), pos.north().west() };
            case TOP_RIGHT:
                return new BlockPos[] { pos, pos.east(), pos.north(), pos.north().east() };
            default:
                return new BlockPos[] { pos };
        }
    }

    private BlockPos[] getPositionsForFacingWest(TablePart part, BlockPos pos) {
        switch (part) {
            case BOTTOM_LEFT:
                return new BlockPos[] { pos, pos.north(), pos.west(), pos.north().west() };
            case BOTTOM_RIGHT:
                return new BlockPos[] { pos, pos.south(), pos.west(), pos.south().west() };
            case TOP_LEFT:
                return new BlockPos[] { pos, pos.north(), pos.east(), pos.north().east() };
            case TOP_RIGHT:
                return new BlockPos[] { pos, pos.south(), pos.east(), pos.south().east() };
            default:
                return new BlockPos[] { pos };
        }
    }

    private void breakPart(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        if (state.isOf(this)) {
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL | Block.SKIP_DROPS);
        }
    }


}