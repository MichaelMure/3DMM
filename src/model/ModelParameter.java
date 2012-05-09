package model;

import cern.colt.function.DoubleDoubleFunction;
import cern.colt.matrix.DoubleFactory1D;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import cern.jet.math.Functions;

public class ModelParameter {

	private final DoubleMatrix1D verticesWeight;
	private final DoubleMatrix1D colorWeight;
	private final int modelCount;

	/** @return a new random ModelParameter with random values.
	 *  Both vertices weights and colors weights are normalized
	 *  (both sums are equal to one).
	 */
	public static ModelParameter getRandom(int modelCount) {
		DoubleMatrix1D vWeight = DoubleFactory1D.dense.random(modelCount);
		DoubleMatrix1D cWeight = DoubleFactory1D.dense.random(modelCount);

		vWeight.assign(Functions.pow(3));
		cWeight.assign(Functions.pow(3));

		ModelParameter param = new ModelParameter(vWeight, cWeight);

		param.normalize();

		return param;
	}

	/** Construct a new ModelParameter with the first coef set to 1, and all the others to 0.
	 *  @param modelCount the number of model in the morphable model
	 */
	public ModelParameter(int modelCount) {
		this.verticesWeight = new DenseDoubleMatrix1D(modelCount);
		this.colorWeight = new DenseDoubleMatrix1D(modelCount);
		this.modelCount = modelCount;

		verticesWeight.set(0, 1.0);
		colorWeight.set(0, 1.0);
	}

	public ModelParameter(DoubleMatrix1D verticesWeight, DoubleMatrix1D colorWeight) {
		if(verticesWeight.size() != colorWeight.size())
			throw new IllegalArgumentException("Different size for color and vertice weights.");
		this.verticesWeight = verticesWeight;
		this.colorWeight = colorWeight;
		this.modelCount = verticesWeight.size();
	}

	/** @return the number of parameter stored. */
	public int getModelCount() {
		return modelCount;
	}

	/** @return the weight vector for the vertices. */
	public DoubleMatrix1D getVerticesWeight() {
		return verticesWeight;
	}

	/** @return the weight vector for the colors. */
	public DoubleMatrix1D getColorWeight() {
		return colorWeight;
	}

	/** @return a linear application of this and another ModelParameter.
	 * return value = (1.0 - alpha) * this + alpha * targetParam
	 */
	public ModelParameter linearApplication(ModelParameter targetParam, double alpha) {
		if(this.modelCount != targetParam.modelCount)
			throw new IllegalArgumentException("Incoherent number of model count.");

		class linapp implements DoubleDoubleFunction {
			private final double alpha;
			public linapp(double alpha) {
				this.alpha = alpha;
			}
			@Override
			public double apply(double x, double y) {
				return (1.0 - alpha) * x + alpha * y;
			}
		}

		DoubleMatrix1D v = verticesWeight.copy().assign(targetParam.verticesWeight, new linapp(alpha));
		DoubleMatrix1D c = colorWeight.copy().assign(targetParam.colorWeight, new linapp(alpha));

		return new ModelParameter(v, c);
	}

	@Override
	public String toString() {
		String result = "ModelParameter: ";
		for(int x = 0; x < modelCount; x++) {
			result += "(" + verticesWeight.get(x) + "," + colorWeight.get(x) + ")";
		}
		return result;
	}

	/** Make sure that the sum of each weight array equal 1.0 */
	private void normalize() {
		double totalVertices = verticesWeight.zSum();
		double totalColor = colorWeight.zSum();

		verticesWeight.assign(Functions.div(totalVertices));
		colorWeight.assign(Functions.div(totalColor));
	}
}
