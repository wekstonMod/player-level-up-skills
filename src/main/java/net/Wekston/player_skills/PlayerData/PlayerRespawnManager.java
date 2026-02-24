package net.Wekston.player_skills.PlayerData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class PlayerRespawnManager {

    private static final String HEALTH_KEY = "player_skills:health_key";

    private static final String POWER_KEY = "player_skills:power_key";

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            CompoundTag persistentData = player.getPersistentData();

            double currentMaxHealth = player.getMaxHealth();
            double baseHealth = 20.0;
            double multiplierHealth = currentMaxHealth / baseHealth;

            persistentData.putDouble(HEALTH_KEY, multiplierHealth);
            AttributeInstance maxPowerAttr = player.getAttribute(Attributes.ATTACK_DAMAGE);

            assert maxPowerAttr != null;
            double PowerAttribute = maxPowerAttr.getBaseValue();
            double basePower = 1.0;
            double multiplierPower = PowerAttribute / basePower;
            persistentData.putDouble(POWER_KEY, multiplierPower);

        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            CompoundTag persistentData = player.getPersistentData();

            if (persistentData.contains(HEALTH_KEY)) {
                double multiplier = persistentData.getDouble(HEALTH_KEY);

                if (multiplier > 1.0) {
                    AttributeInstance maxHealthAttr = player.getAttribute(Attributes.MAX_HEALTH);
                    if (maxHealthAttr != null) {
                        double baseHealth = 20.0;
                        double newMaxHealth = baseHealth * multiplier;
                        maxHealthAttr.setBaseValue(newMaxHealth);
                        player.setHealth((float) newMaxHealth);
                    }
                }
            }
            if (persistentData.contains(POWER_KEY)) {
                double multiplier = persistentData.getDouble(POWER_KEY);

                if (multiplier > 1.0) {
                    AttributeInstance maxPowerAttr = player.getAttribute(Attributes.ATTACK_DAMAGE);
                    if (maxPowerAttr != null) {
                        double basePower = 1.0;
                        double newMaxPower = basePower * multiplier;
                        maxPowerAttr.setBaseValue(newMaxPower);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            CompoundTag oldData = event.getOriginal().getPersistentData();
            CompoundTag newData = event.getEntity().getPersistentData();

            if (oldData.contains(HEALTH_KEY)) {
                newData.putDouble(HEALTH_KEY,
                        oldData.getDouble(HEALTH_KEY));
            }
            if (oldData.contains(POWER_KEY)) {
                newData.putDouble(POWER_KEY,
                        oldData.getDouble(POWER_KEY));
            }
        }
    }
}