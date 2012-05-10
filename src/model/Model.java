package model;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import util.Log;
import util.Log.LogType;

import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.util.BufferUtils;

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;

public class Model {

	private DoubleMatrix1D vertices;
	private DoubleMatrix1D colors;
	private IntBuffer faceIndices;
	private final int vertexCount;
	private long version = 0;

	/** Create a Model from a Java3D Shape3D. */
	public Model(Mesh mesh) {
		vertexCount = mesh.getVertexCount();

		vertices = new DenseDoubleMatrix1D(vertexCount * 3);
		colors = new DenseDoubleMatrix1D(vertexCount * 3);

		FloatBuffer verticesBuffer = (FloatBuffer) mesh.getBuffer(Type.Position).getData();
		for(int x = 0; x < vertexCount * 3; x++) {
			vertices.setQuick(x, verticesBuffer.get(x));
		}

		FloatBuffer colorsBuffer = (FloatBuffer) mesh.getBuffer(Type.Color).getData();
		for(int x = 0; x < vertexCount * 3; x++) {
			colors.setQuick(x, colorsBuffer.get(x));
		}

		faceIndices = (IntBuffer) mesh.getBuffer(Type.Index).getData();
	}

	/** Construct a Model from two vectors for vertices and colors, and indices for faces.
	 *  Shape should be a vector like (x1,y1,y1,x2,y2,z2 ...).
	 *  Texture should be a vector like (r1,b1,g1,r2,g2,b2 ...).
	 */
	public Model(DoubleMatrix1D vertices, DoubleMatrix1D colors, IntBuffer faceIndices) {
		if(vertices.size() <= 0 || colors.size() <= 0 || faceIndices.capacity() <= 0)
			throw new IllegalArgumentException("At least one argument is empty.");
		if(vertices.size() != colors.size())
			throw new IllegalArgumentException("Size of shape and texture inconsistent.");
		if(vertices.size() % 3 != 0)
			throw new IllegalArgumentException("Size not a 3 multiple.");

		this.vertices = vertices;
		this.colors = colors;
		this.faceIndices = faceIndices;
		this.vertexCount = vertices.size() / 3;
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
		synchronized (vertices) {
			this.vertices = shape;
		}
		this.version++;
	}

	/** @return the color matrix. */
	public DoubleMatrix1D getColorMatrix() {
		return colors;
	}

	/** Update the color matrix */
	public void setColorMatrix(DoubleMatrix1D color) {
		synchronized (this.colors) {
			this.colors = color;
		}
		this.version++;
	}

	/** @return a reference to the face indices array. This array should be the same for each Face.
	 * TODO: make this static.
	 */
	public IntBuffer getFaceIndices() {
		return faceIndices;
	}

	/** @return a newly allocated IndexedTriangleArray for this face. */
	public Mesh getMesh() {

		Mesh mesh = new Mesh();

		FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertexCount * 3);
		synchronized (vertices) {
			for(int x = 0; x < vertexCount * 3; x++) {
				verticesBuffer.put((float) vertices.getQuick(x));
			}
		}
		mesh.setBuffer(Type.Position, 3, verticesBuffer);

		FloatBuffer colorsBuffer = BufferUtils.createFloatBuffer(vertexCount * 3);
		synchronized (colors) {
			for(int x = 0; x < vertexCount * 3; x++) {
				colorsBuffer.put((float) colors.getQuick(x));
			}
		}
		mesh.setBuffer(Type.Color, 3, colorsBuffer);

		mesh.setBuffer(Type.Index, 3, faceIndices);

		mesh.updateBound();

		return mesh;
	}

	public long updateMesh(long version, Mesh mesh) {
		if(this.version == version)
			return this.version;

		Log.info(LogType.MODEL, "Update mesh.");

		FloatBuffer verticesBuffer = (FloatBuffer) mesh.getBuffer(Type.Position).getData();
		FloatBuffer colorsBuffer = (FloatBuffer) mesh.getBuffer(Type.Color).getData();

		verticesBuffer.clear();
		colorsBuffer.clear();

		synchronized (vertices) {
			for (int x = 0; x < vertexCount * 3; x++) {
				verticesBuffer.put((float) vertices.getQuick(x));
			}
		}

		synchronized (colors) {
			for (int x = 0; x < vertexCount * 3; x++) {
				colorsBuffer.put((float) colors.getQuick(x));
			}
		}

		mesh.setBuffer(Type.Position, 3, verticesBuffer);
		mesh.setBuffer(Type.Color, 3, colorsBuffer);
		mesh.updateCounts();
		mesh.updateBound();

		return this.version;
	}

	public long getVersion() {
		return version;
	}
}
