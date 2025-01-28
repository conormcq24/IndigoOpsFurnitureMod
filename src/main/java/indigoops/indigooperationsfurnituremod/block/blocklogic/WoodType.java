package indigoops.indigooperationsfurnituremod.block.blocklogic;

import net.minecraft.util.StringIdentifiable;

public enum WoodType implements StringIdentifiable {
    OAK("oak"),
    BIRCH("birch"),
    SPRUCE("spruce"),
    JUNGLE("jungle"),
    DARK_OAK("dark_oak"),
    ACACIA("acacia"),
    MANGROVE("mangrove");

    private final String name;

    WoodType(String name) {
        this.name = name;
    }

    @Override
    public String asString() {
        return this.name;
    }
}