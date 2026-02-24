package net.Wekston.player_skills.menu;

import net.Wekston.player_skills.registry.ModMenuTypes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class SkillsContainer extends AbstractContainerMenu {

    private final Player player;

    public SkillsContainer(int id, Inventory playerInventory, Player player) {
        super(ModMenuTypes.SKILLS_MENU.get(), id);
        this.player = player;
    }

    @Override
    public ItemStack quickMoveStack(Player p_38941_, int p_38942_) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    public Player getPlayer() {
        return player;
    }
}