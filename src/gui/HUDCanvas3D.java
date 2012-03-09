package gui;

import java.awt.Color;
import java.awt.GraphicsConfiguration;

import javax.media.j3d.Canvas3D;
import javax.media.j3d.J3DGraphics2D;

/** Display the HUD in post-render. The HUD display the current frame per second. */
public class HUDCanvas3D extends Canvas3D {
	
	private static final long serialVersionUID = -6935789848946912717L;
	private static final int FPSSmoothingPeriod = 100;
	
	private long[] frametime = new long[FPSSmoothingPeriod];
	private boolean HUDEnabled = true;

	public HUDCanvas3D(GraphicsConfiguration graphicsConfiguration) {
		super(graphicsConfiguration);
		long currentMillis = System.currentTimeMillis();
		for(int x = 0; x < FPSSmoothingPeriod; x++) {
			frametime[x] = currentMillis;
		}
	}
	
	public void postRender() {
		if(!HUDEnabled)
			return;
		
		/* compute a moving average of the frame per second */
		int frameindex = (int) getView().getFrameNumber() % FPSSmoothingPeriod;
		long lastMillis = frametime[frameindex];
		frametime[frameindex] = System.currentTimeMillis();
		int frameRate = (int) ((1000.0f * FPSSmoothingPeriod) / ((frametime[frameindex] - lastMillis)));
		
		J3DGraphics2D g2d = this.getGraphics2D();
		g2d.setColor(Color.white);
		g2d.drawString("FPS: " + frameRate + " ",0,12);
		g2d.flush(false);
    }
	
	public boolean isHUDEnabled() {
		return HUDEnabled;
	}

	public void setHUDEnabled(boolean hUDEnabled) {
		HUDEnabled = hUDEnabled;
	}
}
