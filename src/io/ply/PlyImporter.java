package io.ply;

import java.io.File;
import java.io.IOException;

import javax.media.j3d.GeometryArray;
import javax.media.j3d.IndexedTriangleArray;
import javax.media.j3d.Shape3D;
import org.smurn.jply.Element;
import org.smurn.jply.ElementReader;
import org.smurn.jply.PlyReader;
import org.smurn.jply.PlyReaderFile;

import util.Log;
import util.Log.LogLevel;
import util.Log.LogType;

import io.Importer;

public class PlyImporter extends Importer {

	@Override
	protected Shape3D doLoadObject(File file) throws IOException {
		PlyReader ply = new PlyReaderFile(file);

		int vertexCount = ply.getElementCount("vertex");
		int triangleCount = ply.getElementCount("face");

		IndexedTriangleArray plane = new IndexedTriangleArray(vertexCount,
				GeometryArray.COORDINATES | GeometryArray.COLOR_3
						| GeometryArray.BY_REFERENCE
						| GeometryArray.BY_REFERENCE_INDICES
						| GeometryArray.USE_COORD_INDEX_ONLY, triangleCount * 3);

		ElementReader reader;
		double[] points = null;
		byte[] colors = null;
		int[] faces = null;

		/* iterate over ply reader */
		while ((reader = ply.nextElementReader()) != null) {
			String elementType = reader.getElementType().getName();
			if (elementType.equals("vertex")) {
				/* if some vertex were already read, abort. */
				if (points != null)
					continue;
				points = new double[3 * vertexCount];
				colors = new byte[3 * vertexCount];

				Element element;
				int x = 0;
				while ((element = reader.readElement()) != null) {
					points[3 * x + 0] = element.getDouble("x");
					points[3 * x + 1] = element.getDouble("y");
					points[3 * x + 2] = element.getDouble("z");
					colors[3 * x + 0] = (byte) element.getInt("red");
					colors[3 * x + 1] = (byte) element.getInt("green");
					colors[3 * x + 2] = (byte) element.getInt("blue");
					x++;
				}
				Log.print(LogType.IO, LogLevel.DEBUG, "PLY importer: " + x
						+ " vertices read.");

			} else if (elementType.equals("face")) {
				/* if some faces were already read, abort. */
				if (faces != null)
					continue;
				faces = new int[triangleCount * 3];

				Element element;
				int x = 0;
				while ((element = reader.readElement()) != null) {
					int[] vertex_index = element.getIntList("vertex_indices");
					faces[3 * x + 0] = vertex_index[0];
					faces[3 * x + 1] = vertex_index[1];
					faces[3 * x + 2] = vertex_index[2];
					x++;
				}
				Log.print(LogType.IO, LogLevel.DEBUG, "PLY importer: " + x
						+ " faces read.");

			}

			reader.close();
		}

		ply.close();

		plane.setCoordIndicesRef(faces);
		plane.setCoordRefDouble(points);
		plane.setColorRefByte(colors);

		return new Shape3D(plane);
	}

}
