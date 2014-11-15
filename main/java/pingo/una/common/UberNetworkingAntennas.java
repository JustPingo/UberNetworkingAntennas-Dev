package pingo.una.common;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import dan200.computercraft.api.ComputerCraftAPI;

// 
// Full license : http://industrial-adventure.verygames.net/JMMPL-1.0.txt
// Cool stuff : https://www.youtube.com/watch?v=3N4sSV_iFIA and https://www.youtube.com/watch?v=uAz5Pf8dsXo
@Mod(modid = UberNetworkingAntennas.MODID, name = UberNetworkingAntennas.NAME, version = UberNetworkingAntennas.VERSION, dependencies="required-after:ComputerCraft@[1.7,)")
public class UberNetworkingAntennas
{
    public static final String MODID = "ubernetworkingantennas";
    public static final String NAME = "Uber Networking Antennas";
    public static final String VERSION = "0.1.0";
    
    @Instance(UberNetworkingAntennas.MODID)
	public static UberNetworkingAntennas instance;
    
    @SidedProxy(clientSide = "pingo.una.client.ClientProxy", serverSide = "pingo.una.common.CommonProxy")
	public static CommonProxy proxy;
    
	public static SimpleNetworkWrapper mainNetworkWrapper;
    
    public static Block antennaController;
    public static Block satelliteController;
    public static Block laserController;
    public static Block satelliteCore;
    public static Block satelliteBridgeUpgrade;
    public static Block satelliteBattery;
    public static Block satelliteSolarPanel;
    public static Block satelliteHumanSensor;
    public static Block satelliteLaserFrame;
    public static Block satelliteMilitaryLaser;
    public static Block antennaFrame;
    public static Block antennaSupport;
    public static Block antennaTransmitter;
    
    public static Item satelliteIdentifier;
 
	public static CreativeTabs tabUNA = new UberNetworkingAntennasCT("UberNetworkingAntennas");
	public static List<Satellite> satellites;
	public static List<TileEntityAntennaController> antennas = new ArrayList<TileEntityAntennaController>();
	public static List<TileEntitySatelliteBattery> batteries = new ArrayList<TileEntitySatelliteBattery>();
    
	private void blockRegister() {
		antennaController = new BlockAntennaController(Material.iron).setBlockName("AntennaController").setHardness(3F).setCreativeTab(tabUNA);
		satelliteController = new BlockSatelliteController(Material.iron).setBlockName("SatelliteController").setHardness(3F).setCreativeTab(tabUNA);
		laserController = new BlockLaserController(Material.iron).setBlockName("LaserControler").setHardness(3F).setCreativeTav(tabUNA);
		satelliteCore = new BlockSatelliteCore(Material.iron).setBlockName("SatelliteCore").setHardness(3F).setCreativeTab(tabUNA);
		satelliteBridgeUpgrade = new BlockSatelliteBridgeUpgrade(Material.iron).setBlockName("SatelliteBridgeUpgrade").setHardness(3F).setCreativeTab(tabUNA);
		satelliteBattery = new BlockSatelliteBattery(Material.iron).setBlockName("SatelliteBattery").setHardness(3F).setCreativeTab(tabUNA);
		satelliteSolarPanel = new BlockSatelliteSolarPanel(Material.iron).setBlockName("SatelliteSolarstormsCapacitor").setHardness(3F).setCreativeTab(tabUNA);
		satelliteHumanSensor = new BlockSatelliteHumanSensor(Material.iron).setBlockName("SatelliteHumanSensor").setHardness(3F).setCreativeTab(tabUNA);
		satelliteLaserFrame = new BlockSatelliteLaserFrame(Material.iron).setBlockName("SatelliteLaserFrame").setHardness(3F).setCreativeTab(tabUNA);
		satelliteMilitaryLaser = new BlockMilitaryLaserFrame(Material.iron).setBlockName("SatelliteMilitaryLaser").setHardness(3F).setCreativeTab(tabUNA);
		antennaFrame = new BlockAntennaFrame(Material.iron).setBlockName("AntennaFrame").setHardness(3F).setCreativeTab(tabUNA);
		antennaSupport = new BlockAntennaSupport(Material.iron).setBlockName("AntennaSupport").setHardness(3F).setCreativeTab(tabUNA);
		antennaTransmitter = new BlockAntennaTransmitter(Material.iron).setBlockName("AntennaTransmitter").setHardness(3F).setCreativeTab(tabUNA);
		
		GameRegistry.registerBlock(antennaController, "antenna_controller");
		GameRegistry.registerBlock(satelliteController, "satellite_controller");
		GameRegistry.registerBlock(laserController, "laser_controller");
		GameRegistry.registerBlock(satelliteCore, "satellite_core");
		GameRegistry.registerBlock(satelliteBridgeUpgrade, "satellite_bridge_upgrade");
		GameRegistry.registerBlock(satelliteBattery, ItemBlockSatelliteBattery.class, "satellite_battery");
		GameRegistry.registerBlock(satelliteSolarPanel, "satellite_solarstorms_capacitor");
		GameRegistry.registerBlock(satelliteHumanSensor, "satellite_human_sensor");
		GameRegistry.registerBlock(satelliteLaserFrame, "satellite_laser_frame");
		GameRegistry.registerBlock(satelliteMilitaryLaser, "satellite_military_laser");
		GameRegistry.registerBlock(antennaFrame, "antenna_frame");
		GameRegistry.registerBlock(antennaSupport, "antenna_support");
		GameRegistry.registerBlock(antennaTransmitter, "antenna_transmitter");
	}
	
