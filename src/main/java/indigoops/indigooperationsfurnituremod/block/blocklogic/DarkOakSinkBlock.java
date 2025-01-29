package indigoops.indigooperationsfurnituremod.block.blocklogic;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class DarkOakSinkBlock extends SinkBlock {
    public DarkOakSinkBlock() {
        super();
    }
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new SinkBlockEntity(pos, state);  // Make sure to define a SinkBlockEntity class
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return null;
    }
}
