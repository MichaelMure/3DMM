package parameter;

public class CompleteParameterIterator {

	private final ModelParameterIterator modelParamIt;
	private final RenderParameterIterator renderParamIt;
	public enum State { Model, Render};
	private  State state = State.Model;

	public CompleteParameterIterator() {
		this.modelParamIt = new ModelParameterIterator();
		this.renderParamIt = new RenderParameterIterator();
	}

	public boolean start() {
		state = State.Model;
		if(modelParamIt.start())
			return true;
		state = State.Render;
		return renderParamIt.start();
	}

	public boolean next() {
		switch (state) {
		case Model:
			if(modelParamIt.next())
				return true;
			state = State.Render;
			return renderParamIt.start();

		case Render:
			return renderParamIt.next();
		}
		return false;
	}

	public double get(CompleteParameter param) {
		switch (state) {
		case Model:
			return modelParamIt.get(param.getModelParameter());
		case Render:
			return renderParamIt.get(param.getRenderParameter());
		default:
			assert false;
			return 0;
		}
	}

	public void set(CompleteParameter param, double value) {
		switch (state) {
		case Model:
			modelParamIt.set(param.getModelParameter(), value);
			break;
		case Render:
			renderParamIt.set(param.getRenderParameter(), value);
			break;
		}
	}

	public double getStandartDeviation() {
		switch (state) {
		case Model:
			return modelParamIt.getStandartDeviation();
		case Render:
			return renderParamIt.getStandartDeviation();
		default:
			assert false;
			return 0;
		}
	}

	public double getStandartDeviationSquared() {
		double d = getStandartDeviation();
		return d*d;
	}

	public State getState() {
		return state;
	}

	/** Change the specified parameter according to a ratio,
	 *  with some random in it.
	 */
	public void scaleParam(CompleteParameter param, double ratio) {
		double direction = Math.random() > 0.5 ? 1 : -1;

		switch (state) {
		case Model:
			modelParamIt.set(param.getModelParameter(),
					modelParamIt.get(param.getModelParameter()) + direction * (ratio - 1.0) * modelParamIt.getStandartDeviation());
			break;
		case Render:
			renderParamIt.set(param.getRenderParameter(),
					renderParamIt.get(param.getRenderParameter()) + direction * (ratio - 1.0) * renderParamIt.getStandartDeviation());
			break;
		}
	}

	@Override
	public String toString() {
		switch (state) {
		case Model:
			return "Model[" + modelParamIt.toString() + "]";
		case Render:
			return "Render[" + renderParamIt.toString() + "]";
		}
		assert(false);
		return "";
	}
}
