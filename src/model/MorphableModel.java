package model;

import org.ejml.data.DenseMatrix64F;

import pca.PCA;
import pca.PCA_SVD;

public class MorphableModel {

	private PCA vertices;
	private PCA colors;
	private int[] faceIndices;

	public MorphableModel() {
		faceIndices = null;
		vertices = new PCA_SVD();
		colors = new PCA_SVD();
	}

	public void addModel(Model model) {
		if(faceIndices == null)
			faceIndices = model.getFaceIndices();

		if(vertices == null)
			vertices = new PCA_SVD(model.getVerticesMatrix());
		else
			vertices.addSample(model.getVerticesMatrix());

		if(colors == null)
			colors = new PCA_SVD(model.getColorMatrix());
		else
			colors.addSample(model.getColorMatrix());
	}

	/*public Model getModel(int index) {
		return null;
	}*/

	public Model getModel(ModelParameter param) {
		DenseMatrix64F v = new DenseMatrix64F(1, vertices.getSampleSize(),
				true, vertices.sampleToSampleSpace(param.getVerticesWeight()));
		DenseMatrix64F c = new DenseMatrix64F(1, colors.getSampleSize(),
				true, colors.sampleToSampleSpace(param.getColorWeight()));
		return new Model(v, c, faceIndices);
	}

	public int getSize() {
		assert(vertices.getNumComponents() == colors.getNumComponents());
		return vertices.getNumComponents();
	}

	public Model getAverage() {
		ensurePCA();
		return new Model(vertices.getMean(), colors.getMean(), faceIndices);
	}

	/*@Override
	public String toString() {
		return "Morphable Model: " + models.size() + " faces.";
	}*/

	private void ensurePCA() {
		if(vertices != null && colors != null)
			return;

		/* Ensure computation of the basis, kinda hacky. */
		vertices.getBasis();
		colors.getBasis();
	}
}
