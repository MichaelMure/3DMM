package render;

import java.awt.image.BufferedImage;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import com.jme3.scene.Mesh;

import util.Log;
import util.Log.LogType;

import model.ModelParameter;
import model.MorphableModel;

public class FittingStrategy implements FittingRenderer.Callback {

	private BufferedImage target;
	private FittingScene scene;
	private FittingRater rater;
	private FittingRenderer renderer;
	private MorphableModel mm;
	private Mesh mesh;

	private RenderParameter renderParamsRef;
	private RenderParameter renderParamsCurrent;
	private RenderParameter renderParamsNext;
	private RenderParameter renderParamsStart;
	private ModelParameter modelParamsRef;
	private ModelParameter modelParamsCurrent;
	private ModelParameter modelParamsNext;

	private enum State {Reference, RenderParams, VerticeParams, TextureParams};
	private State state;
	private int step;
	private int superStep = 0;
	private double errorRef;
	private double ratio = 1.001;
	private double lambda = 0.002;
	private double sigma = 100;

	PrintWriter out;

	public FittingStrategy(MorphableModel mm, BufferedImage target) {
		this.mm = mm;
		this.target = target;
	}

	public void start() {
		Log.info(LogType.FITTING, "Creating the fitting strategy.");
		Log.info(LogType.FITTING, "Creating the average mesh.");
		this.mesh = mm.getAverage().getMesh();
		Log.info(LogType.FITTING, "Creating the scene.");
		this.scene = new FittingScene(mesh);
		Log.info(LogType.FITTING, "Creating the rater.");
		this.rater = new FittingRater(target);

		Log.info(LogType.FITTING, "Initialize parameters.");
		this.renderParamsRef = new RenderParameter();
		renderParamsRef.initObjectScale(mesh);
		this.renderParamsCurrent = new RenderParameter(renderParamsRef);
		this.renderParamsNext = new RenderParameter(renderParamsRef);
		this.renderParamsStart = new RenderParameter(renderParamsRef);
		this.modelParamsRef = new ModelParameter(mm.getReducedSize());
		this.modelParamsCurrent = new ModelParameter(modelParamsRef);
		this.modelParamsNext = new ModelParameter(modelParamsRef);

		Log.info(LogType.FITTING, "Creating the renderer.");
		this.renderer = new FittingRenderer(this, scene, target, renderParamsRef);
		this.renderer.setShowSettings(false);

		this.state = State.Reference;
		this.renderer.start();

		try {
			out = new PrintWriter(new FileWriter("debug.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void rendererCallback() {
		if(state == State.Reference) {
			Log.info(LogType.FITTING, "Fitting state: " + state);
			renderer.saveRender(superStep + "_REF");
		}
		else {
			Log.info(LogType.FITTING, "Fitting state: " + state + "  | step: " + step);
			//renderer.saveRender(superStep + "_" + step);
		}

		rater.setRender(renderer.getRender());
		Log.info(LogType.MODEL, "Rate: " + rater.getRate());
		/*Log.info(LogType.MODEL, "Pixels: " + rater.getNbPixels());
		Log.info(LogType.MODEL, "Ratio: " + rater.getRatio());*/

		switch (state) {
		case Reference:
			out.println(renderParamsCurrent.getDataString());
			out.flush();
			errorRef = rater.getRate();
			step = RenderParameter.nextEnabled(-1);
			renderParamsCurrent.scaleParam(step, ratio);
			scene.update(renderParamsCurrent);
			state = State.RenderParams;
			break;

		case RenderParams:
			double d = (1.0/(sigma*sigma)) * (errorRef - rater.getRate()) / (renderParamsRef.get(step) - renderParamsCurrent.get(step));
			d += 2.0 * (renderParamsCurrent.get(step) - renderParamsStart.get(step)) / RenderParameter.getStandartDeviationSquared(step);

			double nextDiff =  - lambda * d;
			renderParamsNext.set(step, renderParamsRef.get(step) + nextDiff);

			Log.info(LogType.FITTING, "Step: " + step + " Derivate: " + d + " | " + renderParamsRef.get(step) + " + (" + nextDiff + ")");

			renderParamsCurrent.copy(renderParamsRef);

			step = RenderParameter.nextEnabled(step);
			if(step != -1) {
				renderParamsCurrent.copy(renderParamsRef);
				renderParamsCurrent.scaleParam(step, ratio);
				scene.update(renderParamsCurrent);
			}
			else {
				superStep++;
				//modelParamsCurrent.scaleVerticeParam(step, ratio);

				/* TODO: Update model */
				//state = State.VerticeParams;
				state = State.Reference;
				renderParamsRef.copy(renderParamsNext);
				scene.update(renderParamsRef);
				System.out.println(renderParamsRef);
			}

			break;

		case VerticeParams:

			break;

		case TextureParams:

			break;
		}

	}



}
