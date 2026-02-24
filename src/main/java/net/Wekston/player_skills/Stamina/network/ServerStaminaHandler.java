package net.Wekston.player_skills.Stamina.network;

import net.Wekston.player_skills.PlayerSkillsMod;
import net.Wekston.player_skills.Stamina.registry.StaminaAttributes;
import net.Wekston.player_skills.Stamina.registry.StaminaCapability;
import net.Wekston.player_skills.Stamina.registry.StaminaConfig;
import net.Wekston.player_skills.network.NetworkHandler;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import java.util.*;

@Mod.EventBusSubscriber(modid = PlayerSkillsMod.MODID)
public class ServerStaminaHandler {

    private static final UUID EXHAUSTED_SPEED_UUID = UUID.fromString("73411111-2222-3333-4444-555555555555");

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {

        Player player = event.player;
        if (player.isCreative()) {
            return;
        }
        if (player.isSpectator()) {
            return;
        }

        if (event.phase != TickEvent.Phase.START || player.level().isClientSide) {
            return;
        }

        player.getCapability(StaminaCapability.INSTANCE).ifPresent(cap -> {
            double baseAttr = 100.0;
            AttributeInstance attr = player.getAttribute(StaminaAttributes.MAX_STAMINA.get());
            if (attr != null) {
                baseAttr = attr.getValue();
            }
            double regenMult = getAttributeValue(player, StaminaAttributes.STAMINA_REGEN.get(), 1.0);
            double usageMult = getAttributeValue(player, StaminaAttributes.STAMINA_USAGE.get(), 1.0);
            cap.maxStamina = (float) baseAttr;
            boolean isConsuming = false;
            float sprintCost = 0.15f;

            if (player.isSprinting() && sprintCost > 0) {
                cap.stamina -= sprintCost * usageMult;
                isConsuming = true;
            }
            if (isConsuming) {
                cap.staminaRegenDelay = StaminaConfig.COMMON.recoveryDelay.get();
                if (cap.stamina <= 0) {
                    cap.exhaustionCooldown = StaminaConfig.COMMON.exhaustionCooldownDuration.get();
                }
            } else {
                if (cap.staminaRegenDelay > 0) {
                    cap.staminaRegenDelay--;
                } else if (cap.stamina < cap.maxStamina && cap.exhaustionCooldown <= 0) {
                    float recovery = StaminaConfig.COMMON.recoveryPerTick.get().floatValue();
                    if (player.onClimbable()) {
                        recovery *= StaminaConfig.COMMON.recoveryClimbMult.get().floatValue();
                    } else if (player.getDeltaMovement().lengthSqr() < 0.005) {
                        recovery *= StaminaConfig.COMMON.recoveryRestMult.get().floatValue();
                    }

                    recovery *= (float) regenMult;
                    cap.stamina += recovery;
                }
            }
            if (cap.stamina < 0) cap.stamina = 0;
            if (cap.stamina > cap.maxStamina) cap.stamina = cap.maxStamina;

            if (cap.exhaustionCooldown > 0) {
                cap.exhaustionCooldown--;
            }
            boolean isExhausted = cap.stamina <= 0 || cap.exhaustionCooldown > 0;
            updateSpeedPenalty(player, isExhausted);
            AttributeInstance currentStaminaAttr = player.getAttribute(StaminaAttributes.CURRENT_STAMINA.get());
            if (currentStaminaAttr != null) {
                if (Math.abs(currentStaminaAttr.getBaseValue() - cap.stamina) > 0.1) {
                    currentStaminaAttr.setBaseValue(cap.stamina);
                }
            }

            cap.lastTickStamina = cap.stamina;
            if (player.tickCount % 5 == 0 || isConsuming) {
                NetworkHandler.CHANNEL.send(
                        PacketDistributor.PLAYER.with(() -> (net.minecraft.server.level.ServerPlayer) player),
                        new PacketSyncStamina(
                                cap.stamina,
                                cap.maxStamina,
                                0, 0, 0,
                                cap.exhaustionCooldown,
                                new float[0]
                        )
                );
            }
        });
    }

    @SubscribeEvent
    public static void onJump(LivingEvent.LivingJumpEvent event) {

        if (event.getEntity() instanceof Player player && !player.level().isClientSide) {
            if (player.isCreative()) {
                return;
            }
            if (player.isSpectator()) {
                return;
            }

            float jumpCost = 0.85f;

            if (jumpCost > 0) {
                player.getCapability(StaminaCapability.INSTANCE).ifPresent(cap -> {
                    double usageMult = getAttributeValue(player, StaminaAttributes.STAMINA_USAGE.get(), 1.0);

                    cap.stamina -= jumpCost * usageMult;

                    if (cap.stamina < 0) cap.stamina = 0;
                    if (cap.stamina > cap.maxStamina) cap.stamina = cap.maxStamina;

                    cap.staminaRegenDelay = StaminaConfig.COMMON.recoveryDelay.get();

                    NetworkHandler.CHANNEL.send(
                            PacketDistributor.PLAYER.with(() -> (net.minecraft.server.level.ServerPlayer) player),
                            new PacketSyncStamina(
                                    cap.stamina,
                                    cap.maxStamina,
                                    0, 0, 0,
                                    cap.exhaustionCooldown,
                                    new float[0]
                            )
                    );
                });
            }
        }
    }

    private static void updateSpeedPenalty(Player player, boolean isExhausted) {
        AttributeInstance speedAttr = player.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED);
        if (speedAttr == null) return;

        AttributeModifier existing = speedAttr.getModifier(EXHAUSTED_SPEED_UUID);

        if (isExhausted) {
            if (existing == null) {
                double penalty = -0.65;
                if (penalty != 0.0) {
                    speedAttr.addTransientModifier(new AttributeModifier(
                            EXHAUSTED_SPEED_UUID,
                            " ",
                            penalty,
                            AttributeModifier.Operation.MULTIPLY_TOTAL
                    ));
                }
            }
        } else {
            if (existing != null) {
                speedAttr.removeModifier(EXHAUSTED_SPEED_UUID);
            }
        }
    }

    private static double getAttributeValue(Player player, net.minecraft.world.entity.ai.attributes.Attribute attr, double fallback) {
        AttributeInstance instance = player.getAttribute(attr);
        return (instance != null) ? instance.getValue() : fallback;
    }
}