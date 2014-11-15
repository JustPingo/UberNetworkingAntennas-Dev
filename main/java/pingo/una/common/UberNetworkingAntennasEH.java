package pingo.una.common;

import java.util.Iterator;

import net.minecraft.init.Blocks;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.WorldEvent.Load;
import net.minecraftforge.event.world.WorldEvent.Save;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class UberNetworkingAntennasEH {
	
	@SubscribeEvent
	public void worldEventLoad(Load event) 
	{    
		if (!event.world.isRemote) {
			UberNetworkingAntennas.satellites = new FileHandler().loadSatellites("/una_satellites");
		}
	}
	
	@SubscribeEvent
	public void worldEventSave(Save event) {
		if (!event.world.isRemote) { new FileHandler().writeSatellites("/una_satellites", UberNetworkingAntennas.satellites); }
	}
	
	@SubscribeEvent
	public void blockBreakEvent(BreakEvent event) {
		if (event.block == Blocks.iron_bars
		 || event.block == UberNetworkingAntennas.antennaFrame
		 || event.block == UberNetworkingAntennas.antennaSupport
		 || event.block == UberNetworkingAntennas.antennaTransmitter) {
			for (Iterator<TileEntityAntennaController> it = UberNetworkingAntennas.antennas.listIterator(); it.hasNext();) {
				TileEntityAntennaController a = it.next();
				if (event.x <= a.xCoord + 5 && event.x >= a.xCoord - 5) {
					if (event.y <= a.yCoord + 13 && event.y >= a.yCoord) {
						if (event.z <= a.zCoord + 5 && event.z >= a.zCoord - 5) {
							a.mustCheckBuilt = true;
							return;
						}
					}
				}
			}
		}
	}
	
}
