package net.Wekston.player_skills.network.packets;

import net.Wekston.player_skills.Stamina.registry.StaminaAttributes;
import net.Wekston.player_skills.PlayerData.PlayerLevelsManager;
import net.Wekston.player_skills.PlayerData.PlayerDataManager;
import net.Wekston.player_skills.network.NetworkHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class StaminaUpgradePacket {
    private final int num;
    private final int xp;

    public StaminaUpgradePacket(int num, int xp) {
        this.num = num;
        this.xp = xp;
    }


    public static void encode(StaminaUpgradePacket packet, FriendlyByteBuf buffer) {
        buffer.writeInt(packet.num);
        buffer.writeInt(packet.xp);
    }

    public static StaminaUpgradePacket decode(FriendlyByteBuf buffer) {
        return new StaminaUpgradePacket(buffer.readInt(), buffer.readInt());
    }
    public static void handle(StaminaUpgradePacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null) {
                return;
            }
            AttributeInstance maxAttributes = player.getAttribute(StaminaAttributes.MAX_STAMINA.get());
            if (maxAttributes == null) {
                return;
            }
            double currentMaxLevel = maxAttributes.getBaseValue();
            double newMaxLevel = currentMaxLevel * Math.pow(1.07, packet.num);
            maxAttributes.setBaseValue(newMaxLevel);
            PlayerDataManager.addStaminaLevel(player, packet.num);
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