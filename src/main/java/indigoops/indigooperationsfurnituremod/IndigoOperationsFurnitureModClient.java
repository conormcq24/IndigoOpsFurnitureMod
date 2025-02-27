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
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

import static indigoops.indigooperationsfurnituremod.block.ModBlocks.ACACIA_TABLE;

public class IndigoOperationsFurnitureModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockEntityRendererRegistry.register(ModBlockEntities.SINK_BLOCK_ENTITY, SinkBlockEntityRenderer::new);
        HandledScreens.register(ModScreens.SINK_SCREEN_HANDLER, SinkScreen::new);
        BlockEntityRendererRegistry.register(ModBlockEntities.COUNTER_TOP_BLOCK_ENTITY, CounterTopBlockEntityRenderer::new);
        HandledScreens.register(ModScreens.COUNTER_TOP_SCREEN_HANDLER, CounterTopScreen::new);

        // handle transparency for cloth, and tint for wood on table objects
        BlockRenderLayerMap.INSTANCE.putBlock(ACACIA_TABLE, RenderLayer.getCutout());
        // In IndigoOperationsFurnitureModClient.java
        ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) -> {
            if (tintIndex == 0) {
                return 0xb05c34; // Tint for wood (#0)
            } else if (tintIndex == 1) {
                if (state.contains(TableBlock.CLOTH_TYPE) && state.get(TableBlock.HAS_CLOTH)) {
                    TableBlock.TableCloth clothType = state.get(TableBlock.CLOTH_TYPE);
                    switch (clothType) {
                        case WHITE: return 0xFFFFFF;
                        case LIGHTGRAY: return 0xABABAB;
                        case GRAY: return 0x8A8A8A;
                        case BLACK: return 0x1D1D1D;
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
            }
            return -1; // Default: no tint applied
        }, ACACIA_TABLE); // Make sure to register this for ALL your table blocks
    }
}
