package parameter;

import cern.colt.bitvector.BitVector;

public class RenderParameterIterator{

	private final BitVector enabled = new BitVector(RenderParameter.PARAMS_SIZE);
	private int index = -1;

	public RenderParameterIterator() {
		enabled.clear();
		enabled.not(); /* Enable all */
		enabled.clear(RenderParameter.OBJECT_SCALE);
		enabled.clear(RenderParameter.OBJECT_ROTATION_X);
		enabled.clear(RenderParameter.OBJECT_ROTATION_Y);
		enabled.clear(RenderParameter.OBJECT_ROTATION_Z);
		enabled.clear(RenderParameter.OBJECT_POSITION_X);
		enabled.clear(RenderParameter.OBJECT_POSITION_Y);

		enabled.clear();
	}

	/** Initialize the iterator */
	public boolean start() {
		index = -1;
		return next();
	}

	/** Increment the iterator.
	 *  @return true if the iterator is still valid, false if the iteration in ended.
	 */
	public boolean next() {
		index++;
		while(index <= RenderParameter.LAST_PARAM) {
			if(enabled.get(index))
				return true;
			index++;
		}
		return false;
	}

	public double get(RenderParameter param) {
		return param.get(index);
	}

	public void set(RenderParameter param, double value) {
		param.set(index, value);
	}

	public double getStandartDeviation() {
		return RenderParameter.getStandartDeviation(index);
	}

	@Override
	public String toString() {
		return new RenderParameter().getDescription(index);
	}

}
