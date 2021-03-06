package pingo.una.common;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockSatelliteSolarPanel extends BlockContainer {

	protected BlockSatelliteSolarPanel(Material material) {
		super(material);
	}

	private IIcon sides;
	
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iiconRegister) {
		this.blockIcon = iiconRegister.registerIcon(UberNetworkingAntennas.MODID + ":topSolar");
		this.sides = iiconRegister.registerIcon(UberNetworkingAntennas.MODID + ":solarPanel");
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

	@Override
	public TileEntity createNewTileEntity(World world, int p_149915_2_) {
		return new TileEntitySatelliteSolarPanel();
	}
	
}
