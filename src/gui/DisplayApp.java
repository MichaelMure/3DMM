package gui;

import util.Log;
import util.Log.LogType;

import com.jme3.app.SimpleApplication;
import com.jme3.bounding.BoundingBox;
import com.jme3.input.ChaseCamera;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

class DisplayApp extends SimpleApplication {

	private Material unshaded;
	private ChaseCamera chaseCam;

	@Override
	public void simpleInitApp() {
		unshaded = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		unshaded.setBoolean("VertexColor", true);

		flyCam.setEnabled(false);
		chaseCam = new ChaseCamera(cam, rootNode, inputManager);
		chaseCam.setMinVerticalRotation((- FastMath.PI / 2.0f));
		chaseCam.setDefaultDistance(2.5f);
		chaseCam.setMinDistance(1.2f);
		chaseCam.setMaxDistance(10f);
		chaseCam.setInvertVerticalAxis(true);
		chaseCam.setDefaultHorizontalRotation((FastMath.PI /2));
		chaseCam.setDefaultVerticalRotation(0);

		cam.setFrustumPerspective(45, settings.getWidth() / settings.getHeight(), 0.1f, 100f);
	}

	public void displayStaticObject(Geometry geom) {
		rootNode.attachChildAt(autoScale(geom), 0);
	}

	public void displayUnshaded(Mesh mesh) {
		Geometry geom = new Geometry("mesh", mesh);
		geom.setMaterial(unshaded);
		displayStaticObject(geom);
	}

	/** Compute the size of the object, and if needed, add a scaling transform
	 *  node to top the size at 1.0.
	 */
	private Spatial autoScale(Geometry geom) {
		BoundingBox bb = (BoundingBox) geom.getModelBound();
		Vector3f extent = bb.getExtent(null);

		if(extent.length() > 1.0) {
			float scale = 1f / extent.length();
			Log.info(LogType.GUI, "Auto-scaling: "+ scale);

			Node scaleNode = new Node("Scale Node");
			scaleNode.setLocalScale(scale);
			scaleNode.attachChild(geom);

			return scaleNode;
		}

		Log.info(LogType.GUI, "Not auto-scaling");
		return geom;
	}


	@SuppressWarnings("unused")
	private void cubeScene() {
		Box b = new Box(1,1,1);
		Geometry geom = new Geometry("cube", b);
		geom.setMaterial(unshaded);
		displayStaticObject(geom);
	}
}