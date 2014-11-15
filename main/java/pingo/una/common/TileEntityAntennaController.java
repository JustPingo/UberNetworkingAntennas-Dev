package pingo.una.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;

public class TileEntityAntennaController extends TileEntity implements IPeripheral {
	
	private final String[] methods = new String[] { "isBuilt", "broadcast", "send", "open", "getFrequency", "setFrequency" };
	
	private boolean isBuilt = false;
	private List<IComputerAccess> connectedComputers = new ArrayList<IComputerAccess>();
	
	// [0] : Connected antennas in an ArrayList, [1] : Network UID, [2] : Satellite object
	public Object[] network;
	
	public String networkUID = "";

	public boolean builtChecked = false;
	public boolean mustCheckBuilt = false;
	
	public TileEntityAntennaController() {
		if (!(FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)) {
			for (Iterator<TileEntityAntennaController> it = UberNetworkingAntennas.antennas.listIterator(); it.hasNext();) {
				TileEntityAntennaController a = it.next();
				if (a == this) return;
			}
			UberNetworkingAntennas.antennas.add(this);
		}
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
        
        if (nbttag.hasKey("networkUID")) this.networkUID = nbttag.getString("networkUID");
    }

    public void writeToNBT(NBTTagCompound nbttag)
    {
        super.writeToNBT(nbttag);
        
        nbttag.setString("networkUID", this.networkUID);
    }
    
