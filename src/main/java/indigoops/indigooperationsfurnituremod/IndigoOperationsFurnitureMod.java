package indigoops.indigooperationsfurnituremod;
import indigoops.indigooperationsfurnituremod.block.ModBlocks;
import indigoops.indigooperationsfurnituremod.block.ModBlockEntities;
import indigoops.indigooperationsfurnituremod.block.blocklogic.SinkScreenHandler;
import indigoops.indigooperationsfurnituremod.screen.ModScreens;
import net.fabricmc.api.ModInitializer;

import net.minecraft.inventory.SimpleInventory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IndigoOperationsFurnitureMod implements ModInitializer {
	public static final String MOD_ID = "indigooperationsfurnituremod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {

		ModBlocks.registerModBlocks();
		ModBlockEntities.registerModBlockEntities();
		ModScreens.register();
	}
}