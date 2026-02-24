package net.Wekston.player_skills.event;

import net.Wekston.player_skills.PlayerData.PlayerDataManager;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber
public class AvoidanceEvent {
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getEntity() instanceof Player player) {
            Random random = new Random();
            float chance = random.nextFloat();
            if ((float) PlayerDataManager.getAvoidanceLevel(player) / 100 > chance) {
                event.setCanceled(true);
            }
        }
    }
}
