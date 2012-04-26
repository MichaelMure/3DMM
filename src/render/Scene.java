package render;

import io.ply.PlyImporter;

import java.awt.image.BufferedImage;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingBox;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.Material;
import javax.media.j3d.Node;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import util.Log;
import util.Log.LogType;

import com.sun.j3d.utils.universe.SimpleUniverse;

public class Scene {

	private static final float EYE_DISTANCE = 5.0f;

	private final BufferedImage target;
	private final SimpleUniverse universe;
	private final BranchGroup scene;

	private Transform3D viewTransform;

	private Transform3D translate;
	private Transform3D rotate;
	private Transform3D scale;

	private DirectionalLight directLight;
	private AmbientLight ambientLight;

	private Shape3D object;
	private Material material;

	public Scene(Canvas3D canvas, BufferedImage target, RenderParameter params) {
		this.target = target;
		universe = new SimpleUniverse(canvas);

		scene = createScene();
		update(params);
		universe.addBranchGraph(scene);
	}

	private BranchGroup createScene() {
		BranchGroup root = new BranchGroup();

		/* Background: likely to go away in favor of post-process compositing. */
		Background background = new Background(new ImageComponent2D(BufferedImage.TYPE_INT_ARGB, target));
		background.setApplicationBounds(new BoundingSphere(new Point3d(0.0, 0.0,0.0),1000.0));
		root.addChild(background);

		/* Object */
		PlyImporter imp = new PlyImporter();
		object = imp.loadObject("data/00006_20080430_04384_neutral_face05.ply");

		/* Object transform */
		translate = new Transform3D();
		TransformGroup translateGroup = new TransformGroup(translate);
		root.addChild(translateGroup);

		rotate = new Transform3D();
		TransformGroup rotateGroup = new TransformGroup(rotate);
		translateGroup.addChild(rotateGroup);

		scale = new Transform3D();
		TransformGroup scaleGroup = new TransformGroup(scale);
		rotateGroup.addChild(scaleGroup);

		//scaleGroup.addChild(object);

		scaleGroup.addChild(autoScale(object));

		/* Object material */
		material = new Material();
		material.setCapability(Material.ALLOW_COMPONENT_WRITE);
		Appearance appearance = new Appearance();
		appearance.setMaterial(material);
		object.setAppearance(appearance);

		/* View */
		viewTransform = new Transform3D();

		/* Lightning */
		directLight = new DirectionalLight();
		directLight.setInfluencingBounds(new BoundingSphere(new Point3d(0.0, 0.0,0.0),1000.0));
		directLight.setCapability(DirectionalLight.ALLOW_DIRECTION_WRITE);
		directLight.setCapability(DirectionalLight.ALLOW_COLOR_WRITE);
		root.addChild(directLight);

		ambientLight = new AmbientLight();
		ambientLight.setInfluencingBounds(new BoundingSphere(new Point3d(0.0, 0.0,0.0),1000.0));
		ambientLight.setCapability(AmbientLight.ALLOW_COLOR_WRITE);
		root.addChild(ambientLight);

		root.compile();
		return root;
	}

	public void update(RenderParameter params) {
		/* Update view */
		viewTransform.lookAt(	new Point3d(0.0, 0.0, params.getCameraDistance()),
													new Point3d(),
													new Vector3d(0.0, 1.0, 0.0));
		viewTransform.invert();
		universe.getViewingPlatform().getViewPlatformTransform().setTransform(viewTransform);
		universe.getViewingPlatform().getViewPlatform().setActivationRadius(10000f);

		/* Place object */
		translate.setTranslation(params.getObjectPosition());
		rotate.setRotation(params.getObjectRotation());
		scale.setScale(params.getObjectScale());

		/* Update lightning */
		directLight.setDirection(params.getDirectedLightDirection());
		directLight.setColor(params.getDirectedLightColor());
		ambientLight.setColor(params.getAmbientLightColor());

		/* Update object */
		material.setShininess(params.getObjectShininess());
	}

	/** Compute the size of the object, and if needed, add a scaling transform node to fit on display. */
	private Node autoScale(Shape3D object) {
		BoundingBox bounds = (BoundingBox) object.getBounds();
		Point3d lower = new Point3d();
		Point3d upper = new Point3d();
		bounds.getLower(lower);
		bounds.getUpper(upper);

		double scale = 1.0 / lower.distance(upper);
		Log.info(LogType.GUI, "Auto-scaling: "+ scale);

		Transform3D transform = new Transform3D();
		transform.setScale(scale);

		TransformGroup objScale = new TransformGroup();
		objScale.setTransform(transform);
		objScale.addChild(object);
		return objScale;
	}
}
