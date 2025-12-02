package fun.wich.mixin;

import net.minecraft.loot.LootTables;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LootTables.class)
public interface BabyBarter_LootTablesMixin {
	@Invoker("registerLootTable") static Identifier registerLootTable(Identifier id) { return null; }
}
