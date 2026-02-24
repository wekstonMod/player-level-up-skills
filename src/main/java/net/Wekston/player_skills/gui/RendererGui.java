package net.Wekston.player_skills.gui;

import net.Wekston.player_skills.PlayerSkillsMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PlayerSkillsMod.MODID, value = Dist.CLIENT)
public class RendererGui {

    private static final ResourceLocation BAR =
            new ResourceLocation("player_skills", "textures/gui/bar.png");
    private static final ResourceLocation HEALTH_BAR =
            new ResourceLocation("player_skills", "textures/gui/health_bar.png");

    private static final ResourceLocation HUNGRY_BAR =
            new ResourceLocation("player_skills", "textures/gui/hungry_bar.png");
    private static final ResourceLocation HUNGRY =
            new ResourceLocation("player_skills", "textures/gui/hunger.png");

    private static final ResourceLocation HEALTH =
            new ResourceLocation("player_skills", "textures/gui/health.png");
    private static final ResourceLocation AIR_BAR =
            new ResourceLocation("player_skills", "textures/gui/air_bar.png");
    private static final ResourceLocation ARMOR_BAR =
            new ResourceLocation("player_skills", "textures/gui/armor_bar.png");
    private static final ResourceLocation AIR =
            new ResourceLocation("player_skills", "textures/gui/air.png");
    private static final ResourceLocation ARMOR =
            new ResourceLocation("player_skills", "textures/gui/armor.png");

    @SubscribeEvent
    public static void onRenderGuiOverlayPre(RenderGuiOverlayEvent.Pre event) {
        if (event.getOverlay() == VanillaGuiOverlay.PLAYER_HEALTH.type() ||
                event.getOverlay() == VanillaGuiOverlay.FOOD_LEVEL.type() ||
                event.getOverlay() == VanillaGuiOverlay.ARMOR_LEVEL.type()||
                event.getOverlay() == VanillaGuiOverlay.AIR_LEVEL.type()) {
            event.setCanceled(true);
        }

    }


    @SubscribeEvent
    public static void onRenderGuiOverlayPost(RenderGuiOverlayEvent.Post event) {
            Minecraft mc = Minecraft.getInstance();
            Player player = mc.player;
            if (player == null || mc.options.hideGui || player.isCreative() || player.isSpectator()
            ) return;

            GuiGraphics guiGraphics = event.getGuiGraphics();
            int screenWidth = mc.getWindow().getGuiScaledWidth();
            int screenHeight = mc.getWindow().getGuiScaledHeight();

            int x = screenWidth / 2;
            int y = screenHeight - 40;

            renderBar(guiGraphics, player, x, y, screenWidth);

    }

    private static void renderBar(GuiGraphics gfx, Player player, int x, int y, int screenWidth) {
        Minecraft mc = Minecraft.getInstance();


        int armorValue = mc.player.getArmorValue();
        if (armorValue > 1) {
            int getarmor = armorValue * 4;

            gfx.blit(BAR,
                    x - 89, y - 10,
                    0, 0,
                    82, 8,
                    82, 8);

            gfx.blit(ARMOR_BAR,
                    x - 88, y - 9,
                    0, 0,
                    getarmor, 6,
                    80, 6);
            gfx.blit(ARMOR,
                    x - 94, y - 10,
                    0, 0,
                    9, 9,
                    9, 9);

        }

        int maxAir = player.getMaxAirSupply();
        int air = player.getAirSupply();

        float airpercent = (float) air / maxAir;
        int getair = (int) (airpercent * 80);
        getair = Math.max(0, Math.min(getair, 80));
        if (player.isEyeInFluidType(ForgeMod.WATER_TYPE.get()) || air < 300) {

            gfx.blit(BAR,
                    x + 9, y - 10,
                    0, 0,
                    82, 8,
                    82, 8);

            gfx.blit(AIR_BAR,
                    x + 10, y - 9,
                    0, 0,
                    getair, 6,
                    80, 6);
            gfx.blit(AIR,
                    x + 6, y - 10,
                    0, 0,
                    9, 9,
                    9, 9);

        }

        float health = player.getHealth();
        float maxHealth = player.getMaxHealth();
        float percent = health / maxHealth;
        int gethealth = (int) (percent * 80);
        gethealth = Math.max(0, Math.min(gethealth, 80));

        gfx.blit(BAR,
                x - 89, y,
                0, 0,
                82, 8,
                82, 8);

        gfx.blit(HEALTH_BAR,
                x - 88, y + 1,
                0, 0,
                gethealth, 6,
                80, 6);

        gfx.blit(HEALTH,
                x - 94, y,
                0, 0,
                9, 9,
                9, 9);

        float food = player.getFoodData().getFoodLevel();
        int getFood = (int) (food * 4);
        gfx.blit(BAR,
                x + 9, y,
                0, 0,
                82, 8,
                82, 8);

        gfx.blit(HUNGRY_BAR,
                x + 10, y + 1,
                0, 0,
                getFood, 6,
                80, 6);

        gfx.blit(HUNGRY,
                x + 6, y,
                0, 0,
                9, 9,
                9, 9);

    }
}