package net.Wekston.player_skills.PlayerData;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class PlayerDataManager {
    public static final String SKILLS_KEY = "player_skills";

    @OnlyIn(Dist.CLIENT)
    private static PlayerLevelsManager clientSkillsCache;

    public static PlayerLevelsManager getPlayerSkills(Player player) {
        if (player.level().isClientSide) {
            if (clientSkillsCache == null) {
                clientSkillsCache = new PlayerLevelsManager();
                clientSkillsCache.setHealthLevel(1);
                clientSkillsCache.setStaminaLevel(1);
                clientSkillsCache.setPowerLevel(1);
                clientSkillsCache.setAvoidanceLevel(1);
            }
            return clientSkillsCache;
        } else {
            CompoundTag persistentData = player.getPersistentData();

            PlayerLevelsManager skills = new PlayerLevelsManager();

            if (persistentData.contains(SKILLS_KEY)) {
                skills.loadFromNBT(persistentData.getCompound(SKILLS_KEY));
            } else {
                CompoundTag skillsData = skills.saveToNBT();
                persistentData.put(SKILLS_KEY, skillsData);
            }

            return skills;
        }
    }

    public static void savePlayerSkills(Player player, PlayerLevelsManager skills) {
        if (player.level().isClientSide) {
            clientSkillsCache = skills;
        } else {
            CompoundTag persistentData = player.getPersistentData();
            persistentData.put(SKILLS_KEY, skills.saveToNBT());
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void syncFromServer(int health, int stamina, int power, int avoidance) {
        if (clientSkillsCache == null) {
            clientSkillsCache = new PlayerLevelsManager();
        }
        clientSkillsCache.setHealthLevel(health);
        clientSkillsCache.setStaminaLevel(stamina);
        clientSkillsCache.setPowerLevel(power);
        clientSkillsCache.setAvoidanceLevel(avoidance);

    }

    public static int getHealthLevel(Player player) {
        int level = getPlayerSkills(player).getHealthLevel();
        return level;
    }

    public static void setHealthLevel(Player player, int level) {
        PlayerLevelsManager skills = getPlayerSkills(player);
        skills.setHealthLevel(level);
        savePlayerSkills(player, skills);
    }

    public static void addHealthLevel(Player player, int amount) {
        PlayerLevelsManager skills = getPlayerSkills(player);
        skills.addHealthLevel(amount);
        savePlayerSkills(player, skills);
    }

    public static int getTotalLevels(Player player) {
        int total = getPlayerSkills(player).getTotalLevels();
        return total;
    }

    public static int getStaminaLevel(Player player) {
        int level = getPlayerSkills(player).getStaminaLevel();
        return level;
    }
    public static void setStaminaLevel(Player player, int level) {
        PlayerLevelsManager skills = getPlayerSkills(player);
        skills.setStaminaLevel(level);
        savePlayerSkills(player, skills);
    }

    public static void addStaminaLevel(Player player, int amount) {
        PlayerLevelsManager skills = getPlayerSkills(player);
        skills.addStaminaLevel(amount);
        savePlayerSkills(player, skills);
    }
    public static void setPowerLevel(Player player, int level) {
        PlayerLevelsManager skills = getPlayerSkills(player);
        skills.setPowerLevel(level);
        savePlayerSkills(player, skills);
    }

    public static int getPowerLevel(Player player) {
        int level = getPlayerSkills(player).getPowerLevel();
        return level;
    }

    public static void addPowerLevel(Player player, int amount) {
        PlayerLevelsManager skills = getPlayerSkills(player);
        skills.addPowerLevel(amount);
        savePlayerSkills(player, skills);
    }
    public static void setAvoidanceLevel(Player player, int level) {
        PlayerLevelsManager skills = getPlayerSkills(player);
        skills.setAvoidanceLevel(level);
        savePlayerSkills(player, skills);
    }
    public static int getAvoidanceLevel(Player player) {
        int level = getPlayerSkills(player).getAvoidanceLevel();
        return level;
    }
    public static void addAvoidanceLevel(Player player, int amount) {
        PlayerLevelsManager skills = getPlayerSkills(player);
        skills.addAvoidanceLevel(amount);
        savePlayerSkills(player, skills);
    }
}