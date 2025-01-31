package indigoops.indigooperationsfurnituremod.block;

import indigoops.indigooperationsfurnituremod.IndigoOperationsFurnitureMod;
import indigoops.indigooperationsfurnituremod.block.blocklogic.CounterTopBlockEntity;
import indigoops.indigooperationsfurnituremod.block.blocklogic.SinkBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {
    // Create the BlockEntityType here
    public static final BlockEntityType<SinkBlockEntity> SINK_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(IndigoOperationsFurnitureMod.MOD_ID, "sink_block_entity"),
                    BlockEntityType.Builder.create(SinkBlockEntity::new,
                            ModBlocks.ACACIA_SINK,
                            ModBlocks.BIRCH_SINK,
                            ModBlocks.CHERRY_SINK,
                            ModBlocks.CRIMSON_SINK,
                            ModBlocks.DARK_OAK_SINK,
                            ModBlocks.JUNGLE_SINK,
                            ModBlocks.MANGROVE_SINK,
                            ModBlocks.OAK_SINK,
                            ModBlocks.SPRUCE_SINK,
                            ModBlocks.WARPED_SINK
                    ).build(null));
    public static final BlockEntityType<CounterTopBlockEntity> COUNTER_TOP_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(IndigoOperationsFurnitureMod.MOD_ID, "counter_top_block_entity"),
                    BlockEntityType.Builder.create(CounterTopBlockEntity::new,
                            ModBlocks.ACACIA_COUNTER_TOP,
                            ModBlocks.BIRCH_COUNTER_TOP,
                            ModBlocks.CHERRY_COUNTER_TOP,
                            ModBlocks.CRIMSON_COUNTER_TOP,
                            ModBlocks.DARK_OAK_COUNTER_TOP,
                            ModBlocks.JUNGLE_COUNTER_TOP,
                            ModBlocks.MANGROVE_COUNTER_TOP,
                            ModBlocks.OAK_COUNTER_TOP,
                            ModBlocks.SPRUCE_COUNTER_TOP,
                            ModBlocks.WARPED_COUNTER_TOP
                    ).build(null));

    public static void registerModBlockEntities() {
        IndigoOperationsFurnitureMod.LOGGER.info("Registering Block Entities for " + IndigoOperationsFurnitureMod.MOD_ID);
    }
}
