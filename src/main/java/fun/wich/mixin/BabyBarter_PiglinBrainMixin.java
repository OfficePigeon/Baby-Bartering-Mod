package fun.wich.mixin;

import fun.wich.BarterWithBabies;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(PiglinBrain.class)
public abstract class BabyBarter_PiglinBrainMixin {
	@Inject(method="getBarteredItem", at=@At("HEAD"), cancellable=true)
	private static void Inject_getBarteredItem_AllowBabyPiglinBartering(PiglinEntity piglin, CallbackInfoReturnable<List<ItemStack>> cir) {
		if (piglin.isBaby()) {
			MinecraftServer server = piglin.getEntityWorld().getServer();
			if (server == null) return;
			LootTable lootTable = server.getLootManager().getLootTable(BarterWithBabies.BABY_PIGLIN_BARTERING_GAMEPLAY);
			cir.setReturnValue(lootTable.generateLoot(new LootContextParameterSet.Builder((ServerWorld)piglin.getWorld()).add(LootContextParameters.THIS_ENTITY, piglin).build(LootContextTypes.BARTER)));
		}
	}
	@Redirect(method="consumeOffHandItem", at=@At(value="INVOKE", target="Lnet/minecraft/entity/mob/PiglinEntity;isAdult()Z"))
	private static boolean Redirect_consumeOffHandItem_AllowBabyPiglinBartering(PiglinEntity instance) { return true; }
	@Redirect(method="isWillingToTrade", at=@At(value="INVOKE", target="Lnet/minecraft/entity/mob/PiglinEntity;isAdult()Z"))
	private static boolean Redirect_isWillingToTrade_AllowBabyPiglinBartering(PiglinEntity instance) { return true; }
}
