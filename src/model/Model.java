package model;

import javax.media.j3d.GeometryArray;
import javax.media.j3d.IndexedTriangleArray;
import javax.media.j3d.Shape3D;

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;

import util.TwoComplement;

public class Model {

	private DoubleMatrix1D vertices;
	private DoubleMatrix1D colors;
	private boolean java3dColorsDirty;
	private boolean java3dCoordsDirty;
	private int[] faceIndices;
	private final int vertexCount;

	/** Create a Model from a Java3D Shape3D. */
	public Model(Shape3D shape3d) {
		IndexedTriangleArray array = (IndexedTriangleArray) shape3d.getGeometry();
		vertexCount = array.getVertexCount();

		vertices = new DenseDoubleMatrix1D(array.getCoordRefDouble());
		colors = new DenseDoubleMatrix1D(vertexCount * 3);

		byte[] java3dColors = array.getColorRefByte();
		for(int x = 0; x < vertexCount * 3; x++) {
			colors.setQuick(x, TwoComplement.to2complement(java3dColors[x]));
		}
		java3dColorsDirty = true;
		java3dCoordsDirty = true;

		faceIndices = array.getCoordIndicesRef();
	}

	/** Construct a Model from two vectors for vertices and colors, and indices for faces.
	 *  Shape should be a vector like (x1,y1,y1,x2,y2,z2 ...).
	 *  Texture should be a vector like (r1,b1,g1,r2,g2,b2 ...).
	 */
	public Model(DoubleMatrix1D vertices, DoubleMatrix1D colors, int[] faceIndices) {
		if(vertices.size() <= 0 || colors.size() <= 0 || faceIndices.length <= 0)
			throw new IllegalArgumentException("At least one argument is empty.");
		if(vertices.size() != colors.size())
			throw new IllegalArgumentException("Size of shape and texture inconsistent.");
		if(vertices.size() % 3 != 0)
			throw new IllegalArgumentException("Size not a 3 multiple.");

		this.vertices = vertices;
		this.colors = colors;
		this.faceIndices = faceIndices;
		this.vertexCount = vertices.size() / 3;
		this.java3dColorsDirty = true;
		this.java3dCoordsDirty = true;
	}

	/** @return the vertex count. */
	public int getVertexCount() {
		return vertexCount;
	}

	/** @return the vertex matrix */
	public DoubleMatrix1D getVerticesMatrix() {
		return vertices;
	}

	/** Update the vertices matrix. */
	public void setVerticesMatrix(DoubleMatrix1D shape) {
		this.vertices = shape;
		this.java3dCoordsDirty = true;
	}

	/** @return the color matrix. */
	public DoubleMatrix1D getColorMatrix() {
		return colors;
	}

	/** Update the color matrix */
	public void setColorMatrix(DoubleMatrix1D color) {
		this.colors = color;
		this.java3dColorsDirty = true;
	}

	/** @return a reference to the face indices array. This array should be the same for each Face.
	 * TODO: make this static.
	 */
	public int[] getFaceIndices() {
		return faceIndices;
	}

	/** @return a newly allocated IndexedTriangleArray for this face. */
	public IndexedTriangleArray getGeometry() {

		IndexedTriangleArray face = new IndexedTriangleArray(vertexCount,
				GeometryArray.COORDINATES | GeometryArray.COLOR_3
						| GeometryArray.BY_REFERENCE
						| GeometryArray.BY_REFERENCE_INDICES
						| GeometryArray.USE_COORD_INDEX_ONLY, faceIndices.length);

		face.setCoordIndicesRef(faceIndices);
		if(face.getCoordRefDouble() == null)
			face.setCoordRefDouble(new double[vertexCount * 3]);
		if(face.getColorRefByte() == null)
			face.setColorRefByte(new byte[vertexCount * 3]);


		updateJava3dVertices(face.getCoordRefDouble());
		updateJava3DColors(face.getColorRefByte());

		face.setCapability(GeometryArray.ALLOW_REF_DATA_WRITE);
		face.setCapability(GeometryArray.ALLOW_REF_DATA_READ);

		return face;
	}

	/** @return a newly allocated Shape3D for this face. */
	public Shape3D getShape3D() {
		return new Shape3D(this.getGeometry());
	}

	/** Update the Java3D vertices array if necessary. */
	private void updateJava3dVertices(double[] java3dVertices) {
		if(java3dCoordsDirty) {
			vertices.toArray(java3dVertices);
		}
	}

	/** Update the Java3D colors array if necessary. */
	private void updateJava3DColors(byte[] java3dColors) {
		if(java3dColorsDirty) {
			for(int x = 0; x < vertexCount * 3; x++) {
				java3dColors[x] = TwoComplement.from2complement(colors.get(x));
			}
		}
	}

}
