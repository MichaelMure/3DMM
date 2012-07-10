package editor;

import java.io.IOException;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;

import com.jmatio.io.MatFileReader;
import com.jmatio.types.MLArray;
import com.jmatio.types.MLDouble;

import parameter.ModelParameter;

public class AttributeUI extends JPanel {

	private static final long serialVersionUID = -697244512831277652L;

	public AttributeUI(String filename, ModelParameter param) throws IOException {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(new JLabel("Attributes:"));

		MatFileReader reader = new MatFileReader(filename);
		Map<String, MLArray> map = reader.getContent();

		/* Sanity check */
		assert(map.containsKey("age_shape"));
		assert(map.containsKey("age_tex"));
		assert(map.containsKey("gender_shape"));
		assert(map.containsKey("gender_tex"));
		assert(map.containsKey("height_shape"));
		assert(map.containsKey("height_tex"));
		assert(map.containsKey("weight_shape"));
		assert(map.containsKey("weight_tex"));

		int dim = ModelParameter.getModelCount();

		this.add(new AttributeButtons(param, "Age", readAttribute(map, "age", dim), 12f));
		this.add(new AttributeButtons(param, "Gender", readAttribute(map, "gender", dim), 1f));
		this.add(new AttributeButtons(param, "Height", readAttribute(map, "height", dim), 3f));
		this.add(new AttributeButtons(param, "Weight", readAttribute(map, "weight", dim), 3f));
	}

	private DoubleMatrix1D readAttribute(Map<String, MLArray> map, String attribute, int dimension) {
		DoubleMatrix1D result = new DenseDoubleMatrix1D(2*dimension);

		MLArray array = map.get(attribute + "_shape");
		assert(array.isDouble());
		MLDouble attrShape =  (MLDouble) array;
		for(int x = 0; x < dimension; x++)
			result.setQuick(x, attrShape.get(x));

		array = map.get(attribute + "_tex");
		assert(array.isDouble());
		MLDouble attrTex =  (MLDouble) array;
		for(int x = 0; x < dimension; x++)
			result.setQuick(dimension + x, attrTex.get(x));

		return result;
	}
}
