package net.Wekston.player_skills.registry;


import com.mojang.blaze3d.platform.InputConstants;
import net.Wekston.player_skills.menu.SkillsMenu;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class KeyBindings {

    public static final String KEY_CATEGORY = "key.category.player_skills";
    public static final String KEY_OPEN_MENU = "key.player_skills.skill_menu";

    public static KeyMapping openMenuKey = new KeyMapping(
            KEY_OPEN_MENU,
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_H,
            KEY_CATEGORY
    );

    @Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {

        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(openMenuKey);
        }
    }
    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (openMenuKey.consumeClick()) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null && mc.screen == null) {
                mc.setScreen(new SkillsMenu(mc.player));
            }
        }
    }
}