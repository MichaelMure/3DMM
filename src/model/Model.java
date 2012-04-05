package model;

import javax.media.j3d.GeometryArray;
import javax.media.j3d.IndexedTriangleArray;
import javax.media.j3d.Shape3D;

import org.ejml.data.DenseMatrix64F;

import util.TwoComplement;

public class Model {

	private DenseMatrix64F vertices;
	private DenseMatrix64F colors;
	private byte[] java3dColors;
	private boolean java3dColorsDirty;
	private int[] faceIndices;
	private final int vertexCount;

	/** Create a Model from a Java3D Shape3D. */
	public Model(Shape3D shape3d) {
		IndexedTriangleArray array = (IndexedTriangleArray) shape3d.getGeometry();
		vertexCount = array.getVertexCount();

		/* We do not copy the vertex data here; data is shared between Shape3d and the matrix */
		vertices = DenseMatrix64F.wrap(vertexCount * 3, 1, array.getCoordRefDouble());

		/* We cannot do the same for the color, since we don't have a integer matrix */
		colors = new DenseMatrix64F(vertexCount * 3, 1);

		java3dColors = array.getColorRefByte();
		java3dColorsDirty = false;
		double[] colorsData = colors.getData();

		for(int x = 0; x < vertexCount * 3; x++) {
			colorsData[x] = TwoComplement.to2complement(java3dColors[x]);
		}

		faceIndices = array.getCoordIndicesRef();
	}

	/** Construct a Model from two matrix for vertices and colors, and indices for faces. */
	public Model(DenseMatrix64F vertices, DenseMatrix64F colors, int[] faceIndices) {
		if(vertices.getNumCols() != 1)
			throw new IllegalArgumentException("Shape should be a vector like (x1,y1,y1,x2,y2,z2 ...).");
		if(colors.getNumCols() != 1)
			throw new IllegalArgumentException("Texture should be a vector like (r1,b1,g1,r2,g2,b2 ...)");
		if(vertices.getNumRows() <= 0 || colors.getNumRows() <= 0 || faceIndices.length <= 0)
			throw new IllegalArgumentException("At least one argument is empty.");
		if(vertices.getNumRows() != colors.getNumRows())
			throw new IllegalArgumentException("Size of shape and texture inconsistent.");
		if(vertices.getNumRows() % 3 != 0)
			throw new IllegalArgumentException("Number of row not a 3 multiple.");

		this.vertices = vertices;
		this.colors = colors;
		this.faceIndices = faceIndices;
		this.vertexCount = vertices.numRows / 3;
		this.java3dColorsDirty = true;
		this.java3dColors = null;
	}

	/** @return the vertex count. */
	public int getVertexCount() {
		return vertexCount;
	}

	/** @return the vertex matrix */
	public DenseMatrix64F getVerticesMatrix() {
		return vertices;
	}

	/** Update the vertices matrix. */
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
		face.setCoordRefDouble(vertices.getData().clone());
		face.setColorRefByte(getJava3DColors());
		face.setCapability(GeometryArray.ALLOW_REF_DATA_WRITE);
		face.setCapability(GeometryArray.ALLOW_REF_DATA_READ);

		return face;
	}

	/** @return a newly allocated Shape3D for this face. */
	public Shape3D getShape3D() {
		return new Shape3D(this.getGeometry());
	}

	/** Update the cache of color for Java3D if necessary, and return it. */
	private byte[] getJava3DColors() {
		if(java3dColorsDirty) {
			if(java3dColors == null)
				java3dColors = new byte[vertexCount * 3];

			double[] colorsData = colors.getData();

			for(int x = 0; x < vertexCount * 3; x++) {
				java3dColors[x] = TwoComplement.from2complement(colorsData[x]);
			}
		}

		return java3dColors;
	}
}
