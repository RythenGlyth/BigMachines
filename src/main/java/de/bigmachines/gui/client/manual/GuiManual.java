package de.bigmachines.gui.client.manual;

import de.bigmachines.Reference;
import de.bigmachines.config.ManualLoader;
import de.bigmachines.utils.RenderHelper;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
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
    private static final int guiWidth = 394;
    private static final int guiHeight = 221;
    private static final int tabWidth = 30;
    private static final int tabHeight = 28;
    private static final int maxTabs = 6;
    private static final int firstTabOffset = (guiHeight - maxTabs * tabHeight) / 2;
    private static int guiLeft;
    private static int guiTop;
    
    private final List<String> tooltips;
    
    private static final String nbtIndex = "openedPage";
    
    private int selectedIndex;
    private int scrollIndexOffset;
    
    private final ItemStack item;
    
    public GuiManual(ItemStack item) {
		super();
		this.item = item;
		tooltips = new LinkedList<>();

		selectedIndex = item.hasTagCompound() && item.getTagCompound().hasKey(nbtIndex) ? item.getTagCompound().getInteger(nbtIndex) : 0;

		selectedIndex = Math.min(Math.max(0, ManualLoader.getTabs().size() - 1), selectedIndex);
		selectedIndex = Math.max(0, selectedIndex);
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
		tag.setInteger(nbtIndex, selectedIndex);
		item.setTagCompound(tag);
	}
	
	private void drawForeground(int mouseX, int mouseY, float partialTicks) {
		final List<ManualTab> tabs = ManualLoader.getTabs();
		for (int i = scrollIndexOffset; i < tabs.size() && i - scrollIndexOffset < maxTabs; i++) {
			mc.getTextureManager().bindTexture(tabs.get(i - scrollIndexOffset).getIcon());
			RenderHelper.drawTexturedModalRect(guiLeft - tabWidth + 2 + 8, guiTop + firstTabOffset + (i - scrollIndexOffset) * tabHeight + 5, 16, 16, 0, 0, 16, 16, zLevel + 1);
		}

		GlStateManager.pushMatrix();
		GlStateManager.disableDepth();
		GL11.glEnable(GL11.GL_STENCIL_TEST);
		GlStateManager.colorMask(false, false, false, false);
		GlStateManager.depthMask(false);
		GL11.glStencilFunc(GL11.GL_NEVER, 1, 0xFF);
		GL11.glStencilOp(GL11.GL_REPLACE, GL11.GL_KEEP, GL11.GL_KEEP);

		GL11.glStencilMask(0xFF);
		GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);
		// draw mask:
		Gui.drawRect(guiLeft, guiTop, guiLeft + guiWidth, guiTop + guiHeight, 0xffffffff);

		GlStateManager.colorMask(true, true, true, true);
		GlStateManager.depthMask(true);
		GL11.glStencilMask(0x00);
		GL11.glStencilFunc(GL11.GL_EQUAL, 0, 0xFF); // draw only where stencil mask is 0
		// nothing to draw
		GL11.glStencilFunc(GL11.GL_EQUAL, 1, 0xFF);
		Gui.drawRect(100, 200, 200, 300, 0xff550000);
		GL11.glDisable(GL11.GL_STENCIL_TEST);
//		GlStateManager.colorMask(true, true, true, false);
//		GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		// draw background:
//		Gui.drawRect(100, 200, 200, 300, 0xff550000);

		int last = guiTop + 10;
		if (tabs.size() > selectedIndex && tabs.get(selectedIndex) != null) {
			for (final ManualContent manualContent : tabs.get(selectedIndex).getContents()) {
				try {
					//last = manualContent.draw(guiLeft + 10, last, mouseX, mouseY, width, partialTicks, zLevel + 1, tooltips);
					if (last > guiTop + guiHeight) break;
				} catch (RuntimeException ex) {
					System.out.println("Could not render mc:");
					ex.printStackTrace();
				}
			}
		}

