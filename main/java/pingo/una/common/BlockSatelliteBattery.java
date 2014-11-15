package pingo.una.common;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockSatelliteBattery extends BlockContainer {

	protected BlockSatelliteBattery(Material material) {
		super(material);
	}

	private IIcon[] sides;
	
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iiconRegister) {
		this.blockIcon = iiconRegister.registerIcon(UberNetworkingAntennas.MODID + ":top");
		this.sides = new IIcon[11];
		this.sides[0] = iiconRegister.registerIcon(UberNetworkingAntennas.MODID + ":battery");
		this.sides[1] = iiconRegister.registerIcon(UberNetworkingAntennas.MODID + ":battery1");
		this.sides[2] = iiconRegister.registerIcon(UberNetworkingAntennas.MODID + ":battery2");
		this.sides[3] = iiconRegister.registerIcon(UberNetworkingAntennas.MODID + ":battery3");
		this.sides[4] = iiconRegister.registerIcon(UberNetworkingAntennas.MODID + ":battery4");
		this.sides[5] = iiconRegister.registerIcon(UberNetworkingAntennas.MODID + ":battery5");
		this.sides[6] = iiconRegister.registerIcon(UberNetworkingAntennas.MODID + ":battery6");
		this.sides[7] = iiconRegister.registerIcon(UberNetworkingAntennas.MODID + ":battery7");
		this.sides[8] = iiconRegister.registerIcon(UberNetworkingAntennas.MODID + ":battery8");
		this.sides[9] = iiconRegister.registerIcon(UberNetworkingAntennas.MODID + ":battery9");
		this.sides[10] = iiconRegister.registerIcon(UberNetworkingAntennas.MODID + ":batteryFull");
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
    {
		
		if (side == 1 || side == 0)
    		return this.blockIcon;
		
		TileEntity teTemp = world.getTileEntity(x, y, z);
		if (teTemp instanceof TileEntitySatelliteBattery) {
			TileEntitySatelliteBattery te = (TileEntitySatelliteBattery) teTemp;
			int index = (int) te.energyStored/(te.energyStorage/10);
			return sides[index];
		}
		return this.getIcon(side, world.getBlockMetadata(x, y, z));
		
    }
	
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int metadata)
	{

		if (side == 1 || side == 0)
    		return this.blockIcon;
		return this.sides[10];
		
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack) {
		if (!world.isRemote && stack.stackTagCompound != null) {
			if (stack.stackTagCompound.hasKey("energyStored")) {
				TileEntity teTemp = world.getTileEntity(x, y, z);
				if (teTemp instanceof TileEntitySatelliteBattery) {
					TileEntitySatelliteBattery te = (TileEntitySatelliteBattery) teTemp;
					te.energyStored = stack.stackTagCompound.getInteger("energyStored");
				}
			}
		}
		world.markBlockForUpdate(x, y, z);
	    super.onBlockPlacedBy(world, x, y, z, player, stack);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int p_149915_2_) {
		return new TileEntitySatelliteBattery();
	}
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		TileEntity te = world.getTileEntity(x, y, z);
		if (te instanceof TileEntitySatelliteBattery) ((TileEntitySatelliteBattery) te).onNeighborBlockChange();
	}
	
	public void onBlockDestroyed(World world, int x, int y, int z) {
		TileEntity te = world.getTileEntity(x, y, z);
		if (te instanceof TileEntitySatelliteBattery) ((TileEntitySatelliteBattery) te).kill();
	}
	
	public void onBlockDestroyedByExplosion(World world, int x, int y, int z, Explosion explosion) {
		this.onBlockDestroyed(world, x, y, z);
	}
	
	public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int meta) {
		this.onBlockDestroyed(world, x, y, z);
	}
	
	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
		ArrayList<ItemStack> retour = new ArrayList<ItemStack>();
		ItemStack stack = new ItemStack(Item.getItemFromBlock(UberNetworkingAntennas.satelliteBattery), 1);
		TileEntity teTemp = world.getTileEntity(x, y, z);
    	if (teTemp instanceof TileEntitySatelliteBattery) {
    		TileEntitySatelliteBattery te = (TileEntitySatelliteBattery) teTemp;
	      	stack.stackTagCompound = new NBTTagCompound();
	      	stack.stackTagCompound.setInteger("energyStored", te.energyStored);
    	}
    	retour.add(stack);
		return retour;
	}
	
	@Override
	public int quantityDropped(Random par1Random) {
		return 0;
	}
	
}
