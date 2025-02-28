package indigoops.indigooperationsfurnituremod.block.blocklogic;

import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.event.GameEvent;
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
    public static final EnumProperty<TableCloth> CLOTH_TYPE = EnumProperty.of("cloth", TableCloth.class);
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    public static final BooleanProperty HAS_CLOTH = BooleanProperty.of("has_cloth");
    private final TableWood woodType;
    private final TableCloth clothType;
    public static final EnumProperty<TableWood> WOOD_TYPE = EnumProperty.of("wood_type", TableWood.class);

    public TableBlock(TableWood woodType, TableCloth clothType) {
        super(Settings.copy(Blocks.OAK_PLANKS).nonOpaque());
        this.woodType = woodType;
        this.clothType = clothType;
        setDefaultState(getStateManager().getDefaultState()
                .with(PART, TablePart.TOP_LEFT)
                .with(FACING, Direction.NORTH)
                .with(WOOD_TYPE, woodType)
                .with(CLOTH_TYPE, TableCloth.NONE)
                .with(HAS_CLOTH, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(PART, FACING, WOOD_TYPE, CLOTH_TYPE, HAS_CLOTH);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos pos = ctx.getBlockPos();
        World world = ctx.getWorld();
        Direction facing = ctx.getHorizontalPlayerFacing();

        // if statement for placement validation, do nothing if not valid
        if (canPlaceTable(world, pos, facing)) {
            return getDefaultState().with(PART, TablePart.BOTTOM_LEFT).with(FACING, facing).with(CLOTH_TYPE, clothType);
        }

        return null;
    }

    private boolean canPlaceTable(World world, BlockPos pos, Direction facing) {
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
            Direction facing = state.get(FACING);

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
                return new BlockPos[] { pos };
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

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient) {
            TablePart part = state.get(PART);
            Direction facing = state.get(FACING);
            boolean hasCloth = state.get(HAS_CLOTH);
            TableCloth clothType = state.get(CLOTH_TYPE);

            // Get all positions that make up the table
            BlockPos[] tablePositions = getPositionsForFacing(part, pos, facing);

            // In survival mode, handle special case for dropping items
            if (!player.isCreative()) {
                // Drop the carpet if this block has cloth
                if (hasCloth) {
                    Item carpetToDrop = getCarpetFromClothColor(clothType);
                    if (carpetToDrop != null) {
                        ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(),
                                new ItemStack(carpetToDrop));
                    }
                }
            }

            // Break all other parts of the table
            for (BlockPos tablePos : tablePositions) {
                if (!tablePos.equals(pos)) {  // Skip the current position
                    BlockState tableState = world.getBlockState(tablePos);
                    if (tableState.isOf(this)) {
                        // In creative mode, or for parts other than BOTTOM_LEFT in survival, don't drop items
                        world.setBlockState(tablePos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL | Block.SKIP_DROPS);
                    }
                }
            }
        }

        // Return the result of the super method to maintain compatibility
        return super.onBreak(world, pos, state, player);
    }

    private void breakPart(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        if (state.isOf(this)) {
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL | Block.SKIP_DROPS);
        }
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        Hand hand = player.getActiveHand();
        ItemStack itemInHand = player.getStackInHand(hand);
        boolean hasCloth = state.get(HAS_CLOTH);
        Direction facing = state.get(FACING);

        if (world.isClient) {
            return ActionResult.SUCCESS;
        }

        // Check if the item is a carpet
        if (itemInHand.getItem() == Items.WHITE_CARPET || itemInHand.getItem() == Items.LIGHT_GRAY_CARPET ||
                itemInHand.getItem() == Items.GRAY_CARPET || itemInHand.getItem() == Items.BLACK_CARPET ||
                itemInHand.getItem() == Items.BROWN_CARPET || itemInHand.getItem() == Items.RED_CARPET ||
                itemInHand.getItem() == Items.ORANGE_CARPET || itemInHand.getItem() == Items.YELLOW_CARPET ||
                itemInHand.getItem() == Items.LIME_CARPET || itemInHand.getItem() == Items.GREEN_CARPET ||
                itemInHand.getItem() == Items.CYAN_CARPET || itemInHand.getItem() == Items.LIGHT_BLUE_CARPET ||
                itemInHand.getItem() == Items.BLUE_CARPET || itemInHand.getItem() == Items.PURPLE_CARPET ||
                itemInHand.getItem() == Items.MAGENTA_CARPET || itemInHand.getItem() == Items.PINK_CARPET) {

            if (!hasCloth) {
                TableCloth clothColor = getClothColorFromCarpet(itemInHand.getItem());


                // Now, update all parts of the table with the new cloth color
                updateTableCloth(world, pos, state, clothColor, facing, true);
            } else {
                TableCloth currentColor = state.get(CLOTH_TYPE);
                TableCloth newColor = getClothColorFromCarpet(itemInHand.getItem());

                if (currentColor != newColor){
                    updateTableCloth(world, pos, state, newColor, facing, true);
                    Item carpetToDrop = getCarpetFromClothColor(currentColor);
                    if (carpetToDrop != null) {
                        ItemScatterer.spawn(world, pos.getX(), pos.getY() + 1, pos.getZ(), new ItemStack(carpetToDrop));
                    }
                    if (!player.isCreative()) {
                        itemInHand.decrement(1);
                    }
                }
            }
        } else if (itemInHand.getItem() == Items.SHEARS && hasCloth) {
            TableCloth currentColor = state.get(CLOTH_TYPE);
            Item carpetToDrop = getCarpetFromClothColor(currentColor);
            updateTableCloth(world, pos, state, TableCloth.NONE, facing, false);
            if (carpetToDrop != null) {
                ItemScatterer.spawn(world, pos.getX(), pos.getY() + 1, pos.getZ(), new ItemStack(carpetToDrop));
            }
            if (!player.isCreative()) {
                itemInHand.decrement(1);
            }
        }

        return ActionResult.SUCCESS;
    }

    private void updateTableCloth(World world, BlockPos pos, BlockState state, TableCloth clothColor, Direction facing, Boolean hasCarpet) {
        // Get all the positions for the table parts based on facing direction
        BlockPos[] positions = getPositionsForFacing(state.get(PART), pos, facing);

        for (BlockPos blockPos : positions) {
            BlockState blockState = world.getBlockState(blockPos);
            if (blockState.getBlock() instanceof TableBlock && hasCarpet) {
                BlockState updatedState = blockState
                        .with(CLOTH_TYPE, clothColor)
                        .with(HAS_CLOTH, true);
                world.setBlockState(blockPos, updatedState, Block.NOTIFY_ALL);
                world.updateNeighbors(blockPos, this);
            } else if (blockState.getBlock() instanceof TableBlock && !hasCarpet){
                BlockState updatedState = blockState
                        .with(CLOTH_TYPE, clothColor)
                        .with(HAS_CLOTH, false);
                world.setBlockState(blockPos, updatedState, Block.NOTIFY_ALL);
                world.updateNeighbors(blockPos, this);
            }
        }
    }

    private TableCloth getClothColorFromCarpet(Item carpetItem) {
        if (carpetItem == Items.WHITE_CARPET) return TableCloth.WHITE;
        if (carpetItem == Items.LIGHT_GRAY_CARPET) return TableCloth.LIGHTGRAY;
        if (carpetItem == Items.GRAY_CARPET) return TableCloth.GRAY;
        if (carpetItem == Items.BLACK_CARPET) return TableCloth.BLACK;
        if (carpetItem == Items.BROWN_CARPET) return TableCloth.BROWN;
        if (carpetItem == Items.RED_CARPET) return TableCloth.RED;
        if (carpetItem == Items.ORANGE_CARPET) return TableCloth.ORANGE;
        if (carpetItem == Items.YELLOW_CARPET) return TableCloth.YELLOW;
        if (carpetItem == Items.LIME_CARPET) return TableCloth.LIME;
        if (carpetItem == Items.GREEN_CARPET) return TableCloth.GREEN;
        if (carpetItem == Items.CYAN_CARPET) return TableCloth.CYAN;
        if (carpetItem == Items.LIGHT_BLUE_CARPET) return TableCloth.LIGHTBLUE;
        if (carpetItem == Items.BLUE_CARPET) return TableCloth.BLUE;
        if (carpetItem == Items.PURPLE_CARPET) return TableCloth.PURPLE;
        if (carpetItem == Items.MAGENTA_CARPET) return TableCloth.MAGENTA;
        if (carpetItem == Items.PINK_CARPET) return TableCloth.PINK;
        return TableCloth.NONE;
    }

    private Item getCarpetFromClothColor(TableCloth clothColor) {
        switch (clothColor) {
            case WHITE: return Items.WHITE_CARPET;
            case LIGHTGRAY: return Items.LIGHT_GRAY_CARPET;
            case GRAY: return Items.GRAY_CARPET;
            case BLACK: return Items.BLACK_CARPET;
            case BROWN: return Items.BROWN_CARPET;
            case RED: return Items.RED_CARPET;
            case ORANGE: return Items.ORANGE_CARPET;
            case YELLOW: return Items.YELLOW_CARPET;
            case LIME: return Items.LIME_CARPET;
            case GREEN: return Items.GREEN_CARPET;
            case CYAN: return Items.CYAN_CARPET;
            case LIGHTBLUE: return Items.LIGHT_BLUE_CARPET;
            case BLUE: return Items.BLUE_CARPET;
            case PURPLE: return Items.PURPLE_CARPET;
            case MAGENTA: return Items.MAGENTA_CARPET;
            case PINK: return Items.PINK_CARPET;
            case NONE:
            default: return null;
        }
    }

    protected static final VoxelShape TABLE_TOP = Block.createCuboidShape(0, 13, 0, 16, 16, 16);
    protected static final VoxelShape BOTTOM_LEFT_LEG = Block.createCuboidShape(1, 0, 12, 4, 13, 15);
    protected static final VoxelShape BOTTOM_RIGHT_LEG = Block.createCuboidShape(12, 0, 12, 15, 13, 15);
    protected static final VoxelShape TOP_LEFT_LEG = Block.createCuboidShape(1, 0, 1, 4, 13, 4);
    protected static final VoxelShape TOP_RIGHT_LEG = Block.createCuboidShape(12, 0, 1, 15, 13, 4);

    // North-facing shapes
    protected static final VoxelShape NORTH_BOTTOM_LEFT_SHAPE = VoxelShapes.union(TABLE_TOP, BOTTOM_LEFT_LEG);
    protected static final VoxelShape NORTH_BOTTOM_RIGHT_SHAPE = VoxelShapes.union(TABLE_TOP, BOTTOM_RIGHT_LEG);
    protected static final VoxelShape NORTH_TOP_LEFT_SHAPE = VoxelShapes.union(TABLE_TOP, TOP_LEFT_LEG);
    protected static final VoxelShape NORTH_TOP_RIGHT_SHAPE = VoxelShapes.union(TABLE_TOP, TOP_RIGHT_LEG);

    // East-facing shapes
    protected static final VoxelShape EAST_BOTTOM_LEFT_SHAPE = VoxelShapes.union(TABLE_TOP, BOTTOM_RIGHT_LEG);
    protected static final VoxelShape EAST_BOTTOM_RIGHT_SHAPE = VoxelShapes.union(TABLE_TOP, TOP_RIGHT_LEG);
    protected static final VoxelShape EAST_TOP_LEFT_SHAPE = VoxelShapes.union(TABLE_TOP, BOTTOM_LEFT_LEG);
    protected static final VoxelShape EAST_TOP_RIGHT_SHAPE = VoxelShapes.union(TABLE_TOP, TOP_LEFT_LEG);

    // South-facing shapes
    protected static final VoxelShape SOUTH_BOTTOM_LEFT_SHAPE = VoxelShapes.union(TABLE_TOP, TOP_RIGHT_LEG);
    protected static final VoxelShape SOUTH_BOTTOM_RIGHT_SHAPE = VoxelShapes.union(TABLE_TOP, TOP_LEFT_LEG);
    protected static final VoxelShape SOUTH_TOP_LEFT_SHAPE = VoxelShapes.union(TABLE_TOP, BOTTOM_RIGHT_LEG);
    protected static final VoxelShape SOUTH_TOP_RIGHT_SHAPE = VoxelShapes.union(TABLE_TOP, BOTTOM_LEFT_LEG);

    // West-facing shapes
    protected static final VoxelShape WEST_BOTTOM_LEFT_SHAPE = VoxelShapes.union(TABLE_TOP, TOP_LEFT_LEG);
    protected static final VoxelShape WEST_BOTTOM_RIGHT_SHAPE = VoxelShapes.union(TABLE_TOP, BOTTOM_LEFT_LEG);
    protected static final VoxelShape WEST_TOP_LEFT_SHAPE = VoxelShapes.union(TABLE_TOP, TOP_RIGHT_LEG);
    protected static final VoxelShape WEST_TOP_RIGHT_SHAPE = VoxelShapes.union(TABLE_TOP, BOTTOM_RIGHT_LEG);

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        TablePart part = state.get(PART);
        Direction direction = state.get(FACING);

        return switch (direction) {
            case NORTH -> switch (part) {
                case BOTTOM_LEFT -> NORTH_BOTTOM_LEFT_SHAPE;
                case BOTTOM_RIGHT -> NORTH_BOTTOM_RIGHT_SHAPE;
                case TOP_LEFT -> NORTH_TOP_LEFT_SHAPE;
                case TOP_RIGHT -> NORTH_TOP_RIGHT_SHAPE;
            };
            case EAST -> switch (part) {
                case BOTTOM_LEFT -> EAST_BOTTOM_LEFT_SHAPE;
                case BOTTOM_RIGHT -> EAST_BOTTOM_RIGHT_SHAPE;
                case TOP_LEFT -> EAST_TOP_LEFT_SHAPE;
                case TOP_RIGHT -> EAST_TOP_RIGHT_SHAPE;
            };
            case SOUTH -> switch (part) {
                case BOTTOM_LEFT -> SOUTH_BOTTOM_LEFT_SHAPE;
                case BOTTOM_RIGHT -> SOUTH_BOTTOM_RIGHT_SHAPE;
                case TOP_LEFT -> SOUTH_TOP_LEFT_SHAPE;
                case TOP_RIGHT -> SOUTH_TOP_RIGHT_SHAPE;
            };
            case WEST -> switch (part) {
                case BOTTOM_LEFT -> WEST_BOTTOM_LEFT_SHAPE;
                case BOTTOM_RIGHT -> WEST_BOTTOM_RIGHT_SHAPE;
                case TOP_LEFT -> WEST_TOP_LEFT_SHAPE;
                case TOP_RIGHT -> WEST_TOP_RIGHT_SHAPE;
            };
            default -> NORTH_BOTTOM_LEFT_SHAPE;
        };
    }

    // Also override these methods to ensure consistent collision behavior
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return getOutlineShape(state, world, pos, context);
    }

    @Override
    public VoxelShape getCullingShape(BlockState state, BlockView world, BlockPos pos) {
        return getOutlineShape(state, world, pos, ShapeContext.absent());
    }
}