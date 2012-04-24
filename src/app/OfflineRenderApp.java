package app;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import render.SceneRenderer;
import util.Log;
import util.Log.LogType;

public class OfflineRenderApp {

	public static void main(String[] args) {
		BufferedImage target = null;
		Log.info(LogType.APP, "Loading image: " + args[0]);
		try {
			target = ImageIO.read(new File(args[0]));
		} catch (IOException e) {
			e.printStackTrace();
		}

		SceneRenderer renderer = new SceneRenderer(target);
		BufferedImage result = renderer.getRender();

		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.getContentPane().add(new JLabel(new ImageIcon(result)));
		frame.pack();
		frame.setVisible(true);
	}
}
