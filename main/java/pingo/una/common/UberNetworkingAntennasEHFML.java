package pingo.una.common;

import java.util.Iterator;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class UberNetworkingAntennasEHFML {
	
	int tick = 0;
	
	@SubscribeEvent
	public void serverTickEvent(TickEvent.ServerTickEvent event){
		this.tick++;
		if (tick % 600 == 0) {
			for (Iterator<TileEntityAntennaController> it = UberNetworkingAntennas.antennas.listIterator(); it.hasNext();) {
				TileEntityAntennaController a = it.next();
				a.builtChecked = false;
			}
			for (Iterator<Satellite> it = UberNetworkingAntennas.satellites.listIterator(); it.hasNext();) {
				Satellite a = it.next();
				a.updateSolarPanels();
			}
			for (Iterator<TileEntitySatelliteBattery> it = UberNetworkingAntennas.batteries.listIterator(); it.hasNext();) {
				TileEntitySatelliteBattery a = it.next();
				a.getFromPanels();
			}
			this.tick = 0;
		}
	}
}
