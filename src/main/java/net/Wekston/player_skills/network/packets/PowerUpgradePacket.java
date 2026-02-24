package net.Wekston.player_skills.network.packets;


import net.Wekston.player_skills.PlayerData.PlayerLevelsManager;
import net.Wekston.player_skills.PlayerData.PlayerDataManager;
import net.Wekston.player_skills.network.NetworkHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PowerUpgradePacket {
    private final int num;
    private final int xp;

    public PowerUpgradePacket(int num, int xp) {
        this.num = num;
        this.xp = xp;
    }


    public static void encode(PowerUpgradePacket packet, FriendlyByteBuf buffer) {
        buffer.writeInt(packet.num);
        buffer.writeInt(packet.xp);
    }

    public static PowerUpgradePacket decode(FriendlyByteBuf buffer) {
        return new PowerUpgradePacket(buffer.readInt(), buffer.readInt());
    }
    public static void handle(PowerUpgradePacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null) {
                return;
            }
            AttributeInstance maxPowerAttr = player.getAttribute(Attributes.ATTACK_DAMAGE);
            if (maxPowerAttr == null) {
                return;
            }
            double currentMaxPower = maxPowerAttr.getBaseValue();
            double newMaxPower = currentMaxPower * Math.pow(1.02, packet.num);
            maxPowerAttr.setBaseValue(newMaxPower);
            PlayerDataManager.addPowerLevel(player, packet.num);
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