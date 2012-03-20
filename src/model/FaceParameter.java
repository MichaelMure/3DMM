package model;

import util.Log;
import util.Log.LogType;


public class FaceParameter {

	private final double[] verticesWeight;
	private final double[] colorWeight;
	private final int modelCount;


	public static FaceParameter getRandomFaceParameter(int modelCount) {
		FaceParameter param = new FaceParameter(modelCount);

		for(int x = 0; x < modelCount; x++) {
			param.verticesWeight[x] = Math.random();
			param.colorWeight[x] = Math.random();
		}

		param.normalize();

		return param;
	}

	/** @return a new FaceParameter with the first coef set to 1, and all the others to 0.
	 *  @param modelCount the number of face in the morphable model
	 */
	public FaceParameter(int modelCount) {
		this.verticesWeight = new double[modelCount];
		this.colorWeight = new double[modelCount];
		this.modelCount = modelCount;

		verticesWeight[0] = 1.0;
		colorWeight[0] = 1.0;

		for(int x = 1; x < modelCount; x++) {
			verticesWeight[x] = 0.0;
			colorWeight[x] = 0.0;
		}
	}

	/** @return the number of parameter stored. */
	public int getModelCount() {
		return modelCount;
	}

	/** @return the weigth array for the vertices. */
	public double[] getVerticesWeight() {
		return verticesWeight;
	}

	/** @return the weigth array for the colors. */
	public double[] getColorWeight() {
		return colorWeight;
	}

	public FaceParameter linearApplication(FaceParameter targetParam, double alpha) {
		if(this.modelCount != targetParam.modelCount)
			throw new IllegalArgumentException("Incoherent number of model count.");

		FaceParameter result = new FaceParameter(modelCount);

		for(int x = 0; x < modelCount; x++) {
			result.colorWeight[x] = (1.0 - alpha) * colorWeight[x] + alpha * targetParam.colorWeight[x];
			result.verticesWeight[x] = (1.0 - alpha) * verticesWeight[x] + alpha * targetParam.verticesWeight[x];
		}

		return result;
	}

	/** Make sure that the total of each weight array equal 1.0 */
	private void normalize() {
		double totalVertices = 0.0;
		double totalColor = 0.0;
		for(int x = 0; x < modelCount; x++) {
			totalVertices += verticesWeight[x];
			totalColor += colorWeight[x];
		}

		for(int x = 0; x < modelCount; x++) {
			verticesWeight[x] /= totalVertices;
			colorWeight[x] /= totalColor;
		}
		Log.debug(LogType.MODEL, "Normalizing by colors: " + 1/totalColor + " vertices: " + 1/totalVertices);
	}
}
