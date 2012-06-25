package model;

import java.io.IOException;
import java.nio.IntBuffer;
import java.util.Map;

import pca.PCA;
import util.Log;
import util.Log.LogType;

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;

import com.jmatio.io.MatFileReader;
import com.jmatio.types.MLArray;
import com.jmatio.types.MLDouble;
import com.jmatio.types.MLSingle;
import com.jme3.util.BufferUtils;

public class MorphableModelLoader {

	/** Load a morphable model:
	 * - the default location
	 * - all the dimensions.
	 */
	public static MorphableModel loadMAT() throws IOException {
		return loadMAT("data/01_MorphableModel.mat");
	}

	/** Load a morphable model:
	 * - the default location
	 * @param dimension number of dimension to load.
	 */
	public static MorphableModel loadMAT(int dimension) throws IOException {
		return loadMAT("data/01_MorphableModel.mat", dimension);
	}

	/** Load a morphable model:
	 * - all the dimensions.
	 * @param filename the matlab file.
	 */
	public static MorphableModel loadMAT(String filename) throws IOException {
		return loadMAT(filename, -1);
	}

	/** Load a morphable model:
	 * @param filename the matlab file.
	 * @param dimension number of dimension to load.
	 */
	public static MorphableModel loadMAT(String filename, int dimension) throws IOException {
		Log.info(LogType.MODEL, "Loading PCA from matlab database.");
		MatFileReader reader = new MatFileReader(filename);
		Map<String, MLArray> map = reader.getContent();

		/* Sanity check */
		assert(map.containsKey("shapeEV"));
		assert(map.containsKey("shapeMU"));
		assert(map.containsKey("shapePC"));
		assert(map.containsKey("texEV"));
		assert(map.containsKey("texMU"));
		assert(map.containsKey("texPC"));
		assert(map.containsKey("tl"));

		/* Read shape matrices */
		MLArray array = map.get("shapeEV");
		if(dimension <= 0)
			dimension = array.getM();
		Log.info(LogType.MODEL, "Dimension: " + dimension);
		assert(array.isSingle());
		MLSingle shapeEV =  (MLSingle) array;
		DoubleMatrix1D eigenValues = new DenseDoubleMatrix1D(dimension);
		for(int x = 0; x < dimension; x++)
			eigenValues.setQuick(x, shapeEV.get(x));
		shapeEV = null;
		map.remove("shapeEV");

		array = map.get("shapeMU");
		assert(array.isSingle());
		MLSingle shapeMU =  (MLSingle) array;
		DoubleMatrix1D mean = new DenseDoubleMatrix1D(shapeMU.getM());
		for(int x = 0; x < shapeMU.getM(); x++)
			mean.setQuick(x, shapeMU.get(x));
		shapeMU = null;
		map.remove("shapeMU");

		array = map.get("shapePC");
		assert(array.isSingle());
		MLSingle shapePC =  (MLSingle) array;
		DoubleMatrix2D reducedData = new DenseDoubleMatrix2D(shapePC.getM(), dimension);
		for(int row = 0; row < shapePC.getM(); row++)
			for(int col = 0; col < dimension; col++)
				reducedData.setQuick(row, col, shapePC.get(row, col));
		shapePC = null;
		map.remove("shapePC");

		PCA verticesPCA = new PCA(dimension, reducedData, eigenValues, mean);

		/* Read color matrices */
		array = map.get("texEV");
		assert(array.isSingle());
		MLSingle texEV =  (MLSingle) array;
		eigenValues = new DenseDoubleMatrix1D(dimension);
		for(int x = 0; x < dimension; x++)
			eigenValues.setQuick(x, texEV.get(x));
		texEV = null;
		map.remove("texEV");

		array = map.get("texMU");
		assert(array.isSingle());
		MLSingle texMU =  (MLSingle) array;
		mean = new DenseDoubleMatrix1D(texMU.getM());
		for(int x = 0; x < texMU.getM(); x++)
			mean.setQuick(x, texMU.get(x) / 255f);
		texMU = null;
		map.remove("texMU");

		array = map.get("texPC");
		assert(array.isSingle());
		MLSingle texPC =  (MLSingle) array;
		reducedData = new DenseDoubleMatrix2D(texPC.getM(), dimension);
		for(int row = 0; row < texPC.getM(); row++)
			for(int col = 0; col < dimension; col++)
				reducedData.setQuick(row, col, texPC.get(row, col) / 255f);
		texMU = null;
		map.remove("texPC");

		PCA colorsPCA = new PCA(dimension, reducedData, eigenValues, mean);

		/* Read triangle data */
		array = map.get("tl");
		assert(array.isDouble());
		MLDouble tl = (MLDouble) array;
		IntBuffer faceIndices = BufferUtils.createIntBuffer(tl.getM() * tl.getN());
		for(int row = 0; row < tl.getM(); row++) {
			for(int col = tl.getN() - 1; col >= 0; col--) { /* Reverse order to have good normals */
				int indice = tl.get(row, col).intValue() - 1; /* triangle start from 1 in matlab data */
				faceIndices.put(indice);
			}
		}

		return new MorphableModel(verticesPCA, colorsPCA, faceIndices);
	}
}