	private void itemRegister() {
		satelliteIdentifier = new ItemSatelliteIdentifier().setUnlocalizedName("SatelliteIdentifier").setTextureName(UberNetworkingAntennas.MODID + ":satelliteIdentifier").setCreativeTab(tabUNA);
		
		GameRegistry.registerItem(satelliteIdentifier, "satellite_identifier");
	}
	
	private void tileEntitiesRegister() {
		GameRegistry.registerTileEntity(TileEntityAntennaController.class, "TileEntityAntennaControler");
		GameRegistry.registerTileEntity(TileEntitySatelliteController.class, "TileEntitySatelliteControler");
		GameRegistry.registerTileEntity(TileEntitySatellitePart.class, "TileEntitySatellitePart");
		GameRegistry.registerTileEntity(TileEntitySatelliteBridgeUpgrade.class, "TileEntitySatelliteBridgeUpgrade");
		GameRegistry.registerTileEntity(TileEntitySatelliteBattery.class, "TileEntitySatelliteBattery");
		GameRegistry.registerTileEntity(TileEntitySatelliteSolarPanel.class, "TileEntitySatelliteSolarPanel");
	}
	
	private void craftingRegister() {
		GameRegistry.addRecipe(new ItemStack(antennaController), new Object[]{"SIS", "GRG", "SIS", 'S', Blocks.stone, 'R', Blocks.redstone_block, 'G', Items.gold_ingot, 'I', Items.iron_ingot });
		GameRegistry.addRecipe(new ItemStack(satelliteController), new Object[]{"SDS", "OIO", "STS", 'S', Blocks.stone, 'R', Blocks.redstone_block, 'O', Blocks.obsidian, 'I', Blocks.iron_block, 'T', Blocks.redstone_torch, 'D', Items.diamond });
		GameRegistry.addRecipe(new ItemStack(satelliteCore), new Object[]{"IOI", "OGO", "IOI", 'O', Blocks.obsidian, 'G', Blocks.gold_block, 'I', Items.iron_ingot });
		GameRegistry.addRecipe(new ItemStack(satelliteBridgeUpgrade), new Object[]{"IOI", "OEO", "IOI", 'O', Blocks.obsidian, 'E', Items.ender_eye, 'I', Items.iron_ingot });
		GameRegistry.addRecipe(new ItemStack(antennaFrame, 2), new Object[]{"II", "BB", 'I', Items.iron_ingot, 'B', Blocks.iron_bars });
		GameRegistry.addRecipe(new ItemStack(antennaSupport, 3), new Object[]{"SSS", "SIS", "SSS", 'I', Items.iron_ingot, 'S', Blocks.stone });
		GameRegistry.addRecipe(new ItemStack(antennaTransmitter), new Object[]{"IEI", "ERE", "IEI", 'E', Items.ender_eye, 'I', Items.iron_ingot, 'R', Blocks.redstone_block });
		GameRegistry.addRecipe(new ItemStack(satelliteBattery), new Object[]{"IOI", "RLR", "IOI", 'I', Items.iron_ingot, 'O', Blocks.obsidian, 'R', Blocks.redstone_block, 'L', Blocks.lapis_block});
		GameRegistry.addRecipe(new ItemStack(satelliteSolarPanel), new Object[]{"DLD", "ISI", "OIO", 'D', Items.diamond, 'L', Blocks.lapis_block, 'I', Items.iron_ingot, 'O', Blocks.obsidian, 'S', Blocks.daylight_detector});
		GameRegistry.addRecipe(new ItemStack(satelliteIdentifier), new Object[]{"VV", "ER", "GG", 'G', Items.gold_ingot, 'I', Items.iron_ingot, 'R', Items.redstone, 'E', Items.ender_pearl, 'V', Blocks.glass_pane });
		
		GameRegistry.addShapelessRecipe(new ItemStack(antennaController), new Object[] { UberNetworkingAntennas.antennaControler });
		GameRegistry.addShapelessRecipe(new ItemStack(satelliteController), new Object[] { UberNetworkingAntennas.satelliteControler });
	}
	
	private void entityRegister() {
		this.registerEntity(EntityFlyingBlock.class, "EntityFlyingBlock");
	}
	
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	itemRegister();
		blockRegister();
		tileEntitiesRegister();
		entityRegister();
    }

	@EventHandler
    public void init(FMLInitializationEvent event)
    {
    	craftingRegister();
    	ComputerCraftAPI.registerPeripheralProvider(new PeripheralProvider());
    	FMLCommonHandler.instance().bus().register(new UberNetworkingAntennasEHFML());
    	MinecraftForge.EVENT_BUS.register(new UberNetworkingAntennasEH());
    	proxy.registerRender();
    }

	@EventHandler
    public void postInit(FMLPostInitializationEvent event) {

	}
	
	@SuppressWarnings("unchecked")
	public void registerEntity(@SuppressWarnings("rawtypes") Class entity, String name) {
		int entityID = EntityRegistry.findGlobalUniqueEntityId();
		
		EntityRegistry.registerGlobalEntityID(entity, name, entityID);
		EntityRegistry.registerModEntity(entity, name, entityID, instance, 64, 1, true);
	}
}
