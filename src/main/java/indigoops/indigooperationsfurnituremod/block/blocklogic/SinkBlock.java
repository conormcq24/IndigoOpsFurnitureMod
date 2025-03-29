package indigoops.indigooperationsfurnituremod.block.blocklogic;

import com.mojang.serialization.MapCodec;
import indigoops.indigooperationsfurnituremod.util.ImplementedInventory;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.DoubleInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

public class SinkBlock extends BlockWithEntity implements BlockEntityProvider {
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    public static final BooleanProperty FAUCET = BooleanProperty.of("faucet_on");
    public static final EnumProperty<SinkBlock.SinkWood> WOOD_TYPE = EnumProperty.of("wood_type", SinkBlock.SinkWood.class);

    public enum SinkWood implements StringIdentifiable {
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

        SinkWood(String woodType) { this.woodType = woodType; }

        @Override
        public String asString() { return this.woodType; }
    }

    public SinkBlock(SinkBlock.SinkWood woodType) {
        super(AbstractBlock.Settings.create()
                .strength(1f)
                .sounds(BlockSoundGroup.WOOD)
                .nonOpaque()
        );
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(FAUCET, false)
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
        return new SinkBlockEntity(pos, state);
    }
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING, FAUCET, WOOD_TYPE);
    }
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction dir = ctx.getHorizontalPlayerFacing().getOpposite();

        // Get the wood type from the default state (which was set in the constructor)
        SinkWood woodType = this.getDefaultState().get(WOOD_TYPE);

        return this.getDefaultState()
                .with(FACING, dir)
                .with(FAUCET, false)
                .with(WOOD_TYPE, woodType);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof SinkBlockEntity) {
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
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        ItemStack itemInHand = player.getStackInHand(Hand.MAIN_HAND);
        boolean isFaucetOn = state.get(FAUCET);

        // Right click - empty bucket - faucet on
        if (itemInHand.getItem() == Items.BUCKET && isFaucetOn) {
            int bucketCount = itemInHand.getCount();
            ItemStack fullBucket = new ItemStack(Items.WATER_BUCKET);
            if (bucketCount == 1){
                world.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0f, 1.0f);
                player.setStackInHand(Hand.MAIN_HAND, fullBucket);
                return ActionResult.SUCCESS;
            } else {
                itemInHand.decrement(1);
                world.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0f, 1.0f);
                if (!player.getInventory().insertStack(fullBucket)) {
                    // Try to find an empty spot in the inventory for the full bucket
                    if (!player.getInventory().insertStack(fullBucket)) {
                        // If no space, drop the filled bucket on the ground
                        double x = player.getX();
                        double y = player.getY();
                        double z = player.getZ();

                        // Drop the full bucket at the player's (slightly offset)
                        ItemEntity itemEntity = new ItemEntity(world, x, y + 0.5, z, fullBucket);
                        world.spawnEntity(itemEntity);
                    }
                }
                return ActionResult.SUCCESS;
            }

        }
        // Right click - full bucket of water or lava
        if (itemInHand.getItem() == Items.WATER_BUCKET || itemInHand.getItem() == Items.LAVA_BUCKET) {
            itemInHand.decrement(1);
            ItemStack emptyBucket = new ItemStack(Items.BUCKET);
            player.setStackInHand(Hand.MAIN_HAND, emptyBucket);
            world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0f, 1.0f);
            return ActionResult.SUCCESS;
        }
        // Empty hand and sneaking
        if (player.isSneaking()) {
            if (!world.isClient) {
                boolean newFaucetState = !state.get(FAUCET);
                world.setBlockState(pos, state.with(FAUCET, newFaucetState), 3);
                world.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0f, 1.0f);
                return ActionResult.SUCCESS;
            }
        }
        // Empty hand and not sneaking
        if (itemInHand.isEmpty() && !player.isSneaking()) {
            openChest(world, pos, player);
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    // Method to open the chest inventory
    private void openChest(World world, BlockPos pos, PlayerEntity player) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof SinkBlockEntity) {
            player.openHandledScreen((SinkBlockEntity) blockEntity);
        }
    }

}