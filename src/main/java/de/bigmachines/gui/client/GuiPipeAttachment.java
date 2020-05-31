package de.bigmachines.gui.client;

import de.bigmachines.BigMachines;
import de.bigmachines.Reference;
import de.bigmachines.blocks.blocks.pipes.TileEntityPipeBase;
import de.bigmachines.blocks.blocks.pipes.TileEntityPipeBase.PipeAttachment;
import de.bigmachines.gui.GuiContainerBase;
import de.bigmachines.gui.container.ContainerPipeAttachment;
import de.bigmachines.gui.elements.ElementButtonIcon;
import de.bigmachines.gui.elements.ElementSelectionButtons;
import de.bigmachines.gui.elements.ElementSwitchButton;
import de.bigmachines.gui.elements.tabs.Tab;
import de.bigmachines.gui.elements.tabs.TabRedstoneControl;
import de.bigmachines.network.messages.MessageChangePipeAttachmentMode;
import de.bigmachines.utils.classes.EnumSide;
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
		elementSelectionButtons.addButton(new ElementButtonIcon(this, 56 + 00, 18, 48, 16)).tooltips.add("In- and Output");
		elementSelectionButtons.addButton(new ElementButtonIcon(this, 56 + 16, 18, 16, 16)).tooltips.add("Output");
		elementSelectionButtons.addButton(new ElementButtonIcon(this, 56 + 32, 18, 32, 16)).tooltips.add("Input");
		elementSelectionButtons.addButton(new ElementButtonIcon(this, 56 + 48, 18, 0, 16)).tooltips.add("Ignored");

		elementSwitchWhiteBlackButton = (ElementSwitchButton) addElement(new ElementSwitchButton(this, 26, 43, 80, 16, 64, 16));
		
        if(!tileEntityPipeBase.hasAttachment(side)) this.mc.player.closeScreen();
        final PipeAttachment attachment = tileEntityPipeBase.getAttachment(side);
		
		addTab(new TabRedstoneControl(this, EnumSide.RIGHT, attachment, () -> attachment.sendUpdateToServer(tileEntityPipeBase.getPos(), side)));
        
		if(attachment.canExtract() && attachment.canInsert()) {
			elementSelectionButtons.select(0, false);
		} else if(attachment.canExtract()) {
			elementSelectionButtons.select(1, false);
		} else if(attachment.canInsert()) {
			elementSelectionButtons.select(2, false);
		} else {
			elementSelectionButtons.select(3, false);
		}
		
		elementSwitchWhiteBlackButton.setSwitched(attachment.isWhitelist());
		elementSwitchWhiteBlackButton.normalTooltips.add("Whitelist");
		elementSwitchWhiteBlackButton.switchedTooltips.add("Blacklist");
		
		elementSelectionButtons.setOnChange((index) -> {
			attachment.setInsertationByIndex(index);
			attachment.sendUpdateToServer(tileEntityPipeBase.getPos(), side);
			//BigMachines.networkHandlerMain.sendToServer(new MessageChangePipeAttachmentMode(tileEntityPipeBase.getPos(), side, attachment.getRedstoneMode(), attachment.isWhitelist(), attachment.canExtract(), attachment.canInsert()));
		});
		elementSwitchWhiteBlackButton.setOnChanged((switched) -> {
			attachment.setWhitelist(switched);
			attachment.sendUpdateToServer(tileEntityPipeBase.getPos(), side);
			//BigMachines.networkHandlerMain.sendToServer(new MessageChangePipeAttachmentMode(tileEntityPipeBase.getPos(), side, attachment.getRedstoneMode(), attachment.isWhitelist(), attachment.canExtract(), attachment.canInsert()));
		});
	}
	
	@Override
	public void updateElements() {
		super.updateElements();
		
        if(tileEntityPipeBase.isInvalid() || !tileEntityPipeBase.hasAttachment(side)) this.mc.player.closeScreen();
        final PipeAttachment attachment = tileEntityPipeBase.getAttachment(side);
        if(attachment == null) this.mc.player.closeScreen();
        
		if(attachment.canExtract() && attachment.canInsert()) {
			elementSelectionButtons.select(0, false);
		} else if(attachment.canExtract()) {
			elementSelectionButtons.select(1, false);
		} else if(attachment.canInsert()) {
			elementSelectionButtons.select(2, false);
		} else {
			elementSelectionButtons.select(3, false);
		}
		
		
	}
	
}
