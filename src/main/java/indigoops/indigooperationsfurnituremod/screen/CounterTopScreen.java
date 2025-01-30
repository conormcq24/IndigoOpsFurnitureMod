package indigoops.indigooperationsfurnituremod.screen;

import indigoops.indigooperationsfurnituremod.block.blocklogic.CounterTopScreenHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.client.gui.DrawContext;

public class CounterTopScreen extends HandledScreen<CounterTopScreenHandler> {

    private static final Identifier TEXTURE = Identifier.of("indigooperationsfurnituremod", "textures/gui/sink.png");  // Path to your custom GUI texture

    public CounterTopScreen(CounterTopScreenHandler handler, PlayerInventory playerInventory, Text title) {
        super(handler, playerInventory, title);
    }
    @Override
    protected void init() {
        super.init();
        this.x = (this.width - 176) / 2; // Center texture horizontally
        this.y = (this.height - 133) / 2; // Center texture vertically
    }
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        // Bind the texture
        MinecraftClient.getInstance().getTextureManager().bindTexture(TEXTURE);
        context.drawTexture(TEXTURE, this.x, this.y, 0, 0, 176, 133);
    }
    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        // Draw the title (it will be drawn at the center as defined in `init()` method)
        context.drawText(this.textRenderer, "Counter Inventory", 8, 7, 4210752, false);

        // Draw the player's inventory label
        context.drawText(this.textRenderer, "Player Inventory", 8, 40, 4210752, false);
    }
}
