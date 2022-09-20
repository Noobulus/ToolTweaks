package mod.noobulus.tooltweaks.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class MixinItemStack {

	@Shadow
	private NbtCompound nbt;

	@Shadow
	public abstract Item getItem();

	// this is shamelessly stolen from yttr
	@Inject(at=@At("RETURN"), method="getMaxDamage", cancellable=true)
	public void getMaxDamage(CallbackInfoReturnable<Integer> ci) {
		if (nbt != null && nbt.contains("tooltweaks:DurabilityBonus")) {
			// each level of durability bonus is +10%
			ci.setReturnValue((ci.getReturnValueI() * (10 + (nbt.getInt("tooltweaks:DurabilityBonus")))) / 10);
		}
	}

	@Inject(at=@At("RETURN"), method="getMiningSpeedMultiplier", cancellable=true)
	public void getMiningSpeedMultiplier(BlockState state, CallbackInfoReturnable<Float> ci) {
		if (nbt != null && nbt.contains("tooltweaks:MiningSpeedBonus")) {
			// each level is +1 to speed (janky minecraft code nonwithstanding)
			ci.setReturnValue(ci.getReturnValueF() + (this.getItem().isSuitableFor(state) ? nbt.getFloat("tooltweaks:MiningSpeedBonus") : 0F));
		}
	}
}
