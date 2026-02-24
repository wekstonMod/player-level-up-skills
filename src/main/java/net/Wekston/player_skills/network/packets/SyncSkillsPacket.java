package net.Wekston.player_skills.network.packets;

import net.Wekston.player_skills.PlayerData.PlayerDataManager;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;
public class SyncSkillsPacket {
    private final int healthLevel;
    private final int staminaLevel;
    private final int powerLevel;
    private final int speedLevel;

    public SyncSkillsPacket(int healthLevel, int staminaLevel, int powerLevel, int speedLevel) {
        this.healthLevel = healthLevel;
        this.staminaLevel = staminaLevel;
        this.powerLevel = powerLevel;
        this.speedLevel = speedLevel;
    }

    public static void encode(SyncSkillsPacket packet, FriendlyByteBuf buffer) {
        buffer.writeInt(packet.healthLevel);
        buffer.writeInt(packet.staminaLevel);
        buffer.writeInt(packet.powerLevel);
        buffer.writeInt(packet.speedLevel);
    }

    public static SyncSkillsPacket decode(FriendlyByteBuf buffer) {
        return new SyncSkillsPacket(
                buffer.readInt(),
                buffer.readInt(),
                buffer.readInt(),
                buffer.readInt()
        );
    }

    public static void handle(SyncSkillsPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null) {
                PlayerDataManager.setHealthLevel(mc.player, packet.healthLevel);
                PlayerDataManager.setStaminaLevel(mc.player, packet.staminaLevel);
                PlayerDataManager.setPowerLevel(mc.player, packet.powerLevel);
                PlayerDataManager.setAvoidanceLevel(mc.player, packet.speedLevel);
            }
        });
        context.setPacketHandled(true);
    }
}