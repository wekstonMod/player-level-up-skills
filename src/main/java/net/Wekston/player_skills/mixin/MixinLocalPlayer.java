package net.Wekston.player_skills.mixin;

import net.Wekston.player_skills.Stamina.registry.StaminaCapability;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(LocalPlayer.class)
public class MixinLocalPlayer {

    @Inject(method = "tick", at = @At("HEAD"),
            remap = false,
            require = 0)
    private void preventSprintingWhenExhausted(CallbackInfo ci) {

        LocalPlayer player = (LocalPlayer) (Object) this;

        player.getCapability(StaminaCapability.INSTANCE).ifPresent(cap -> {
            if (cap.stamina <= 0 && player.isSprinting()) {
                player.setSprinting(false);
            }
        });
    }
}