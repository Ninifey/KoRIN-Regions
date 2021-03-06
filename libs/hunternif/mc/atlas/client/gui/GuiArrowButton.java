package hunternif.mc.atlas.client.gui;

import hunternif.mc.atlas.client.Textures;
import hunternif.mc.atlas.client.gui.core.GuiComponentButton;
import hunternif.mc.atlas.util.AtlasRenderHelper;
import net.minecraft.client.renderer.RenderHelper;

import org.lwjgl.opengl.GL11;

public class GuiArrowButton extends GuiComponentButton {
	public static final int WIDTH = 12;
	public static final int HEIGHT = 12;
	private static final int IMAGE_WIDTH = 24;
	private static final int IMAGE_HEIGHT = 24;
	
	public static enum ArrowDirection {
		UP("Up"), DOWN("Down"), LEFT("Left"), RIGHT("Right");
		
		public String description;
		ArrowDirection(String text) {
			this.description = text;
		}
	}
	
	public ArrowDirection direction;
	
	public GuiArrowButton(ArrowDirection direction) {
		setSize(WIDTH, HEIGHT);
		this.direction = direction;
	}
	
	public static GuiArrowButton up() {
		return new GuiArrowButton(ArrowDirection.UP);
	}
	public static GuiArrowButton down() {
		return new GuiArrowButton(ArrowDirection.DOWN);
	}
	public static GuiArrowButton left() {
		return new GuiArrowButton(ArrowDirection.LEFT);
	}
	public static GuiArrowButton right() {
		return new GuiArrowButton(ArrowDirection.RIGHT);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTick) {
		RenderHelper.disableStandardItemLighting();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		int x = getGuiX(), y = getGuiY();
		if (isMouseOver) {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		} else {
			// Fade out when the mouse is far from them:
			int distanceSq = (mouseX - x - getWidth()/2)*(mouseX - x - getWidth()/2) +
					(mouseY - y - getHeight()/2)*(mouseY - y - getHeight()/2);
			double alpha = distanceSq < 400 ? 0.5 : Math.pow((double)distanceSq, -0.28);
			GL11.glColor4d(1, 1, 1, alpha);
		}
		
		int u = 0, v = 0;
		switch (direction) {
		case LEFT: u = 0; v = 0; break;
		case RIGHT: u = 0; v = 12; break;
		case UP: u = 12; v = 0; break;
		case DOWN: u = 12; v = 12; break;
		}
		AtlasRenderHelper.drawTexturedRect(Textures.BTN_ARROWS, x, y, u, v, WIDTH, HEIGHT, IMAGE_WIDTH, IMAGE_HEIGHT);
		
		GL11.glDisable(GL11.GL_BLEND);
	}
}
