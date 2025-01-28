package indigoops.indigooperationsfurnituremod;
import indigoops.indigooperationsfurnituremod.block.ModBlocks;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IndigoOperationsFurnitureMod implements ModInitializer {
	public static final String MOD_ID = "indigooperationsfurnituremod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModBlocks.registerModBlocks();
	}
}