package indigoops.indigooperationsfurnituremod.block.blocklogic;

import indigoops.indigooperationsfurnituremod.IndigoOperationsFurnitureMod;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.math.random.Random;

public class SinkBlockEntityRenderer implements BlockEntityRenderer<SinkBlockEntity> {
    public SinkBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        // No additional initialization needed
    }

    @Override
    public void render(SinkBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {

    }
}