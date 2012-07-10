package editor;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;

import parameter.ModelParameter;
import parameter.RenderParameter;
import render.FittingScene;

import model.MorphableModel;

import com.jme3.app.SimpleApplication;
import com.jme3.scene.Mesh;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;

public class Editor {
	private JmeCanvasContext context;
  private Canvas canvas;
  private JFrame frame;
  private JPanel sliderPanel;

  private ModelParameter modelParam;
  private RenderParameter renderParam;
  private Mesh mesh;
  private FittingScene scene;

  public Editor(MorphableModel mm) throws IOException {
		ModelParameter.setMorphableModel(mm);

		modelParam = new ModelParameter();
		renderParam = new RenderParameter();

		mesh = modelParam.getMesh();
		renderParam.initObjectScale(mesh);
		scene = new FittingScene(mesh);

		createCanvas();
		createFrame();
  }

	private void createCanvas() {
		AppSettings settings = new AppSettings(true);
    settings.setWidth(480);
    settings.setHeight(640);
    /* Disable audio */
    settings.setAudioRenderer(null);

    EditorApplication app = new EditorApplication();
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

	private void createFrame() throws IOException {
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

		sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.Y_AXIS));

		createSliders();

		frame.getContentPane().add(splitPane, BorderLayout.CENTER);

		frame.pack();
		frame.setVisible(true);
	}

	private void createSliders() throws IOException {
		sliderPanel.add(new ModelParameterUI(modelParam));
		sliderPanel.add(new RenderParameterUI(renderParam));
		sliderPanel.add(new AttributeUI("data/04_attributes.mat", modelParam));
	}

	private class EditorApplication extends SimpleApplication {
		@Override
		public void simpleInitApp() {
			scene.createScene(rootNode, cam, assetManager, viewPort, renderParam);
			flyCam.setEnabled(false);
		}
		@Override
		public void simpleUpdate(float tpf) {
			scene.update(renderParam);
			modelParam.updateMesh(mesh);
    }
	}
}
