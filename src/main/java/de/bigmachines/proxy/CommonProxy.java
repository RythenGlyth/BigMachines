package de.bigmachines.proxy;

import de.bigmachines.interfaces.IModelRegister;

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
	
}
