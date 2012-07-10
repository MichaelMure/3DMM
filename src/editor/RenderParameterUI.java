package editor;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import parameter.RenderParameter;

public class RenderParameterUI extends JPanel {

	private static final long serialVersionUID = -1206184947044995069L;

	public RenderParameterUI(RenderParameter param) {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(new JLabel("Render parameters:"));

		for(int i = 0; i <= RenderParameter.LAST_PARAM; i++) {
			this.add(new ParamSlider(param, i));
		}
	}
}
