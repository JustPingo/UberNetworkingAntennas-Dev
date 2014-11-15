package pingo.una.common;

import java.io.Serializable;

 /*
 * 
 * AddOn objects are essentially made for future improvements with Satellites
 * They dynamically store upgrades, and allow me to separate upgrades in the same
 * Satellite in preparation for when you will be able to downgrade Satellites.
 * 
 */

public class AddOn implements Serializable {

	private static final long serialVersionUID = -82694867657988568L;

	private int connectionSlots = 0;
	private int energyStorage = 0;
	private int energyStored = 0;
	private int solarLevel = 0;
	private boolean isHumanSensor = false;
	
	private boolean locked = false;
	
	protected AddOn() {
		
	}
	
	public AddOn lock() {
		this.locked = true;
		return this;
	}
	
	public AddOn setConnectionSlots(int toSet) {
		if (!this.locked) this.connectionSlots = toSet;
		return this;
	}
	
	public AddOn setEnergyStorage(int toSet) {
		if (!this.locked) this.energyStorage = toSet;
		return this;
	}
	
	public AddOn setEnergyStored(int toSet) {
		if (this.energyStored + toSet <= this.energyStorage) this.energyStored = toSet;
		return this;
	}
	
	public AddOn setSolarLevel(int toSet) {
		if (!this.locked) this.solarLevel = toSet;
		return this;
	}
	
	public AddOn setHumanSensor(boolean toSet) {
		if (!this.locked) this.isHumanSensor = toSet;
		return this;
	}
	
	public int addEnergyStored(int toAdd) {
		if (this.energyStored + toAdd <= this.energyStorage) { this.energyStored += toAdd; return 0; }
		else { int retour = this.energyStored + toAdd - this.energyStorage; this.energyStored = this.energyStorage; return retour; }
	}
	
	public int takeEnergyStored(int toTake) {
		if (this.energyStored - toTake >= 0) { this.energyStored -= toTake; return 0; }
		else { int retour = toTake - this.energyStored; this.energyStored = 0; return retour; }
	}
	
	public int getConnectionSlots() {
		return this.connectionSlots;
	}
	
	public int getEnergyStorage() {
		return this.energyStorage;
	}
	
	public int getEnergyStored() {
		return this.energyStored;
	}
	
	public int getSolarLevel() {
		return this.solarLevel;
	}
	
	public boolean isHumanSensor() {
		return this.isHumanSensor;
	}
	
}
