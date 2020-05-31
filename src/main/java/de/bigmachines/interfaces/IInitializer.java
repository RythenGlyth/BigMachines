package de.bigmachines.interfaces;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IInitializer {
	
	@SideOnly(Side.CLIENT)
    void postRegister();
	
}
