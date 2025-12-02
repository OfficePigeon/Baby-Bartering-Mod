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
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(PiglinBrain.class)
public abstract class BabyBarter_PiglinBrainMixin {
	@Shadow private static void doBarter(PiglinEntity entity, List<ItemStack> items) { }
	@Shadow private static boolean acceptsForBarter(ItemStack stack) { return false; }
	@Unique
	private static List<ItemStack> GetBarteredItemForBabies(PiglinEntity entity) {
		MinecraftServer server = entity.getEntityWorld().getServer();
		if (server == null) return new ArrayList<>();
		LootTable lootTable = server.getLootManager().getLootTable(BarterWithBabies.BABY_PIGLIN_BARTERING_GAMEPLAY);
		return lootTable.generateLoot((new LootContextParameterSet.Builder((ServerWorld)entity.getEntityWorld())).add(LootContextParameters.THIS_ENTITY, entity).build(LootContextTypes.BARTER));
	}
	@Inject(method="consumeOffHandItem", at=@At("HEAD"), cancellable=true)
	private static void Inject_consumeOffHandItem_AllowBabyPiglinBartering(PiglinEntity piglin, boolean barter, CallbackInfo ci) {
		if (barter && piglin.isBaby() && acceptsForBarter(piglin.getStackInHand(Hand.OFF_HAND))) {
			piglin.setStackInHand(Hand.OFF_HAND, ItemStack.EMPTY);
			doBarter(piglin, GetBarteredItemForBabies(piglin));
			ci.cancel();
		}
	}
	//Remove the adult-only restriction from bartering
	@Redirect(method="isWillingToTrade", at=@At(value="INVOKE", target="Lnet/minecraft/entity/mob/PiglinEntity;isAdult()Z"))
	private static boolean Redirect_isWillingToTrade_AllowBabyPiglinBartering(PiglinEntity instance) { return true; }
}
