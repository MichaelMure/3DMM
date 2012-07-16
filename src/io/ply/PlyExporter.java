package io.ply;

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

public class PlyExporter extends Exporter {

	@Override
	protected void doExportObject(Mesh mesh, File file) throws IOException {
		PrintWriter out = new PrintWriter(file);

		FloatBuffer points = mesh.getFloatBuffer(Type.Position);
		FloatBuffer colors = mesh.getFloatBuffer(Type.Color);
		IndexBuffer faces = mesh.getIndexBuffer();

		int nbPoint = points.capacity() / 3;
		int nbFace = faces.size() / 3;

		Log.debug(LogType.IO, "PLY exporter: writing " + nbPoint + " vertices.");
		Log.debug(LogType.IO, "PLY exporter: writing " + nbFace + " faces.");

		out.println("ply");
		out.println("format ascii 1.0");
		out.println("element vertex " + nbPoint);
		out.println("property float x");
		out.println("property float y");
		out.println("property float z");
		out.println("property uchar red");
		out.println("property uchar green");
		out.println("property uchar blue");
		out.println("element face " + nbFace);
		out.println("property list uchar int vertex_indices");
		out.println("end_header");

		for (int x = 0; x < nbPoint; x++) {
			out.print(points.get(3 * x + 0) + " ");
			out.print(points.get(3 * x + 1) + " ");
			out.print(points.get(3 * x + 2) + " ");
			out.print((int) (colors.get(3 * x + 0) * 255.0) + " ");
			out.print((int) (colors.get(3 * x + 1) * 255.0) + " ");
			out.println((int) (colors.get(3 * x + 2) * 255.0));
		}

		for (int x = 0; x < nbFace; x++) {
			out.print("3 ");
			out.print(faces.get(3 * x + 0) + " ");
			out.print(faces.get(3 * x + 1) + " ");
			out.println(faces.get(3 * x + 2));
		}

		out.flush();
		out.close();
	}

}
