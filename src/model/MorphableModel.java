package model;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import cern.colt.matrix.DoubleMatrix1D;

import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;

import parameter.ModelParameter;
import pca.PCA;
import pca.PCA_SVD;
import util.Log;
import util.Log.LogType;

public class MorphableModel {

	public final static int PCA_DIMENSION = 5;
	private PCA vertices;
	private PCA colors;
	private IntBuffer faceIndices;

	public MorphableModel() {
		faceIndices = null;
		vertices = new PCA_SVD();
		colors = new PCA_SVD();
	}

	public MorphableModel(PCA vertices, PCA colors, IntBuffer faceIndices) {
		this.vertices = vertices;
		this.colors = colors;
		this.faceIndices = faceIndices;
	}

	public void addModel(Model model) {
		if(faceIndices == null)
			faceIndices = model.getFaceIndices();

		if(vertices == null)
			vertices = new PCA_SVD();
		if(colors == null)
			colors = new PCA_SVD();

		vertices.addSample(model.getVerticesMatrix());
		colors.addSample(model.getColorMatrix());
	}

	public Model getModel(int index) {
		return new Model(vertices.getSample(index), colors.getSample(index), faceIndices);
	}

	public Model getReducedModel(int index) {
		ensurePCA();
		return new Model(vertices.getFeatureSample(index), colors.getFeatureSample(index), faceIndices);
	}

	public Model getModel(ModelParameter param) {
		ensurePCA();

		return new Model(vertices.sampleToSampleSpace(param.getVerticesWeight()),
									   colors.sampleToSampleSpace(param.getColorWeight()),
										 faceIndices);
	}

	public void updateModel(Model model, ModelParameter param) {
		ensurePCA();
		model.setVerticesMatrix(vertices.sampleToSampleSpace(param.getVerticesWeight()));
		model.setColorMatrix(colors.sampleToSampleSpace(param.getColorWeight()));
	}

	/** Unconditional update of a mesh with the given parameters.
	 *  This feed the GPU with new vertices and textures data.
	 */
	public void updateMesh(Mesh mesh, ModelParameter param) {
		ensurePCA();
		Log.info(LogType.MODEL, "Update mesh.");

		FloatBuffer verticesBuffer = (FloatBuffer) mesh.getBuffer(Type.Position).getData();
		FloatBuffer colorsBuffer = (FloatBuffer) mesh.getBuffer(Type.Color).getData();

		verticesBuffer.clear();
		colorsBuffer.clear();

		/* TODO: non efficient: only one copy should be needed. */
		DoubleMatrix1D newVertices = vertices.sampleToSampleSpace(param.getVerticesWeight());
		DoubleMatrix1D newColors = colors.sampleToSampleSpace(param.getColorWeight());

		for (int x = 0; x < newVertices.size(); x++) {
			verticesBuffer.put((float) newVertices.getQuick(x));
		}

		for (int x = 0; x < newColors.size(); x++) {
			colorsBuffer.put((float) newColors.getQuick(x));
		}

		mesh.setBuffer(Type.Position, 3, verticesBuffer);
		mesh.setBuffer(Type.Color, 3, colorsBuffer);
		mesh.updateCounts();
		mesh.updateBound();
	}

	public int getSize() {
		assert(vertices.getSampleNumber() == colors.getSampleNumber());
		return vertices.getSampleNumber();
	}

	public int getReducedSize() {
		ensurePCA();
		assert(vertices.getNumComponents() == colors.getNumComponents());
		return vertices.getNumComponents();
	}

	public Model getAverage() {
		return new Model(vertices.getMean(), colors.getMean(), faceIndices);
	}

	public double getColorEV(int index) {
		return colors.getEigenValue(index);
	}

	public double getVerticeEV(int index) {
		return vertices.getEigenValue(index);
	}

	public void compute(int dimension) {
		if(!vertices.computationDone())
			vertices.computePCA(dimension);

		if(!colors.computationDone())
			colors.computePCA(dimension);
	}

	@Override
	public String toString() {
		if(!vertices.computationDone())
			return "Morphable Model (unlocked)";
		else
			return "Morphable Model (locked): " + vertices.getNumComponents() + " dimensions.";
	}

	private void ensurePCA() {
		compute(PCA_DIMENSION);
	}
}
