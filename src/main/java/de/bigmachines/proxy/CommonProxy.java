package de.bigmachines.proxy;

import de.bigmachines.interfaces.IModelRegister;

/**
 * 
 * @author RythenGlyth
 *
 */

public interface CommonProxy {
	
	public void preInit();
	
	public void init();
	
	public void postInit();

	public void addIModelRegister(IModelRegister modelRegister);
	
}
