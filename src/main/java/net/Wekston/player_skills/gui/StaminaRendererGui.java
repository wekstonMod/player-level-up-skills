package net.Wekston.player_skills.gui;

import net.Wekston.player_skills.PlayerSkillsMod;
import net.Wekston.player_skills.Stamina.registry.StaminaCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PlayerSkillsMod.MODID, value = Dist.CLIENT)
public class StaminaRendererGui {

    private static final ResourceLocation BAR_START =
            new ResourceLocation("player_skills", "textures/gui/bar_start.png");

    private static final ResourceLocation BAR_END =
            new ResourceLocation("player_skills", "textures/gui/bar_end.png");

    private static float displayedStamina = 181.0f;
    private static float displayedMaxStamina = 181.0f;

    @SubscribeEvent
    public static void onRenderGuiOverlayPost(RenderGuiOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null || mc.options.hideGui || player.isCreative() || player.isSpectator()
        ) return;


        player.getCapability(StaminaCapability.INSTANCE).ifPresent(cap -> {
            renderStaminaBar(event.getGuiGraphics(), mc, player, cap);
        });
    }

    private static void renderStaminaBar(GuiGraphics gfx, Minecraft mc, Player player, StaminaCapability cap) {
        displayedStamina += (cap.stamina - displayedStamina) * 0.2f;
        displayedMaxStamina += (cap.maxStamina - displayedMaxStamina) * 0.2f;

        if (Math.abs(cap.stamina - displayedStamina) < 0.05f) {
            displayedStamina = cap.stamina;
        }
        if (Math.abs(cap.maxStamina - displayedMaxStamina) < 0.05f) {
            displayedMaxStamina = cap.maxStamina;
        }
        int endX = 1;
        if (displayedStamina < 1) {
            endX = 0;
        }

        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();

        int x = (screenWidth / 2);
        int y = screenHeight - 5;

        float percent = displayedStamina / displayedMaxStamina;
        int getStamina = (int) (percent * 180);
        getStamina = Math.max(0, Math.min(getStamina, 180));

        gfx.blit(BAR_START,
                x - getStamina / 2 - 1, y - 20,
                0, 0,
                getStamina, 3,
                181, 3);
        gfx.blit(BAR_END,
                x + getStamina / 2 - 1, y - 20,
                0, 0,
                endX, 3,
                1, 3);

    }
}