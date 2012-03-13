package model;

import java.util.Vector;

import org.ejml.data.DenseMatrix64F;

public class MorphableModel {

	private Face averageFace;
	private Vector<Face> faces;
	
	public MorphableModel() {
		faces = new Vector<Face>();
		averageFace = null;
	}
	
	public void addFace(Face face) {
		faces.add(face);
		if(averageFace == null)
			averageFace = face;
		else
			computeAverage();
	}
	
	public Face getFace(int index) {
		return faces.get(index);
	}
	
	private void computeAverage() {
		if(faces.size() == 0)
			return;
		
		int vertexCount = faces.get(0).getGeometry().getVertexCount();
		
		DenseMatrix64F averageShape = new DenseMatrix64F(vertexCount, 3);
		DenseMatrix64F averageTexture = new DenseMatrix64F(vertexCount, 3);
		
		for(int n = 0; n < vertexCount; n++) {
			double x = 0;
			double y = 0;
			double z = 0;
			long r = 0;
			long g = 0;
			long b = 0;
			
			for(int m = 0; m < faces.size(); m++) {
				DenseMatrix64F shape = faces.get(m).getShapeMatrix();
				DenseMatrix64F color = faces.get(m).getColorMatrix();
				
				x += shape.get(n, 0);
				y += shape.get(n, 1);
				z += shape.get(n, 2);
				r += color.get(n, 0);
				g += color.get(n, 1);
				b += color.get(n, 2);
			}
			
			averageShape.set(n, 0, x / faces.size());
			averageShape.set(n, 1, y / faces.size());
			averageShape.set(n, 2, z / faces.size());
			averageTexture.set(n, 0, r / faces.size());
			averageTexture.set(n, 1, g / faces.size());
			averageTexture.set(n, 2, b / faces.size());
		}
		
		averageFace = new Face(averageShape, averageTexture, faces.get(0).getFaceIndices());
	}
	
	
}
