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

    private static final Identifier OAK_SINK = Identifier.of("indigooperationsfurnituremod", "block/oak_sink");
    private static final Identifier OAK_SINK_FULL = Identifier.of("indigooperationsfurnituremod", "block/oak_sink_full");

    public SinkBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        // Initialize any additional resources like models if necessary
    }

    @Override
    public void render(SinkBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        // Get the block state (to check facing and faucet state)
        BlockState blockState = blockEntity.getCachedState();

        // Get faucet state (whether it's on or off) and facing direction (North, East, South, West)
        boolean faucetOn = blockState.get(SinkBlock.FAUCET);  // true if faucet is on, false otherwise
        Direction facing = blockState.get(SinkBlock.FACING);  // Block's facing direction

        // Set up rotation based on the facing direction (rotation needs to face the opposite direction of the player)
        int rotationY = 0;
        switch (facing) {
            case EAST:
                rotationY = 0;  // If facing East, we want the texture facing West (opposite)
                break;
            case SOUTH:
                rotationY = 0;  // If facing South, we want the texture facing North
                break;
            case WEST:
                rotationY = 0;  // If facing West, we want the texture facing East
                break;
            case NORTH:
            default:
                rotationY = 0;  // If facing North, we want the texture facing South
                break;
        }

        // Determine which model to render (full sink if faucet is on, regular sink otherwise)
        Identifier modelToRender = faucetOn ? OAK_SINK_FULL : OAK_SINK;
        BakedModel bakedModel = MinecraftClient.getInstance().getBakedModelManager().getModel(modelToRender);

        // Start rendering the model
        matrices.push();

        // Translate the model so that it's centered on its block position
        if (rotationY != 0){
            matrices.translate(0F, 0F, 0F);  // Ensure the model is centered
        }


        // Apply rotation based on the facing direction using RotationAxis
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotationY));  // Rotate the model

        // Get the world and block position (this is important for correct rendering)
        ClientWorld world = MinecraftClient.getInstance().world;
        BlockPos pos = blockEntity.getPos();

        // Get the BlockRenderManager instance
        BlockRenderManager blockRenderManager = MinecraftClient.getInstance().getBlockRenderManager();

        // Get a VertexConsumer from the VertexConsumerProvider
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getCutout());  // Using the Cutout render layer for blocks

        // Create an instance of Random (Minecraft's internal random class)
        Random random = Random.create();

        // Render the block using the BlockRenderManager
        blockRenderManager.renderBlock(blockState, pos, world, matrices, vertexConsumer, false, random);

        // Restore the transformation stack
        matrices.pop();
    }
}
