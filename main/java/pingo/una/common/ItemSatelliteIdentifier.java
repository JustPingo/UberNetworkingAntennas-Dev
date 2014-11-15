package pingo.una.common;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dan200.computercraft.api.filesystem.IMount;
import dan200.computercraft.api.media.IMedia;

public class ItemSatelliteIdentifier extends Item implements IMedia {

	public void generateStackCompound(ItemStack stack) {
		   stack.stackTagCompound = new NBTTagCompound();
		   stack.stackTagCompound.setBoolean("hasSatellite", false);
		   stack.stackTagCompound.setBoolean("hasLabel", false);
	}
	
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addInformation(ItemStack stack, EntityPlayer player, List info, boolean par4) {
	    if (stack.stackTagCompound != null) {
	    	if (stack.stackTagCompound.getBoolean("hasLabel")) {
	    		info.add(EnumChatFormatting.WHITE + stack.stackTagCompound.getString("label"));
	    	}
	    	if (stack.stackTagCompound.getBoolean("hasSatellite")) {
	    		info.add(EnumChatFormatting.DARK_GRAY + stack.stackTagCompound.getString("satellite"));
	    	}
	    }
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int p_7663_4, boolean p_7663_5) {
		if (stack.stackTagCompound == null) { this.generateStackCompound(stack); }
	}

	@Override
	public String getLabel(ItemStack stack) {
		if (stack.stackTagCompound != null) if (stack.stackTagCompound.getBoolean("hasLabel")) return stack.stackTagCompound.getString("label");
		return null;
	}

	@Override
	public boolean setLabel(ItemStack stack, String label) {
		if (stack.stackTagCompound != null && label != null) {
			if (label == "") { stack.stackTagCompound.setBoolean("hasLabel", false); return true; }
			if (!stack.stackTagCompound.getBoolean("hasLabel")) stack.stackTagCompound.setBoolean("hasLabel", true);
			stack.stackTagCompound.setString("label", label); return true;
		}
		return false;
	}

	@Override
	public String getAudioTitle(ItemStack stack) {
		return null;
	}

	@Override
	public String getAudioRecordName(ItemStack stack) {
		return null;
	}

	@Override
	public IMount createDataMount(ItemStack stack, World world) {
		return null;
	}

}
