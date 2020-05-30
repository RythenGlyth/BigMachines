package de.bigmachines.gui.elements.tabs;

import java.util.concurrent.Callable;

import de.bigmachines.gui.GuiContainerBase;
import de.bigmachines.gui.elements.ElementButtonIcon;
import de.bigmachines.gui.elements.ElementSelectionButtons;
import de.bigmachines.utils.classes.EnumSide;
import de.bigmachines.utils.classes.IHasRedstoneControl;
import de.bigmachines.utils.classes.RedstoneMode;
import net.minecraft.tileentity.TileEntity;

public class TabRedstoneControl extends Tab {
	
	public IHasRedstoneControl redstoneControl;
	public ElementSelectionButtons elementSelectionButtons;
	
	public TabRedstoneControl(GuiContainerBase gui, EnumSide side, IHasRedstoneControl redstoneControl, Runnable sendUpdateToServer) {
		super(gui, side);
		this.maxHeight = 60;
		
		this.redstoneControl = redstoneControl;
		this.name = "Redstone Control";
		setColor(0xffff0000);
		
		elementSelectionButtons = (ElementSelectionButtons)addElement(new ElementSelectionButtons(gui));
		
		elementSelectionButtons.addButton(new ElementButtonIcon(gui, maxWidth / 2 - 24, 24, 112, 16)).tooltips.add("Ignored");
		elementSelectionButtons.addButton(new ElementButtonIcon(gui, maxWidth / 2 - 8, 24, 128, 16)).tooltips.add("High Power");
		elementSelectionButtons.addButton(new ElementButtonIcon(gui, maxWidth / 2 + 8, 24, 144, 16)).tooltips.add("Low Power");
		
		elementSelectionButtons.select(redstoneControl.getRedstoneMode().ordinal());
		
		elementSelectionButtons.setOnChange((index) -> {
			redstoneControl.setRedstoneMode(RedstoneMode.values()[index]);
			sendUpdateToServer.run();
		});
	}
	
	@Override
	public void update(int mouseX, int mouseY, int offsetY) {
		super.update(mouseX, mouseY, offsetY);
		
		elementSelectionButtons.select(redstoneControl.getRedstoneMode().ordinal());
	}
	
}
