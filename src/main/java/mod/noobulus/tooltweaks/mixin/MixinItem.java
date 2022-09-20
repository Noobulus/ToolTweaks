package mod.noobulus.tooltweaks.mixin;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class MixinItem {

	// technically i did it first, i just pr'd to yttr
	@Inject(at=@At("RETURN"), method="getItemBarStep", cancellable=true)
	public void getItemBarStep(ItemStack stack, CallbackInfoReturnable<Integer> ci) {
		if (((AccessorItemStack) (Object) stack).getNbt() != null && ((AccessorItemStack) (Object) stack).getNbt().contains("tooltweaks:DurabilityBonus")) {
			ci.setReturnValue(Math.round(13.0F - (float)stack.getDamage() * 13.0F / (float)stack.getMaxDamage()));
		}
	}

	@Inject(at=@At("RETURN"), method="getItemBarColor", cancellable=true)
	public void getItemBarColor(ItemStack stack, CallbackInfoReturnable<Integer> ci) {
		if (((AccessorItemStack) (Object) stack).getNbt() != null && ((AccessorItemStack) (Object) stack).getNbt().contains("tooltweaks:DurabilityBonus")) {
			float f = Math.max(0.0F, ((float)stack.getMaxDamage() - (float)stack.getDamage()) / (float)stack.getMaxDamage());
			ci.setReturnValue(MathHelper.hsvToRgb(f / 3.0F, 1.0F, 1.0F));
		}
	}
}
