package pingo.una.client;

import pingo.una.common.CommonProxy;
import pingo.una.common.EntityFlyingBlock;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {
	
	@Override
	public void registerRender()
	{
        RenderingRegistry.registerEntityRenderingHandler(EntityFlyingBlock.class, new RenderEntityFlyingBlock());
	}
	
}
