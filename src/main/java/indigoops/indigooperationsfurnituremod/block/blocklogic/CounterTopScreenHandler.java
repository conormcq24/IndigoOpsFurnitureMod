package indigoops.indigooperationsfurnituremod.block.blocklogic;
import indigoops.indigooperationsfurnituremod.screen.ModScreens;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;

public class CounterTopScreenHandler extends ScreenHandler {
    private final Inventory inventory;

    // Constructor matching the Factory interface requirements
    // This constructor is called on the client
    public CounterTopScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(27));
    }

    // This constructor is called on the server and gets an Inventory implementation
    public CounterTopScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(ModScreens.COUNTER_TOP_SCREEN_HANDLER, syncId);
        this.inventory = inventory;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(inventory, j + i * 9, 8 + j * 18, 18 + i * 18));
            }
        }

        // Add the player's inventory slots (for hotbar)
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }

        // Add the rest of the player's inventory slots
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, 9 + j + i * 9, 8 + j * 18, 84 + i * 18));
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

            // If the clicked slot is from the Counter Top's inventory (0-8), try to insert the item into the player's inventory
            if (index < 27) {
                if (!this.insertItem(stack, 27, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            }
            // If the clicked slot is from the player's inventory (9+), try to insert it into the Counter Top's inventory (0-8)
            else {
                if (!this.insertItem(stack, 0, 27, false)) {
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