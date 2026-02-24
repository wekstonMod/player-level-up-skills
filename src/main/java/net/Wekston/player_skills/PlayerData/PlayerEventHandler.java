package net.Wekston.player_skills.PlayerData;

import net.Wekston.player_skills.PlayerSkillsMod;
import net.Wekston.player_skills.network.NetworkHandler;
import net.Wekston.player_skills.network.packets.SyncSkillsPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PlayerSkillsMod.MODID)
public class PlayerEventHandler {

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            CompoundTag persistentData = serverPlayer.getPersistentData();
            PlayerLevelsManager skills;
            if (!persistentData.contains(PlayerDataManager.SKILLS_KEY)) {
                skills = new PlayerLevelsManager();
                } else {
                skills = new PlayerLevelsManager();
                CompoundTag skillsTag = persistentData.getCompound(PlayerDataManager.SKILLS_KEY);
                skills.loadFromNBT(skillsTag);
            }
            persistentData.put(PlayerDataManager.SKILLS_KEY, skills.saveToNBT());
            syncPlayerSkills(serverPlayer);
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            syncPlayerSkills(serverPlayer);
        }
    }

    @SubscribeEvent
    public static void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            syncPlayerSkills(serverPlayer);
        }
    }
    @SubscribeEvent
    public static void onPlayerSave(PlayerEvent.SaveToFile event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            PlayerLevelsManager skills = PlayerDataManager.getPlayerSkills(serverPlayer);
            CompoundTag persistentData = serverPlayer.getPersistentData();
            persistentData.put(PlayerDataManager.SKILLS_KEY, skills.saveToNBT());
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            Player original = event.getOriginal();
            Player newPlayer = event.getEntity();
            if (original instanceof ServerPlayer oldPlayer && newPlayer instanceof ServerPlayer newServerPlayer) {
                CompoundTag oldData = oldPlayer.getPersistentData();
                if (oldData.contains(PlayerDataManager.SKILLS_KEY)) {
                    CompoundTag skillsData = oldData.getCompound(PlayerDataManager.SKILLS_KEY);
                    newServerPlayer.getPersistentData().put(PlayerDataManager.SKILLS_KEY, skillsData);
                    }
            }
        }
    }

    public static void syncPlayerSkills(ServerPlayer player) {
        if (player.connection == null) {
            return;
        }
        PlayerLevelsManager skills = PlayerDataManager.getPlayerSkills(player);
        NetworkHandler.sendToClient(player, new SyncSkillsPacket(
                skills.getHealthLevel(),
                skills.getStaminaLevel(),
                skills.getPowerLevel(),
                skills.getAvoidanceLevel()
        ));
    }
}