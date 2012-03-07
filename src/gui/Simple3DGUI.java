package gui;

import java.awt.Dimension;
import java.awt.GraphicsConfiguration;

import javax.media.j3d.*;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.universe.SimpleUniverse;

public class Simple3DGUI extends JFrame {

	private static final long serialVersionUID = 7138232013272665387L;

	private GraphicsConfiguration config;
	private Canvas3D canvas;
	private BranchGroup scene;
	private SimpleUniverse universe;
	
	public Simple3DGUI() {
		this(800, 600);
	}
	
	public Simple3DGUI(int width, int height) {
		initComponents(width, height);
		rotatingCubeScene();
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
        canvas = new Canvas3D(config);
        universe = new SimpleUniverse(canvas);
        scene = new BranchGroup();

        universe.getViewingPlatform().setNominalViewingTransform();
        universe.addBranchGraph(scene);
        
        this.add(canvas);
        pack();
	}
	
	/** Replace the scene with a static cube */
	@SuppressWarnings("unused")
	private void cubeScene() {
		scene = new BranchGroup();
		scene.addChild(new ColorCube(0.4));
		universe.addBranchGraph(scene);
	}
	
	
	/** Replace the scene with a rotating cube */
	private void rotatingCubeScene() {
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
		
		/* Cube */
		objSpin.addChild(new ColorCube(0.4));
		
		/* Interpolator for spinning */
		RotationInterpolator rotator = new RotationInterpolator(new Alpha(-1, 4000), objSpin, new Transform3D(),
						     0.0f, (float) Math.PI*2.0f);
		BoundingSphere bounds = new BoundingSphere();
		rotator.setSchedulingBounds(bounds);
		objSpin.addChild(rotator);
		
		universe.addBranchGraph(scene);
	}
	
	
}
