package pingo.una.common;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;

public class TileEntitySatelliteController extends TileEntity implements IPeripheral {
	
	private final String[] methods = new String[] { "isComponentSendable", "sendNewSatellite", "sendNewElement", "selfDestruct", "getFrequency", "disconnect", "getEnergy", "getSatelliteEnergy" };
	
	private Satellite connectedSatellite;
	private String connectedSatelliteUID = "";

	private boolean waitBeforeSending = false;

	private boolean mustConnectSatellite = false;

	private boolean mustSendNewElement = false;

	private TileEntitySatelliteBattery bot;
	private TileEntitySatelliteBattery left;
	private TileEntitySatelliteBattery right;
	private TileEntitySatelliteBattery back;
	private TileEntitySatelliteBattery front;

	private boolean init = false;

	public TileEntitySatelliteController() {
		this.init = true;
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
        
        if (nbttag.hasKey("connectedSatelliteUID")) {
        	connectedSatelliteUID = nbttag.getString("connectedSatelliteUID");
        	if (connectedSatellite == null && connectedSatelliteUID != "") mustConnectSatellite  = true;
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
    
    public void onBlockActivated(World world, EntityPlayer player) {
    	if (!worldObj.isRemote) {
	    	ItemStack held = player.getHeldItem();
	    	if (held != null) {
	        	Item heldItem = held.getItem();
		    	if (heldItem.equals(UberNetworkingAntennas.satelliteIdentifier)) {
			    	if (held.stackTagCompound.getBoolean("hasSatellite")) {
			    		String potentialSatellite = held.stackTagCompound.getString("satellite");
			    		if (this.satelliteExists(potentialSatellite)) {
			    			this.connectedSatelliteUID = potentialSatellite;
			    			this.connectSatellite(potentialSatellite);
			    			player.addChatMessage(new ChatComponentText("Controller frequency set to : " + potentialSatellite));
			    		} else {
			    			player.addChatMessage(new ChatComponentText("Can't reach satellite. Maybe it is destroyed or not on orbit yet."));
			    		}
			    	} else if (this.isComponentReadySendable(UberNetworkingAntennas.satelliteCore) && this.waitBeforeSending) {
					    int consumption = 10000+(worldObj.getActualHeight()-yCoord+8)*50;
			    		if (this.getEnergyInBatteries() >= consumption) {
			    			this.takeEnergyFromBatteries(consumption);
			    			Satellite sat = new Satellite();
					    	held.stackTagCompound.setBoolean("hasSatellite", true);
					    	held.stackTagCompound.setString("satellite", sat.getUID());
					    	this.sendElement(sat, UberNetworkingAntennas.satelliteCore, false);
					    }
			    		this.waitBeforeSending = false;
			    	}
		    	}
	    	}
    	}
    }

	private void takeEnergyFromBatteries(int consumption) {
		if (!worldObj.isRemote) {
			if (bot != null) consumption = bot.takeEnergyStored(consumption);
			if (consumption == 0) return;
			if (left != null) consumption = left.takeEnergyStored(consumption);
			if (consumption == 0) return;
			if (right != null) consumption = right.takeEnergyStored(consumption);
			if (consumption == 0) return;
			if (back != null) consumption = back.takeEnergyStored(consumption);
			if (consumption == 0) return;
			if (front != null) consumption = front.takeEnergyStored(consumption);
		}
	}

	private int getEnergyInBatteries() {
		int retour = 0;
		if (!worldObj.isRemote) {
			if (bot != null) { if (bot.isDead()) bot = null; }
			if (left != null) { if (left.isDead()) left = null; }
			if (right != null) { if (right.isDead()) right = null; }
			if (front != null) { if (front.isDead()) front = null; }
			if (back != null) { if (back.isDead()) back = null; }
			if (bot != null) retour += bot.energyStored;
			if (left != null) retour += left.energyStored;
			if (right != null) retour += right.energyStored;
			if (front != null) retour += front.energyStored;
			if (back != null) retour += back.energyStored;
		}
		return retour;
	}

	private boolean connectSatellite(String uid) {
		if (!worldObj.isRemote) {
			if (UberNetworkingAntennas.satellites != null) {
				for (Satellite sat : UberNetworkingAntennas.satellites) {
					if (sat.getUID().equals(uid)) { this.connectedSatellite = sat; sat.addControler(this); 
					this.mustConnectSatellite = false;
					return true; }
				}
			}
		}
		return false;
	}
	
	public void deconnectSatellite() {
		if (connectedSatellite != null) {
			connectedSatellite.deleteControler(this);
			connectedSatellite = null;
			connectedSatelliteUID = "";
		}
	}
	
	@Override
	public void updateEntity() {
		if (this.init != false && worldObj != null) {
			if (!worldObj.isRemote) this.onNeighborBlockChange();
			this.init = false;
		}
		if (this.mustSendNewElement) {
			int consumption = 10000+(worldObj.getActualHeight()-yCoord+8)*50;
    		if (this.getEnergyInBatteries() >= consumption) {
    			this.takeEnergyFromBatteries(consumption);
    			this.sendElement(this.connectedSatellite, null, true);
    		}
			this.mustSendNewElement = false;
		}
	}
	
	public boolean isComponentReadySendable(Block block) {
		if (block != null) {
			if (worldObj.getBlock(xCoord, yCoord+1, zCoord) == block) {
				return true;
			}
		} else {
			if (worldObj.getTileEntity(xCoord, yCoord+1, zCoord) instanceof TileEntitySatellitePart) {
				return true;
			}
		}
		return false;
	}
	
	public void sendElement(Satellite sat, Block block, boolean teToSend) {
		if (block != null) {
			if (!(worldObj.getBlock(xCoord, yCoord+1, zCoord) == block)) {
				return;
			} 
		} else {
			if (!(worldObj.getTileEntity(xCoord, yCoord+1, zCoord) instanceof TileEntitySatellitePart)) {
				return;
			}
		}
		TileEntity teTemp = worldObj.getTileEntity(xCoord, yCoord+1, zCoord);
		if (teTemp instanceof TileEntitySatellitePart) {
			TileEntitySatellitePart te = (TileEntitySatellitePart) teTemp;
			if (!teToSend) { te.liftOff(sat, false); }
			else { te.liftOff(sat, true); }
		}
	}

	@Override
	public String getType() {
		return "satellite_controler";
	}

	@Override
	public String[] getMethodNames() {
		return this.methods;
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
		this.waitBeforeSending = false;
		if (!worldObj.isRemote) {
			if (this.mustConnectSatellite) {
				boolean isSatelliteOK = this.connectSatellite(connectedSatelliteUID);
				if (!isSatelliteOK) { this.connectedSatelliteUID = ""; }
			}
			switch (method) {
			
				// isComponentSendable()
				case 0: return new Boolean[] { this.isComponentReadySendable(null) };
				
				// sendNewSatellite()
				case 1: if (this.isComponentReadySendable(UberNetworkingAntennas.satelliteCore)) {
					this.waitBeforeSending  = true;
					return new Boolean[] { true };
				} else { return new Boolean[] { false }; }
				
				// sendNewElement()
				case 2: if (this.isComponentReadySendable(null) && this.connectedSatellite != null) {
					this.mustSendNewElement  = true;
					return new Boolean[] { true };
				} else { return new Boolean[] { false }; }
				
				// selfDestruct()
				case 3: if (this.connectedSatellite != null) {
					this.connectedSatellite.selfDestruct();
					return new Boolean[] { true };
				} else { return new Boolean[] { false }; }
				
				// getFrequency()
				case 4: if (this.connectedSatellite != null) return new String[] { this.connectedSatellite.getUID() };
				
				// disconnect()
				case 5: this.deconnectSatellite();
				
				// getEnergy()
				case 6: return new Double[] { (double) this.getEnergyInBatteries() };
				
				// getSatelliteEnergy()
				case 7: if (this.connectedSatellite != null) {
						return new Double[] { (double) this.connectedSatellite.getEnergy() };
					} else return null;
				
				// unknown
				default: return null;
				
			}
		}
		return null;
	}
	
	public void onNeighborBlockChange() {
		if (!worldObj.isRemote) {
			TileEntity te;
			te = worldObj.getTileEntity(xCoord, yCoord-1, zCoord);
			if (te instanceof TileEntitySatelliteBattery) this.bot = (TileEntitySatelliteBattery) te;
			else this.bot = null;
			te = worldObj.getTileEntity(xCoord, yCoord, zCoord+1);
			if (te instanceof TileEntitySatelliteBattery) this.front = (TileEntitySatelliteBattery) te;
			else this.front = null;
			te = worldObj.getTileEntity(xCoord, yCoord, zCoord-1);
			if (te instanceof TileEntitySatelliteBattery) this.back = (TileEntitySatelliteBattery) te;
			else this.back = null;
			te = worldObj.getTileEntity(xCoord+1, yCoord, zCoord);
			if (te instanceof TileEntitySatelliteBattery) this.right = (TileEntitySatelliteBattery) te;
			else this.right = null;
			te = worldObj.getTileEntity(xCoord-1, yCoord, zCoord);
			if (te instanceof TileEntitySatelliteBattery) this.left = (TileEntitySatelliteBattery) te;
			else this.left = null;
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
