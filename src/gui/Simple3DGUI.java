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
  private Canvas canvas;
  private DisplayApp app;
  private JFrame frame;

  private final int width;
  private final int height;

  public Simple3DGUI() {
  	this(640,480);
  }

  public Simple3DGUI(int width, int height) {
  	this.width = width;
  	this.height = height;
  	run();
  }

	private void run() {
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
    settings.setWidth(width);
    settings.setHeight(height);
    /* Disable audio */
    settings.setAudioRenderer(null);

    app = new DisplayApp();
    app.setPauseOnLostFocus(false);
    app.setSettings(settings);
    app.createCanvas();
    app.startCanvas();

    JmeCanvasContext context = (JmeCanvasContext) app.getContext();
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

	public void displayUnshaded(Model model) {
		app.displayUnshaded(model);
	}

}
