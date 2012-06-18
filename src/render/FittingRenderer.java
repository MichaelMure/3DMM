package render;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import parameter.RenderParameter;

import com.jme3.app.SimpleApplication;
import com.jme3.post.SceneProcessor;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.texture.FrameBuffer;
import com.jme3.util.BufferUtils;
import com.jme3.util.Screenshots;

public class FittingRenderer extends SimpleApplication implements SceneProcessor {

	public interface Callback {
		public void rendererCallback();
	}

	private final Callback callback;
	private final FittingScene scene;
	private final BufferedImage target;

	private ByteBuffer cpuBuf;
	private BufferedImage renderOutput;

	private RenderParameter params; /* Only for init */

	public FittingRenderer(Callback callback, FittingScene scene, BufferedImage target, RenderParameter params) {
		this.callback = callback;
		this.scene = scene;
		this.target = target;
		this.params = params;
	}

	@Override
	public void simpleInitApp() {
		settings.setWidth(target.getWidth());
		settings.setHeight(target.getHeight());
		settings.setAudioRenderer(null);
		this.setPauseOnLostFocus(false);

		cpuBuf = BufferUtils.createByteBuffer(target.getWidth() * target.getHeight() * 7);
		renderOutput = new BufferedImage(target.getWidth(),target.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		scene.createScene(rootNode, cam, assetManager, viewPort, params);

		flyCam.setEnabled(false);
		cam.setFrustumPerspective(45, settings.getWidth() / settings.getHeight(), 0.1f, 100f);

		viewPort.addProcessor(this);
		this.params = null;
		this.restart();
	}

	public BufferedImage getRender() {
		BufferedImage image = new BufferedImage(target.getWidth(),
				target.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		Screenshots.convertScreenShot(cpuBuf, image);
		return image;
	}

	public void saveRender(String filename) {
		try {
			ImageIO.write(getRender(), "png", new File(filename + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void postFrame(FrameBuffer out) {
		cpuBuf.clear();
		renderer.readFrameBuffer(out, cpuBuf);
		Screenshots.convertScreenShot(cpuBuf, renderOutput);

		if(callback != null)
			callback.rendererCallback();
	}

	@Override
	public boolean isInitialized() {
		return true;
	}
	@Override
	public void simpleUpdate(float tpf) {
	}
	@Override
	public void cleanup() {}
	@Override
	public void initialize(RenderManager rm, ViewPort vp) {}
	@Override
	public void preFrame(float tpf) {}
	@Override
	public void postQueue(RenderQueue rq) {}
	@Override
	public void reshape(ViewPort vp, int w, int h) {}

}
