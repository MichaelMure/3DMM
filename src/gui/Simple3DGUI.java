package gui;

import java.awt.Dimension;
import java.awt.GraphicsConfiguration;

import javax.media.j3d.Alpha;
import javax.media.j3d.Background;
import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingBox;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Node;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;

import util.Log;
import util.Log.LogType;

import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.universe.SimpleUniverse;

public class Simple3DGUI extends JFrame {

	private static final long serialVersionUID = 7138232013272665387L;
	private static final float EYE_DISTANCE = 5.0f;

	private GraphicsConfiguration config;
	private Canvas3D canvas;
	private BranchGroup scene;
	private SimpleUniverse universe;

	public Simple3DGUI() {
		this(800, 600);
	}

	public Simple3DGUI(int width, int height) {
		initComponents(width, height);
		Log.debug(LogType.GUI, "Simple 3D GUI initialized");
	}

	public void displayStaticShape(Shape3D shape) {
		scene = new BranchGroup();
		scene.addChild(autoScale(shape));
	}

	public void displayRotatingShape(Shape3D shape) {
		scene = new BranchGroup();

		/* Small rotation on the root */
		Transform3D rotate = new Transform3D();
		rotate.rotX(Math.PI/16.0d);
		TransformGroup objRotate = new TransformGroup(rotate);
		scene.addChild(objRotate);

		/* Spinning group */
		TransformGroup objSpin = new TransformGroup();
		objSpin.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		objRotate.addChild(objSpin);

		/* Object */
		objSpin.addChild(autoScale(shape));

		/* Interpolator for spinning */
		RotationInterpolator rotator = new RotationInterpolator(new Alpha(-1, 4000), objSpin, new Transform3D(),
				0.0f, (float) Math.PI*2.0f);
		BoundingSphere bounds = new BoundingSphere();
		rotator.setSchedulingBounds(bounds);
		objSpin.addChild(rotator);
	}

	/** Add a custom updater to the scene. */
	public void addBehavior(Behavior behavior) {
		scene.addChild(behavior);
	}

	public void run() {
		scene.compile();
		universe.addBranchGraph(scene);
	}

	private void initComponents(int width, int height) {
		/* JFrame stuff */
		Dimension mainWDim = new Dimension(width, height);

		this.setPreferredSize(mainWDim);
		this.setSize(mainWDim);
		this.setResizable(true);
		this.setVisible(true);

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		/* Canvas3D stuff */
		config = SimpleUniverse.getPreferredConfiguration();
		canvas = new HUDCanvas3D(config);
		universe = new SimpleUniverse(canvas);
		scene = new BranchGroup();

		/* Set the background color and its application bounds */
		Background background = new Background();
		background.setColor(new Color3f(0.5f,0.5f,0.5f));
		background.setCapability(Background.ALLOW_COLOR_WRITE);
		BoundingSphere worldBounds = new BoundingSphere(new Point3d(0.0, 0.0,0.0), // Center
				1000.0); // Extent
		background.setApplicationBounds(worldBounds);
		scene.addChild(background);


		universe.getViewingPlatform().setNominalViewingTransform();
		universe.addBranchGraph(scene);

		this.add(canvas);
		pack();
	}

	/** Compute the size of the object, and if needed, add a scaling transform node to fit on display. */
	private Node autoScale(Shape3D shape) {
		BoundingBox bounds = (BoundingBox) shape.getBounds();
		Point3d lower = new Point3d();
		Point3d upper = new Point3d();
		bounds.getLower(lower);
		bounds.getUpper(upper);

		if(lower.distance(upper) > 0.0) {
			double screenRadius = EYE_DISTANCE * Math.tan(universe.getViewer().getView().getFieldOfView() / 2.0);
			double scale = 0.8 * screenRadius / lower.distance(upper);
			Log.info(LogType.GUI, "Auto-scaling: "+ scale);

			// Scale the content branch to display at the correct size on the screen.
			Transform3D transform = new Transform3D();
			transform.setScale(scale);

			TransformGroup objScale = new TransformGroup();
			objScale.setTransform(transform);
			objScale.addChild(shape);
			return objScale;
		}

		Log.info(LogType.GUI, "Not auto-scaling");
		return shape;
	}

	/** Replace the scene with a static cube */
	@SuppressWarnings("unused")
	private void cubeScene() {
		displayStaticShape(new ColorCube(0.4));
	}


	/** Replace the scene with a rotating cube */
	@SuppressWarnings("unused")
	private void rotatingCubeScene() {
		displayRotatingShape(new ColorCube(0.4));
	}
}
