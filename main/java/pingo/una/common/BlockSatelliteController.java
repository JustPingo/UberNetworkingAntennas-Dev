package pingo.una.common;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockSatelliteController extends BlockContainer {

	protected BlockSatelliteController(Material material) {
		super(material);
	}
	
	private IIcon sides;
	private IIcon top;
	
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iiconRegister) {
		this.blockIcon = iiconRegister.registerIcon(UberNetworkingAntennas.MODID + ":top");
		this.sides = iiconRegister.registerIcon(UberNetworkingAntennas.MODID + ":satelliteControler");
		this.top = iiconRegister.registerIcon(UberNetworkingAntennas.MODID + ":satelliteControlerTop");
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
			return this.top;
		return this.sides;
		
	}
	
	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntitySatelliteController();
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
		TileEntity te = world.getTileEntity(x, y, z);
		if (te instanceof TileEntitySatelliteController) ((TileEntitySatelliteController) te).onBlockActivated(world, player);
		return true;
	}
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		TileEntity te = world.getTileEntity(x, y, z);
		if (te instanceof TileEntitySatelliteController) ((TileEntitySatelliteController) te).onNeighborBlockChange();
	}

}
