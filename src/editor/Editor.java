package editor;

import gui.DisplayApp;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;

import render.RenderParameter;

import model.Model;
import model.ModelParameter;
import model.MorphableModel;

import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;

public class Editor {
	private JmeCanvasContext context;
  private Canvas canvas;
  private DisplayApp app;
  private JFrame frame;
  private JPanel sliderPanel;

  private ModelParameter modelParam;
  private RenderParameter renderParam;
  private Model model;

  public Editor(MorphableModel mm) {
		ModelParameter.setMorphableModel(mm);

		modelParam = new ModelParameter();
		renderParam = new RenderParameter();
		model = modelParam.getModel();
		renderParam.initObjectScale(model.getMesh());

		createCanvas();
		app.displayUnshaded(model);
		createFrame();
  }

	private void createCanvas() {
		AppSettings settings = new AppSettings(true);
    settings.setWidth(480);
    settings.setHeight(640);
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
    canvas.setMinimumSize(new Dimension(200, 200));
    canvas.setPreferredSize(new Dimension(settings.getWidth(), settings.getHeight()));
    //canvas.setSize(settings.getWidth(), settings.getHeight());
	}

	private void createFrame() {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JSplitPane splitPane = new JSplitPane();
		JScrollPane scrollPane = new JScrollPane();
		sliderPanel = new JPanel();

		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		splitPane.setLeftComponent(canvas);
		splitPane.setRightComponent(scrollPane);

		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setViewportView(sliderPanel);
		//scrollPane.setMinimumSize(new Dimension(200, 200));

		sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.Y_AXIS));
		sliderPanel.setMinimumSize(new Dimension(200, 200));
		sliderPanel.setPreferredSize(new Dimension(400, 600));

		createSliders();

		frame.getContentPane().add(splitPane, BorderLayout.CENTER);

		frame.pack();
		frame.setVisible(true);
	}

	private void createSliders() {
		sliderPanel.add(new JLabel("Model parameters:"));
		for(int i = 0; i < modelParam.getModelCount(); i++) {
			sliderPanel.add(new Slider("Eigen Face " + (i+1), -1, 1, 0.01, 0));
		}
	}
}
