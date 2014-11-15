package pingo.una.common;

import net.minecraft.tileentity.TileEntity;

public class TileEntitySatellitePart extends TileEntity {
		
	public void liftOff(Satellite sat, boolean isAddon) {
		if (isAddon) {
			AddOn addOn = new AddOn();
			TileEntity te = this;
			if (te instanceof TileEntitySatelliteBridgeUpgrade) addOn.setConnectionSlots(((TileEntitySatelliteBridgeUpgrade) te).connections).lock();
			else if (te instanceof TileEntitySatelliteBattery) addOn.setEnergyStorage(((TileEntitySatelliteBattery) te).energyStorage).setEnergyStored(((TileEntitySatelliteBattery) te).energyStored).lock();
			else if (te instanceof TileEntitySatelliteSolarPanel) addOn.setSolarLevel(((TileEntitySatelliteSolarPanel) te).getSolarLevel()).lock();
			else if (te instanceof TileEntitySatelliteHumanSensor) addOn.setHumanSensor(true).lock();
			EntityFlyingBlock entityflyingblock = new EntityFlyingBlock(worldObj, worldObj.getBlock(xCoord, yCoord, zCoord), sat);
			entityflyingblock.setPosition(xCoord, yCoord+0.1D, zCoord);
			entityflyingblock.setAddon(addOn);
			worldObj.setBlockToAir(xCoord, yCoord, zCoord);
			worldObj.spawnEntityInWorld(entityflyingblock);
		} else {
			EntityFlyingBlock entityflyingblock = new EntityFlyingBlock(worldObj, worldObj.getBlock(xCoord, yCoord, zCoord), sat);
			entityflyingblock.setPosition(xCoord, yCoord+0.1D, zCoord);
			worldObj.setBlockToAir(xCoord, yCoord, zCoord);
			worldObj.spawnEntityInWorld(entityflyingblock);
		}
	}

}
