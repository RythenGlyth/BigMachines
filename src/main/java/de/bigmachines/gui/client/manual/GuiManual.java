package de.bigmachines.gui.client.manual;

import de.bigmachines.Reference;
import de.bigmachines.config.ManualLoader;
import de.bigmachines.utils.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

// TODO scrollbar
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
    
    private final List<String> tooltips;
    
    private static final String nbtIndex = "openedPage";
    
    private int selectedTabIndex;
    private int scrollIndexOffsetTabs;
    
    private int scrollIndexOffsetContent;
    
    private final ItemStack item;
    
    public GuiManual(ItemStack item) {
		super();
		this.item = item;
		tooltips = new LinkedList<>();

		selectedTabIndex = item.hasTagCompound() && item.getTagCompound().hasKey(nbtIndex) ? item.getTagCompound().getInteger(nbtIndex) : 0;

		selectedTabIndex = Math.min(Math.max(0, ManualLoader.getTabs().size() - 1), selectedTabIndex);
		selectedTabIndex = Math.max(0, selectedTabIndex);
	}
    
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
		tag.setInteger(nbtIndex, selectedTabIndex);
		item.setTagCompound(tag);
	}
	
	private void drawForeground(int mouseX, int mouseY, float partialTicks) {
		ScaledResolution sr = new ScaledResolution(mc);
		final List<ManualTab> tabs = ManualLoader.getTabs();
		for (int i = scrollIndexOffsetTabs; i < tabs.size() && i - scrollIndexOffsetTabs < maxTabs; i++) {
			mc.getTextureManager().bindTexture(tabs.get(i - scrollIndexOffsetTabs).getIcon());
			RenderHelper.drawTexturedModalRect(guiLeft - tabWidth + 2 + 8, guiTop + firstTabOffset + (i - scrollIndexOffsetTabs) * tabHeight + 5, 16, 16, 0, 0, 16, 16, zLevel + 1);
		}
		
		GL11.glScissor((guiLeft + 2) * sr.getScaleFactor(), (sr.getScaledHeight() - guiTop - guiHeight + 4) * sr.getScaleFactor(), (guiWidth - 6) * sr.getScaleFactor(), (guiHeight - 6) * sr.getScaleFactor());
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		
		int last = guiTop + 4 - scrollIndexOffsetContent;
		if (tabs.size() > selectedTabIndex && tabs.get(selectedTabIndex) != null) {
			for (final ManualContent manualContent : tabs.get(selectedTabIndex).getContents()) {
				try {
					last = manualContent.draw(guiLeft + 4, last, mouseX, mouseY, width, partialTicks, zLevel + 1, tooltips);
					if (last > guiTop + guiHeight - 6) break;
				} catch (RuntimeException ex) {
					System.out.println("Could not render mc:");
					ex.printStackTrace();
				}
			}
		}
		
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
		
		drawHoveringText(tooltips, mouseX, mouseY);
	}

	private void drawScrollbar() {
		mc.getTextureManager().bindTexture(background);
	}
    
	private void drawBackground(int mouseX, int mouseY, float partialTicks) {
		mc.getTextureManager().bindTexture(background);
		
		final List<ManualTab> tabs = ManualLoader.getTabs();
		for (int i = scrollIndexOffsetTabs; i < tabs.size() && i - scrollIndexOffsetTabs < maxTabs; i++) {
			RenderHelper.drawTexturedModalRect(guiLeft - tabWidth + 2, guiTop + firstTabOffset + (i - scrollIndexOffsetTabs) * tabHeight, tabWidth, tabHeight - 1, 447,
					selectedTabIndex == i ? 0 : 27, 512, 512, selectedTabIndex == i ? zLevel + 1 : zLevel);
		}

		RenderHelper.drawTexturedModalRect(guiLeft, guiTop, guiWidth, guiHeight, 0, 0, 512, 512, zLevel);
		
		RenderHelper.drawTexturedModalRect(guiLeft - tabWidth + 6, guiTop + 4, 22, 17, 479, 0, 512, 512, zLevel);

		RenderHelper.drawTexturedModalRectOnHead(guiLeft - tabWidth + 6, guiTop + guiHeight - 4, 22, 17, 479, 0, 512, 512, 0);

		if (mouseX > guiLeft - tabWidth + 3 && mouseX < guiLeft + 3
				&& mouseY > guiTop + firstTabOffset && mouseY < guiTop + guiHeight - firstTabOffset) {
			final int tab = (mouseY - guiTop - firstTabOffset) / tabHeight + scrollIndexOffsetTabs;
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

			} else if (mouseX >= 4 && mouseX <= guiWidth - 4
					&& mouseY >= 4 && mouseY <= guiHeight - 4) {
				if (dWheel < 0) scrollIndexOffsetContent += 10;
				else scrollIndexOffsetContent -= 10;
				if(scrollIndexOffsetContent < 0) scrollIndexOffsetContent = 0;
			}
		}
		super.handleMouseInput();
	}
	
	private static boolean checkIfValidOffset(final int offset) {
		final int len = ManualLoader.getTabs().size() - maxTabs;
		if (offset < 0) return false;
		return offset < len;
	}
	
	private void scrollDown() {
		if (checkIfValidOffset(scrollIndexOffsetTabs + 1))
			scrollIndexOffsetTabs++;
	}
	
	private void scrollUp() {
		if (checkIfValidOffset(scrollIndexOffsetTabs - 1))
			scrollIndexOffsetTabs--;
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		
		if (mouseX > guiLeft - tabWidth + 3 && mouseX < guiLeft + 3
				&& mouseY > guiTop + firstTabOffset && mouseY < guiTop + guiHeight - firstTabOffset) {
			selectedTabIndex = (mouseY - guiTop - firstTabOffset) / tabHeight + scrollIndexOffsetTabs;
			scrollIndexOffsetContent = 0;
			mc.player.playSound(SoundEvents.UI_BUTTON_CLICK, 1.0F, 1.0F);
		} else if (mouseX > guiLeft - tabWidth - 6 && mouseX < guiLeft - tabWidth + 6 + 17
				&& mouseY > guiTop + 4 && mouseY < guiTop + 4 + 17) {
			mc.player.playSound(SoundEvents.UI_BUTTON_CLICK, 1.0F, 1.0F);
			//scrollUp();
			selectedTabIndex = Math.max(selectedTabIndex - 1, 0);
			scrollIndexOffsetContent = 0;
		} else if (mouseX > guiLeft - tabWidth - 6 && mouseX < guiLeft - tabWidth + 6 + 17
				&& mouseY > guiTop + guiHeight - 4 - 22 && mouseY < guiTop + guiHeight - 4) {
			mc.player.playSound(SoundEvents.UI_BUTTON_CLICK, 1.0F, 1.0F);
			//scrollDown();
			selectedTabIndex = Math.min(selectedTabIndex + 1, Math.max(ManualLoader.getTabs().size() - 1, 0));
			scrollIndexOffsetContent = 0;
		} else if (mouseX >= guiLeft + 4 && mouseX <= guiLeft + guiWidth - 4
				&& mouseY >= guiTop + 4 && mouseY <= guiTop + guiHeight - 4) {
			
		}
		
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
