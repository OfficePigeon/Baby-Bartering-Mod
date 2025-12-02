package fun.wich;

import fun.wich.mixin.BabyBarter_LootTablesMixin;
import net.fabricmc.api.ModInitializer;

import net.minecraft.util.Identifier;

public class BarterWithBabies implements ModInitializer {
	public static final String MOD_ID = "wich";
	public static final Identifier BABY_PIGLIN_BARTERING_GAMEPLAY = BabyBarter_LootTablesMixin.registerLootTable(Identifier.of(MOD_ID, "gameplay/baby_piglin_bartering"));
	@Override
	public void onInitialize() { }
}