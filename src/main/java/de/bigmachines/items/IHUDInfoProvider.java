package de.bigmachines.items;

import de.bigmachines.config.Config.HUDPostitions;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Interface for Items which provide a hud
 * @author RythenGlyth
 *
 */

public interface IHUDInfoProvider {

	@SideOnly(Side.CLIENT)
	/**
	 * @param item
	 * @param hudPosition
	 * @param offset the offset (height of the elements drawn before)
	 * @param partialTicks
	 * @return the height of the element
	 */
	int drawHUDInfo(ItemStack item, HUDPostitions hudPosition, int offset, float partialTicks);
	
}
