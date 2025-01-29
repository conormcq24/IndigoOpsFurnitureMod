package indigoops.indigooperationsfurnituremod.block.blocklogic;

import indigoops.indigooperationsfurnituremod.block.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class SinkBlockEntity extends BlockEntity {
    public SinkBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SINK_BLOCK_ENTITY, pos, state);
    }
}
