package indigoops.indigooperationsfurnituremod.util;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

public interface ImplementedInventory extends Inventory {
    DefaultedList<ItemStack> getItems();

    @Override
    default int size() {
        return getItems().size();
    }

    @Override
    default boolean isEmpty() {
        for (ItemStack stack : getItems()) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    default ItemStack getStack(int slot) {
        return getItems().get(slot);
    }

    @Override
    default ItemStack removeStack(int slot, int count) {
        ItemStack stack = getItems().get(slot);
        if (stack.isEmpty()) return ItemStack.EMPTY;
        return stack.split(count);
    }

    @Override
    default ItemStack removeStack(int slot) {
        return getItems().set(slot, ItemStack.EMPTY);
    }

    @Override
    default void setStack(int slot, ItemStack stack) {
        getItems().set(slot, stack);
        if (this instanceof BlockEntity be) {
            be.markDirty();
        }
    }

    @Override
    default void clear() {
        getItems().clear();
    }
}
