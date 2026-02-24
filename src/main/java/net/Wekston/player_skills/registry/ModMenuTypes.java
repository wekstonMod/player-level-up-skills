package net.Wekston.player_skills.registry;


import net.Wekston.player_skills.PlayerSkillsMod;
import net.Wekston.player_skills.menu.SkillsContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {

    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, PlayerSkillsMod.MODID);

    public static final RegistryObject<MenuType<SkillsContainer>> SKILLS_MENU =
            MENUS.register("skills_menu",
                    () -> IForgeMenuType.create((windowId, inv, data) ->
                            new SkillsContainer(windowId, inv, inv.player)));
}