package hunternif.mc.atlas.client.gui.core;

import hunternif.mc.atlas.util.AtlasRenderHelper;

public class GuiVScrollbar extends AGuiScrollbar {
	
	public GuiVScrollbar(GuiViewport viewport) {
		super(viewport);
	}

	@Override
	protected void drawAnchor() {
		// Draw top cap:
		AtlasRenderHelper.drawTexturedRect(texture, getGuiX(), getGuiY() + anchorPos, 0, 0,
				textureWidth, capLength, textureWidth, textureHeight);
		
		// Draw body:
		AtlasRenderHelper.drawTexturedRect(texture, getGuiX(), getGuiY() + anchorPos + capLength,
				0, capLength, textureWidth, textureBodyLength, textureWidth, textureHeight, 1, bodyTextureScale);
		
		// Draw bottom cap:
		AtlasRenderHelper.drawTexturedRect(texture,
				getGuiX(), getGuiY() + anchorPos + anchorSize - capLength,
				0, textureHeight - capLength,
				textureWidth, capLength, textureWidth, textureHeight);
	}

	@Override
	protected int getTextureLength() {
		return textureHeight;
	}
	
	@Override
	protected int getScrollbarLength() {
		return getHeight();
	}

	@Override
	protected int getViewportSize() {
		return viewport.getHeight();
	}

	@Override
	protected int getContentSize() {
		return viewport.contentHeight;
	}

	@Override
	protected int getMousePos(int mouseX, int mouseY) {
		return mouseY - getGuiY();
	}

	@Override
	protected void updateContentPos() {
		viewport.content.setRelativeCoords(viewport.content.getRelativeX(), - scrollPos);
	}

	@Override
	protected void setScrollbarWidth(int textureWidth, int textureHeight) {
		setSize(textureWidth, getHeight());
	}
	
}
