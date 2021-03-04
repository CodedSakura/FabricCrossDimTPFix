package eu.codedsakura.fabriccrossdimtpfix.mixin;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.network.packet.s2c.play.EntityStatusEffectS2CPacket;
import net.minecraft.network.packet.s2c.play.ExperienceBarUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class TPMixin {

    @Inject(method = "teleport", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerInteractionManager;setWorld(Lnet/minecraft/server/world/ServerWorld;)V"))
    void teleportFix(ServerWorld targetWorld, double x, double y, double z, float yaw, float pitch, CallbackInfo ci) {
        ServerPlayerEntity self = (ServerPlayerEntity) (Object) this;

        for (StatusEffectInstance statusEffectInstance : self.getStatusEffects())
            self.networkHandler.sendPacket(new EntityStatusEffectS2CPacket(self.getEntityId(), statusEffectInstance));

        self.networkHandler.sendPacket(new ExperienceBarUpdateS2CPacket(self.experienceProgress, self.totalExperience, self.experienceLevel));
    }
}
