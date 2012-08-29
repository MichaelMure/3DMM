package render;

import java.awt.image.BufferedImage;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import parameter.CompleteParameter;
import parameter.ModelParameter;

import com.jme3.scene.Mesh;

import util.Log;
import util.Log.LogType;

import model.MorphableModel;

public class ErrorTracer implements FittingRenderer.Callback {

	private BufferedImage target;
	private FittingScene scene;
	private FittingRater rater;
	private FittingRenderer renderer;
	private Mesh mesh;

	private CompleteParameter start;

	PrintWriter out1, out2, out3, out4, out5, out6, out7, out8, out9, out10;

	private final double MIN = -5;
	private final double MAX = 9;
	private final double STEP = 0.1;

	public ErrorTracer(MorphableModel mm, BufferedImage target) {
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

		start.getModelParameter().set(0, MIN);
		start.getModelParameter().set(1, MIN);
		start.updateMesh(mesh);

		Log.info(LogType.FITTING, "Creating the scene.");
		this.scene = new FittingScene(mesh);
		Log.info(LogType.FITTING, "Creating the rater.");
		this.rater = new FittingRater(target);

		Log.info(LogType.FITTING, "Creating the renderer.");
		this.renderer = new FittingRenderer(this, scene, target, start.getRenderParameter());
		this.renderer.setShowSettings(false);

		this.renderer.start();

		try {
			out1 = new PrintWriter(new FileWriter("debug1.csv"));
			out2 = new PrintWriter(new FileWriter("debug2.csv"));
			out3 = new PrintWriter(new FileWriter("debug3.csv"));
			out4 = new PrintWriter(new FileWriter("debug4.csv"));
			out5 = new PrintWriter(new FileWriter("debug5.csv"));
			out6 = new PrintWriter(new FileWriter("debug6.csv"));
			out7 = new PrintWriter(new FileWriter("debug7.csv"));
			out8 = new PrintWriter(new FileWriter("debug8.csv"));
			out9 = new PrintWriter(new FileWriter("debug9.csv"));
			out10 = new PrintWriter(new FileWriter("debug10.csv"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void rendererCallback() {
		ModelParameter mp = start.getModelParameter();

		rater.setRender(renderer.getRender());

		out1.print(rater.getRate1() + "\t");
		out2.print(rater.getRate2() + "\t");
		out3.print(rater.getRate3() + "\t");
		out4.print(rater.getRate4() + "\t");
		out5.print(rater.getRate1() / rater.getNbPixels() + "\t");
		out6.print(rater.getRate2() / rater.getNbPixels() + "\t");
		out7.print(rater.getRate3() / rater.getNbPixels() + "\t");
		out8.print(rater.getRate4() / rater.getNbPixels() + "\t");

		out9.print(rater.getRate5() + "\t");
		out10.print(rater.getRate5() / rater.getNbPixels() + "\t");

		mp.set(1, mp.get(1) + STEP);

		if(mp.get(1) >= MAX) {
			mp.set(0, mp.get(0) + STEP);
			mp.set(1, MIN);
			out1.println();
			out2.println();
			out3.println();
			out4.println();
			out5.println();
			out6.println();
			out7.println();
			out8.println();
			out9.println();
			out10.println();
		}

		if(mp.get(0) >= MAX) {
			out1.println("STOP");
		}

		out1.flush();
		out2.flush();
		out3.flush();
		out4.flush();
		out5.flush();
		out6.flush();
		out7.flush();
		out8.flush();
		out9.flush();
		out10.flush();

		start.updateMesh(mesh);
	}
}
