package indigoops.indigooperationsfurnituremod.block.blocklogic;

import com.mojang.serialization.Decoder;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.MapCodec;
import indigoops.indigooperationsfurnituremod.entity.ModEntities;
import indigoops.indigooperationsfurnituremod.entity.custom.ChairEntity;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public class ChairBlock extends HorizontalFacingBlock {
    public static final MapCodec<ChairBlock> CODEC = MapCodec.of(Encoder.empty(), Decoder.unit(() -> new ChairBlock(ChairWood.OAK)));
    public static final EnumProperty<ChairBlock.ChairPart> PART = EnumProperty.of("part", ChairBlock.ChairPart.class);
    public static final EnumProperty<ChairBlock.ChairWood> WOOD_TYPE = EnumProperty.of("wood_type", ChairBlock.ChairWood.class);
    public static final EnumProperty<ChairBlock.ChairCoushin> COUSHIN_TYPE = EnumProperty.of("coushin_type", ChairBlock.ChairCoushin.class);
    private final ChairBlock.ChairWood woodType;
    private final ChairBlock.ChairCoushin coushinType;

    public enum ChairCoushin implements StringIdentifiable {
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

        ChairCoushin(String color) { this.color = color; }

        @Override
        public String asString() { return this.color; }

    }

    // Enum for all chair wood types
    public enum ChairWood implements StringIdentifiable {
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

        ChairWood(String woodType) { this.woodType = woodType; }

        @Override
        public String asString() { return this.woodType; }

    }

    // Enum for chair parts
    public enum ChairPart implements StringIdentifiable {
        CHAIR("chair"),
        CHAIR_TOP("chair_top");

        private final String name;

        ChairPart(String name) {
            this.name = name;
        }

        @Override
        public String asString() {
            return this.name;
        }
    }

    public ChairBlock(ChairWood woodType) {
        super(Settings.copy(Blocks.OAK_PLANKS).nonOpaque());
        this.woodType = woodType;
        this.coushinType = ChairCoushin.WHITE;

    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit){
        if(!world.isClient()){
            // Check if player is holding a carpet or shears
            ItemStack heldItem = player.getMainHandStack();
            boolean isHoldingCarpet = !heldItem.isEmpty() && heldItem.getItem() instanceof BlockItem &&
                    ((BlockItem)heldItem.getItem()).getBlock() instanceof CarpetBlock;
            boolean isHoldingShears = !heldItem.isEmpty() && heldItem.getItem() instanceof ShearsItem;

            // Only allow sitting if player is empty-handed or not holding carpet or shears
            if(!isHoldingCarpet && !isHoldingShears) {
                Entity entity = null;
                List<ChairEntity> entities = world.getEntitiesByType(ModEntities.CHAIR, new Box(pos), chair -> true);
                if(entities.isEmpty()) {
                    entity = ModEntities.CHAIR.spawn((ServerWorld) world, pos, SpawnReason.TRIGGERED);
                } else {
                    entity = entities.get(0);
                }

                player.startRiding(entity);
                return ActionResult.SUCCESS;
            }

            // If holding a carpet or shears, return PASS to allow other interactions (like placing the carpet or using shears)
            handleCoushin(isHoldingCarpet, isHoldingShears, state, world, pos, player, hit );
            return ActionResult.PASS;
        }

        return ActionResult.SUCCESS;
    }

    protected void handleCoushin(boolean carpet, boolean shears, BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit){
        ChairPart hitPart = state.get(PART); //hitPart == ChairPart.CHAIR or CHAIR_TOP
        if (carpet){
            replaceCoushin(hitPart, state, world, pos, player, hit);
        } else {
            removeCoushin(hitPart, state, world, pos, player, hit);
        }
    }
    protected void replaceCoushin(ChairPart hitPart, BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        ItemStack heldItem = player.getMainHandStack();
        String carpetId = heldItem.getItem().toString().toLowerCase();

        // get the new and old coushin type
        ChairCoushin newCoushinType = getCoushinTypeFromCarpetId(carpetId);
        ChairCoushin currentCoushin = state.get(COUSHIN_TYPE);

        if (newCoushinType != null){
            if (currentCoushin != ChairCoushin.WHITE) {
                // Drop a carpet of the old color
                ItemStack carpetToDrop = getCarpetItemForCoushin(currentCoushin);
                if (!carpetToDrop.isEmpty()) {
                    // Drop the item at the position of the chair
                    Block.dropStack(world, pos, carpetToDrop);
                }
            }
            if (hitPart == ChairPart.CHAIR){
                world.setBlockState(pos, state.with(COUSHIN_TYPE, newCoushinType), Block.NOTIFY_ALL);
                BlockPos topPos = pos.up();
                BlockState topState = world.getBlockState(topPos);
                world.setBlockState(topPos, topState.with(COUSHIN_TYPE, newCoushinType), Block.NOTIFY_ALL);
            } else {
                world.setBlockState(pos, state.with(COUSHIN_TYPE, newCoushinType), Block.NOTIFY_ALL);
                BlockPos bottomPos = pos.down();
                BlockState bottomState = world.getBlockState(bottomPos);
                world.setBlockState(bottomPos, bottomState.with(COUSHIN_TYPE, newCoushinType), Block.NOTIFY_ALL);
            }
            if (!player.getAbilities().creativeMode) {
                heldItem.decrement(1);
            }
        }
    }
    protected void removeCoushin(ChairPart hitPart, BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit){
        ChairCoushin currentCoushin = state.get(COUSHIN_TYPE);
        if (currentCoushin != null) {
            if(currentCoushin != ChairCoushin.WHITE) {
                ItemStack carpetToDrop = getCarpetItemForCoushin(currentCoushin);
                if (!carpetToDrop.isEmpty()) {
                    // Drop the item at the position of the chair
                    Block.dropStack(world, pos, carpetToDrop);
                }
            }
            if (hitPart == ChairPart.CHAIR){
                world.setBlockState(pos, state.with(COUSHIN_TYPE, ChairCoushin.WHITE), Block.NOTIFY_ALL);
                BlockPos topPos = pos.up();
                BlockState topState = world.getBlockState(topPos);
                world.setBlockState(topPos, topState.with(COUSHIN_TYPE, ChairCoushin.WHITE), Block.NOTIFY_ALL);
            } else {
                world.setBlockState(pos, state.with(COUSHIN_TYPE, ChairCoushin.WHITE), Block.NOTIFY_ALL);
                BlockPos bottomPos = pos.down();
                BlockState bottomState = world.getBlockState(bottomPos);
                world.setBlockState(bottomPos, bottomState.with(COUSHIN_TYPE, ChairCoushin.WHITE), Block.NOTIFY_ALL);
            }
        }
    }
    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {

        Direction facing = state.get(FACING);
        VoxelShape chairBack = getChairBackForFacing(facing, false);

        if (state.get(PART) == ChairPart.CHAIR) {
            // For the chair base part, combine seat and legs
            return VoxelShapes.union(
                    ChairSeat,
                    ChairLegOne,
                    ChairLegTwo,
                    ChairLegThree,
                    ChairLegFour,
                    chairBack
            );
        }
        if ((state.get(PART) == ChairPart.CHAIR_TOP))
        {
            return getChairBackForFacing(facing, true);
        }
        else {
            return VoxelShapes.fullCube();
        }
    }

    @Override
    protected MapCodec<? extends HorizontalFacingBlock> getCodec() { return CODEC; }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos pos = ctx.getBlockPos();
        World world = ctx.getWorld();

        if (world.getBlockState(pos.up()).isAir()) {
            return this.getDefaultState()
                    .with(FACING, ctx.getHorizontalPlayerFacing().getOpposite())
                    .with(PART, ChairPart.CHAIR)
                    .with(WOOD_TYPE, this.woodType)
                    .with(COUSHIN_TYPE, this.coushinType);
        }

        return null;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (!world.isClient && state.get(PART) == ChairPart.CHAIR) {
            // Place the top part above the base
            BlockPos topPos = pos.up();
            if (world.getBlockState(topPos).isAir()) {
                world.setBlockState(topPos, state
                                .with(PART, ChairPart.CHAIR_TOP)
                                .with(WOOD_TYPE, this.woodType)
                                .with(FACING, state.get(FACING)),
                        Block.NOTIFY_ALL);
            }
        }
        super.onPlaced(world, pos, state, placer, itemStack);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            if (!world.isClient) {
                // Only drop cushion if being broken normally (not as a result of the other part being broken)
                boolean isBeingRemovedByOtherPart = false;

                if (state.get(PART) == ChairPart.CHAIR) {
                    // If base is being removed by code breaking the top part
                    BlockPos topPos = pos.up();
                    BlockState topState = world.getBlockState(topPos);
                    if (!topState.isOf(this) || topState.get(PART) != ChairPart.CHAIR_TOP) {
                        // Top part is already gone, so this is being broken by code
                        isBeingRemovedByOtherPart = true;
                    }
                } else if (state.get(PART) == ChairPart.CHAIR_TOP) {
                    // If top is being removed by code breaking the bottom part
                    BlockPos bottomPos = pos.down();
                    BlockState bottomState = world.getBlockState(bottomPos);
                    if (!bottomState.isOf(this) || bottomState.get(PART) != ChairPart.CHAIR) {
                        // Bottom part is already gone, so this is being broken by code
                        isBeingRemovedByOtherPart = true;
                    }
                }

                // Only drop the carpet if this part is being broken directly by a player
                // and not as a result of the other part being broken
                if (!isBeingRemovedByOtherPart) {
                    ChairCoushin currentCoushin = state.get(COUSHIN_TYPE);
                    if (currentCoushin != ChairCoushin.WHITE) {
                        ItemStack carpetToDrop = getCarpetItemForCoushin(currentCoushin);
                        if (!carpetToDrop.isEmpty()) {
                            Block.dropStack(world, pos, carpetToDrop);
                        }
                    }
                }
            }

            if (state.get(PART) == ChairPart.CHAIR) {
                // If the base is broken, remove the top part
                BlockPos topPos = pos.up();
                BlockState topState = world.getBlockState(topPos);
                if (topState.isOf(this) && topState.get(PART) == ChairPart.CHAIR_TOP) {
                    world.removeBlock(topPos, false);
                }
            } else if (state.get(PART) == ChairPart.CHAIR_TOP) {
                // If the top is broken, removethe bottom part
                BlockPos bottomPos = pos.down();
                BlockState bottomState = world.getBlockState(bottomPos);
                if (bottomState.isOf(this) && bottomState.get(PART) == ChairPart.CHAIR) {
                    world.removeBlock(bottomPos, false);
                }
            }
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState,
                                                WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(PART) == ChairPart.CHAIR_TOP && direction == Direction.DOWN) {
            // If the block below the top part is not a chair base, remove the top part
            if (!neighborState.isOf(this) || neighborState.get(PART) != ChairPart.CHAIR) {
                return Blocks.AIR.getDefaultState();
            }
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) { builder.add(FACING, PART, WOOD_TYPE, COUSHIN_TYPE); }

    protected static final VoxelShape ChairSeat = Block.createCuboidShape(2, 9, 2, 14, 10, 14);
    protected static final VoxelShape ChairLegOne = Block.createCuboidShape(2, 0, 2, 4, 8, 4);
    protected static final VoxelShape ChairLegTwo = Block.createCuboidShape(2, 0, 12, 4, 8, 14);
    protected static final VoxelShape ChairLegThree = Block.createCuboidShape(12, 0, 2, 14, 8, 4);
    protected static final VoxelShape ChairLegFour = Block.createCuboidShape(12, 0, 12, 14, 8, 14);
    protected static final VoxelShape ChairBackBottom = Block.createCuboidShape(2, 10, 12, 14, 16, 14);
    protected static final VoxelShape ChairBackTop = Block.createCuboidShape(2,0,12,14,5,14);

    // ChairBack Bottom and Top need to rotate the rest of the parts dont, this function handles rotation
    private VoxelShape getChairBackForFacing(Direction facing, boolean isTop) {
        if (isTop) {
            // Return the top part shape based on facing
            return switch (facing) {
                case NORTH -> ChairBackTop;
                case WEST -> Block.createCuboidShape(12, 0, 2, 14, 5, 14); // Rotated 90°
                case SOUTH -> Block.createCuboidShape(2, 0, 2, 14, 5, 4);  // Rotated 180°
                case EAST -> Block.createCuboidShape(2, 0, 2, 4, 5, 14);   // Rotated 270°
                default -> ChairBackTop;
            };
        } else {
            // Return the bottom part shape based on facing
            return switch (facing) {
                case NORTH -> ChairBackBottom;
                case WEST -> Block.createCuboidShape(12, 10, 2, 14, 16, 14); // Rotated 90°
                case SOUTH -> Block.createCuboidShape(2, 10, 2, 14, 16, 4);  // Rotated 180°
                case EAST -> Block.createCuboidShape(2, 10, 2, 4, 16, 14);   // Rotated 270°
                default -> ChairBackBottom;
            };
        }
    }

    private ChairCoushin getCoushinTypeFromCarpetId(String carpetId) {
        // Map carpet IDs to cushion types
        if (carpetId.contains("white")) return ChairCoushin.WHITE;
        if (carpetId.contains("light_gray") || carpetId.contains("silver")) return ChairCoushin.LIGHTGRAY;
        if (carpetId.contains("gray")) return ChairCoushin.GRAY;
        if (carpetId.contains("black")) return ChairCoushin.BLACK;
        if (carpetId.contains("brown")) return ChairCoushin.BROWN;
        if (carpetId.contains("red")) return ChairCoushin.RED;
        if (carpetId.contains("orange")) return ChairCoushin.ORANGE;
        if (carpetId.contains("yellow")) return ChairCoushin.YELLOW;
        if (carpetId.contains("lime")) return ChairCoushin.LIME;
        if (carpetId.contains("green")) return ChairCoushin.GREEN;
        if (carpetId.contains("cyan")) return ChairCoushin.CYAN;
        if (carpetId.contains("light_blue")) return ChairCoushin.LIGHTBLUE;
        if (carpetId.contains("blue")) return ChairCoushin.BLUE;
        if (carpetId.contains("purple")) return ChairCoushin.PURPLE;
        if (carpetId.contains("magenta")) return ChairCoushin.MAGENTA;
        if (carpetId.contains("pink")) return ChairCoushin.PINK;

        // Default to white if no match is found
        return ChairCoushin.WHITE;
    }

    private ItemStack getCarpetItemForCoushin(ChairCoushin coushin) {
        // Map cushion types to carpet blocks
        Block carpetBlock = switch (coushin) {
            case WHITE -> Blocks.WHITE_CARPET;
            case LIGHTGRAY -> Blocks.LIGHT_GRAY_CARPET;
            case GRAY -> Blocks.GRAY_CARPET;
            case BLACK -> Blocks.BLACK_CARPET;
            case BROWN -> Blocks.BROWN_CARPET;
            case RED -> Blocks.RED_CARPET;
            case ORANGE -> Blocks.ORANGE_CARPET;
            case YELLOW -> Blocks.YELLOW_CARPET;
            case LIME -> Blocks.LIME_CARPET;
            case GREEN -> Blocks.GREEN_CARPET;
            case CYAN -> Blocks.CYAN_CARPET;
            case LIGHTBLUE -> Blocks.LIGHT_BLUE_CARPET;
            case BLUE -> Blocks.BLUE_CARPET;
            case PURPLE -> Blocks.PURPLE_CARPET;
            case MAGENTA -> Blocks.MAGENTA_CARPET;
            case PINK -> Blocks.PINK_CARPET;
            default -> null;
        };

        if (carpetBlock != null) {
            return new ItemStack(carpetBlock);
        }

        return ItemStack.EMPTY;
    }
}
