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

    /* chairs */
    public static final Block ACACIA_CHAIR = registerChairBlock("acacia_chair", ChairBlock.ChairWood.ACACIA);
    public static final Block BIRCH_CHAIR = registerChairBlock("birch_chair", ChairBlock.ChairWood.BIRCH);
    public static final Block CHERRY_CHAIR = registerChairBlock("cherry_chair", ChairBlock.ChairWood.CHERRY);
    public static final Block CRIMSON_CHAIR = registerChairBlock("crimson_chair", ChairBlock.ChairWood.CRIMSON);
    public static final Block DARK_OAK_CHAIR = registerChairBlock("dark_oak_chair", ChairBlock.ChairWood.DARKOAK);
    public static final Block JUNGLE_CHAIR = registerChairBlock("jungle_chair", ChairBlock.ChairWood.JUNGLE);
    public static final Block MANGROVE_CHAIR = registerChairBlock("mangrove_chair", ChairBlock.ChairWood.MANGROVE);
    public static final Block OAK_CHAIR = registerChairBlock("oak_chair", ChairBlock.ChairWood.OAK);
    public static final Block SPRUCE_CHAIR = registerChairBlock("spruce_chair", ChairBlock.ChairWood.SPRUCE);
    public static final Block WARPED_CHAIR = registerChairBlock("warped_chair", ChairBlock.ChairWood.WARPED);

    /* sinks */
    public static final Block ACACIA_SINK = registerSinkBlock("acacia_sink", SinkBlock.SinkWood.ACACIA);
    public static final Block BIRCH_SINK = registerSinkBlock("birch_sink", SinkBlock.SinkWood.BIRCH);
    public static final Block CHERRY_SINK = registerSinkBlock("cherry_sink", SinkBlock.SinkWood.CHERRY);
    public static final Block CRIMSON_SINK = registerSinkBlock("crimson_sink", SinkBlock.SinkWood.CRIMSON);
    public static final Block DARK_OAK_SINK = registerSinkBlock("dark_oak_sink", SinkBlock.SinkWood.DARKOAK);
    public static final Block JUNGLE_SINK = registerSinkBlock("jungle_sink", SinkBlock.SinkWood.JUNGLE);
    public static final Block MANGROVE_SINK = registerSinkBlock("mangrove_sink", SinkBlock.SinkWood.MANGROVE);
    public static final Block OAK_SINK = registerSinkBlock("oak_sink", SinkBlock.SinkWood.OAK);
    public static final Block SPRUCE_SINK = registerSinkBlock("spruce_sink", SinkBlock.SinkWood.SPRUCE);
    public static final Block WARPED_SINK = registerSinkBlock("warped_sink", SinkBlock.SinkWood.WARPED);

    /* counter tops */
    public static final Block ACACIA_COUNTER_TOP = registerCounterTopBlock("acacia_counter_top", CounterTopBlock.CounterTopWood.ACACIA);
    public static final Block BIRCH_COUNTER_TOP = registerCounterTopBlock("birch_counter_top", CounterTopBlock.CounterTopWood.BIRCH);
    public static final Block CHERRY_COUNTER_TOP = registerCounterTopBlock("cherry_counter_top", CounterTopBlock.CounterTopWood.CHERRY);
    public static final Block CRIMSON_COUNTER_TOP = registerCounterTopBlock("crimson_counter_top", CounterTopBlock.CounterTopWood.CRIMSON);
    public static final Block DARK_OAK_COUNTER_TOP = registerCounterTopBlock("dark_oak_counter_top", CounterTopBlock.CounterTopWood.DARKOAK);
    public static final Block JUNGLE_COUNTER_TOP = registerCounterTopBlock("jungle_counter_top", CounterTopBlock.CounterTopWood.JUNGLE);
    public static final Block OAK_COUNTER_TOP = registerCounterTopBlock("oak_counter_top", CounterTopBlock.CounterTopWood.OAK);
    public static final Block MANGROVE_COUNTER_TOP = registerCounterTopBlock("mangrove_counter_top", CounterTopBlock.CounterTopWood.MANGROVE);
    public static final Block SPRUCE_COUNTER_TOP = registerCounterTopBlock("spruce_counter_top", CounterTopBlock.CounterTopWood.SPRUCE);
    public static final Block WARPED_COUNTER_TOP = registerCounterTopBlock("warped_counter_top", CounterTopBlock.CounterTopWood.WARPED);

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

    private static Block registerSinkBlock(String name, SinkBlock.SinkWood woodType) {
        return registerBlock(name, new SinkBlock(woodType));
    }

    private static Block registerCounterTopBlock(String name, CounterTopBlock.CounterTopWood woodType) {
        return registerBlock(name, new CounterTopBlock(woodType));
    }

    private static Block registerBlock(String name, Block block){
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(IndigoOperationsFurnitureMod.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block){
        Registry.register(Registries.ITEM, Identifier.of(IndigoOperationsFurnitureMod.MOD_ID, name),
                new BlockItem(block, new Item.Settings()));
    }

    private static Block registerChairBlock(String name, ChairBlock.ChairWood woodType) {
        return registerBlock(name, new ChairBlock(woodType));
    }

    public static void registerModBlocks(){
        IndigoOperationsFurnitureMod.LOGGER.info("Registering Mod Blocks for " + IndigoOperationsFurnitureMod.MOD_ID);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries -> {

            entries.add(ModBlocks.ACACIA_CHAIR);
            entries.add(ModBlocks.BIRCH_CHAIR);
            entries.add(ModBlocks.CHERRY_CHAIR);
            entries.add(ModBlocks.CRIMSON_CHAIR);
            entries.add(ModBlocks.DARK_OAK_CHAIR);
            entries.add(ModBlocks.JUNGLE_CHAIR);
            entries.add(ModBlocks.MANGROVE_CHAIR);
            entries.add(ModBlocks.OAK_CHAIR);
            entries.add(ModBlocks.SPRUCE_CHAIR);
            entries.add(ModBlocks.WARPED_CHAIR);

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
