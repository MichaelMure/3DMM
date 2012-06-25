package model;

import java.io.IOException;
import java.nio.IntBuffer;
import java.util.Map;

import pca.PCA;

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

	public static MorphableModel loadMAT() throws IOException {
		return loadMAT("data/01_MorphableModel.mat");
	}

	public static MorphableModel loadMAT(String filename) throws IOException {
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
		int numComponent = array.getM();
		assert(array.isSingle());
		MLSingle shapeEV =  (MLSingle) array;
		DoubleMatrix1D eigenValues = new DenseDoubleMatrix1D(shapeEV.getM());
		for(int x = 0; x < shapeEV.getM(); x++)
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
		DoubleMatrix2D reducedData = new DenseDoubleMatrix2D(shapePC.getM(), shapePC.getN());
		for(int row = 0; row < shapePC.getM(); row++)
			for(int col = 0; col < shapePC.getN(); col++)
				reducedData.setQuick(row, col, shapePC.get(row, col));
		shapePC = null;
		map.remove("shapePC");

		PCA verticesPCA = new PCA(numComponent, reducedData, eigenValues, mean);

		/* Read color matrices */
		array = map.get("texEV");
		assert(array.isSingle());
		MLSingle texEV =  (MLSingle) array;
		eigenValues = new DenseDoubleMatrix1D(texEV.getM());
		for(int x = 0; x < texEV.getM(); x++)
			eigenValues.setQuick(x, texEV.get(x));
		texEV = null;
		map.remove("texEV");

		array = map.get("texMU");
		assert(array.isSingle());
		MLSingle texMU =  (MLSingle) array;
		mean = new DenseDoubleMatrix1D(texMU.getM());
		for(int x = 0; x < texMU.getM(); x++)
			mean.setQuick(x, texMU.get(x));
		texMU = null;
		map.remove("texMU");

		array = map.get("texPC");
		assert(array.isSingle());
		MLSingle texPC =  (MLSingle) array;
		reducedData = new DenseDoubleMatrix2D(texPC.getM(), texPC.getN());
		for(int row = 0; row < texPC.getM(); row++)
			for(int col = 0; col < texPC.getN(); col++)
				reducedData.setQuick(row, col, texPC.get(row, col));
		texMU = null;
		map.remove("texPC");

		PCA colorsPCA = new PCA(numComponent, reducedData, eigenValues, mean);

		/* Read triangle data */
		array = map.get("tl");
		assert(array.isDouble());
		MLDouble tl = (MLDouble) array;
		IntBuffer faceIndices = BufferUtils.createIntBuffer(tl.getM() * tl.getN());
		for(int col = 0; col < tl.getN(); col++)
			for(int row = 0; row < tl.getM(); row++)
				faceIndices.put(tl.get(row, col).intValue());

		return new MorphableModel(verticesPCA, colorsPCA, faceIndices);
	}
}
