package render;

import java.awt.image.BufferedImage;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import parameter.CompleteParameter;
import parameter.CompleteParameterIterator;
import parameter.ModelParameter;

import com.jme3.scene.Mesh;

import util.Log;
import util.Log.LogType;

import model.MorphableModel;

public class FittingStrategy implements FittingRenderer.Callback {

	private BufferedImage target;
	private FittingScene scene;
	private FittingRater rater;
	private FittingRenderer renderer;
	private Mesh mesh;

	private CompleteParameter start;
	private CompleteParameter ref;
	private CompleteParameter current;
	private CompleteParameter next;

	private CompleteParameterIterator it;

	private enum State {Reference, Fitting};
	private State state;
	private int superStep = 0;
	private double errorRef;
	private double ratio = 1.06;
	private double lambda = 0.02;
	private double sigma = 5000;

	PrintWriter out;

	public FittingStrategy(MorphableModel mm, BufferedImage target) {
		ModelParameter.setMorphableModel(mm);
		this.target = target;
	}

	public void start() {
		Log.info(LogType.FITTING, "Creating the fitting strategy.");

		this.start = new CompleteParameter();

		Log.info(LogType.FITTING, "Creating the average mesh.");
		this.mesh = start.getMesh();

		Log.info(LogType.FITTING, "Initialize parameters.");
		start.getRenderParameter().initObjectScale(mesh);
		this.ref = new CompleteParameter(start);
		this.current = new CompleteParameter(start);
		this.next = new CompleteParameter(start);

		this.it = new CompleteParameterIterator();

		Log.info(LogType.FITTING, "Creating the scene.");
		this.scene = new FittingScene(mesh);
		Log.info(LogType.FITTING, "Creating the rater.");
		this.rater = new FittingRater(target);

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
			//renderer.saveRender(superStep + "_REF");
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
			it.start();
			it.scaleParam(current, ratio);

			switch(it.getState()) {
			case Model:   current.updateMesh(mesh);	break;
			case Render:	current.updateScene(scene); break;
			}
			state = State.Fitting;

			if(superStep > 100) sigma = 4000;
			if(superStep > 200) sigma = 3000;
			break;

		case Fitting:
			double d = (1.0/(sigma*sigma)) * (errorRef - rater.getRate()) / (it.get(ref) - it.get(current));

			switch(it.getState()) {
			case Model:
				d += 2.0 * it.get(current) / it.getStandartDeviationSquared();
				break;
			case Render:
				d += 2.0 * (it.get(current) - it.get(start)) / it.getStandartDeviationSquared();
				break;
			}

			/* We add a random noise to try to avoid local minimum */
			double nextDiff =  - lambda * d * (1.0 + (Math.random() - 0.5) * 0.1);
			Log.info(LogType.FITTING, "Derivate: " + d + " | " + it.get(ref) + " + (" + nextDiff + ")");

			it.set(next, it.get(ref) + nextDiff);
			current.copy(ref);

			if(it.next()) {
				current.copy(ref);
				it.scaleParam(current, ratio);
				switch(it.getState()) {
				case Model:   current.updateMesh(mesh);	break;
				case Render:	current.updateScene(scene); break;
				}
			}
			else {
				superStep++;
				state = State.Reference;
				ref.copy(next);
				ref.updateMesh(mesh);
				ref.updateScene(scene);
				System.out.println(ref);
			}

			break;
		}
	}
}
