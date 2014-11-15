package pingo.una.common;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBlockSatelliteBattery extends ItemBlock {

	public ItemBlockSatelliteBattery(Block block) {
		super(block);
	}
		
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List info, boolean par4) {
		super.addInformation(stack, player, info, par4);
		if (stack.stackTagCompound != null) {
			if (stack.stackTagCompound.hasKey("energyStored")) {
				info.add("Energy stored : " + stack.stackTagCompound.getInteger("energyStored"));
			}
		}
	}

}
