package util;

import javax.media.j3d.IndexedTriangleArray;
import javax.vecmath.Vector3d;

public class NormalGenerator {

	public static void ComputeNormal(IndexedTriangleArray array) {
		TimeCounter t = new TimeCounter("Computing normals");
		int vertexCount = array.getVertexCount();
		int facesCount = array.getIndexCount() / 3;

		float[] normals = array.getNormalRefFloat();
		if(normals == null)
			normals = new float[vertexCount * 3];

		for(int x = 0; x < vertexCount * 3; x++) {
			normals[x] = 0.0f;
		}

		double[] coords = array.getCoordRefDouble();
		int[] faces = array.getCoordIndicesRef();

		Vector3d vecAB = new Vector3d();
		Vector3d vecBC = new Vector3d();
		Vector3d normal = new Vector3d();
		for(int x = 0; x < facesCount; x++) {
			int indiceA = faces[3*x];
			int indiceB = faces[3*x+1];
			int indiceC = faces[3*x+2];

			vecAB.x = coords[3*indiceB+0] - coords[3*indiceA+0];
			vecAB.y = coords[3*indiceB+1] - coords[3*indiceA+1];
			vecAB.z = coords[3*indiceB+2] - coords[3*indiceA+2];
			vecBC.x = coords[3*indiceC+0] - coords[3*indiceB+0];
			vecBC.y = coords[3*indiceC+1] - coords[3*indiceB+1];
			vecBC.z = coords[3*indiceC+2] - coords[3*indiceB+2];

			vecAB.normalize();
			vecBC.normalize();

			normal.cross(vecAB, vecBC);
			normal.normalize();

			normals[3*indiceA+0] += (float) -normal.x;
			normals[3*indiceA+1] += (float) -normal.y;
			normals[3*indiceA+2] += (float) -normal.z;
			normals[3*indiceB+0] += (float) -normal.x;
			normals[3*indiceB+1] += (float) -normal.y;
			normals[3*indiceB+2] += (float) -normal.z;
			normals[3*indiceC+0] += (float) -normal.x;
			normals[3*indiceC+1] += (float) -normal.y;
			normals[3*indiceC+2] += (float) -normal.z;
		}

		double lenght;
		for(int x = 0; x < vertexCount; x++) {
			lenght = Math.sqrt(normals[3 * x + 0] * normals[3 * x + 0]
					+ normals[3 * x + 1] * normals[3 * x + 1]
					+ normals[3 * x + 2] * normals[3 * x + 2]);
			normals[3 * x + 0] = (float) (normals[3 * x + 0] / lenght);
			normals[3 * x + 1] = (float) (normals[3 * x + 1] / lenght);
			normals[3 * x + 2] = (float) (normals[3 * x + 2] / lenght);
		}

		array.setNormalRefFloat(normals);
		t.stop();
	}

}
