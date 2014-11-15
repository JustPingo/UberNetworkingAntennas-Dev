package pingo.una.common;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class BlockAntennaSupport extends Block {

	protected BlockAntennaSupport(Material material) {
		super(material);
	}
	
	private IIcon sides;
	
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iiconRegister) {
		this.blockIcon = iiconRegister.registerIcon(UberNetworkingAntennas.MODID + ":antennaSupportTop");
		this.sides = iiconRegister.registerIcon(UberNetworkingAntennas.MODID + ":antennaSupport");
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
    {
		return this.getIcon(side, world.getBlockMetadata(x, y, z));
    }
	
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int metadata)
	{
		
		if (side == 1 || side == 0)
    		return this.blockIcon;
		return this.sides;
		
	}

}
