package gui;

import java.awt.Canvas;
import java.awt.EventQueue;
import javax.swing.JFrame;

import model.Model;

import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;

import util.Log;
import util.Log.LogType;

public class Simple3DGUI {
	private JmeCanvasContext context;
  private Canvas canvas;
  private DisplayApp app;
  private JFrame frame;


	public void run() {
		try {
			EventQueue.invokeAndWait(new Runnable() {

				@Override
				public void run() {
					createCanvas();
					createFrame();
				}
			});
		} catch (Exception e) {
			Log.error(LogType.GUI, "Error while launching the GUI.");
			e.printStackTrace();
		}
	}

	private void createCanvas() {
		AppSettings settings = new AppSettings(true);
    settings.setWidth(640);
    settings.setHeight(480);
    /* Disable audio */
    settings.setAudioRenderer(null);

    app = new DisplayApp();
    app.setPauseOnLostFocus(false);
    app.setSettings(settings);
    app.createCanvas();
    app.startCanvas();

    context = (JmeCanvasContext) app.getContext();
    context.setSystemListener(app);
    canvas = context.getCanvas();
    canvas.setSize(settings.getWidth(), settings.getHeight());
	}

	private void createFrame() {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(canvas);
		frame.pack();
		frame.setVisible(true);
	}

	public void displayUnshaded(Model mesh) {
		app.displayUnshaded(mesh);
	}

}
