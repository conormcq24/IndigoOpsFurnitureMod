package indigoops.indigooperationsfurnituremod.block.blocklogic;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class CounterTopBlock extends BlockWithEntity implements BlockEntityProvider {
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    public static final EnumProperty<CounterTopBlock.CounterTopWood> WOOD_TYPE = EnumProperty.of("wood_type", CounterTopBlock.CounterTopWood.class);

    public enum CounterTopWood implements StringIdentifiable {
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

        CounterTopWood(String woodType) { this.woodType = woodType; }

        @Override
        public String asString() { return this.woodType; }
    }

    public CounterTopBlock(CounterTopBlock.CounterTopWood woodType) {
        super(AbstractBlock.Settings.create()
                .strength(1f)
                .sounds(BlockSoundGroup.WOOD)
                .nonOpaque()
        );
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(WOOD_TYPE, woodType));
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        // A simple implementation that doesn't need to handle serialization/deserialization
        // This works for many block entities that don't need special serialization logic
        return BlockWithEntity.createCodec(properties -> this);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CounterTopBlockEntity(pos, state);
    }
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING, WOOD_TYPE);
    }
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        CounterTopBlock.CounterTopWood woodType = this.getDefaultState().get(WOOD_TYPE);
        Direction dir = ctx.getHorizontalPlayerFacing().getOpposite();
        return this.getDefaultState().with(FACING, dir).with(WOOD_TYPE, woodType);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof CounterTopBlockEntity) {
                // Drop all items in the inventory
                Inventory inventory = (Inventory) blockEntity;
                for (int i = 0; i < inventory.size(); i++) {
                    ItemStack stack = inventory.getStack(i);
                    if (!stack.isEmpty()) {
                        // Create an ItemEntity for each non-empty slot
                        double x = pos.getX() + 0.5;
                        double y = pos.getY() + 0.5;
                        double z = pos.getZ() + 0.5;
                        ItemEntity itemEntity = new ItemEntity(world, x, y, z, stack);
                        world.spawnEntity(itemEntity);
                    }
                }
            }
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        // player is not sneaking
        if (!player.isSneaking()) {
            openChest(world, pos, player);
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    // Method to open the chest inventory
    private void openChest(World world, BlockPos pos, PlayerEntity player) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof CounterTopBlockEntity) {
            player.openHandledScreen((CounterTopBlockEntity) blockEntity);
        }
    }
}
