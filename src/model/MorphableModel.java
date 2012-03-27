package model;

import java.util.Vector;

import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.CommonOps;

public class MorphableModel {

	private Model averageModel;
	private Vector<Model> models;

	public MorphableModel() {
		models = new Vector<Model>();
		averageModel = null;
	}

	public void addModel(Model model) {
		models.add(model);
		averageModel = null;
	}

	public Model getModel(int index) {
		return models.get(index);
	}

	public Model getModel(ModelParameter param) {
		if(getSize() == 0)
			throw null;
		if(param.getModelCount() != models.size())
			throw new IllegalArgumentException("Wrong number of parameter: "
									+ param.getModelCount() + " instead of " + models.size());

		int vertexCount = models.get(0).getVertexCount();

		DenseMatrix64F vertices = new DenseMatrix64F(vertexCount, 3);
		DenseMatrix64F colors = new DenseMatrix64F(vertexCount, 3);

		vertices.zero();
		colors.zero();

		int x = 0;
		for(Model f : models) {
			CommonOps.addEquals(vertices, param.getVerticesWeight()[x], f.getVerticesMatrix());
			CommonOps.addEquals(colors, param.getColorWeight()[x] ,f.getColorMatrix());
			x++;
		}

		return new Model(vertices, colors, models.get(0).getFaceIndices());
	}

	private void computeAverage() {
		if(getSize() == 0)
			return;

		int vertexCount = models.get(0).getVertexCount();

		DenseMatrix64F averageVertices = new DenseMatrix64F(vertexCount, 3);
		DenseMatrix64F averageColors = new DenseMatrix64F(vertexCount, 3);

		averageVertices.zero();
		averageColors.zero();

		for(Model f : models) {
			CommonOps.addEquals(averageVertices, f.getVerticesMatrix());
			CommonOps.addEquals(averageColors, f.getColorMatrix());
		}

		CommonOps.divide(models.size(), averageVertices);
		CommonOps.divide(models.size(), averageColors);

		averageModel = new Model(averageVertices, averageColors, models.get(0).getFaceIndices());
	}

	public Model getAverage() {
		if(averageModel == null)
			computeAverage();
		return averageModel;
	}

	public int getSize() {
		return models.size();
	}

	@Override
	public String toString() {
		return "Morphable Model: " + models.size() + " faces.";
	}
}
