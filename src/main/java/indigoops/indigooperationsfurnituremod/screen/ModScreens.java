package indigoops.indigooperationsfurnituremod.screen;

import indigoops.indigooperationsfurnituremod.block.blocklogic.SinkScreenHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ModScreens {
    // Removed static initialization to prevent it from running too early
    public static ScreenHandlerType<SinkScreenHandler> SINK_SCREEN_HANDLER;

    // Register screen handler during mod initialization
    public static void register() {
        SINK_SCREEN_HANDLER = Registry.register(
                Registries.SCREEN_HANDLER,
                Identifier.of("indigooperationsfurnituremod", "sink"),
                new ScreenHandlerType<>(SinkScreenHandler::new, FeatureSet.empty())
        );
    }

    @Environment(EnvType.CLIENT)
    public static void registerScreens() {
        HandledScreens.register(SINK_SCREEN_HANDLER, SinkScreen::new);
    }
}
