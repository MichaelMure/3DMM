package render;

import java.awt.GraphicsConfiguration;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

import javax.media.j3d.Canvas3D;
import javax.media.j3d.ImageComponent2D;

import com.sun.j3d.utils.universe.SimpleUniverse;

public class SceneRenderer {

	private final Canvas3D canvas;
	private final Scene scene;
	private final int renderWidth;
	private final int renderHeigth;
	private float[] colorsGains = {1.0f, 1.0f, 1.0f, 1.0f};
	private float[] colorsOffsets = {0.0f, 0.0f, 0.0f, 0.0f};

	public SceneRenderer(BufferedImage target) {
		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
		canvas = new Canvas3D(config, true);

		renderWidth = target.getWidth();
		renderHeigth = target.getHeight();
		canvas.getScreen3D().setSize(renderWidth, renderHeigth);
		canvas.getScreen3D().setPhysicalScreenWidth(renderWidth * 0.0254/90.0);
		canvas.getScreen3D().setPhysicalScreenHeight(renderHeigth * 0.0254/90.0);

		/* TODO: deal with render parameter */
		scene = new Scene(canvas, target, new RenderParameter());
	}

	public void updateScene(RenderParameter params) {
		colorsGains = params.getColorsGains();
		colorsOffsets = params.getColorsOffsets();
		scene.update(params);
	}

	public BufferedImage getRender() {
		BufferedImage image = new BufferedImage(renderWidth, renderHeigth, BufferedImage.TYPE_INT_ARGB);
		ImageComponent2D buffer = new ImageComponent2D(BufferedImage.TYPE_INT_ARGB, image);

		canvas.setOffScreenBuffer(buffer);
		canvas.renderOffScreenBuffer();
		canvas.waitForOffScreenRendering();
		image = canvas.getOffScreenBuffer().getImage();

		/* post-process */
		RescaleOp op = new RescaleOp(colorsGains, colorsOffsets, null);
		op.filter(image, image);

		return image;
	}

}
