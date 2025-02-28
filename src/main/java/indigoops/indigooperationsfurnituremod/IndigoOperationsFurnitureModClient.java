package indigoops.indigooperationsfurnituremod;

import indigoops.indigooperationsfurnituremod.block.ModBlockEntities;
import indigoops.indigooperationsfurnituremod.block.ModBlocks;
import indigoops.indigooperationsfurnituremod.block.blocklogic.CounterTopBlockEntityRenderer;
import indigoops.indigooperationsfurnituremod.block.blocklogic.SinkBlockEntityRenderer;
import indigoops.indigooperationsfurnituremod.block.blocklogic.SinkScreenHandler;
import indigoops.indigooperationsfurnituremod.block.blocklogic.TableBlock;
import indigoops.indigooperationsfurnituremod.screen.ModScreens;
import indigoops.indigooperationsfurnituremod.screen.SinkScreen;
import indigoops.indigooperationsfurnituremod.screen.CounterTopScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

import static indigoops.indigooperationsfurnituremod.block.ModBlocks.*;

public class IndigoOperationsFurnitureModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockEntityRendererRegistry.register(ModBlockEntities.SINK_BLOCK_ENTITY, SinkBlockEntityRenderer::new);
        HandledScreens.register(ModScreens.SINK_SCREEN_HANDLER, SinkScreen::new);
        BlockEntityRendererRegistry.register(ModBlockEntities.COUNTER_TOP_BLOCK_ENTITY, CounterTopBlockEntityRenderer::new);
        HandledScreens.register(ModScreens.COUNTER_TOP_SCREEN_HANDLER, CounterTopScreen::new);

        // handle transparency for cloth, and tint for wood on table objects
        BlockRenderLayerMap.INSTANCE.putBlock(ACACIA_TABLE, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(BIRCH_TABLE, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(CHERRY_TABLE, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(CRIMSON_TABLE, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(DARK_OAK_TABLE, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(JUNGLE_TABLE, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(MANGROVE_TABLE, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(OAK_TABLE, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(SPRUCE_TABLE, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(WARPED_TABLE, RenderLayer.getTranslucent());

        // Register color providers for each table type
        registerTableColorProvider(ACACIA_TABLE, 0xb05c34);
        registerTableColorProvider(BIRCH_TABLE, 0xd4c484);
        registerTableColorProvider(CHERRY_TABLE, 0xdc9c94);
        registerTableColorProvider(CRIMSON_TABLE, 0x843c5c);
        registerTableColorProvider(DARK_OAK_TABLE, 0x4c341c);
        registerTableColorProvider(JUNGLE_TABLE, 0xb8885c);
        registerTableColorProvider(MANGROVE_TABLE, 0x8c4c3c);
        registerTableColorProvider(OAK_TABLE, 0xc49c64);
        registerTableColorProvider(SPRUCE_TABLE, 0x7c5c34);
        registerTableColorProvider(WARPED_TABLE, 0x3c8c8c);

        registerItemColorProviders();
    }

    private void registerTableColorProvider(Block tableBlock, int woodColor) {
        ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) -> {
            if (tintIndex == 0) {
                return woodColor; // Wood tint
            } else if (tintIndex == 1) {
                if (state.contains(TableBlock.CLOTH_TYPE) && state.get(TableBlock.HAS_CLOTH)) {
                    return getClothColor(state.get(TableBlock.CLOTH_TYPE));
                }
            }
            return -1; // No tint applied
        }, tableBlock);
    }

    private int getClothColor(TableBlock.TableCloth clothType) {
        switch (clothType) {
            case WHITE: return 0xFFFFFF;
            case LIGHTGRAY: return 0xABABAB;
            case GRAY: return 0x6F6F6F;
            case BLACK: return 0x333333;
            case BROWN: return 0x724728;
            case RED: return 0xB02E26;
            case ORANGE: return 0xF9801D;
            case YELLOW: return 0xFED83D;
            case LIME: return 0x80C71F;
            case GREEN: return 0x5D7C15;
            case CYAN: return 0x169C9C;
            case LIGHTBLUE: return 0x3AB3DA;
            case BLUE: return 0x3C44AA;
            case PURPLE: return 0x8932B8;
            case MAGENTA: return 0xC74EBD;
            case PINK: return 0xF38BAA;
            case NONE:
            default: return -1; // No tint applied
        }
    }
    private void registerItemColorProviders() {
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
            if (tintIndex == 0) {
                Item item = stack.getItem();
                // Add 0xFF000000 to ensure full alpha
                if (item == ACACIA_TABLE.asItem()) return 0xFF000000 | 0xb05c34;
                if (item == BIRCH_TABLE.asItem()) return 0xFF000000 | 0xd4c484;
                if (item == CHERRY_TABLE.asItem()) return 0xFF000000 | 0xdc9c94;
                if (item == CRIMSON_TABLE.asItem()) return 0xFF000000 | 0x843c5c;
                if (item == DARK_OAK_TABLE.asItem()) return 0xFF000000 | 0x4c341c;
                if (item == JUNGLE_TABLE.asItem()) return 0xFF000000 | 0xb8885c;
                if (item == MANGROVE_TABLE.asItem()) return 0xFF000000 | 0x8c4c3c;
                if (item == OAK_TABLE.asItem()) return 0xFF000000 | 0xc49c64;
                if (item == SPRUCE_TABLE.asItem()) return 0xFF000000 | 0x7c5c34;
                if (item == WARPED_TABLE.asItem()) return 0xFF000000 | 0x3c8c8c;
            }
            return -1;
        }, ACACIA_TABLE.asItem(), BIRCH_TABLE.asItem(), CHERRY_TABLE.asItem(), CRIMSON_TABLE.asItem(),
                DARK_OAK_TABLE.asItem(), JUNGLE_TABLE.asItem(), MANGROVE_TABLE.asItem(), OAK_TABLE.asItem(), SPRUCE_TABLE.asItem(),
                    WARPED_TABLE.asItem());
    }
}