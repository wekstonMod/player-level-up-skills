package net.Wekston.player_skills.menu.button;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
public class TextButton extends Button {

    public TextButton(int x, int y, int width, int height, Component message, OnPress onPress) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        int textColor;
        Component displayMessage;

        textColor = 0xFFFFFF;
        if (!this.active) {
            displayMessage = this.getMessage();
        } else if (this.isHovered()) {
            String text = this.getMessage().getString();
            displayMessage = Component.literal("§n" + text);
        } else {
            displayMessage = this.getMessage();
        }

        graphics.drawString(Minecraft.getInstance().font,
                displayMessage,
                this.getX(),
                this.getY(),
                textColor);
    }
}