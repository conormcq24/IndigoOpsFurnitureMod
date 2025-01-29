package indigoops.indigooperationsfurnituremod.block;

import indigoops.indigooperationsfurnituremod.IndigoOperationsFurnitureMod;
import indigoops.indigooperationsfurnituremod.block.blocklogic.OakSinkBlock;
import indigoops.indigooperationsfurnituremod.block.blocklogic.SinkBlock;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlocks {
    // Use concrete classes
    public static final Block OAK_SINK = registerBlock("oak_sink", new OakSinkBlock());
    // Add more concrete subclasses as needed

    private static Block registerBlock(String name, Block block){
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(IndigoOperationsFurnitureMod.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block){
        Registry.register(Registries.ITEM, Identifier.of(IndigoOperationsFurnitureMod.MOD_ID, name),
                new BlockItem(block, new Item.Settings()));
    }

    public static void registerModBlocks(){
        IndigoOperationsFurnitureMod.LOGGER.info("Registering Mod Blocks for " + IndigoOperationsFurnitureMod.MOD_ID);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries -> {
            entries.add(ModBlocks.OAK_SINK);
            // Add other blocks as needed
        });
    }
}
