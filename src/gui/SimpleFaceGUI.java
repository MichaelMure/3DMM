package gui;

import javax.media.j3d.IndexedTriangleArray;
import javax.media.j3d.Shape3D;

public class SimpleFaceGUI extends Simple3DGUI {

	private static final long serialVersionUID = 5731521866570029959L;

	private IndexedTriangleArray faceArray;

	public SimpleFaceGUI() {
	}

	public SimpleFaceGUI(int width, int height) {
		super(width, height);
	}

	public void updateFace(Shape3D target) {
		if(faceArray == null) {
			faceArray = (IndexedTriangleArray) target.getGeometry();
			this.displayStaticShape(target);
			return;
		}

		IndexedTriangleArray targetArray = (IndexedTriangleArray) target.getGeometry();
		faceArray.setCoordRefDouble(targetArray.getCoordRefDouble());
		faceArray.setColorRefByte(targetArray.getColorRefByte());
	}
}
