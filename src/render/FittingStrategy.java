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

	private CompleteParameter start;
	private CompleteParameter ref;
	private CompleteParameter current;
	private CompleteParameter next;

	private enum State {Reference, Fitting};
	private State state;
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
		ModelParameter.setModelCount(mm.getReducedSize());
		this.start = new CompleteParameter();
		start.getRenderParameter().initObjectScale(mesh);
		this.ref = new CompleteParameter(start);
		this.current = new CompleteParameter(start);
		this.next = new CompleteParameter(start);

		Log.info(LogType.FITTING, "Creating the renderer.");
		this.renderer = new FittingRenderer(this, scene, target, ref.getRenderParameter());
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

		rater.setRender(renderer.getRender());
		Log.info(LogType.MODEL, "Rate: " + rater.getRate());
		/*Log.info(LogType.MODEL, "Pixels: " + rater.getNbPixels());
		Log.info(LogType.MODEL, "Ratio: " + rater.getRatio());*/

		switch (state) {
		case Reference:
			out.println(current.getDataString());
			out.flush();
			errorRef = rater.getRate();
			CompleteParameter.start();
			current.scaleParam(ratio);
			scene.update(current.getRenderParameter());
			state = State.Fitting;
			break;

		case Fitting:
			double d = 0;

			switch(CompleteParameter.getState()) {
			case Model:
				break;
			case Render:
				d = (1.0/(sigma*sigma)) * (errorRef - rater.getRate()) / (ref.get() - current.get());
				d += 2.0 * (current.get() - start.get()) / CompleteParameter.getStandartDeviationSquared();
				break;
			}

			double nextDiff =  - lambda * d;
			Log.info(LogType.FITTING, "Derivate: " + d + " | " + ref.get() + " + (" + nextDiff + ")");

			next.set(ref.get() + nextDiff);
			current.copy(ref);

			if(CompleteParameter.next()) {
				current.copy(ref);
				current.scaleParam(ratio);
				scene.update(current.getRenderParameter());
				/* TODO: Update model */
			}
			else {
				superStep++;
				state = State.Reference;
				ref.copy(next);
				scene.update(ref.getRenderParameter());
				System.out.println(ref);
			}

			break;
		}
	}
}
