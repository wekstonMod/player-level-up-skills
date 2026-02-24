package net.Wekston.player_skills.PlayerData;

import net.minecraft.nbt.CompoundTag;

public class PlayerLevelsManager {
    private int healthLevel = 1;
    private int staminaLevel = 1;
    private int powerLevel = 1;
    private int avoidanceLevel = 1;

    public int getHealthLevel() { return healthLevel; }
    public void setHealthLevel(int level) { this.healthLevel = level; }
    public void addHealthLevel(int amount) { this.healthLevel += amount; }

    public int getStaminaLevel() { return staminaLevel; }
    public void setStaminaLevel(int level) { this.staminaLevel = level; }
    public void addStaminaLevel(int amount) { this.staminaLevel += amount; }

    public int getPowerLevel() { return powerLevel; }
    public void setPowerLevel(int level) { this.powerLevel = level; }
    public void addPowerLevel(int amount) { this.powerLevel += amount; }

    public int getAvoidanceLevel() { return avoidanceLevel; }
    public void setAvoidanceLevel(int level) { this.avoidanceLevel = level; }
    public void addAvoidanceLevel(int amount) { this.avoidanceLevel += amount; }

    public int getTotalLevels() {
        return healthLevel + staminaLevel + powerLevel + avoidanceLevel;
    }

    public CompoundTag saveToNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("healthLevel", healthLevel);
        tag.putInt("staminaLevel", staminaLevel);
        tag.putInt("powerLevel", powerLevel);
        tag.putInt("avoidanceLevel", avoidanceLevel);
        return tag;
    }


    public void loadFromNBT(CompoundTag tag) {
        if (tag.contains("healthLevel")) {
            this.healthLevel = tag.getInt("healthLevel");
        }
        if (tag.contains("staminaLevel")) {
            this.staminaLevel = tag.getInt("staminaLevel");
        }
        if (tag.contains("powerLevel")) {
            this.powerLevel = tag.getInt("powerLevel");
        }
        if (tag.contains("avoidanceLevel")) {
            this.avoidanceLevel = tag.getInt("avoidanceLevel");
        }
    }
}