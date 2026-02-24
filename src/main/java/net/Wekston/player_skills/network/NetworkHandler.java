package net.Wekston.player_skills.network;

import net.Wekston.player_skills.Stamina.network.PacketSyncStamina;
import net.Wekston.player_skills.network.packets.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("player_skills", "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int packetId = 0;

    public static void register() {

        CHANNEL.registerMessage(packetId++,
                HealthUpgradePacket.class,
                HealthUpgradePacket::encode,
                HealthUpgradePacket::decode,
                HealthUpgradePacket::handle);

        CHANNEL.registerMessage(packetId++,
                AvoidanceUpgradePacket.class,
                AvoidanceUpgradePacket::encode,
                AvoidanceUpgradePacket::decode,
                AvoidanceUpgradePacket::handle);

        CHANNEL.registerMessage(packetId++,
                PowerUpgradePacket.class,
                PowerUpgradePacket::encode,
                PowerUpgradePacket::decode,
                PowerUpgradePacket::handle);

        CHANNEL.registerMessage(packetId++,
                StaminaUpgradePacket.class,
                StaminaUpgradePacket::encode,
                StaminaUpgradePacket::decode,
                StaminaUpgradePacket::handle);

        CHANNEL.registerMessage(packetId++,
                SyncSkillsPacket.class,
                SyncSkillsPacket::encode,
                SyncSkillsPacket::decode,
                SyncSkillsPacket::handle);

        CHANNEL.registerMessage(packetId++,
                PacketSyncStamina.class,
                PacketSyncStamina::encode,
                PacketSyncStamina::decode,
                PacketSyncStamina::handle);
    }



    public static void sendHealthUpgradePacket(int num, int xp) {
        CHANNEL.sendToServer(new HealthUpgradePacket(num, xp));
    }

    public static void sendStaminaUpgradePacket(int num, int xp) {
        CHANNEL.sendToServer(new StaminaUpgradePacket(num, xp));
    }
    public static void sendAvoidanceUpgradePacket(int num, int xp) {
        CHANNEL.sendToServer(new AvoidanceUpgradePacket(num, xp));
    }
    public static void sendPowerUpgradePacket(int num, int xp) {
        CHANNEL.sendToServer(new PowerUpgradePacket(num, xp));
    }

    public static void sendToClient(ServerPlayer player, Object packet) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), packet);
    }
}