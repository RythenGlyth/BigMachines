package de.bigmachines.gui.elements;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;

import akka.actor.dsl.Inbox.Select;
import de.bigmachines.gui.GuiContainerBase;

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
	
	public ElementSelectionButtons addButton(ElementButtonIcon elementButton) {
		buttons.add(elementButton);
		return this;
	}

	@Override
	public void drawForeground(int mouseX, int mouseY) {
		buttons.forEach(button -> {
			button.drawForeground(mouseX, mouseY);
		});
	}

	@Override
	public void drawBackground(int mouseX, int mouseY, float partialTick) {
		buttons.forEach(button -> {
			button.drawBackground(mouseX, mouseY, partialTick);
		});
	}
	
	public void select(int index) {
		buttons.forEach(button -> {
			button.selected = false;
		});
		if(index >= 0 && index < buttons.size()) buttons.get(index).selected = true;
		if(onChange != null) onChange.accept(index);
	}
	
	@Override
	public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
		for(int i = 0; i < buttons.size(); i++) {
			if(buttons.get(i).mouseClicked(mouseX, mouseY, mouseButton)) {
				select(i);
			}
		}
		return false;
	}
	
	
}
