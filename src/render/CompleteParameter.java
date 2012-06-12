package render;

import com.jme3.scene.Mesh;

import model.Model;
import model.ModelParameter;

public class CompleteParameter {

	public enum State {
		Model,
		Render
	}

	private ModelParameter modelParams;
	private RenderParameter renderParams;
	private static State state;

	public CompleteParameter() {
		this.modelParams = new ModelParameter();
		this.renderParams = new RenderParameter();
	}

	public CompleteParameter(CompleteParameter cp) {
		this.modelParams = new ModelParameter(cp.modelParams);
		this.renderParams = new RenderParameter(cp.renderParams);
	}

	public void copy(CompleteParameter cp) {
		this.modelParams.copy(cp.modelParams);
		this.renderParams.copy(cp.renderParams);
	}

	public static boolean start() {
		state = State.Model;
		if(ModelParameter.start())
			return true;
		state = State.Render;
		return RenderParameter.start();
	}

	public static boolean next() {
		switch (state) {
		case Model:
			if(ModelParameter.next())
				return true;
			state = State.Render;
			return RenderParameter.start();

		case Render:
			return RenderParameter.next();
		}
		return false;
	}

	public static State getState() {
		return state;
	}

	public double get() {
		switch (state) {
		case Model:
			return modelParams.get();
		case Render:
			return renderParams.get();
		default:
			assert false;
			return 0;
		}
	}

	public void set(double value) {
		switch (state) {
		case Model:
			modelParams.set(value);
			break;
		case Render:
			renderParams.set(value);
			break;
		}
	}

	/** Change the specified parameter according to a ratio,
	 * with some random in it.
	 */
	public void scaleParam(double ratio) {
		double direction = Math.random() > 0.5 ? 1 : -1;

		switch (state) {
		case Model:
			modelParams.set(modelParams.get() + direction * (ratio - 1.0) * ModelParameter.getStandartDeviation());
			break;
		case Render:
			renderParams.set(renderParams.get() + direction * (ratio - 1.0) * RenderParameter.getStandartDeviation());
			break;
		}
	}

	public static double getStandartDeviation() {
		switch (state) {
		case Model:
			return ModelParameter.getStandartDeviation();
		case Render:
			return RenderParameter.getStandartDeviation();
		default:
			assert false;
			return 0;
		}
	}

	public static double getStandartDeviationSquared() {
		double d = getStandartDeviation();
		return d*d;
	}

	@Override
	public String toString() {
		return modelParams.toString() + "\n" + renderParams.toString();
	}

	public String getDataString() {
		return modelParams.getDataString() + "\t" + renderParams.getDataString();
	}

	public RenderParameter getRenderParameter() {
		return renderParams;
	}

	public ModelParameter getModelParameter() {
		return modelParams;
	}

	/** Utility shortcut to retrieve a Model from a ModelParameter */
	public Model getModel() {
		return modelParams.getModel();
	}

	/** Utility shortcut to retrieve a mesh from a ModelParameter */
	public Mesh getMesh() {
		return modelParams.getMesh();
	}

	/** Utility shortcut to update a mesh from a ModelParameter */
	public void updateMesh(Mesh mesh) {
		modelParams.updateMesh(mesh);
	}

	public void updateScene(FittingScene scene) {
		scene.update(renderParams);
	}
}
