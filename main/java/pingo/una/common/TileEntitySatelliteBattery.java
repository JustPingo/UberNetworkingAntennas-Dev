package pingo.una.common;

import java.util.Iterator;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;


public class TileEntitySatelliteBattery extends TileEntitySatellitePart implements IPeripheral {

	public int energyStored = 0;
	public int energyStorage = 10000;
	
	private String[] methods = new String[] { "getEnergy", "getCapacity" };
	
	private boolean shouldAddToList;
	private boolean dead;
	
	private int lastIndex;
	
	private TileEntitySatelliteSolarPanel top;
	private TileEntitySatelliteSolarPanel left;
	private TileEntitySatelliteSolarPanel right;
	private TileEntitySatelliteSolarPanel front;
	private TileEntitySatelliteSolarPanel back;
	
	public TileEntitySatelliteBattery() {
		this.shouldAddToList = true;
		this.dead = false;
	}
	
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)

    {

        this.readFromNBT(pkt.func_148857_g());
        
        this.worldObj.markBlockRangeForRenderUpdate(this.xCoord, this.yCoord, this.zCoord, this.xCoord, this.yCoord, this.zCoord);

    }
	
	public Packet getDescriptionPacket()

    {

        NBTTagCompound nbttagcompound = new NBTTagCompound();

        this.writeToNBT(nbttagcompound);

        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 3, nbttagcompound);

    }
	
	@Override
	public void updateEntity() {
		if (this.shouldAddToList && worldObj != null) {
			if (!worldObj.isRemote) {
				UberNetworkingAntennas.batteries.add(this);
				this.onNeighborBlockChange();
			}
			this.shouldAddToList = false;
		}
	}
	
	public void checkMarkForUpdate() {
		if (lastIndex != energyStored/(energyStorage/10)) {
			lastIndex = energyStored/(energyStorage/10);
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
	}
	
	public int addEnergyStored(int toAdd) {
		if (!worldObj.isRemote) {
			if (this.energyStored + toAdd <= this.energyStorage) { this.energyStored += toAdd; checkMarkForUpdate(); return 0; }
			else { int retour = this.energyStored + toAdd - this.energyStorage; this.energyStored = this.energyStorage; checkMarkForUpdate(); return retour; }
		}
		return toAdd;
	}
	
	public int takeEnergyStored(int toTake) {
		if (!worldObj.isRemote) {
			if (this.energyStored - toTake >= 0) { this.energyStored -= toTake; checkMarkForUpdate(); return 0; }
			else { int retour = toTake - this.energyStored; this.energyStored = 0; checkMarkForUpdate(); return retour; }
		}
		return toTake;
	}
	
	@Override
	public void invalidate() {
		if (!worldObj.isRemote) {
			for (Iterator<TileEntitySatelliteBattery> it = UberNetworkingAntennas.batteries.listIterator(); it.hasNext();) {
				TileEntitySatelliteBattery a = it.next();
				if (a == this) { it.remove(); return; }
			}
		}
		this.dead = true;
	}
	
	public boolean isDead() {
		return this.dead;
	}
	
	public void getFromPanels() {
		if (worldObj != null) {
			if (!worldObj.isRemote) {
				int mult = 0;
				if (top != null) { if (top.isDead()) top = null; }
				if (left != null) { if (left.isDead()) left = null; }
				if (right != null) { if (right.isDead()) right = null; }
				if (front != null) { if (front.isDead()) front = null; }
				if (back != null) { if (back.isDead()) back = null; }
				if (worldObj.getWorldTime() <= 14000 && worldObj.getWorldTime() > 1000) {
					if (top != null && worldObj.canBlockSeeTheSky(xCoord, yCoord+2, zCoord)) if (top.connectedBattery == this) mult += top.getSolarLevel();
					if (left != null && worldObj.canBlockSeeTheSky(xCoord-1, yCoord+1, zCoord)) if (left.connectedBattery == this) mult += left.getSolarLevel();
					if (right != null && worldObj.canBlockSeeTheSky(xCoord+1, yCoord+1, zCoord)) if (right.connectedBattery == this) mult += right.getSolarLevel();
					if (front != null && worldObj.canBlockSeeTheSky(xCoord, yCoord+1, zCoord-1)) if (front.connectedBattery == this) mult += front.getSolarLevel();
					if (back != null && worldObj.canBlockSeeTheSky(xCoord, yCoord+1, zCoord+1)) if (back.connectedBattery == this) mult += back.getSolarLevel();
				}
				this.addEnergyStored(300 * mult);
			}
		}
	}
	
	public void readFromNBT(NBTTagCompound nbttag) {
        super.readFromNBT(nbttag);
        
        if (nbttag.hasKey("energyStored")) energyStored = nbttag.getInteger("energyStored");
    }

    public void writeToNBT(NBTTagCompound nbttag) {
        super.writeToNBT(nbttag);
        
        nbttag.setInteger("energyStored", energyStored);
    }

	public void onNeighborBlockChange() {
		if (!worldObj.isRemote) {
			TileEntity te;
			te = worldObj.getTileEntity(xCoord, yCoord+1, zCoord);
			if (te instanceof TileEntitySatelliteSolarPanel) { this.top = (TileEntitySatelliteSolarPanel) te; this.top.connectedBattery = this; }
			else this.top = null;
			te = worldObj.getTileEntity(xCoord, yCoord, zCoord+1);
			if (te instanceof TileEntitySatelliteSolarPanel) { this.front = (TileEntitySatelliteSolarPanel) te; this.front.connectedBattery = this; }
			else this.front = null;
			te = worldObj.getTileEntity(xCoord, yCoord, zCoord-1);
			if (te instanceof TileEntitySatelliteSolarPanel) { this.back = (TileEntitySatelliteSolarPanel) te; this.back.connectedBattery = this; }
			else this.back = null;
			te = worldObj.getTileEntity(xCoord+1, yCoord, zCoord);
			if (te instanceof TileEntitySatelliteSolarPanel) { this.right = (TileEntitySatelliteSolarPanel) te; this.right.connectedBattery = this; }
			else this.right = null;
			te = worldObj.getTileEntity(xCoord-1, yCoord, zCoord);
			if (te instanceof TileEntitySatelliteSolarPanel) { this.left = (TileEntitySatelliteSolarPanel) te; this.left.connectedBattery = this; }
			else this.left = null;
		}
	}

	public void kill() {
		this.dead = true;
	}

	@Override
	public String getType() {
		return "satellite_battery";
	}

	@Override
	public String[] getMethodNames() {
		return methods;
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
		
		switch (method) {
			
			// getEnergy()
			case 0: return new Double[] { (double) this.energyStored };
			
			// getCapacity()
			case 1: return new Double[] { (double) this.energyStorage };
			
			// unknown
			default: return null;
			
		}
		
	}

	@Override
	public void attach(IComputerAccess computer) {
		
	}

	@Override
	public void detach(IComputerAccess computer) {
		
	}

	@Override
	public boolean equals(IPeripheral other) {
		if (other.getType() == this.getType()) return true;
		return false;
	}
	
}
