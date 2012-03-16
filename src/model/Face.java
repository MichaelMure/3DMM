package model;

import javax.media.j3d.GeometryArray;
import javax.media.j3d.IndexedTriangleArray;
import javax.media.j3d.Shape3D;
import javax.vecmath.Color3b;
import javax.vecmath.Point3d;

import org.ejml.data.DenseMatrix64F;

public class Face {

	private DenseMatrix64F shape;
	private DenseMatrix64F color;
	private int[] faceIndices;
	private int vertexCount;
	
	public Face(Shape3D shape3d) {
		IndexedTriangleArray array = (IndexedTriangleArray) shape3d.getGeometry();

		vertexCount = array.getVertexCount();
		shape = new DenseMatrix64F(vertexCount, 3);
		color = new DenseMatrix64F(vertexCount, 3);
		
		double[] shape3dCoords = array.getCoordRefDouble();
		byte[] shape3dColors = array.getColorRefByte();
		
		for(int x = 0; x < vertexCount; x++) {
			shape.set(x, 0, shape3dCoords[3*x+0]);
			shape.set(x, 1, shape3dCoords[3*x+1]);
			shape.set(x, 2, shape3dCoords[3*x+2]);
			color.set(x, 0, shape3dColors[3*x+0]);
			color.set(x, 1, shape3dColors[3*x+1]);
			color.set(x, 2, shape3dColors[3*x+2]);
		}
		
		faceIndices = array.getCoordIndicesRef();
	}
	
	public Face(DenseMatrix64F shape, DenseMatrix64F texture, int[] faceIndices) {
		if(shape.getNumCols() != 3)
			throw new IllegalArgumentException("Number of columns for shape should be 3 (X,Y,Z).");
		if(texture.getNumCols() != 3)
			throw new IllegalArgumentException("Number of columns for texture should be 3 (R,G,B)");
		if(shape.getNumRows() <= 0 || texture.getNumRows() <= 0 || faceIndices.length <= 0)
			throw new IllegalArgumentException("At least one argument is empty.");
		if(shape.getNumRows() != texture.getNumRows())
			throw new IllegalArgumentException("Size of shape and texture inconsistent.");
		
		this.shape = shape;
		this.color = texture;
		this.faceIndices = faceIndices;
		this.vertexCount = shape.numRows;
	}

	public DenseMatrix64F getShapeMatrix() {
		return shape;
	}

	public void setShapeMatrix(DenseMatrix64F shape) {
		this.shape = shape;
	}

	public DenseMatrix64F getColorMatrix() {
		return color;
	}

	public void setColorMatrix(DenseMatrix64F texture) {
		this.color = texture;
	}
	
	public int[] getFaceIndices() {
		return faceIndices;
	}
	
	public IndexedTriangleArray getGeometry() {
		Point3d[] points = new Point3d[vertexCount];
		Color3b[] colors = new Color3b[vertexCount];
		
		for(int x = 0; x < vertexCount; x++) {
			points[x] = new Point3d(shape.get(x, 0), shape.get(x, 1), shape.get(x, 2));
			colors[x] = new Color3b((byte) color.get(x, 0), (byte) color.get(x, 1), (byte) color.get(x, 2));
		}
		
		IndexedTriangleArray face = new IndexedTriangleArray(vertexCount,GeometryArray.COORDINATES | GeometryArray.COLOR_3, faceIndices.length);
		
		face.setCoordinateIndices(0, faceIndices);
		face.setCoordinates(0, points);
		face.setColors(0,colors);
		face.setColorIndices(0, faceIndices);
		return face;
	}
	
	public Shape3D getShape3D() {
		return new Shape3D(this.getGeometry());
	}
	
}
