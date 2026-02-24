package net.Wekston.player_skills.network.packets;


import net.Wekston.player_skills.PlayerData.PlayerLevelsManager;
import net.Wekston.player_skills.PlayerData.PlayerDataManager;
import net.Wekston.player_skills.network.NetworkHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class AvoidanceUpgradePacket {
    private final int num;
    private final int xp;

    public AvoidanceUpgradePacket(int num, int xp) {
        this.num = num;
        this.xp = xp;
    }

    public static void encode(AvoidanceUpgradePacket packet, FriendlyByteBuf buffer) {
        buffer.writeInt(packet.num);
        buffer.writeInt(packet.xp);
    }

    public static AvoidanceUpgradePacket decode(FriendlyByteBuf buffer) {
        return new AvoidanceUpgradePacket(buffer.readInt(), buffer.readInt());
    }
    public static void handle(AvoidanceUpgradePacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null) {
                return;
            }
            PlayerDataManager.addAvoidanceLevel(player, packet.num);
            player.giveExperiencePoints(-packet.xp);
            PlayerLevelsManager skills = PlayerDataManager.getPlayerSkills(player);
            NetworkHandler.sendToClient(player, new SyncSkillsPacket(
                    skills.getHealthLevel(),
                    skills.getStaminaLevel(),
                    skills.getPowerLevel(),
                    skills.getAvoidanceLevel()
            ));
        });
        context.setPacketHandled(true);
    }
}