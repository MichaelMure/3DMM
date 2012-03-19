package model;

import javax.media.j3d.GeometryArray;
import javax.media.j3d.IndexedTriangleArray;
import javax.media.j3d.Shape3D;

import org.ejml.data.DenseMatrix64F;

import util.TwoComplement;

public class Face {

	private DenseMatrix64F vertices;
	private DenseMatrix64F colors;
	private int[] faceIndices;
	private int vertexCount;

	public Face(Shape3D shape3d) {
		IndexedTriangleArray array = (IndexedTriangleArray) shape3d.getGeometry();
		vertexCount = array.getVertexCount();

		/* We do not copy the vertex data here; data is shared between Shape3d and the matrix */
		vertices = DenseMatrix64F.wrap(vertexCount, 3, array.getCoordRefDouble());

		/* We cannot do the same for the color, since we don't have a integer matrix */
		colors = new DenseMatrix64F(vertexCount, 3);

		byte[] shape3dColors = array.getColorRefByte();
		double[] colorsData = colors.getData();

		for(int x = 0; x < vertexCount * 3; x++) {
			colorsData[x] = TwoComplement.to2complement(shape3dColors[x]);
		}

		faceIndices = array.getCoordIndicesRef();
	}

	public Face(DenseMatrix64F vertices, DenseMatrix64F colors, int[] faceIndices) {
		if(vertices.getNumCols() != 3)
			throw new IllegalArgumentException("Number of columns for shape should be 3 (X,Y,Z).");
		if(colors.getNumCols() != 3)
			throw new IllegalArgumentException("Number of columns for texture should be 3 (R,G,B)");
		if(vertices.getNumRows() <= 0 || colors.getNumRows() <= 0 || faceIndices.length <= 0)
			throw new IllegalArgumentException("At least one argument is empty.");
		if(vertices.getNumRows() != colors.getNumRows())
			throw new IllegalArgumentException("Size of shape and texture inconsistent.");

		this.vertices = vertices;
		this.colors = colors;
		this.faceIndices = faceIndices;
		this.vertexCount = vertices.numRows;
	}

	/** @return the vertex matrix */
	public DenseMatrix64F getVerticesMatrix() {
		return vertices;
	}

	public void setVerticesMatrix(DenseMatrix64F shape) {
		this.vertices = shape;
	}

	/** @return the color matrix. */
	public DenseMatrix64F getColorMatrix() {
		return colors;
	}

	/** Update the color matrix */
	public void setColorMatrix(DenseMatrix64F texture) {
		this.colors = texture;
	}

	/** @return a reference to the face indices array. This array should be the same for each Face.
	 * TODO: make this static.
	 */
	public int[] getFaceIndices() {
		return faceIndices;
	}

	/** @return a newly allocated IndexedTriangleArray for this face. */
	public IndexedTriangleArray getGeometry() {

		double[] colorsData = colors.getData();
		byte[] colorsCopy = new byte[3 * vertexCount];

		for(int x = 0; x < vertexCount * 3; x++) {
			colorsCopy[x] = TwoComplement.from2complement(colorsData[x]);
		}

		IndexedTriangleArray face = new IndexedTriangleArray(vertexCount,
				GeometryArray.COORDINATES | GeometryArray.COLOR_3
						| GeometryArray.BY_REFERENCE
						| GeometryArray.BY_REFERENCE_INDICES
						| GeometryArray.USE_COORD_INDEX_ONLY, faceIndices.length);

		face.setCoordIndicesRef(faceIndices);
		face.setCoordRefDouble(vertices.getData().clone());
		face.setColorRefByte(colorsCopy);

		return face;
	}

	/** @return a newly allocated Shape3D for this face. */
	public Shape3D getShape3D() {
		return new Shape3D(this.getGeometry());
	}

	/** Update the vertex and color of a face Shape3D. */
	public void updateShape3D(Shape3D shape) {
		IndexedTriangleArray array = (IndexedTriangleArray) shape.getGeometry();


	}
}
