package indigoops.indigooperationsfurnituremod.block.blocklogic;

import indigoops.indigooperationsfurnituremod.block.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class SinkBlockEntity extends BlockEntity implements Inventory {
    private final Inventory inventory = new SimpleInventory(9);  // 9-item inventory like a chest

    public SinkBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SINK_BLOCK_ENTITY, pos, state);
    }

    // Inventory methods
    @Override
    public int size() {
        return inventory.size();
    }

    @Override
    public boolean isEmpty() {
        return inventory.isEmpty();
    }

    @Override
    public ItemStack getStack(int slot) {
        return inventory.getStack(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return inventory.removeStack(slot, amount);
    }

    @Override
    public ItemStack removeStack(int slot) {
        return inventory.removeStack(slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        inventory.setStack(slot, stack);
    }

    @Override
    public void markDirty() {
        super.markDirty();
        inventory.markDirty();
    }
    @Override
    public void clear() {
        inventory.clear();
    }
    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;  // Allow any player to use the inventory
    }
}