    public void checkBuild() {
    	if (!worldObj.isRemote) {
	    	boolean everythingOk = true;
	    	for (int i = 1; i <= 6; i++) {
	    		if (worldObj.getBlock(xCoord, yCoord+i, zCoord) != Blocks.iron_bars) {
	    			everythingOk = false;
	    		}
	    	}
	    	if (everythingOk) {
	    		if (worldObj.getBlock(xCoord, yCoord+7, zCoord) == UberNetworkingAntennas.antennaFrame) {
	    			for (int i = 8; i <= 12; i++) {
	    	    		if (worldObj.getBlock(xCoord, yCoord+i, zCoord) != Blocks.iron_bars) {
	    	    			everythingOk = false;
	    	    		}
	    	    	}
	    			if (everythingOk) {
	    				if (worldObj.getBlock(xCoord, yCoord+13, zCoord) == UberNetworkingAntennas.antennaTransmitter) {
	    					if (worldObj.getBlock(xCoord-5, yCoord, zCoord-1) == UberNetworkingAntennas.antennaSupport) {
	    						if (worldObj.getBlock(xCoord-4, yCoord, zCoord-1) == UberNetworkingAntennas.antennaSupport) {
	    							if (worldObj.getBlock(xCoord-3, yCoord, zCoord-1) == UberNetworkingAntennas.antennaSupport) {
	    								if (worldObj.getBlock(xCoord-5, yCoord, zCoord) == UberNetworkingAntennas.antennaSupport) {
	    									if (worldObj.getBlock(xCoord-4, yCoord, zCoord) == UberNetworkingAntennas.antennaSupport) {
	    										if (worldObj.getBlock(xCoord-3, yCoord, zCoord) == UberNetworkingAntennas.antennaSupport) {
	    											if (worldObj.getBlock(xCoord-5, yCoord, zCoord+1) == UberNetworkingAntennas.antennaSupport) {
	    												if (worldObj.getBlock(xCoord-4, yCoord, zCoord+1) == UberNetworkingAntennas.antennaSupport) {
	    													if (worldObj.getBlock(xCoord-3, yCoord, zCoord+1) == UberNetworkingAntennas.antennaSupport) {
	    														if (worldObj.getBlock(xCoord+5, yCoord, zCoord-1) == UberNetworkingAntennas.antennaSupport) {
	    								    						if (worldObj.getBlock(xCoord+4, yCoord, zCoord-1) == UberNetworkingAntennas.antennaSupport) {
	    								    							if (worldObj.getBlock(xCoord+3, yCoord, zCoord-1) == UberNetworkingAntennas.antennaSupport) {
	    								    								if (worldObj.getBlock(xCoord+5, yCoord, zCoord) == UberNetworkingAntennas.antennaSupport) {
	    								    									if (worldObj.getBlock(xCoord+4, yCoord, zCoord) == UberNetworkingAntennas.antennaSupport) {
	    								    										if (worldObj.getBlock(xCoord+3, yCoord, zCoord) == UberNetworkingAntennas.antennaSupport) {
	    								    											if (worldObj.getBlock(xCoord+5, yCoord, zCoord+1) == UberNetworkingAntennas.antennaSupport) {
	    								    												if (worldObj.getBlock(xCoord+4, yCoord, zCoord+1) == UberNetworkingAntennas.antennaSupport) {
	    								    													if (worldObj.getBlock(xCoord+3, yCoord, zCoord+1) == UberNetworkingAntennas.antennaSupport) {
	    					if (worldObj.getBlock(xCoord-1, yCoord, zCoord-5) == UberNetworkingAntennas.antennaSupport) {
	    						if (worldObj.getBlock(xCoord-1, yCoord, zCoord-4) == UberNetworkingAntennas.antennaSupport) {
	    							if (worldObj.getBlock(xCoord-1, yCoord, zCoord-3) == UberNetworkingAntennas.antennaSupport) {
	    								if (worldObj.getBlock(xCoord, yCoord, zCoord-5) == UberNetworkingAntennas.antennaSupport) {
	    									if (worldObj.getBlock(xCoord, yCoord, zCoord-5) == UberNetworkingAntennas.antennaSupport) {
	    										if (worldObj.getBlock(xCoord, yCoord, zCoord-5) == UberNetworkingAntennas.antennaSupport) {
	    											if (worldObj.getBlock(xCoord+1, yCoord, zCoord-5) == UberNetworkingAntennas.antennaSupport) {
	    												if (worldObj.getBlock(xCoord+1, yCoord, zCoord-5) == UberNetworkingAntennas.antennaSupport) {
	    													if (worldObj.getBlock(xCoord+1, yCoord, zCoord-5) == UberNetworkingAntennas.antennaSupport) {
	    														if (worldObj.getBlock(xCoord-1, yCoord, zCoord+5) == UberNetworkingAntennas.antennaSupport) {
	    								    						if (worldObj.getBlock(xCoord-1, yCoord, zCoord+4) == UberNetworkingAntennas.antennaSupport) {
	    								    							if (worldObj.getBlock(xCoord-1, yCoord, zCoord+3) == UberNetworkingAntennas.antennaSupport) {
	    								    								if (worldObj.getBlock(xCoord, yCoord, zCoord+5) == UberNetworkingAntennas.antennaSupport) {
	    								    									if (worldObj.getBlock(xCoord, yCoord, zCoord+5) == UberNetworkingAntennas.antennaSupport) {
	    								    										if (worldObj.getBlock(xCoord, yCoord, zCoord+5) == UberNetworkingAntennas.antennaSupport) {
	    								    											if (worldObj.getBlock(xCoord+1, yCoord, zCoord+5) == UberNetworkingAntennas.antennaSupport) {
	    								    												if (worldObj.getBlock(xCoord+1, yCoord, zCoord+5) == UberNetworkingAntennas.antennaSupport) {
	    								    													if (worldObj.getBlock(xCoord+1, yCoord, zCoord+5) == UberNetworkingAntennas.antennaSupport) {
	    					if (worldObj.getBlock(xCoord-4, yCoord+1, zCoord) == UberNetworkingAntennas.antennaFrame) {
	    						if (worldObj.getBlock(xCoord+4, yCoord+1, zCoord) == UberNetworkingAntennas.antennaFrame) {
	    							if (worldObj.getBlock(xCoord, yCoord+1, zCoord-4) == UberNetworkingAntennas.antennaFrame) {
	    								if (worldObj.getBlock(xCoord, yCoord+1, zCoord+4) == UberNetworkingAntennas.antennaFrame) {
	    									if (worldObj.getBlock(xCoord-4, yCoord+2, zCoord) == UberNetworkingAntennas.antennaFrame) {
	    			    						if (worldObj.getBlock(xCoord+4, yCoord+2, zCoord) == UberNetworkingAntennas.antennaFrame) {
	    			    							if (worldObj.getBlock(xCoord, yCoord+2, zCoord-4) == UberNetworkingAntennas.antennaFrame) {
	    			    								if (worldObj.getBlock(xCoord, yCoord+2, zCoord+4) == UberNetworkingAntennas.antennaFrame) {
	    			    									if (worldObj.getBlock(xCoord-3, yCoord+2, zCoord) == UberNetworkingAntennas.antennaFrame) {
	    			    			    						if (worldObj.getBlock(xCoord+3, yCoord+2, zCoord) == UberNetworkingAntennas.antennaFrame) {
	    			    			    							if (worldObj.getBlock(xCoord, yCoord+2, zCoord-3) == UberNetworkingAntennas.antennaFrame) {
	    			    			    								if (worldObj.getBlock(xCoord, yCoord+2, zCoord+3) == UberNetworkingAntennas.antennaFrame) {
	    			    									if (worldObj.getBlock(xCoord-3, yCoord+3, zCoord) == UberNetworkingAntennas.antennaFrame) {
	    			    			    						if (worldObj.getBlock(xCoord+3, yCoord+3, zCoord) == UberNetworkingAntennas.antennaFrame) {
	    			    			    							if (worldObj.getBlock(xCoord, yCoord+3, zCoord-3) == UberNetworkingAntennas.antennaFrame) {
	    			    			    								if (worldObj.getBlock(xCoord, yCoord+3, zCoord+3) == UberNetworkingAntennas.antennaFrame) {
	    			    			    									if (worldObj.getBlock(xCoord-3, yCoord+4, zCoord) == UberNetworkingAntennas.antennaFrame) {
	    			    			    			    						if (worldObj.getBlock(xCoord+3, yCoord+4, zCoord) == UberNetworkingAntennas.antennaFrame) {
	    			    			    			    							if (worldObj.getBlock(xCoord, yCoord+4, zCoord-3) == UberNetworkingAntennas.antennaFrame) {
	    			    			    			    								if (worldObj.getBlock(xCoord, yCoord+4, zCoord+3) == UberNetworkingAntennas.antennaFrame) {
	    			    			    			    									if (worldObj.getBlock(xCoord-2, yCoord+4, zCoord) == UberNetworkingAntennas.antennaFrame) {
	    			    			    			    			    						if (worldObj.getBlock(xCoord+2, yCoord+4, zCoord) == UberNetworkingAntennas.antennaFrame) {
	    			    			    			    			    							if (worldObj.getBlock(xCoord, yCoord+4, zCoord-2) == UberNetworkingAntennas.antennaFrame) {
	    			    			    			    			    								if (worldObj.getBlock(xCoord, yCoord+4, zCoord+2) == UberNetworkingAntennas.antennaFrame) {
	    			    			    			    									if (worldObj.getBlock(xCoord-2, yCoord+5, zCoord) == UberNetworkingAntennas.antennaFrame) {
	    			    			    			    			    						if (worldObj.getBlock(xCoord+2, yCoord+5, zCoord) == UberNetworkingAntennas.antennaFrame) {
	    			    			    			    			    							if (worldObj.getBlock(xCoord, yCoord+5, zCoord-2) == UberNetworkingAntennas.antennaFrame) {
	    			    			    			    			    								if (worldObj.getBlock(xCoord, yCoord+5, zCoord+2) == UberNetworkingAntennas.antennaFrame) {
	    			    			    			    			    									if (worldObj.getBlock(xCoord-2, yCoord+6, zCoord) == UberNetworkingAntennas.antennaFrame) {
	    			    			    			    			    			    						if (worldObj.getBlock(xCoord+2, yCoord+6, zCoord) == UberNetworkingAntennas.antennaFrame) {
	    			    			    			    			    			    							if (worldObj.getBlock(xCoord, yCoord+6, zCoord-2) == UberNetworkingAntennas.antennaFrame) {
	    			    			    			    			    			    								if (worldObj.getBlock(xCoord, yCoord+6, zCoord+2) == UberNetworkingAntennas.antennaFrame) {
	    			    			    			    			    			    									if (worldObj.getBlock(xCoord-1, yCoord+6, zCoord) == UberNetworkingAntennas.antennaFrame) {
	    			    			    			    			    			    			    						if (worldObj.getBlock(xCoord+1, yCoord+6, zCoord) == UberNetworkingAntennas.antennaFrame) {
	    			    			    			    			    			    			    							if (worldObj.getBlock(xCoord, yCoord+6, zCoord-1) == UberNetworkingAntennas.antennaFrame) {
	    			    			    			    			    			    			    								if (worldObj.getBlock(xCoord, yCoord+6, zCoord+1) == UberNetworkingAntennas.antennaFrame) {
	    			    			    			    			    			    									if (worldObj.getBlock(xCoord-1, yCoord+7, zCoord) == UberNetworkingAntennas.antennaFrame) {
	    			    			    			    			    			    			    						if (worldObj.getBlock(xCoord+1, yCoord+7, zCoord) == UberNetworkingAntennas.antennaFrame) {
	    			    			    			    			    			    			    							if (worldObj.getBlock(xCoord, yCoord+7, zCoord-1) == UberNetworkingAntennas.antennaFrame) {
	    			    			    			    			    			    			    								if (worldObj.getBlock(xCoord, yCoord+7, zCoord+1) == UberNetworkingAntennas.antennaFrame) {
	    			    			    			    			    			    			    		    						
	    			    			    			    			    			    			    									this.saveAsBuilt(true);
	    			    			    			    			    			    			    									return;
	    			    			    			    			    			    			    									
	    			    			    			    			    			    			    		    					}
	    			    			    			    			    			    			    	    					}
	    			    			    			    			    			    			        					}
	    			    			    			    			    			    			    					}
	    			    			    			    			    			    		    					}
	    			    			    			    			    			    	    					}
	    			    			    			    			    			        					}
	    			    			    			    			    			    					}
	    			    			    			    			    		    					}
	    			    			    			    			    	    					}
	    			    			    			    			        					}
	    			    			    			    			    					}
	    			    			    			    		    					}
	    			    			    			    	    					}
	    			    			    			        					}
	    			    			    			    					}
	    			    			    		    					}
	    			    			    	    					}
	    			    			        					}
	    			    			    					}
	    			    		    					}
	    			    	    					}
	    			        					}
	    			    					}
	    		    					}
	    	    					}
	        					}
	    					}
	    								    							    					}
	    								    						    					}
	    								    					    					}
	    								    				    					}
	    								    			    					}
	    								    		    					}
	    								    	    					}
	    								        					}
	    								    					}
	    							    					}
	    						    					}
	    					    					}
	    				    					}
	    			    					}
	    		    					}
	    	    					}
	        					}
	    					} } } } } } } } } } } } }
	    								    							    					}
	    								    						    					}
	    								    					    					}
	    								    				    					}
	    								    			    					}
	    								    		    					}
	    								    	    					}
	    								        					}
	    								    					}
	    							    					}
	    						    					}
	    					    					}
	    				    					}
	    			    					}
	    		    					}
	    	    					}
	        					}
	    					}
	    				}
	    			}
	    		}
	    	}
	    	this.saveAsBuilt(false);
	    	this.builtChecked = true;
    	}
	}

