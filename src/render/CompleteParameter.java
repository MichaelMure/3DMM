package render;

import model.ModelParameter;

public class CompleteParameter {

	private enum State {
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
		this.modelParams = new ModelParameter(modelParams);
		this.renderParams = new RenderParameter(renderParams);
	}

	public void copy(CompleteParameter cp) {
		this.modelParams.copy(cp.modelParams);
		this.renderParams.copy(cp.renderParams);
	}

	public static void start() {
		state = State.Model;
		ModelParameter.start();
	}

	public static boolean next() {
		switch (state) {
		case Model:
			if(ModelParameter.next())
				return true;
			state = State.Render;
			RenderParameter.start();
			break;

		case Render:
			if(RenderParameter.next())
				return true;
			return false;
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
			modelParams.set(modelParams.get() + direction * (ratio - 1.0) * modelParams.getStandartDeviation());
			modelParams.normalize();
			break;
		case Render:
			renderParams.set(renderParams.get() + direction * (ratio - 1.0) * renderParams.getStandartDeviation());
			break;
		}
	}

	public double getStandartDeviation() {
		switch (state) {
		case Model:
			return modelParams.getStandartDeviation();
		case Render:
			return renderParams.getStandartDeviation();
		default:
			assert false;
			return 0;
		}
	}

	public double getStandartDeviationSquared() {
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

}
