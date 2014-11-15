package pingo.una.common;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class BlockSatelliteLaserFrame extends Block {

	protected BlockSatelliteLaserFrame(Material material) {
		super(material);
	}

	private IIcon sides;
	private IIcon bottom;
	
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iiconRegister) {
		this.blockIcon = iiconRegister.registerIcon(UberNetworkingAntennas.MODID + ":top");
		this.bottom = iiconRegister.registerIcon(UberNetworkingAntennas.MODID + ":laserFrameBottom");
		this.sides = iiconRegister.registerIcon(UberNetworkingAntennas.MODID + ":laserFrame");
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
    {
		return this.getIcon(side, world.getBlockMetadata(x, y, z));
    }
	
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int metadata)
	{

		if (side == 0)
    		return this.blockIcon;
		if (side == 1)
			return this.bottom;
		return this.sides;
		
	}
	
}