	public void saveAsBuilt(boolean bol) {
		this.isBuilt = bol;
		this.builtChecked = true;
	}
	
	public void invalidate() {
		if (!worldObj.isRemote) {
			for (Iterator<TileEntityAntennaController> it = UberNetworkingAntennas.antennas.listIterator(); it.hasNext();) {
				TileEntityAntennaController a = it.next();
				if (a == this) it.remove();
			}
		}
	}

	public boolean receiveMessage(String message, Double senderID, Double receiverID) {
		if (!worldObj.isRemote) {
			if (this.isBuilt) {
				if (network != null) {
					if (network[2] instanceof Satellite) {
						Satellite sat = (Satellite) network[2];
		    			if (sat.takeEnergy(20)) {
						    boolean toreturn = false;
						    for ( Iterator<IComputerAccess> it = connectedComputers.iterator(); it.hasNext(); ) {
						    	IComputerAccess a = it.next();
						    	if (a != null) {
						    		if (receiverID == null || receiverID == a.getID()) {
						    			// event, sender, message
						    			a.queueEvent("uber_message", (new Object[] { senderID, message }));
						    			toreturn = true;
						    		}
						    	}
						    }
						    return toreturn;
		    			}
					}
				}
			}
		}
		return false;
    }
    
	@Override
	public String getType() {
		return "uber_networking_antenna";
	}

