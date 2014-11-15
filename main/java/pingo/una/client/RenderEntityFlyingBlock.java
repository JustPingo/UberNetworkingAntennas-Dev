package pingo.una.client;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import pingo.una.common.EntityFlyingBlock;
import pingo.una.common.UberNetworkingAntennas;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderEntityFlyingBlock extends Render
{
    private RenderBlocks blockRenderer = new RenderBlocks();
    
    public void renderEntityFlyingBlock(EntityFlyingBlock entity, double par2, double par4, double par6, float par8, float par9) {        
        if (entity.block == null) entity.block = UberNetworkingAntennas.satelliteCore; 
    	GL11.glPushMatrix();
        GL11.glTranslatef((float) par2, (float) par4, (float) par6);
        this.blockRenderer.renderBlockAsItem(entity.block, 0, entity.getBrightness(par9));
        GL11.glPopMatrix();
    }
    
    public void doRender(Entity entity, double par2, double par4, double par6, float par8, float par9)
    {
        this.renderEntityFlyingBlock((EntityFlyingBlock)entity, par2, par4, par6, par8, par9);
    }

	@Override
	protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
		return null;
	}
    
}