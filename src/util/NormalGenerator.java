package util;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.util.BufferUtils;

public class NormalGenerator {

	public static void ComputeNormal(Mesh mesh) {
		TimeCounter t = new TimeCounter("Computing normals");
		int vertexCount = mesh.getVertexCount();

		FloatBuffer verticeBuffer = (FloatBuffer) mesh.getBuffer(Type.Position).getData();
		Vector3f positions[] = BufferUtils.getVector3Array(verticeBuffer);

		IntBuffer indexBuffer = (IntBuffer) mesh.getBuffer(Type.Index).getData();
		int facesCount = indexBuffer.limit() / 3;

		FloatBuffer normalBuffer;
		if(mesh.getBuffer(Type.Normal) == null)
			normalBuffer = BufferUtils.createVector3Buffer(vertexCount);
		else
			normalBuffer = (FloatBuffer) mesh.getBuffer(Type.Normal).getData();

		for(int x = 0; x < vertexCount; x++) {
			normalBuffer.put(x, 0f);
		}

		Vector3f AB,BC;
		Vector3f normal = new Vector3f();

		for(int x = 0; x < facesCount; x++) {
			int indiceA = indexBuffer.get(3*x+0);
			int indiceB = indexBuffer.get(3*x+1);
			int indiceC = indexBuffer.get(3*x+2);

			AB = positions[indiceB].subtract(positions[indiceA]);
			BC = positions[indiceC].subtract(positions[indiceB]);

			/*vecAB.normalize();
			vecBC.normalize();*/

			AB.cross(BC, normal);
			normal.normalize();

			BufferUtils.addInBuffer(normal, normalBuffer, indiceA);
			BufferUtils.addInBuffer(normal, normalBuffer, indiceB);
			BufferUtils.addInBuffer(normal, normalBuffer, indiceC);
		}

		for(int x = 0; x < vertexCount; x++) {
			BufferUtils.normalizeVector3(normalBuffer, x);
		}

		mesh.setBuffer(Type.Normal, 3, normalBuffer);
		t.stop();
	}

}
