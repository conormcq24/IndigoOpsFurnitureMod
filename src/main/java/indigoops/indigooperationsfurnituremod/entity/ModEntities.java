package indigoops.indigooperationsfurnituremod.entity;

import indigoops.indigooperationsfurnituremod.IndigoOperationsFurnitureMod;
import indigoops.indigooperationsfurnituremod.entity.custom.ChairEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {

    public static final EntityType<ChairEntity> CHAIR = Registry.register(Registries.ENTITY_TYPE,
            Identifier.of(IndigoOperationsFurnitureMod.MOD_ID, "chair"),
            EntityType.Builder.create(ChairEntity::new, SpawnGroup.MISC)
                    .dimensions(1f, 2.5f).build());

    public static void registerModEntities() {
        IndigoOperationsFurnitureMod.LOGGER.info("Registering Mod Entities for " + IndigoOperationsFurnitureMod.MOD_ID);
    }
}
