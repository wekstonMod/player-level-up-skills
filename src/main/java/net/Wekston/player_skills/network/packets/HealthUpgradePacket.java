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
public class HealthUpgradePacket {
    private final int num;
    private final int xp;

    public HealthUpgradePacket(int num, int xp) {
        this.num = num;
        this.xp = xp;
    }

    public static void encode(HealthUpgradePacket packet, FriendlyByteBuf buffer) {
        buffer.writeInt(packet.num);
        buffer.writeInt(packet.xp);
    }

    public static HealthUpgradePacket decode(FriendlyByteBuf buffer) {
        return new HealthUpgradePacket(buffer.readInt(), buffer.readInt());
    }
    public static void handle(HealthUpgradePacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();

            if (player == null) {
                return;
            }

            AttributeInstance maxHealthAttr = player.getAttribute(Attributes.MAX_HEALTH);
            if (maxHealthAttr == null) {
                return;
            }
            double currentMaxHealth = maxHealthAttr.getBaseValue();
            double newMaxHealth = currentMaxHealth * Math.pow(1.05, packet.num);
            maxHealthAttr.setBaseValue(newMaxHealth);
            PlayerDataManager.addHealthLevel(player, packet.num);
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