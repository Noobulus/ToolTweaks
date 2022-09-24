package mod.noobulus.tooltweaks.mixin;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class MixinItem {

	// technically i did it first, i just pr'd to yttr
	// ...and then una fuckin' schooled me on a better way to do it lmao

	@Shadow @Final @Mutable
	private int maxDamage;

	private Integer oldMaxDamage;

	@Inject(at=@At("HEAD"), method={"getItemBarStep", "getItemBarColor"}, cancellable=true)
	public void correctDuraForBonus(ItemStack stack, CallbackInfoReturnable<Integer> ci) {
		if (stack.hasNbt() && stack.getNbt().contains("tooltweaks:DurabilityBonus")) {
			oldMaxDamage = this.maxDamage;
			this.maxDamage = stack.getMaxDamage();
		}
	}

	@Inject(at=@At("RETURN"), method={"getItemBarStep", "getItemBarColor"}, cancellable=true)
	public void uncorrectDuraForBonus(ItemStack stack, CallbackInfoReturnable<Integer> ci) {
		if (oldMaxDamage != null) {
			this.maxDamage = oldMaxDamage;
			oldMaxDamage = null;
		}
	}
}
