package model;

import com.jme3.scene.Mesh;

import cern.colt.bitvector.BitVector;
import cern.colt.function.DoubleDoubleFunction;
import cern.colt.matrix.DoubleFactory1D;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import cern.jet.math.Functions;

public class ModelParameter {

	/* Morphable Model */
	private static MorphableModel mm;
	private static int modelCount = 0;

	/* Iterator */
	private enum State { Vertice, Color};
	private static State state = State.Vertice;
	private static int index = -1;

	private final DoubleMatrix1D verticesWeight;
	private final DoubleMatrix1D colorWeight;

	private static BitVector enabledVertice = null;
	private static BitVector enabledColor = null;

	private static void initEnabled() {
		if (enabledColor == null   || enabledColor.size() != modelCount ||
			  enabledVertice == null || enabledVertice.size() != modelCount) {
			enabledColor = new BitVector(modelCount);
			enabledColor.clear();
			enabledColor.not(); /* Enable all */
			enabledVertice = new BitVector(modelCount);
			enabledVertice.clear();
			enabledVertice.not(); /* Enable all */
		}
	}

	public static void setMorphableModel(MorphableModel mm) {
		ModelParameter.mm = mm;
		ModelParameter.modelCount = mm.getReducedSize();
		initEnabled();
	}

	/** @return a new random ModelParameter with random values.
	 *  Both vertices weights and colors weights are normalized
	 *  (both sums are equal to one).
	 */
	public static ModelParameter getRandom() {
		DoubleMatrix1D vWeight = DoubleFactory1D.dense.random(modelCount);
		DoubleMatrix1D cWeight = DoubleFactory1D.dense.random(modelCount);

		vWeight.assign(Functions.pow(3));
		cWeight.assign(Functions.pow(3));

		ModelParameter param = new ModelParameter(vWeight, cWeight);

		param.normalize();

		return param;
	}

	/** Construct a new ModelParameter with all weight set to 0 (average model). */
	public ModelParameter() {
		this.verticesWeight = new DenseDoubleMatrix1D(modelCount);
		this.colorWeight = new DenseDoubleMatrix1D(modelCount);
	}

	/** Construct a new ModelParameter from the provided weight. */
	public ModelParameter(DoubleMatrix1D verticesWeight, DoubleMatrix1D colorWeight) {
		if(verticesWeight.size() != modelCount || colorWeight.size() != modelCount)
			throw new IllegalArgumentException("Incoherent model count. Use setModelCount() if not a bug.");
		this.verticesWeight = new DenseDoubleMatrix1D(modelCount);
		this.verticesWeight.assign(verticesWeight);
		this.colorWeight = new DenseDoubleMatrix1D(modelCount);
		this.colorWeight.assign(colorWeight);
	}

	/** Copy constructor */
	public ModelParameter(ModelParameter param) {
		this.verticesWeight = new DenseDoubleMatrix1D(modelCount);
		this.verticesWeight.assign(param.verticesWeight);
		this.colorWeight = new DenseDoubleMatrix1D(modelCount);
		this.colorWeight.assign(param.colorWeight);
	}

	public void copy(ModelParameter modelParams) {
		this.verticesWeight.assign(modelParams.verticesWeight);
		this.colorWeight.assign(modelParams.colorWeight);
	}

	/** Initialize the iterator */
	public static boolean start() {
		state = State.Vertice;
		index = -1;
		return next();
	}

	/** Increment the iterator.
	 *  @return true if the iterator is still valid, false if the iteration in ended.
	 */
	public static boolean next() {
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

	/** In place randomization of the parameters */
	public void randomize() {
		colorWeight.assign(Functions.random());
		verticesWeight.assign(Functions.random());

		colorWeight.assign(Functions.pow(3));
		verticesWeight.assign(Functions.pow(3));

		normalize();
	}

	/** @return the number of parameter stored in each
	 * vertices and color.
	 */
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

	public double get() {
		switch (state) {
		case Vertice:
			return verticesWeight.getQuick(index);
		case Color:
			return colorWeight.getQuick(index);
		default:
			assert false;
			return 0;
		}
	}

	public void set(double value) {
		switch (state) {
		case Vertice:
			verticesWeight.setQuick(index, value);
			break;
		case Color:
			colorWeight.setQuick(index, value);
			break;
		}
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

	public static double getStandartDeviation() {
		switch(state) {
		case Color: return mm.getColorEV(index);
		case Vertice: return mm.getVerticeEV(index);
		}
		assert(false);
		return 0;
	}

	public String getDataString() {
		String result = "";
		for(int x = 0; x < modelCount; x++)
			result += String.format("%f\t", verticesWeight.getQuick(x));
		for(int x = 0; x < modelCount -1; x++)
			result += String.format("%f\t", colorWeight.getQuick(x));
		return result + String.format("%f", colorWeight.getQuick(modelCount-1));
	}

	/** Utility shortcut to retrieve a Model from a ModelParameter */
	public Model getModel() {
		return mm.getModel(this);
	}

	/** Utility shortcut to retrieve a mesh from a ModelParameter */
	public Mesh getMesh() {
		return mm.getModel(this).getMesh();
	}

	/** Utility shortcut to update a mesh from a ModelParameter */
	public void updateMesh(Mesh mesh) {
		mm.updateMesh(mesh, this);
	}
}
