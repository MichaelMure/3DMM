package io.ply;

import io.Importer;

import java.io.File;
import java.io.IOException;

import org.smurn.jply.Element;
import org.smurn.jply.ElementReader;
import org.smurn.jply.PlyReader;
import org.smurn.jply.PlyReaderFile;

import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.util.BufferUtils;

import util.Log;
import util.Log.LogType;

public class PlyImporter extends Importer {

	@Override
	protected Mesh doLoadObject(File file) throws IOException {
		PlyReader ply = new PlyReaderFile(file);

		int vertexCount = ply.getElementCount("vertex");
		int triangleCount = ply.getElementCount("face");

		ElementReader reader;
		float[] points = null;
		float[] colors = null;
		int[] faces = null;

		/* iterate over ply reader */
		while ((reader = ply.nextElementReader()) != null) {
			String elementType = reader.getElementType().getName();
			if (elementType.equals("vertex")) {
				/* if some vertex were already read, abort. */
				if (points != null)
					continue;
				points = new float[3 * vertexCount];
				colors = new float[3 * vertexCount];

				Element element;
				int x = 0;
				while ((element = reader.readElement()) != null) {
					points[3 * x + 0] = (float) element.getDouble("x");
					points[3 * x + 1] = (float) element.getDouble("y");
					points[3 * x + 2] = (float) element.getDouble("z");
					colors[3 * x + 0] = (float) element.getDouble("red") / 255f;
					colors[3 * x + 1] = (float) element.getDouble("green") / 255f;
					colors[3 * x + 2] = (float) element.getDouble("blue") / 255f;
					x++;
				}
				Log.debug(LogType.IO, "PLY importer: " + x + " vertices read.");

			} else if (elementType.equals("face")) {
				/* if some faces were already read, abort. */
				if (faces != null)
					continue;
				faces = new int[triangleCount * 3];

				Element element;
				int x = 0;
				while ((element = reader.readElement()) != null) {
					int[] vertex_index = null;

					try {
						vertex_index = element.getIntList("vertex_indices");
					}
					/* Property may have a different name, do nothing. */
					catch (Exception e) {}

					if(vertex_index == null) {
						try {
							vertex_index = element.getIntList("vertex_index");
						}
						/* Property may have a different name, do nothing. */
						catch (Exception e) {}
					}

					if(vertex_index == null) {
						throw new IOException("Failed to read vertices");
					}

					faces[3 * x + 0] = vertex_index[0];
					faces[3 * x + 1] = vertex_index[1];
					faces[3 * x + 2] = vertex_index[2];
					x++;
				}
				Log.debug(LogType.IO, "PLY importer: " + x + " faces read.");
			}

			reader.close();
		}

		ply.close();

		Mesh mesh = new Mesh();
		mesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(points));
		mesh.setBuffer(Type.Index, 3, BufferUtils.createIntBuffer(faces));
		mesh.setBuffer(Type.Color, 3, BufferUtils.createFloatBuffer(colors));

		mesh.updateBound();

		return mesh;
	}

}
