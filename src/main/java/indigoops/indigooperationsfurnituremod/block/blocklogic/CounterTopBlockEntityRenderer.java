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

public class CounterTopBlockEntityRenderer implements BlockEntityRenderer<CounterTopBlockEntity> {

    public CounterTopBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        // Initialize any additional resources like models if necessary
    }

    @Override
    public void render(CounterTopBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {

    }
}
