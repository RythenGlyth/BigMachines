package de.bigmachines.gui.client;

import de.bigmachines.Reference;
import de.bigmachines.blocks.blocks.pipes.TileEntityPipeBase;
import de.bigmachines.blocks.blocks.pipes.TileEntityPipeBase.PipeAttachment;
import de.bigmachines.gui.GuiContainerBase;
import de.bigmachines.gui.container.ContainerPipeAttachment;
import de.bigmachines.gui.elements.ElementButtonIcon;
import de.bigmachines.gui.elements.ElementSelectionButtons;
import de.bigmachines.gui.elements.ElementSwitchButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class GuiPipeAttachment extends GuiContainerBase {
	
	TileEntityPipeBase tileEntityPipeBase;
	EnumFacing side;
	
	ElementSelectionButtons elementSelectionButtons;
	ElementSwitchButton elementSwitchWhiteBlackButton;
	
	public GuiPipeAttachment(InventoryPlayer inventory, TileEntityPipeBase tileEntityPipeBase, EnumFacing side) {
		super(new ContainerPipeAttachment(inventory, tileEntityPipeBase, side), new ResourceLocation(Reference.MOD_ID, "textures/gui/duct.png"));
		
		this.drawInventory = true;
		this.tileEntityPipeBase = tileEntityPipeBase;
		this.side = side;
		
		this.xSize = 176;
		this.ySize = 157;
		
		elementSelectionButtons = (ElementSelectionButtons) addElement(new ElementSelectionButtons(this));
		elementSelectionButtons.addButton(new ElementButtonIcon(this, 56 + 00, 18, 48, 16));
		elementSelectionButtons.addButton(new ElementButtonIcon(this, 56 + 16, 18, 16, 16));
		elementSelectionButtons.addButton(new ElementButtonIcon(this, 56 + 32, 18, 32, 16));
		elementSelectionButtons.addButton(new ElementButtonIcon(this, 56 + 48, 18, 0, 16));

		elementSwitchWhiteBlackButton = (ElementSwitchButton) addElement(new ElementSwitchButton(this, 26, 43, 80, 16, 64, 16));

        if(!tileEntityPipeBase.hasAttachment(side)) this.mc.player.closeScreen();
        final PipeAttachment attachment = tileEntityPipeBase.getAttachment(side);
        
		if(attachment.canExtract() && attachment.canInsert()) {
			elementSelectionButtons.select(0);
		} else if(attachment.canExtract()) {
			elementSelectionButtons.select(1);
		} else if(attachment.canInsert()) {
			elementSelectionButtons.select(2);
		} else {
			elementSelectionButtons.select(3);
		}
		
		elementSwitchWhiteBlackButton.setSwitched(attachment.isWhitelist());
		
		elementSelectionButtons.setOnChange((index) -> attachment.setInsertationByIndex(index));
		elementSwitchWhiteBlackButton.setOnChanged((switched) -> attachment.setWhitelist(switched));
	}
	
	
	
}
