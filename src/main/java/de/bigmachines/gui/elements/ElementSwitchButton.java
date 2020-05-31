package de.bigmachines.gui.elements;

import de.bigmachines.gui.GuiContainerBase;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ElementSwitchButton extends ElementButtonIcon {
	
	protected final int switchU;
	protected final int switchV;
	
	protected final int normalU;
	protected final int normalV;
	
	boolean switched;
	
	protected Consumer<Boolean> onChanged;
	
	public final List<String> normalTooltips = new ArrayList<>();
	public final List<String> switchedTooltips = new ArrayList<>();
	
	public ElementSwitchButton(GuiContainerBase gui, int posX, int posY, int normalU, int normalV, int switchU, int switchV) {
		super(gui, posX, posY, normalU, normalV);
		this.normalU = normalU;
		this.normalV = normalV;
		this.switchU = switchU;
		this.switchV = switchV;
	}
	
	public void setOnChanged(Consumer<Boolean> onChanged) {
		this.onChanged = onChanged;
	}
	
	public void setSwitched(boolean switched) {
		this.switched = switched;
	}
	
	public void doSwitch() {
		switched = !switched;
		onChanged.accept(switched);
		tooltips.clear();
		tooltips.addAll(switched ? switchedTooltips : normalTooltips);
	}
	
	@Override
	public void drawForeground(int mouseX, int mouseY) {
		if(switched) {
			u = switchU;
			v = switchV;
		} else {
			u = normalU;
			v = normalV;
		}
		super.drawForeground(mouseX, mouseY);
	}
	
	@Override
	public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
		boolean clicked = super.mouseClicked(mouseX, mouseY, mouseButton);
		if(clicked) {
			doSwitch();
		}
		return clicked;
	}
	
}
