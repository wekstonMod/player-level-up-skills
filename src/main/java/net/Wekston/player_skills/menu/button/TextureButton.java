package net.Wekston.player_skills.menu.button;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class TextureButton extends Button {
    private static final ResourceLocation BUTTON_TEXTURE =
            new ResourceLocation("player_skills", "textures/gui/button.png");

    private static final ResourceLocation BUTTON_ACTIVE =
            new ResourceLocation("player_skills", "textures/gui/button_active.png");

    public TextureButton(int x, int y, int width, int height, Component message, OnPress onPress) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        ResourceLocation buttonTexture;
        if (!this.active) {
            buttonTexture = BUTTON_TEXTURE;
        } else if (this.isHovered()) {
            buttonTexture = BUTTON_ACTIVE;
        } else {
            buttonTexture = BUTTON_TEXTURE;
        }

        graphics.blit(buttonTexture,
                this.getX(), this.getY(),
                0, 0,
                this.width, this.height,
                120, 15);

        graphics.drawString(Minecraft.getInstance().font,
                this.getMessage(),
                this.getX() + (this.width - Minecraft.getInstance().font.width(this.getMessage())) / 2,
                this.getY() + (this.height - 8) / 2,
                0xFFFFFF);
    }
}