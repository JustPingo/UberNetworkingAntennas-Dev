package pingo.una.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

 /*
 * 
 * Satellite objects are objects that store all the data about Satellites you send in space.
 * They allow me to manage antenna control more easily. I think their usage is pretty obvious xD
 * 
 */

public class Satellite implements Serializable {

	private static final long serialVersionUID = -7271600773186997057L;
	
	public static final int satelliteObjectVersion = 1;
	
	private String UID;
	private List<AddOn> addOns = new ArrayList<AddOn>();
	private transient List<TileEntityAntennaController> antennasToUpdate;
	private transient List<TileEntitySatelliteController> controlersToUpdate;
	private transient List<TileEntityLaserController> laserControllersToUpdate;
	
	private int connectableAntennas = 0;

	protected Satellite() {
		if (UID == null) this.UID = java.util.UUID.randomUUID().toString();
	}
	
	public void deleteFromList(TileEntityAntennaController tileEntityAntennaControler) {
		if (antennasToUpdate != null) {
			for ( Iterator<TileEntityAntennaController> it = antennasToUpdate.iterator(); it.hasNext(); ) {
				TileEntityAntennaController a = it.next();
	    		if (a == tileEntityAntennaControler) { it.remove(); this.globalNetworkUpdate(); return; }
			}
		}
	}
	
	public void deleteControler(TileEntitySatelliteController tileEntitySatelliteControler) {
		if (controlersToUpdate != null) {
			for ( Iterator<TileEntitySatelliteController> it = controlersToUpdate.iterator(); it.hasNext(); ) {
				TileEntitySatelliteController a = it.next();
	    		if (a == tileEntitySatelliteControler) { it.remove(); return; }
			}
		}
	}
	
	public void deleteLaserController(TileEntityLaserController tileEntityLaserControler) {
		if (laserControllersToUpdate != null) {
			for ( Iterator<TileEntityLaserController> it = laserControllersToUpdate.iterator(); it.hasNext(); ) {
				TileEntityLaserController a = it.next();
	    		if (a == tileEntityLaserControler) { it.remove(); return; }
			}
		}
	}

	public int addToList(TileEntityAntennaController tileEntityAntennaControler) {
		if (antennasToUpdate == null) antennasToUpdate = new ArrayList<TileEntityAntennaController>();
		if (this.connectableAntennas > this.antennasToUpdate.size()) {
			if (antennasToUpdate.toArray().length != 0) {
				for ( Iterator<TileEntityAntennaController> it = antennasToUpdate.iterator(); it.hasNext(); ) {
					TileEntityAntennaController a = it.next();
		    		if (a == tileEntityAntennaControler) { this.globalNetworkUpdate(); return 0; }
				}
			}
			antennasToUpdate.add(tileEntityAntennaControler);
			this.globalNetworkUpdate();
			return 0;
		}
		return 1;
	}
	
	public void addControler(TileEntitySatelliteController tileEntitySatelliteControler) {
		if (controlersToUpdate != null) {
			for ( Iterator<TileEntitySatelliteController> it = controlersToUpdate.iterator(); it.hasNext(); ) {
				TileEntitySatelliteController a = it.next();
	    		if (a == tileEntitySatelliteControler) return;
			}
		}
		if (controlersToUpdate == null) controlersToUpdate = new ArrayList<TileEntitySatelliteController>();
		controlersToUpdate.add(tileEntitySatelliteControler);
	}
	
	public void addLaserController(TileEntityLaserController tileEntityLaserControler) {
		if (laserControllersToUpdate != null) {
			for ( Iterator<TileEntityLaserController> it = laserControllersToUpdate.iterator(); it.hasNext(); ) {
				TileEntityLaserController a = it.next();
	    		if (a == tileEntityLaserControler) return;
			}
		}
		if (laserControllersToUpdate == null) laserControllersToUpdate = new ArrayList<TileEntityLaserController>();
		laserControllersToUpdate.add(tileEntityLaserControler);
	}
	
	private void globalNetworkUpdate() {
		if (antennasToUpdate != null) {
			for ( Iterator<TileEntityAntennaController> it = antennasToUpdate.iterator(); it.hasNext(); ) {
				TileEntityAntennaController a = it.next();
	    		a.network = new Object[] { antennasToUpdate, UID, this };
			}
		}
	}
	
	public void selfDestruct() {
		for ( Iterator<Satellite> it = UberNetworkingAntennas.satellites.iterator(); it.hasNext(); ) {
			Satellite a = it.next();
    		if (a == this) it.remove();
		}
		if (antennasToUpdate != null) {
			for ( Iterator<TileEntityAntennaController> it = antennasToUpdate.iterator(); it.hasNext(); ) {
				TileEntityAntennaController a = it.next();
	    		a.network = null;
	    		a.networkUID = "";
			}
			antennasToUpdate = null;
		}
		if (controlersToUpdate != null) {
			for ( Iterator<TileEntitySatelliteController> it = controlersToUpdate.iterator(); it.hasNext(); ) {
				TileEntitySatelliteController a = it.next();
	    		a.deconnectSatellite();
			}
			controlersToUpdate = null;
		}
		if (laserControllersToUpdate != null) {
			for ( Iterator<TileEntityLaserController> it = laserControllersToUpdate.iterator(); it.hasNext(); ) {
				TileEntityLaserController a = it.next();
	    		a.deconnectSatellite();
			}
			laserControllersToUpdate = null;
		}
	}
	
	public void newAddOn(AddOn addOn) {
		addOns.add(addOn);
		this.recalculateAddOns();
	}

	public void recalculateAddOns() {
		this.connectableAntennas = 0;
		for (Iterator<AddOn> it = addOns.iterator(); it.hasNext(); ) {
			AddOn a = it.next();
    		this.connectableAntennas += a.getConnectionSlots();
		}
		this.globalNetworkUpdate();
	}
	
	public int getEnergy() {
		int energyStored = 0;
		for (Iterator<AddOn> it = addOns.iterator(); it.hasNext(); ) {
			AddOn a = it.next();
			energyStored += a.getEnergyStored();
		}
		return energyStored;
	}
	
	public boolean takeEnergy(int toTake) {
		if (this.getEnergy() >= toTake) {
			for (Iterator<AddOn> it = addOns.iterator(); it.hasNext(); ) {
				AddOn a = it.next();
				toTake = a.takeEnergyStored(toTake);
			}
			return true;
		}
		return false;
	}
	
	public void updateSolarPanels() {
		int energyToAdd = 0;
		for (AddOn row : addOns) {
			energyToAdd += 300 * row.getSolarLevel();
		}
		for (AddOn row : addOns) {
			energyToAdd = row.addEnergyStored(energyToAdd);
			if (energyToAdd == 0) return;
		}
	}
	
	public int[] locatePlayer(World world, String name) {
		for (AddOn row : addOns) {
			if (row.isHumanSensor()) {
				EntityPlayer player;
				for (Object object : world.playerEntities) {
					if (object instanceof EntityPlayer) {
						player = (EntityPlayer) object;
						if (player.getDisplayName() == name) {
							if (world.canBlockSeeTheSky((int) player.posX, (int) player.posY, (int) player.posZ)) { 
								return new int[] { (int) player.posX, (int) player.posY, (int) player.posZ };
							}
							return null;
						}
					}
				}
				return null;
			}
		}
		return null;
	}

	public String getUID() {
		return UID;
	}

	public String[] listLasers() {
		return null;
	}
	
}
