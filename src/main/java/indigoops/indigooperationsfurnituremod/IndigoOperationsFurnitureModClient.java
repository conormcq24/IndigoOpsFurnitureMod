package indigoops.indigooperationsfurnituremod;

import indigoops.indigooperationsfurnituremod.block.ModBlockEntities;
import indigoops.indigooperationsfurnituremod.block.blocklogic.CounterTopBlockEntityRenderer;
import indigoops.indigooperationsfurnituremod.block.blocklogic.SinkBlockEntityRenderer;
import indigoops.indigooperationsfurnituremod.block.blocklogic.SinkScreenHandler;
import indigoops.indigooperationsfurnituremod.screen.ModScreens;
import indigoops.indigooperationsfurnituremod.screen.SinkScreen;
import indigoops.indigooperationsfurnituremod.screen.CounterTopScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class IndigoOperationsFurnitureModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockEntityRendererRegistry.register(ModBlockEntities.SINK_BLOCK_ENTITY, SinkBlockEntityRenderer::new);
        HandledScreens.register(ModScreens.SINK_SCREEN_HANDLER, SinkScreen::new);
        BlockEntityRendererRegistry.register(ModBlockEntities.COUNTER_TOP_BLOCK_ENTITY, CounterTopBlockEntityRenderer::new);
        HandledScreens.register(ModScreens.COUNTER_TOP_SCREEN_HANDLER, CounterTopScreen::new);
    }
}
