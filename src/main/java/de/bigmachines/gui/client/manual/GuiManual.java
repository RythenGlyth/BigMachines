package de.bigmachines.gui.client.manual;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import de.bigmachines.Reference;
import de.bigmachines.config.ManualLoader;
import de.bigmachines.utils.RenderHelper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class GuiManual extends GuiScreen {
    private static final ResourceLocation background = new ResourceLocation(Reference.MOD_ID, "textures/gui/manual.png");
    private static final int guiWidth = 395;
    private static final int guiHeight = 222;
    private static final int tabWidth = 30;
    private static final int tabHeight = 28;
    private static final int maxTabs = 6;
    private static final int firstTabOffset = (guiHeight - maxTabs * tabHeight) / 2;
    private static int guiLeft;
    private static int guiTop;
    
    private List<String> tooltips;
    
    private static final String nbtIndex = "openedPage";
    
    private int selectedIndex;
    private int scrollIndexOffset;
    
    final private ItemStack item;
    
    public GuiManual(ItemStack item) {
    	this.item = item;
    	this.tooltips = new LinkedList<String>();
    	
    	this.selectedIndex = item.hasTagCompound() && item.getTagCompound().hasKey(nbtIndex) ? item.getTagCompound().getInteger(nbtIndex) : 0;
    }
    
    // TODO tooltip
    
    @Override
    public void initGui() {
        guiLeft = (width - guiWidth) / 2;
        guiTop = (height - guiHeight) / 2;
    }
    
    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
    	super.keyTyped(typedChar, keyCode);
    }
    
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		tooltips.clear();
        GlStateManager.color(1, 1, 1, 1);
        drawDefaultBackground();
        drawBackground(mouseX, mouseY, partialTicks);
        drawForeground(mouseX, mouseY, partialTicks);
	}
	
	/**
	 * Save current page on gui close.
	 */
	@Override
	public void onGuiClosed() {
		NBTTagCompound tag = item.hasTagCompound() ? item.getTagCompound() : new NBTTagCompound();
		tag.setInteger(nbtIndex, selectedIndex);
		item.setTagCompound(tag);
	}
	
	private void drawForeground(int mouseX, int mouseY, float partialTicks) {
		final List<ManualTab> tabs = ManualLoader.getTabs();
		for (int i = scrollIndexOffset; i < tabs.size() && i - scrollIndexOffset < maxTabs; i++) {
			this.mc.getTextureManager().bindTexture(tabs.get(i - scrollIndexOffset).getIcon());
			RenderHelper.drawTexturedModalRect(guiLeft - tabWidth + 3 + 8, guiTop + firstTabOffset + (i - scrollIndexOffset) * tabHeight + 5, 16, 16, 0, 0, 16, 16, zLevel + 2);
		}
		
		drawHoveringText(tooltips, mouseX, mouseY);
	}
    
	private void drawBackground(int mouseX, int mouseY, float partialTicks) {
		this.mc.getTextureManager().bindTexture(background);
		RenderHelper.drawTexturedModalRect(guiLeft, guiTop, guiWidth, guiHeight, 0, 0, 512, 512, zLevel + 1);
		
		final List<ManualTab> tabs = ManualLoader.getTabs();
		for (int i = scrollIndexOffset; i < tabs.size() && i - scrollIndexOffset < maxTabs; i++) {
			RenderHelper.drawTexturedModalRect(guiLeft - tabWidth + 3, guiTop + firstTabOffset + (i - scrollIndexOffset) * tabHeight, tabWidth, tabHeight - 1, 447,
					selectedIndex == i ? 0 : 27, 512, 512, selectedIndex == i ? zLevel + 1 : zLevel);
		}
		
		RenderHelper.drawTexturedModalRect(guiLeft - tabWidth + 6, guiTop + 4, 22, 17, 479, 0, 512, 512, zLevel);

		RenderHelper.drawTexturedModalRectOnHead(guiLeft - tabWidth + 6, guiTop + guiHeight - 4, 22, 17, 479, 0, 512, 512, 0);

		if (mouseX > guiLeft - tabWidth + 3 && mouseX < guiLeft + 3
				&& mouseY > guiTop + firstTabOffset && mouseY < guiTop + guiHeight - firstTabOffset) {
			final int tab = (mouseY - guiTop - firstTabOffset) / tabHeight + scrollIndexOffset;
			if(tabs.size() > tab && tab >= 0) tooltips.add(tabs.get(tab).getTitle());
		}
	}
	
	@Override
	public void handleMouseInput() throws IOException {
		int mouseX = (Mouse.getEventX() * width / mc.displayWidth) - guiLeft;
		int mouseY = (height - Mouse.getEventY() * height / mc.displayHeight - 1)  - guiTop;

		int dWheel = Mouse.getEventDWheel(); 
		if (dWheel != 0) {
			if (mouseX > - tabWidth + 3 && mouseX < 3
					&& mouseY > firstTabOffset && mouseY < guiHeight - firstTabOffset) {
				// TODO needs max
				if (dWheel < 0) scrollDown();
				else scrollUp();

			}
		}
		super.handleMouseInput();
	}
	
	private void scrollDown() {
		scrollIndexOffset = Math.min(ManualLoader.getTabs().size() - maxTabs, scrollIndexOffset + 1);
	}
	
	private void scrollUp() {
		scrollIndexOffset = Math.max(0, scrollIndexOffset - 1);
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		
		if (mouseX > guiLeft - tabWidth + 3 && mouseX < guiLeft + 3
				&& mouseY > guiTop + firstTabOffset && mouseY < guiTop + guiHeight - firstTabOffset) {
			final int tab = (mouseY - guiTop - firstTabOffset) / tabHeight + scrollIndexOffset;
			selectedIndex = tab;
		} else if (mouseX > guiLeft - tabWidth - 6 && mouseX < guiLeft - tabWidth + 6 + 17
				&& mouseY > guiTop + 4 && mouseY < guiTop + 4 + 17) {
			scrollUp();
		} else if (mouseX > guiLeft - tabWidth - 6 && mouseX < guiLeft - tabWidth + 6 + 17
				&& mouseY > guiTop + guiHeight - 4 - 22 && mouseY < guiTop + guiHeight - 4) {
			scrollDown();
		} else if (mouseX > guiLeft + 3 && mouseX < guiLeft + guiWidth
				&& mouseY > guiTop && mouseY < guiTop + guiHeight) {
			
		}
		
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
























