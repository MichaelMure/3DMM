package parameter;

import cern.colt.bitvector.BitVector;

public class ModelParameterIterator {

	private int modelCount = -1;
	private BitVector enabledVertice = null;
	private BitVector enabledColor = null;

	/* Iterator */
	private enum State { Vertice, Color};
	private State state = State.Vertice;
	private int index = -1;

	public ModelParameterIterator() {
		this.modelCount = ModelParameter.getModelCount();
		enabledColor = new BitVector(modelCount);
		enabledColor.clear();
		enabledColor.not(); /* Enable all */
		enabledVertice = new BitVector(modelCount);
		enabledVertice.clear();
		enabledVertice.not(); /* Enable all */
	}

	/** Initialize the iterator */
	public boolean start() {
		state = State.Vertice;
		index = -1;
		return next();
	}

	/** Increment the iterator.
	 *  @return true if the iterator is still valid, false if the iteration in ended.
	 */
	public boolean next() {
		index++;

		switch (state) {
		case Vertice:
			while(index < modelCount) {
				if(enabledVertice.get(index))
					return true;
				index++;
			}
			index = 0;
			state = State.Color;
			/* No break here, we slip to color handling. */

		case Color:
			while(index < modelCount) {
				if(enabledColor.get(index))
					return true;
				index++;
			}
			/* No break here neither */

		default:
			return false;
		}
	}

	public void enableEigenLevel(int index, boolean value) {
		if(index >= modelCount)
			return;

		if(value) {
			enabledVertice.set(index);
			enabledColor.set(index);
		}
		else {
			enabledVertice.clear(index);
			enabledColor.clear(index);
		}
	}

	public double get(ModelParameter param) {
		switch (state) {
		case Vertice:
			return param.get(index);
		case Color:
			return param.get(index + modelCount);
		default:
			assert false;
			return 0;
		}
	}

	public void set(ModelParameter param, double value) {
		switch (state) {
		case Vertice:
			param.set(index, value);
			break;
		case Color:
			param.set(index + modelCount, value);
			break;
		}
	}

	public double getStandartDeviation() {
		switch (state) {
		case Vertice:
			return ModelParameter.getStandartDeviation(index);
		case Color:
			return ModelParameter.getStandartDeviation(index + modelCount);
		default:
			assert false;
			return 0;
		}
	}

	@Override
	public String toString() {
		switch (state) {
		case Vertice:
			return "Vertice " + index;
		case Color:
			return "Color " + index;
		default:
			assert false;
			return "";
		}
	}
}
