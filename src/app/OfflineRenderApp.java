package app;

import io.Importer;
import io.ply.PlyImporter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import parameter.ModelParameter;
import parameter.RenderParameter;


import model.MorphableModel;
import model.MorphableModelBuilder;

import com.jme3.scene.Mesh;

import render.FittingRenderer;
import render.FittingScene;
import util.Log;
import util.Log.LogType;

public class OfflineRenderApp implements FittingRenderer.Callback {

	private FittingRenderer renderer;

	public OfflineRenderApp(String[] args) {
		Log.info(LogType.APP, "Loading image: " + args[0]);

		Importer importer = new PlyImporter();

		try {
			Mesh face = importer.loadObject("data/00017_20061201_00812_neutral_face05_BAD.ply");
			BufferedImage target = ImageIO.read(new File(args[0]));
			FittingScene scene = new FittingScene(face);
			RenderParameter param = new RenderParameter();
			param.initObjectScale(face);

			renderer = new FittingRenderer(this, scene, target, param);
			renderer.setShowSettings(false);
			renderer.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void rendererCallback() {
		BufferedImage result = renderer.getRender();
		renderer.stop();

		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.getContentPane().add(new JLabel(new ImageIcon(result)));
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		Logger.getLogger("").setLevel(Level.WARNING);
		new OfflineRenderApp(args);
	}
}
