package editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cern.colt.matrix.DoubleMatrix1D;

import parameter.ModelParameter;

public class AttributeButtons extends JPanel {

	private static final long serialVersionUID = -4409767652756651033L;

	private final ModelParameter param;
	private final DoubleMatrix1D attribute;
	private final float resolution;

	private JLabel description;

	public AttributeButtons(ModelParameter param, String descr, DoubleMatrix1D attribute, float resolution) {
		this.param = param;
		this.attribute = attribute;
		this.resolution = resolution;

		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		this.description = new JLabel(descr);
		this.add(this.description);


		JButton btnMinus = new JButton("-");
		btnMinus.addActionListener(new BtnMinusListener());
		this.add(btnMinus);

		JButton btnPlus = new JButton("+");
		btnPlus.addActionListener(new BtnPlusListener());
		this.add(btnPlus);
	}

	private class BtnMinusListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			for(int i = 0; i < ModelParameter.getModelCount() * 2; i++) {
				param.set(i, param.get(i) - attribute.get(i) * resolution);
			}
		}
	}

	private class BtnPlusListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			for(int i = 0; i < ModelParameter.getModelCount() * 2; i++) {
				param.set(i, param.get(i) + attribute.get(i) * resolution);
			}
		}
	}
}
