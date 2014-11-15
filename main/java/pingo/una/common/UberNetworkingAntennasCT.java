package pingo.una.common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class UberNetworkingAntennasCT extends CreativeTabs {

	public UberNetworkingAntennasCT(String lable) {
		super(lable);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getTabIconItem() {
		return Item.getItemFromBlock(UberNetworkingAntennas.antennaController);
	}

}
