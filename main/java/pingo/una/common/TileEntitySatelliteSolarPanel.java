package pingo.una.common;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;

public class TileEntitySatelliteSolarPanel extends TileEntitySatellitePart {

	private int solarLevel;
	
	public TileEntitySatelliteBattery connectedBattery;

	private boolean dead = false;
	
	public TileEntitySatelliteSolarPanel() {
		this.solarLevel = 1;
	}
	
	public TileEntitySatelliteSolarPanel(int level) {
		this.solarLevel = level;
	}
	
	public int getSolarLevel() {
		return solarLevel;
	}
	
	public boolean isDead() {
		return this.dead;
	}
	
	@Override
	public void invalidate() {
		this.dead = true;
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
	
	public void readFromNBT(NBTTagCompound nbttag)
    {
        super.readFromNBT(nbttag);
        
        if (nbttag.hasKey("solarLevel")) this.solarLevel = nbttag.getInteger("solarLevel");
    }

    public void writeToNBT(NBTTagCompound nbttag)
    {
        super.writeToNBT(nbttag);
        
        nbttag.setInteger("solarLevel", this.solarLevel);
    }
	
}