//        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ZERO);
//        GlStateManager.colorMask( false, false, false, true);
		// draw mask:
//		GlStateManager.clearColor(0, 0, 0, 0);
//		GlStateManager.clear(GL11.GL_COLOR_BUFFER_BIT);
//

//		GlStateManager.colorMask(true, true, true, true);
//		GlStateManager.blendFunc(GlStateManager.SourceFactor.DST_ALPHA, GlStateManager.DestFactor.ONE_MINUS_DST_ALPHA);
//		GlStateManager.enableBlend();
//		GlStateManager.glBlendEquation(GL11.GL_ADD);
//		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.ONE_MINUS_DST_ALPHA, GlStateManager.DestFactor.DST_ALPHA,
//				GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
		// draw foreground:

		Gui.drawRect(90, 180, 220, 320, 0xff000000);


//		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
		
		drawHoveringText(tooltips, mouseX, mouseY);
	}

	private void drawScrollbar() {
		mc.getTextureManager().bindTexture(background);
	}
    
	private void drawBackground(int mouseX, int mouseY, float partialTicks) {
		mc.getTextureManager().bindTexture(background);
		
		final List<ManualTab> tabs = ManualLoader.getTabs();
		for (int i = scrollIndexOffset; i < tabs.size() && i - scrollIndexOffset < maxTabs; i++) {
			RenderHelper.drawTexturedModalRect(guiLeft - tabWidth + 2, guiTop + firstTabOffset + (i - scrollIndexOffset) * tabHeight, tabWidth, tabHeight - 1, 447,
					selectedIndex == i ? 0 : 27, 512, 512, selectedIndex == i ? zLevel + 1 : zLevel);
		}

		RenderHelper.drawTexturedModalRect(guiLeft, guiTop, guiWidth, guiHeight, 0, 0, 512, 512, zLevel);
		
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
	
	private static boolean checkIfValidOffset(final int offset) {
		final int len = ManualLoader.getTabs().size() - maxTabs;
		if (offset < 0) return false;
		return offset < len;
	}
	
	private void scrollDown() {
		if (checkIfValidOffset(scrollIndexOffset + 1))
			scrollIndexOffset++;
	}
	
	private void scrollUp() {
		if (checkIfValidOffset(scrollIndexOffset - 1))
			scrollIndexOffset--;
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		
		if (mouseX > guiLeft - tabWidth + 3 && mouseX < guiLeft + 3
				&& mouseY > guiTop + firstTabOffset && mouseY < guiTop + guiHeight - firstTabOffset) {
			selectedIndex = (mouseY - guiTop - firstTabOffset) / tabHeight + scrollIndexOffset;
			mc.player.playSound(SoundEvents.UI_BUTTON_CLICK, 1.0F, 1.0F);
		} else if (mouseX > guiLeft - tabWidth - 6 && mouseX < guiLeft - tabWidth + 6 + 17
				&& mouseY > guiTop + 4 && mouseY < guiTop + 4 + 17) {
			mc.player.playSound(SoundEvents.UI_BUTTON_CLICK, 1.0F, 1.0F);
			//scrollUp();
			selectedIndex = Math.max(selectedIndex - 1, 0);
		} else if (mouseX > guiLeft - tabWidth - 6 && mouseX < guiLeft - tabWidth + 6 + 17
				&& mouseY > guiTop + guiHeight - 4 - 22 && mouseY < guiTop + guiHeight - 4) {
			mc.player.playSound(SoundEvents.UI_BUTTON_CLICK, 1.0F, 1.0F);
			//scrollDown();
			selectedIndex = Math.min(selectedIndex + 1, Math.max(ManualLoader.getTabs().size() - 1, 0));
		} else if (mouseX > guiLeft + 3 && mouseX < guiLeft + guiWidth
				&& mouseY > guiTop && mouseY < guiTop + guiHeight) {
			// TODO
		}
		
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
