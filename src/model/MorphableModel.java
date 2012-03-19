package model;

import java.util.Vector;

import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.CommonOps;

public class MorphableModel {

	private Face averageFace;
	private Vector<Face> faces;

	public MorphableModel() {
		faces = new Vector<Face>();
		averageFace = null;
	}

	public void addFace(Face face) {
		faces.add(face);
		averageFace = null;
	}

	public Face getFace(int index) {
		return faces.get(index);
	}

	private void computeAverage() {
		if(faces.size() == 0)
			return;

		int vertexCount = faces.get(0).getVertexCount();

		DenseMatrix64F averageVertices = new DenseMatrix64F(vertexCount, 3);
		DenseMatrix64F averageColors = new DenseMatrix64F(vertexCount, 3);

		averageVertices.zero();
		averageColors.zero();

		for(Face f : faces) {
			CommonOps.addEquals(averageVertices, f.getVerticesMatrix());
			CommonOps.addEquals(averageColors, f.getColorMatrix());
		}

		CommonOps.divide(faces.size(), averageVertices);
		CommonOps.divide(faces.size(), averageColors);

		averageFace = new Face(averageVertices, averageColors, faces.get(0).getFaceIndices());
	}

	public Face getAverage() {
		if(averageFace == null)
			computeAverage();
		return averageFace;
	}

	@Override
	public String toString() {
		return "Morphable Model: " + faces.size() + " faces.";
	}
}