	@Override
	public String[] getMethodNames() {
		return this.methods;
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
		
		if (!worldObj.isRemote) {
			if ((!this.isBuilt && !this.builtChecked) || this.mustCheckBuilt ) {
				checkBuild();
				this.builtChecked = true;
				this.mustCheckBuilt = false;
			}
			
			if (this.isBuilt) {
				if (this.networkUID != "" && this.network == null) {
					for (Satellite row : UberNetworkingAntennas.satellites) {
						if (row.getUID() == networkUID) {
							row.addToList(this);
							break;
						}
					}
				}
			}
			
			switch (method) {
							
				// isBuilt()
				case 0: return (new Boolean[] { isBuilt });
				
				// broadcast(string message)
				case 1: if (this.isBuilt && network != null) {
					if (arguments.length != 0) {
						if (arguments[0] != null && arguments[0] instanceof String) {
							boolean received = false;
							for (@SuppressWarnings("unchecked") Iterator<TileEntityAntennaController> it = ((List<TileEntityAntennaController>) network[0]).iterator(); it.hasNext(); ) {
								TileEntityAntennaController a = it.next();
								if (a != this && a != null) received = a.receiveMessage((String) arguments[0], ((Integer) computer.getID()).doubleValue(), (Double) null);					
							}
							return (new Object[] { (Boolean) received });
						} else { throw new LuaException("Expected string"); }
					} else { throw new LuaException("Expected string"); }
				} else { return (new Object[] { (Boolean) false }); }
				
				// send(double receiverID, string message)
				case 2: if (this.isBuilt && network != null) {
					if (arguments.length != 0) {
						if (arguments[1] != null && arguments[1] instanceof String && arguments[0] != null && arguments[0] instanceof Double) {
							boolean received = false;
							for (@SuppressWarnings("unchecked") Iterator<TileEntityAntennaController> it = ((List<TileEntityAntennaController>) network[0]).iterator(); it.hasNext(); ) {
								TileEntityAntennaController a = it.next();
								if (a != this && a != null) received = a.receiveMessage((String) arguments[1], ((Integer) computer.getID()).doubleValue(), (Double) arguments[0]);					
							}
							return (new Object[] { (Boolean) received });
						} else { throw new LuaException("Expected number, string"); }
					} else { throw new LuaException("Expected number, string"); }
				} else { return (new Object[] { (Boolean) false }); }
				
				// open()
				case 3: if (this.isBuilt && network != null) {
					for ( Iterator<IComputerAccess> it = connectedComputers.iterator(); it.hasNext(); ) {
			    		IComputerAccess a = it.next();
			    		if (a == computer) return (new Object[] { (Boolean) false });
					}
					connectedComputers.add(computer);
					return (new Object[] { (Boolean) true });
				} else { return (new Object[] { (Boolean) false }); }
				
				// getFrequency()
				case 4: if (this.isBuilt && network != null) {
					return (new String[] { (String) network[1] });
				} break;
				
				// setFrequency(string uid)
				case 5: if (this.isBuilt && arguments != null) {
						if (arguments.length != 0) {
							if (arguments[0] instanceof String) {
								if (network != null) {
									((Satellite) network[2]).deleteFromList(this);
								}
								network = null;
								for (Satellite sat : UberNetworkingAntennas.satellites) {
									if (sat.getUID().equals(arguments[0])) { this.networkUID = (String) arguments[0]; sat.addToList(this); return (new Object[] { (Boolean) true }); }
								}
							} else { throw new LuaException("Expected string"); }
						} else { throw new LuaException("Expected string"); }
					return (new Object[] { (Boolean) false });
				} else { return (new Object[] { (Boolean) false }); }
				
				// unknown
				default: return null;
			}
		}
		
		return null;
	}

