package io.obj;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.FloatBuffer;

import util.Log;
import util.Log.LogType;

import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.mesh.IndexBuffer;

import io.Exporter;

/** Exporter to an Obj file. As the Obj format does not support the
 *  vertex color data, this exporter does not either.
 */
public class ObjExporter extends Exporter {

	@Override
	protected void doExportObject(Mesh mesh, File file) throws IOException {
		PrintWriter out = new PrintWriter(file);

		FloatBuffer points = mesh.getFloatBuffer(Type.Position);
		IndexBuffer faces = mesh.getIndexBuffer();

		int nbPoint = points.capacity() / 3;
		int nbFace = faces.size() / 3;

		Log.debug(LogType.IO, "Obj exporter: writing " + nbPoint + " vertices.");
		Log.debug(LogType.IO, "Obj exporter: writing " + nbFace + " faces.");

		for (int x = 0; x < nbPoint; x++) {
			out.print("v ");
			out.print(points.get(3 * x + 0) + " ");
			out.print(points.get(3 * x + 1) + " ");
			out.println(points.get(3 * x + 2));
		}

		for (int x = 0; x < nbFace; x++) {
			out.print("f ");
			out.print((faces.get(3 * x + 0) + 1) + " ");
			out.print((faces.get(3 * x + 1) + 1) + " ");
			out.println((faces.get(3 * x + 2) + 1));
		}

		out.flush();
		out.close();
	}

}
