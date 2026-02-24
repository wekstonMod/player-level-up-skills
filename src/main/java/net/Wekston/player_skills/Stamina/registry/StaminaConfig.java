package net.Wekston.player_skills.Stamina.registry;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;
public class StaminaConfig {

    public static final Common COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;

    static {
        final Pair<Common, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = commonSpecPair.getRight();
        COMMON = commonSpecPair.getLeft();
    }

    public static class Common {
        public final ForgeConfigSpec.DoubleValue recoveryPerTick;
        public final ForgeConfigSpec.DoubleValue recoveryRestMult;
        public final ForgeConfigSpec.DoubleValue recoveryClimbMult;
        public final ForgeConfigSpec.IntValue recoveryDelay;
        public final ForgeConfigSpec.IntValue exhaustionCooldownDuration;

        public Common(ForgeConfigSpec.Builder builder) {
            recoveryPerTick = builder.comment("Stamina recovered per tick").defineInRange("recoveryPerTick", 0.45, 0.0, 100.0);
            recoveryRestMult = builder.comment("Multiplier for recovery when standing completely still").defineInRange("recoveryRestMult", 1.45, 1.0, 10.0);
            recoveryClimbMult = builder.comment("Multiplier for recovery when hanging on a ladder/vine (slow climbing or not moving)").defineInRange("recoveryClimbMult", 0.2, 0.0, 10.0);
            recoveryDelay = builder.comment("Ticks before stamina starts regenerating after action (20 ticks = 1 sec)").defineInRange("recoveryDelay", 50, 0, 2000);
            builder.push("Exhaustion Penalties");
            exhaustionCooldownDuration = builder.comment("Ticks the penalties persist after stamina regenerates above 0 (0 to disable)").defineInRange("exhaustionCooldownDuration", 60, 0, 12000);
            builder.pop();
        }
    }
}