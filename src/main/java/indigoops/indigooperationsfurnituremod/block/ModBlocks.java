package indigoops.indigooperationsfurnituremod.block;

import indigoops.indigooperationsfurnituremod.IndigoOperationsFurnitureMod;
import indigoops.indigooperationsfurnituremod.block.blocklogic.SinkBlock;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ModBlocks {
    public static final Block ACACIA_SINK = registerBlock("acacia_sink", new SinkBlock());
    public static final Block BIRCH_SINK = registerBlock("birch_sink", new SinkBlock());
    public static final Block CHERRY_SINK = registerBlock("cherry_sink", new SinkBlock());
    public static final Block CRIMSON_SINK = registerBlock("crimson_sink", new SinkBlock());
    public static final Block DARK_OAK_SINK = registerBlock("dark_oak_sink", new SinkBlock());
    public static final Block JUNGLE_SINK = registerBlock("jungle_sink", new SinkBlock());
    public static final Block MANGROVE_SINK = registerBlock("mangrove_sink", new SinkBlock());
    public static final Block OAK_SINK = registerBlock("oak_sink", new SinkBlock());
    public static final Block SPRUCE_SINK = registerBlock("spruce_sink", new SinkBlock());
    public static final Block WARPED_SINK = registerBlock("warped_sink", new SinkBlock());

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
                entries.add(ModBlocks.ACACIA_SINK);
                entries.add(ModBlocks.BIRCH_SINK);
                entries.add(ModBlocks.CHERRY_SINK);
                entries.add(ModBlocks.CRIMSON_SINK);
                entries.add(ModBlocks.DARK_OAK_SINK);
                entries.add(ModBlocks.JUNGLE_SINK);
                entries.add(ModBlocks.MANGROVE_SINK);
                entries.add(ModBlocks.OAK_SINK);
                entries.add(ModBlocks.SPRUCE_SINK);
                entries.add(ModBlocks.WARPED_SINK);
            });
        }
}