	@Override
	public void attach(IComputerAccess computer) {
		/*  */
	}

	@Override
	public void detach(IComputerAccess computer) {
		for ( Iterator<IComputerAccess> it = connectedComputers.iterator(); it.hasNext(); ) {
    		IComputerAccess a = it.next();
    		if (a == computer) { it.remove(); return; }
		}
	}

	@Override
	public boolean equals(IPeripheral other) {
		if (other.getType() == this.getType()) return true;
		else return false;
	}
	
	public boolean satelliteExists(String UID) {
		if (!worldObj.isRemote) {
	    	for (Satellite sat : UberNetworkingAntennas.satellites) {
	    		if (sat.getUID() == UID) return true;
	    	}
		}
    	return false;
    }
	
	public void onBlockActivated(World world, EntityPlayer player) {
		if ((!this.isBuilt && !this.builtChecked) || this.mustCheckBuilt ) {
			checkBuild();
			this.builtChecked = true;
			this.mustCheckBuilt = false;
		}
		if (!worldObj.isRemote) {
			ItemStack held = player.getHeldItem();
			if (held != null) {
	        	Item heldItem = held.getItem();
				if (heldItem.equals(UberNetworkingAntennas.satelliteIdentifier) && this.isBuilt) {
					if (held.stackTagCompound != null) {
						if (held.stackTagCompound.getBoolean("hasSatellite")) {
							String newSatellite = held.stackTagCompound.getString("satellite");
							if (network != null) {
								((Satellite) network[2]).deleteFromList(this);
							}
							network = null;
							int error = 2;
							for (Satellite sat : UberNetworkingAntennas.satellites) {
								if (sat.getUID().equals(newSatellite)) { this.networkUID = newSatellite; error = sat.addToList(this); }
							}
							if (error == 0) player.addChatMessage(new ChatComponentText("Antenna frequency set to : " + newSatellite));
							if (error == 1) player.addChatMessage(new ChatComponentText("Too many antennas connected / not enough Satellite Bridge Upgrades."));
							else if (error == 2) player.addChatMessage(new ChatComponentText("Can't reach satellite. Maybe it is destroyed or not on orbit yet."));
						}
					}
				}
			}
		}
	}

	public boolean getIsBuilt() {
		return this.isBuilt;
	}
	
}
