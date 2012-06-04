package render;

import model.ModelParameter;

public class CompleteParameter {

	private enum State {
		Model,
		Render
	}

	private ModelParameter modelParams;
	private RenderParameter renderParams;
	private State state;

	public CompleteParameter(int modelCount) {
		this.modelParams = new ModelParameter(modelCount);
		this.renderParams = new RenderParameter();
		this.state = State.Model;
	}

	public void copy(CompleteParameter cp) {
		this.modelParams.copy(cp.modelParams);
		this.renderParams.copy(cp.renderParams);
	}

	public void start() {
		state = State.Model;
		modelParams.start();
	}

	public boolean next() {
		switch (state) {
		case Model:
			if(modelParams.next())
				return true;
			state = State.Render;
			renderParams.start();
			break;

		case Render:
			if(renderParams.next())
				return true;
			return false;
		}
		return false;
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
