package pingo.una.common;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/*
 * 
 * EntityFlyingBlock is the entity I use to make Satellites lift off.
 * It stores Satellite or AddOn object and "release" them when it reach mapHeight + 8.
 * 
 */

public class EntityFlyingBlock extends Entity {

	public Block block;
	public int metadata;
	
	private Satellite sat;
	private AddOn satAddOn;
			
	public int maxHeight;
	
	public EntityFlyingBlock(World world, Block block, Satellite sat) {
		super(world);
        this.block = block;
        this.sat = sat;
        this.setSize(1.0F, 1.0F); 
        this.yOffset = 0.5F; 
        this.preventEntitySpawning = true;
        this.maxHeight = world.getActualHeight() + 8;
	}
	
    public EntityFlyingBlock(World world, Block block) {
		super(world);
        this.block = block;
        this.setSize(1.0F, 1.0F); 
        this.yOffset = 0.5F; 
        this.preventEntitySpawning = true;
        this.maxHeight = world.getActualHeight() + 8;
	}
    
    public EntityFlyingBlock(World world) {
		super(world);
		this.setSize(1.0F, 1.0F); 
        this.yOffset = 0.5F; 
        this.preventEntitySpawning = true;
        this.maxHeight = world.getActualHeight() + 8;
	}

	@Override
	public void onUpdate()
    {
        this.moveEntity(0, 0.5, 0);
        // Display particles under the flying entity, on in each corner, but with a random position.
        worldObj.spawnParticle("flame", posX + 0.5D * rand.nextDouble(), posY-1.1D, posZ + 0.5D * rand.nextDouble(), 0.0D, 0.0D, 0.0D);
        worldObj.spawnParticle("flame", posX+0.5D + 0.5D * rand.nextDouble(), posY-1.1D, posZ + 0.5D * rand.nextDouble(), 0.0D, 0.0D, 0.0D);
        worldObj.spawnParticle("flame", posX + 0.5D * rand.nextDouble(), posY-1.1D, posZ+0.5D + 0.5D * rand.nextDouble(), 0.0D, 0.0D, 0.0D);
        worldObj.spawnParticle("flame", posX+0.5D + 0.5D * rand.nextDouble(), posY-1.1D, posZ+0.5D + 0.5D * rand.nextDouble(), 0.0D, 0.0D, 0.0D);
        if (this.isCollided) {
        	if (!worldObj.isRemote) this.worldObj.createExplosion(null, posX, posY, posZ, 4F, true);
        	this.kill();
        } else if (this.posY > this.maxHeight) {
        	this.callback();
        	this.kill();
        }
    }
	
	public void callback() { // Register the new Satellite.
		if (!worldObj.isRemote) {
			if (this.satAddOn != null && this.sat != null) {
				this.sat.newAddOn(this.satAddOn);
			} else if (this.sat != null) {
				UberNetworkingAntennas.satellites.add(sat);
			}
		}
	}
	
	public void setAddon(AddOn addon) {
		this.satAddOn = addon;
	}
	
	@Override
	protected void entityInit() {

	}
	
	protected void writeEntityToNBT(NBTTagCompound nbttag)
    {
		
    }
    
    protected void readEntityFromNBT(NBTTagCompound nbttag)
    {

    }
	
}
