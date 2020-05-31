package de.bigmachines.gui.elements;

import de.bigmachines.gui.GuiContainerBase;
import net.minecraft.client.renderer.GlStateManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

public class ElementSelectionButtons extends Element {
	
	public List<ElementButtonIcon> buttons;
	public int selectedIndex;
	protected Consumer<Integer> onChange;
	
	public ElementSelectionButtons(GuiContainerBase gui) {
		this(gui, -1);
	}
	
	public ElementSelectionButtons(GuiContainerBase gui, int selectedIndex) {
		super(gui, 0, 0);
		this.buttons = new ArrayList<ElementButtonIcon>();
		this.selectedIndex = selectedIndex;
	}
	
	public void setOnChange(Consumer<Integer> onChange) {
		this.onChange = onChange;
	}
	
	public void setSelectedIndex(int selectedIndex) {
		this.selectedIndex = selectedIndex;
	}
	
	public ElementButtonIcon addButton(ElementButtonIcon elementButton) {
		buttons.add(elementButton);
		
		this.posX = Collections.min(buttons, Comparator.comparing(button -> button.posX)).posX;
		this.posY = Collections.min(buttons, Comparator.comparing(button -> button.posY)).posY;
		this.sizeX = Collections.max(buttons, Comparator.comparing(button -> button.posX)).posX + 16 - this.posX;
		this.sizeY = Collections.max(buttons, Comparator.comparing(button -> button.posY)).posY + 16 - this.posY;
		
		return elementButton;
	}
	
	@Override
	public void addTooltip(int mouseX, int mouseY, List<String> tooltips) {
		for(ElementButtonIcon button : buttons) {
			if(button.intersectsWith(mouseX, mouseY)) button.addTooltip(mouseX, mouseY, tooltips);
		}
	}

	@Override
	public void drawForeground(int mouseX, int mouseY) {
		buttons.forEach(button -> {
			button.drawForeground(mouseX, mouseY);
		});
	}

	@Override
	public void drawBackground(int mouseX, int mouseY, float partialTick) {
		//gui.drawRect(this.posX - 4, this.posY - 4, this.posX + this.sizeX + 4, this.posY + this.sizeY + 4, 0x33000000);
		GlStateManager.color(1f, 1f, 1f, 1f);
		buttons.forEach(button -> {
			button.drawBackground(mouseX, mouseY, partialTick);
		});
	}

	/**
	 *
	 * @param index
	 * @param changed if set to true, update will be sent to server
	 */
	public void select(int index, boolean changed) {
		buttons.forEach(button -> {
			button.selected = false;
		});
		//System.out.println(index);
		if(index >= 0 && index < buttons.size()) buttons.get(index).selected = true;
		if(changed && onChange != null) onChange.accept(index);
	}
	
	@Override
	public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
		for(int i = 0; i < buttons.size(); i++) {
			if(buttons.get(i).mouseClicked(mouseX, mouseY, mouseButton)) {
				select(i, true);
				return true;
			}
		}
		return false;
	}
	
	
}
