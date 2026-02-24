package net.Wekston.player_skills.Stamina.registry;
import net.Wekston.player_skills.PlayerSkillsMod;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class StaminaAttributes {

    public static final DeferredRegister<Attribute> ATTRIBUTES
            = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, PlayerSkillsMod.MODID);

    public static final RegistryObject<Attribute> MAX_STAMINA = ATTRIBUTES.register("max_stamina",
            () -> new RangedAttribute("attribute.player_skills.max_stamina", 20, 0.0D, 1024.0D).setSyncable(true));

    public static final RegistryObject<Attribute> SLOW_CLIMB_SPEED = ATTRIBUTES.register("slow_climb_speed",
            () -> new RangedAttribute("attribute.player_skills.slow_climb_speed", 0.4D, 0.0D, 1.0D).setSyncable(true));

    public static final RegistryObject<Attribute> STAMINA_REGEN = ATTRIBUTES.register("stamina_regen",
            () -> new RangedAttribute("attribute.player_skills.stamina_regen", 1.0D, 0.0D, 1024.0D).setSyncable(true));

    public static final RegistryObject<Attribute> STAMINA_USAGE = ATTRIBUTES.register("stamina_usage",
            () -> new RangedAttribute("attribute.player_skills.stamina_usage", 1.0D, 0.0D, 1024.0D).setSyncable(true));

    public static final RegistryObject<Attribute> CURRENT_STAMINA = ATTRIBUTES.register("current_stamina",
            () -> new RangedAttribute("attribute.player_skills.current_stamina", 0.0D, 0.0D, 1024.0D).setSyncable(true));

    public static final RegistryObject<Attribute> SPRINT_SPEED = ATTRIBUTES.register("sprint_speed",
            () -> new RangedAttribute("attribute.player_skills.sprint_speed", 1.0D, 0.0D, 1024.0D).setSyncable(true));

}