package talloran.autosorter.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import talloran.autosorter.Autosorter;

public class AutoSorterScreen extends HandledScreen<AutoSorterScreenHandler> {

    //текстура инвенторя
    private static final Identifier TEXTURE = new Identifier(Autosorter.MOD_ID, "textures/gui/auto_sorter_gui.png");

    //какая-то важная штука
    public AutoSorterScreen(AutoSorterScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        backgroundWidth = 256;
        backgroundHeight = 222;  //эти поля для определения размера инвенторя

        super.init();

        //надпись заголовка (название блок энтити)
        titleY = 6;
        titleX = 48;  //было 30

        //надпись инвенторя
        playerInventoryTitleY = 128;
        playerInventoryTitleX = 48;  //тут тоже 30
    }


    //отрисовка фона
    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        context.drawTexture(TEXTURE, x + 40 ,y, 0, 0, backgroundWidth, backgroundHeight, 256, 256);
    }                               //а тут ^ было 22
}
