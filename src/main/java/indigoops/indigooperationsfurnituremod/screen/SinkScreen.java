package indigoops.indigooperationsfurnituremod.screen;

import indigoops.indigooperationsfurnituremod.block.blocklogic.SinkScreenHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.client.gui.DrawContext;

public class SinkScreen extends HandledScreen<SinkScreenHandler> {

    private static final Identifier TEXTURE = Identifier.of("indigooperationsfurnituremod", "textures/gui/sink_gui.png");  // Path to your custom GUI texture

    public SinkScreen(SinkScreenHandler handler, PlayerInventory playerInventory, Text title) {
        super(handler, playerInventory, title);
    }

    @Override
    protected void init() {
        super.init();
        // Center the title
        this.titleX = (this.width - this.textRenderer.getWidth(this.title)) / 2;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        this.drawForeground(context, mouseX, mouseY);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        // Bind the texture
        MinecraftClient.getInstance().getTextureManager().bindTexture(TEXTURE);

        // Draw the background texture at the specified position (x, y)
        context.drawTexture(TEXTURE, this.x, this.y, 0, 0, this.width, this.height);  // Adjust the size as needed
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        // Draw the title (it will be drawn at the center as defined in `init()` method)
        context.drawText(this.textRenderer, "Sink Inventory", this.titleX, 5, 0, false);

        // Draw the player's inventory label
        context.drawText(this.textRenderer, "Player Inventory", 8, this.height - 94, 0, false);
    }
}
