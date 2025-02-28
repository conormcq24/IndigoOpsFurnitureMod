package indigoops.indigooperationsfurnituremod.block;

import indigoops.indigooperationsfurnituremod.IndigoOperationsFurnitureMod;
import indigoops.indigooperationsfurnituremod.block.blocklogic.*;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlocks {
    /* sinks */
    public static final Block ACACIA_SINK = registerBlock("acacia_sink", new AcaciaSinkBlock());
    public static final Block BIRCH_SINK = registerBlock("birch_sink", new BirchSinkBlock());
    public static final Block CHERRY_SINK = registerBlock("cherry_sink", new CherrySinkBlock());
    public static final Block CRIMSON_SINK = registerBlock("crimson_sink", new CrimsonSinkBlock());
    public static final Block DARK_OAK_SINK = registerBlock("dark_oak_sink", new DarkOakSinkBlock());
    public static final Block JUNGLE_SINK = registerBlock("jungle_sink", new JungleSinkBlock());
    public static final Block MANGROVE_SINK = registerBlock("mangrove_sink", new MangroveSinkBlock());
    public static final Block OAK_SINK = registerBlock("oak_sink", new OakSinkBlock());
    public static final Block SPRUCE_SINK = registerBlock("spruce_sink", new SpruceSinkBlock());
    public static final Block WARPED_SINK = registerBlock("warped_sink", new WarpedSinkBlock());

    /* counter tops */
    public static final Block OAK_COUNTER_TOP = registerBlock("oak_counter_top", new OakCounterTopBlock());
    public static final Block ACACIA_COUNTER_TOP = registerBlock("acacia_counter_top", new AcaciaCounterTopBlock());
    public static final Block BIRCH_COUNTER_TOP = registerBlock("birch_counter_top", new BirchCounterTopBlock());
    public static final Block CHERRY_COUNTER_TOP = registerBlock("cherry_counter_top", new CherryCounterTopBlock());
    public static final Block CRIMSON_COUNTER_TOP = registerBlock("crimson_counter_top", new CrimsonCounterTopBlock());
    public static final Block DARK_OAK_COUNTER_TOP = registerBlock("dark_oak_counter_top", new DarkOakCounterTopBlock());
    public static final Block JUNGLE_COUNTER_TOP = registerBlock("jungle_counter_top", new JungleCounterTopBlock());
    public static final Block MANGROVE_COUNTER_TOP = registerBlock("mangrove_counter_top", new MangroveCounterTopBlock());
    public static final Block SPRUCE_COUNTER_TOP = registerBlock("spruce_counter_top", new SpruceCounterTopBlock());
    public static final Block WARPED_COUNTER_TOP = registerBlock("warped_counter_top", new WarpedCounterTopBlock());

    /* tables */
    public static final Block ACACIA_TABLE = registerTableBlock("acacia_table", TableBlock.TableWood.ACACIA, TableBlock.TableCloth.NONE);
    public static final Block BIRCH_TABLE = registerTableBlock("birch_table", TableBlock.TableWood.BIRCH, TableBlock.TableCloth.NONE);
    public static final Block CHERRY_TABLE = registerTableBlock("cherry_table", TableBlock.TableWood.CHERRY, TableBlock.TableCloth.NONE);
    public static final Block CRIMSON_TABLE = registerTableBlock("crimson_table", TableBlock.TableWood.CRIMSON, TableBlock.TableCloth.NONE);
    public static final Block DARK_OAK_TABLE = registerTableBlock("dark_oak_table", TableBlock.TableWood.DARKOAK, TableBlock.TableCloth.NONE);
    public static final Block JUNGLE_TABLE = registerTableBlock("jungle_table", TableBlock.TableWood.JUNGLE, TableBlock.TableCloth.NONE);
    public static final Block MANGROVE_TABLE = registerTableBlock("mangrove_table", TableBlock.TableWood.MANGROVE, TableBlock.TableCloth.NONE);
    public static final Block OAK_TABLE = registerTableBlock("oak_table", TableBlock.TableWood.OAK, TableBlock.TableCloth.NONE);
    public static final Block SPRUCE_TABLE = registerTableBlock("spruce_table", TableBlock.TableWood.SPRUCE, TableBlock.TableCloth.NONE);
    public static final Block WARPED_TABLE = registerTableBlock("warped_table", TableBlock.TableWood.WARPED, TableBlock.TableCloth.NONE);

    private static Block registerTableBlock(String name, TableBlock.TableWood woodType, TableBlock.TableCloth clothType) {
        return registerBlock(name, new TableBlock(woodType, clothType));
    }

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

            entries.add(ModBlocks.ACACIA_COUNTER_TOP);
            entries.add(ModBlocks.BIRCH_COUNTER_TOP);
            entries.add(ModBlocks.CHERRY_COUNTER_TOP);
            entries.add(ModBlocks.CRIMSON_COUNTER_TOP);
            entries.add(ModBlocks.DARK_OAK_COUNTER_TOP);
            entries.add(ModBlocks.JUNGLE_COUNTER_TOP);
            entries.add(ModBlocks.MANGROVE_COUNTER_TOP);
            entries.add(ModBlocks.OAK_COUNTER_TOP);
            entries.add(ModBlocks.SPRUCE_COUNTER_TOP);
            entries.add(ModBlocks.WARPED_COUNTER_TOP);

            entries.add(ModBlocks.ACACIA_TABLE);
            entries.add(ModBlocks.BIRCH_TABLE);
            entries.add(ModBlocks.CHERRY_TABLE);
            entries.add(ModBlocks.CRIMSON_TABLE);
            entries.add(ModBlocks.DARK_OAK_TABLE);
            entries.add(ModBlocks.JUNGLE_TABLE);
            entries.add(ModBlocks.MANGROVE_TABLE);
            entries.add(ModBlocks.OAK_TABLE);
            entries.add(ModBlocks.SPRUCE_TABLE);
            entries.add(ModBlocks.WARPED_TABLE);
        });
    }
}
