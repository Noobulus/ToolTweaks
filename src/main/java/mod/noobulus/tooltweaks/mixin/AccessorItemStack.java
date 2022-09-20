package mod.noobulus.tooltweaks.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemStack.class)
public interface AccessorItemStack {
	@Accessor
	NbtCompound getNbt();

}
