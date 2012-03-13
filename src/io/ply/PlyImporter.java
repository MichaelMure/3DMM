package io.ply;

import java.io.IOException;

import javax.media.j3d.GeometryArray;
import javax.media.j3d.IndexedTriangleArray;
import javax.media.j3d.Shape3D;
import javax.vecmath.Color3b;
import javax.vecmath.Point3d;
import org.smurn.jply.Element;
import org.smurn.jply.ElementReader;
import org.smurn.jply.PlyReader;
import org.smurn.jply.PlyReaderFile;

import util.Log;
import util.Log.LogLevel;
import util.Log.LogType;

import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.ParsingErrorException;

import io.Importer;

public class PlyImporter extends Importer {

	@Override
	protected Shape3D doLoadObject(String file)	throws IncorrectFormatException, ParsingErrorException, IOException {
		PlyReader ply = new PlyReaderFile(file);

		int vertexCount = ply.getElementCount("vertex");
		int triangleCount = ply.getElementCount("face");
		
		IndexedTriangleArray plane = new IndexedTriangleArray(vertexCount,GeometryArray.COORDINATES | GeometryArray.COLOR_3, triangleCount * 3);
		
		ElementReader reader;
		Point3d[] points = null;
		Color3b[] colors = null;
		int[] faces = null;
				
		/* iterate over ply reader */
		while ((reader = ply.nextElementReader()) != null) {
			String elementType = reader.getElementType().getName();
			if(elementType.equals("vertex")) {
				/* if some vertex were already read, abort. */
				if(points != null)
					continue;
				points = new Point3d[vertexCount];
				colors = new Color3b[vertexCount];
				
				Element element;
				int x = 0;
				while((element = reader.readElement()) != null) {
					points[x] = new Point3d(element.getDouble("x"), element.getDouble("y"), element.getDouble("z"));
					colors[x] = new Color3b((byte) element.getInt("red"), (byte) element.getInt("green"), (byte) element.getInt("blue"));
					x++;
				}
				Log.print(LogType.IO, LogLevel.DEBUG, "PLY importer: " + x + " vertices read.");
				
			} else if(elementType.equals("face")) {
				/* if some faces were already read, abort. */
				if(faces != null)
					continue;
				faces = new int[triangleCount * 3];
				
				Element element;
				int x = 0;	
				while((element = reader.readElement()) != null) {
					int[] vertex_index = element.getIntList("vertex_indices");
					faces[3*x+0] = vertex_index[0];
					faces[3*x+1] = vertex_index[1];
					faces[3*x+2] = vertex_index[2];
					x++;
				}
				Log.print(LogType.IO, LogLevel.DEBUG, "PLY importer: " + x + " faces read.");
				
			}
			
			reader.close();
		}
		
		ply.close();

		plane.setCoordinateIndices(0, faces);
		plane.setCoordinates(0, points);
		plane.setColors(0,colors);
		plane.setColorIndices(0, faces);
		
		return new Shape3D(plane);
	}

}
