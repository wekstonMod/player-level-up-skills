package net.Wekston.player_skills;

import net.Wekston.player_skills.Stamina.registry.StaminaAttributes;
import net.Wekston.player_skills.Stamina.registry.StaminaCapability;
import net.Wekston.player_skills.Stamina.registry.StaminaConfig;
import net.Wekston.player_skills.gui.RendererGui;
import net.Wekston.player_skills.network.NetworkHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.spongepowered.asm.mixin.MixinEnvironment;


@Mod(PlayerSkillsMod.MODID)
public class PlayerSkillsMod
{
    public static final String MODID = "player_skills";

    public PlayerSkillsMod(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new RendererGui());
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, StaminaConfig.COMMON_SPEC);
        StaminaAttributes.ATTRIBUTES.register(modEventBus);
        modEventBus.addListener(this::attachAttributes);
        modEventBus.addListener(this::registerCaps);

        modEventBus.addListener(this::commonSetup);
        if (FMLEnvironment.dist == Dist.CLIENT) {
            MixinEnvironment.getDefaultEnvironment().setObfuscationContext("searge");
        }

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.addGenericListener(net.minecraft.world.entity.Entity.class, this::attachEntityCaps);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(NetworkHandler::register);
    }
    private void attachAttributes(EntityAttributeModificationEvent event) {
        if (!event.has(net.minecraft.world.entity.EntityType.PLAYER, StaminaAttributes.MAX_STAMINA.get())) {
            event.add(net.minecraft.world.entity.EntityType.PLAYER, StaminaAttributes.MAX_STAMINA.get());
        }
        if (!event.has(net.minecraft.world.entity.EntityType.PLAYER, StaminaAttributes.SLOW_CLIMB_SPEED.get())) {
            event.add(net.minecraft.world.entity.EntityType.PLAYER, StaminaAttributes.SLOW_CLIMB_SPEED.get());
        }
        if (!event.has(net.minecraft.world.entity.EntityType.PLAYER, StaminaAttributes.STAMINA_REGEN.get())) {
            event.add(net.minecraft.world.entity.EntityType.PLAYER, StaminaAttributes.STAMINA_REGEN.get());
        }
        if (!event.has(net.minecraft.world.entity.EntityType.PLAYER, StaminaAttributes.STAMINA_USAGE.get())) {
            event.add(net.minecraft.world.entity.EntityType.PLAYER, StaminaAttributes.STAMINA_USAGE.get());
        }

        event.add(net.minecraft.world.entity.EntityType.PLAYER, StaminaAttributes.CURRENT_STAMINA.get());
        event.add(net.minecraft.world.entity.EntityType.PLAYER, StaminaAttributes.SPRINT_SPEED.get());
    }

    private void registerCaps(RegisterCapabilitiesEvent event) {
        event.register(StaminaCapability.class);
    }

    public void attachEntityCaps(AttachCapabilitiesEvent<net.minecraft.world.entity.Entity> event) {
        if (event.getObject() instanceof net.minecraft.world.entity.player.Player) {
            if (!event.getObject().getCapability(StaminaCapability.INSTANCE).isPresent()) {
                event.addCapability(new ResourceLocation(MODID, "stamina"), new StaminaCapability.Provider());
            }
        }
    }
}
