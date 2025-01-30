package indigoops.indigooperationsfurnituremod.block.blocklogic;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.random.Random;

public class SinkBlockEntityRenderer implements BlockEntityRenderer<SinkBlockEntity> {
    public SinkBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        // Initialize any additional resources like models if necessary
    }

    @Override
    public void render(SinkBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        // Get the block state (to check facing and faucet state)
        BlockState blockState = blockEntity.getCachedState();

        // Start rendering the model
        matrices.push();

        // Get the world and block position (this is important for correct rendering)
        ClientWorld world = MinecraftClient.getInstance().world;
        BlockPos pos = blockEntity.getPos();

        // Get the BlockRenderManager instance
        BlockRenderManager blockRenderManager = MinecraftClient.getInstance().getBlockRenderManager();

        // Get a VertexConsumer from the VertexConsumerProvider
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getCutout());  // Using the Cutout render layer for blocks

        // Create an instance of Random (Minecraft's internal random class)
        Random random = Random.create();

        // I played with matrices for 4 hours and all I got was this stupid line of code
        blockRenderManager.renderBlock(blockState, pos, world, matrices, vertexConsumer, true, random);

        // Restore the transformation stack
        matrices.pop();
    }
}
