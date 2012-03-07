package gui;

import java.awt.Dimension;
import java.awt.GraphicsConfiguration;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
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
	
}
