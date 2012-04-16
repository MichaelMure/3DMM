package model;

import org.ejml.data.DenseMatrix64F;

import pca.PCA;
import pca.PCA_SVD;

public class MorphableModel {

	public final static int PCA_DIMENSION = 5;
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

	public Model getModel(int index) {
		return new Model(vertices.getSample(index), colors.getSample(index), faceIndices);
	}

	public Model getModel(ModelParameter param) {
		ensurePCA();
		DenseMatrix64F v = new DenseMatrix64F(1, vertices.getSampleSize(),
				true, vertices.sampleToSampleSpace(param.getVerticesWeight()));
		DenseMatrix64F c = new DenseMatrix64F(1, colors.getSampleSize(),
				true, colors.sampleToSampleSpace(param.getColorWeight()));
		return new Model(v, c, faceIndices);
	}

	public int getSize() {
		ensurePCA();
		assert(vertices.getNumComponents() == colors.getNumComponents());
		return vertices.getNumComponents();
	}

	public Model getAverage() {
		return new Model(vertices.getMean(), colors.getMean(), faceIndices);
	}

	@Override
	public String toString() {
		if(!vertices.computationDone())
			return "Morphable Model (unlocked)";
		else
			return "Morphable Model (locked): " + vertices.getNumComponents() + " dimensions.";
	}

	private void ensurePCA() {
		if(!vertices.computationDone())
			vertices.computeBasis(PCA_DIMENSION);

		if(!colors.computationDone())
			colors.computeBasis(PCA_DIMENSION);
	}
}
