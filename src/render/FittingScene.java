package render;

import parameter.RenderParameter;
import util.NormalGenerator;

import com.jme3.asset.AssetManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.shadow.BasicShadowRenderer;

public class FittingScene {

	private Mesh mesh = null;
	private Geometry geom = null;
	private Material mat = null;
	private Camera cam;

	private DirectionalLight directLight;
	private AmbientLight ambientLight;
	private BasicShadowRenderer bsr;
	private ColorRescaleFilter crf;

	public FittingScene(Mesh mesh) {
		this.mesh = mesh;
	}

	public void createScene(Node rootNode, Camera cam, AssetManager assetManager, ViewPort viewPort, RenderParameter params) {
		this.cam = cam;

		/* Object */
		NormalGenerator.ComputeNormal(mesh);

		geom = new Geometry("mesh", mesh);

		/* Material */
		mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
		mat.setBoolean("UseVertexColor", true);
		mat.setBoolean("LowQuality", true);

		geom.setMaterial(mat);

		/* Object transform */
		rootNode.attachChild(geom);

		/* Lightning */
		directLight = new DirectionalLight();
    rootNode.addLight(directLight);

    ambientLight = new AmbientLight();
    rootNode.addLight(ambientLight);

    /* Shadow */
    geom.setShadowMode(ShadowMode.CastAndReceive);

    /*PssmShadowRenderer pssmRenderer = new PssmShadowRenderer(assetManager, 1024, 1);
    pssmRenderer.setDirection(new Vector3f(0,-1,10).normalizeLocal()); // light direction
    pssmRenderer.setFilterMode(FilterMode.Bilinear);
    //pssmRenderer.displayDebug();
    viewPort.addProcessor(pssmRenderer);*/

    bsr = new BasicShadowRenderer(assetManager, 1024);
    //viewPort.addProcessor(bsr);

    /*FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
		SSAOFilter ssaoFilter = new SSAOFilter(12.94f, 43.92f, 0.33f, 0.61f);
		fpp.addFilter(ssaoFilter);
		viewPort.addProcessor(fpp);*/

		/* Colors */
		FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
		crf = new ColorRescaleFilter();
		fpp.addFilter(crf);
		viewPort.addProcessor(fpp);

		update(params);
	}

	public void update(RenderParameter params) {
		/* Update view */
		cam.setLocation(new Vector3f(0f, 0f, params.getCameraDistance()));

		/* Place object */
		geom.setLocalRotation(params.getObjectRotation());
		geom.setLocalTranslation(params.getObjectPosition());
		geom.setLocalScale(params.getObjectScale());

		/* Update lightning */
		directLight.setDirection(params.getDirectedLightDirection());
		directLight.setColor(params.getDirectedLightColor());
		ambientLight.setColor(params.getAmbientLightColor());
		bsr.setDirection(params.getDirectedLightDirection());

		/* Colors */
		crf.setOffsets(params.getColorsOffsets());
		crf.setGains(params.getColorsGains());

		/* Update object */
		mat.setFloat("Shininess", params.getObjectShininess());
	}
}