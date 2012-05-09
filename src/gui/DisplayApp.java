package gui;

import model.Model;
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

class DisplayApp extends SimpleApplication {

	private ChaseCamera chaseCam;
	private Model model = null;
	private Geometry geom = null;
	private Mesh mesh = null;
	private long modelVersion;

	@Override
	public void simpleInitApp() {
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

	@Override
	public void simpleUpdate(float tpf) {
		if (model == null)
			return;

		if (geom == null) {
			mesh = model.getMesh();
			geom = new Geometry("mesh", mesh);
			modelVersion = model.getVersion();
			Material unshaded = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
			unshaded.setBoolean("VertexColor", true);
			geom.setMaterial(unshaded);
			rootNode.attachChildAt(autoScale(geom), 0);
		}

		modelVersion = model.updateMesh(modelVersion, mesh);
	}

	/** Display a model with just vertex colors. */
	public void displayUnshaded(Model model) {
		this.model = model;
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
}