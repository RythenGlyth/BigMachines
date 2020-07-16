package de.bigmachines.proxy;

import de.bigmachines.interfaces.IModelRegister;
import net.minecraft.block.BlockLeaves;

/**
 * 
 * @author RythenGlyth
 *
 */

public interface CommonProxy {
	
	void preInit();
	
	void init();
	
	void postInit();

	void addIModelRegister(IModelRegister modelRegister);
	
	void setGraphicsLevel(BlockLeaves block, boolean fancyEnabled);
	
}
