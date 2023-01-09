package mod.noobulus.tooltweaks.mixin;

import com.google.common.collect.ImmutableMultimap;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(ArmorItem.class)
public class MixinArmorItem {
	@Shadow @Final
	private static final UUID[] MODIFIERS = new UUID[]{UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"), UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"), UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")};

	private ArmorMaterial material;
	private int slotID;
	private float penalty;

	@Inject(method = "<init>(Lnet/minecraft/item/ArmorMaterial;Lnet/minecraft/entity/EquipmentSlot;Lnet/minecraft/item/Item$Settings;)V", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableMultimap$Builder;build()Lcom/google/common/collect/ImmutableMultimap;"))
	private void grabLocals(ArmorMaterial armorMaterial, EquipmentSlot equipmentSlot, Item.Settings settings, CallbackInfo ci) {
		material = armorMaterial;
		if (armorMaterial == ArmorMaterials.NETHERITE) {
			slotID = equipmentSlot.getEntitySlotId();
			penalty = -0.05f;
			/*if (equipmentSlot.getEntitySlotId() == 2) {
				penalty = -0.10f;
			}*/
		}
	}

	@Redirect(method = "<init>(Lnet/ minecraft/item/ArmorMaterial;Lnet/minecraft/entity/EquipmentSlot;Lnet/minecraft/item/Item$Settings;)V", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableMultimap$Builder;build()Lcom/google/common/collect/ImmutableMultimap;"))
	private ImmutableMultimap addSpeedModifier(ImmutableMultimap.Builder instance) {
		if (material == ArmorMaterials.NETHERITE) {
			UUID uUID = MODIFIERS[slotID];
			instance.put(EntityAttributes.GENERIC_MOVEMENT_SPEED, new EntityAttributeModifier(uUID, "Armor movement penalty", penalty, EntityAttributeModifier.Operation.MULTIPLY_BASE));
		}
		return instance.build();
	}
}
