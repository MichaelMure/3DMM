package render;

import java.awt.image.BufferedImage;

import com.jme3.scene.Mesh;

import util.Log;
import util.Log.LogType;

import model.MorphableModel;

public class FittingStrategy implements FittingRenderer.Callback {

	private final BufferedImage target;
	private final FittingScene scene;
	private final FittingRater rater;
	private final FittingRenderer renderer;
	private final MorphableModel mm;
	private final Mesh mesh;

	private RenderParameter lastParams;
	private RenderParameter newParams;

	public FittingStrategy(MorphableModel mm, BufferedImage target) {
		this.mm = mm;
		this.target = target;
		Log.info(LogType.FITTING, "Creating the fitting strategy.");
		this.lastParams = new RenderParameter();
		Log.info(LogType.FITTING, "Creating the average mesh.");
		this.mesh = mm.getAverage().getMesh();
		Log.info(LogType.FITTING, "Creating the scene.");
		this.scene = new FittingScene(mesh, lastParams);
		Log.info(LogType.FITTING, "Creating the rater.");
		this.rater = new FittingRater(target);
		Log.info(LogType.FITTING, "Creating the renderer.");
		this.renderer = new FittingRenderer(this, scene, target);
		this.renderer.setShowSettings(false);
	}

	public void start() {
		this.renderer.start();
	}

	@Override
	public void rendererCallback() {


		/*
		rater.setRender(renderOutput);
		Log.info(LogType.MODEL, "Rate: " + rater.getRate());
		Log.info(LogType.MODEL, "Pixels: " + rater.getNbPixels());
		Log.info(LogType.MODEL, "Ratio: " + rater.getRatio());
		*/

	}



}
