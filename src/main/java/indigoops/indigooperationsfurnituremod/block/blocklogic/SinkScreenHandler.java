package indigoops.indigooperationsfurnituremod.block.blocklogic;

import indigoops.indigooperationsfurnituremod.block.blocklogic.SinkBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;

public class SinkScreenHandler extends ScreenHandler {
    private final Inventory inventory;

    public SinkScreenHandler(int syncId, PlayerEntity player, Inventory inventory) {
        super(null, syncId); // We pass null for the 'type' since it's not required here.
        this.inventory = inventory;

        // Add the Sink's inventory slots (9 slots for a 3x3 grid)
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(inventory, i, 62 + i * 18, 17));
        }

        // Add the player's inventory slots (for hotbar and the rest of the inventory)
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(player.getInventory(), i, 8 + i * 18, 142));
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(player.getInventory(), 9 + j + i * 9, 8 + j * 18, 84 + i * 18));
            }
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        // If the slot has a stack (item)
        if (slot.hasStack()) {
            ItemStack stack = slot.getStack();
            itemStack = stack.copy(); // Make a copy of the stack

            // If the clicked slot is from the Sink's inventory (0-8), try to insert the item into the player's inventory
            if (index < 9) {
                if (!this.insertItem(stack, 9, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            }
            // If the clicked slot is from the player's inventory (9+), try to insert it into the Sink's inventory (0-8)
            else {
                if (!this.insertItem(stack, 0, 9, false)) {
                    return ItemStack.EMPTY;
                }
            }

            // If the item stack is empty, remove the item from the slot
            if (stack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return itemStack;
    }
}