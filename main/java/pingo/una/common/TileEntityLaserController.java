package pingo.una.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;

public class TileEntityLaserController extends TileEntity implements IPeripheral {

	private final String[] methods = new String[] { "getFrequency", "disconnect", "getSatelliteEnergy" };
	
	private Satellite connectedSatellite;
	private String connectedSatelliteUID = "";

	private boolean mustConnectSatellite;
	
	public void deconnectSatellite() {
		if (connectedSatellite != null) {
			connectedSatellite.deleteLaserController(this);
			connectedSatellite = null;
			connectedSatelliteUID = "";
		}
	}
	
	public void onBlockActivated(World world, EntityPlayer player) {
    	if (!worldObj.isRemote) {
	    	ItemStack held = player.getHeldItem();
	    	if (held != null) {
	        	Item heldItem = held.getItem();
		    	if (heldItem.equals(UberNetworkingAntennas.satelliteIdentifier) || heldItem.equals(UberNetworkingAntennas.laserIdentifier)) {
			    	if (held.stackTagCompound.getBoolean("hasSatellite")) {
			    		String potentialSatellite = held.stackTagCompound.getString("satellite");
			    		if (this.satelliteExists(potentialSatellite)) {
			    			this.connectedSatelliteUID = potentialSatellite;
			    			this.connectSatellite(potentialSatellite);
			    			player.addChatMessage(new ChatComponentText("Controller frequency set to : " + potentialSatellite));
			    		} else {
			    			player.addChatMessage(new ChatComponentText("Can't reach satellite. Maybe it is destroyed or not on orbit yet."));
			    		}
			    	}
		    	}
	    	}
    	}
    }
	
	public void readFromNBT(NBTTagCompound nbttag)
    {
        super.readFromNBT(nbttag);
        
        if (nbttag.hasKey("connectedSatelliteUID")) {
        	connectedSatelliteUID = nbttag.getString("connectedSatelliteUID");
        	if (connectedSatellite == null && connectedSatelliteUID != "") mustConnectSatellite = true;
        }
    }

    public void writeToNBT(NBTTagCompound nbttag)
    {
        super.writeToNBT(nbttag);
        
        nbttag.setString("connectedSatelliteUID", connectedSatelliteUID);
    }
	
	public boolean satelliteExists(String UID) {
    	if (!worldObj.isRemote) {
	    	for (Satellite sat : UberNetworkingAntennas.satellites) {
	    		if (sat.getUID().equals(UID)) return true;
	    	}
    	}
    	return false;
    }
	
	private boolean connectSatellite(String uid) {
		if (!worldObj.isRemote) {
			if (UberNetworkingAntennas.satellites != null) {
				for (Satellite sat : UberNetworkingAntennas.satellites) {
					if (sat.getUID().equals(uid)) { this.connectedSatellite = sat; sat.addLaserController(this); 
					this.mustConnectSatellite = false;
					return true; }
				}
			}
		}
		return false;
	}
	
	@Override
	public String getType() {
		return "laser_controller";
	}

	@Override
	public String[] getMethodNames() {
		return this.methods;
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
		if (!worldObj.isRemote) {
			if (this.mustConnectSatellite) {
				boolean isSatelliteOK = this.connectSatellite(connectedSatelliteUID);
				if (!isSatelliteOK) { this.connectedSatelliteUID = ""; }
			}
			switch (method) {
			
				// getFrequency()
				case 0: if (this.connectedSatellite != null) return new String[] { this.connectedSatellite.getUID() };
			
				// disconnect()
				case 1: this.deconnectSatellite();
				
				// getSatelliteEnergy()
				case 2: if (this.connectedSatellite != null) {
					return new Double[] { (double) this.connectedSatellite.getEnergy() };
				} else return null;
				
				// getConnectedSatellites()
				case 3: if (this.connectedSatellite != null) {
					return this.connectedSatellite.listLasers();
				} else return null;
			
				// unknown
				default: return null;
				
			}
		}
		return null;
	}

	@Override
	public void attach(IComputerAccess computer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void detach(IComputerAccess computer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean equals(IPeripheral other) {
		// TODO Auto-generated method stub
		return false;
	}

	
	
}
