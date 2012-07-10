package editor;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import parameter.ModelParameter;

public class ModelParameterUI extends JPanel {

	private static final long serialVersionUID = 7252416861944924355L;

	public ModelParameterUI(ModelParameter param) {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(new JLabel("Model parameters:"));

		int modelCount = ModelParameter.getModelCount();

		for(int i = 0; i < modelCount; i++) {
			this.add(new ParamSlider(param, i));
		}

		for(int i = 0; i < modelCount; i++) {
			this.add(new ParamSlider(param, modelCount + i));
		}
	}
}
