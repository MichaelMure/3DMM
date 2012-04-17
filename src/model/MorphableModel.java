package model;

import pca.PCA;
import pca.PCA_L1;

public class MorphableModel {

	public final static int PCA_DIMENSION = 5;
	private PCA vertices;
	private PCA colors;
	private int[] faceIndices;

	public MorphableModel() {
		faceIndices = null;
		vertices = new PCA_L1();
		colors = new PCA_L1();
	}

	public void addModel(Model model) {
		if(faceIndices == null)
			faceIndices = model.getFaceIndices();

		if(vertices == null)
			vertices = new PCA_L1();
		if(colors == null)
			colors = new PCA_L1();

		vertices.addSample(model.getVerticesMatrix());
		colors.addSample(model.getColorMatrix());
	}

	public Model getModel(int index) {
		return new Model(vertices.getSample(index), colors.getSample(index), faceIndices);
	}

	public Model getModel(ModelParameter param) {
		ensurePCA();

		return new Model(vertices.sampleToSampleSpace(param.getVerticesWeight()),
									   colors.sampleToSampleSpace(param.getColorWeight()),
										 faceIndices);
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
