package render;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

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

	public FittingRenderer(Callback callback, FittingScene scene, BufferedImage target) {
		this.callback = callback;
		this.scene = scene;
		this.target = target;
	}

	@Override
	public void simpleInitApp() {
		settings.setWidth(target.getWidth());
		settings.setHeight(target.getHeight());
		settings.setAudioRenderer(null);
		this.setPauseOnLostFocus(false);

		cpuBuf = BufferUtils.createByteBuffer(target.getWidth() * target.getHeight() * 7);
		renderOutput = new BufferedImage(target.getWidth(),target.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);

		scene.createScene(rootNode, cam, assetManager, viewPort);

		flyCam.setEnabled(false);
		cam.setFrustumPerspective(45, settings.getWidth() / settings.getHeight(), 0.1f, 100f);

		viewPort.addProcessor(this);
		this.restart();
	}

	@Override
	public void simpleUpdate(float tpf) {
		// modelVersion = model.updateMesh(modelVersion, mesh);
	}

	public BufferedImage getRender() {
		BufferedImage image = new BufferedImage(target.getWidth(),
				target.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		synchronized (cpuBuf) {
			Screenshots.convertScreenShot(cpuBuf, image);
		}
		return image;
	}

	@Override
	public void postFrame(FrameBuffer out) {
		cpuBuf.clear();
		renderer.readFrameBuffer(out, cpuBuf);
		Screenshots.convertScreenShot(cpuBuf, renderOutput);

		callback.rendererCallback();
		/*try {
			ImageIO.write(getRender(), "png", new File("out.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}

	@Override
	public void cleanup() {
	}

	@Override
	public void initialize(RenderManager rm, ViewPort vp) {	}

	@Override
	public boolean isInitialized() {
		return true;
	}

	@Override
	public void preFrame(float tpf) {}

	@Override
	public void postQueue(RenderQueue rq) {	}

	@Override
	public void reshape(ViewPort vp, int w, int h) {}

}
