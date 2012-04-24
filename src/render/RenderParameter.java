package render;

import javax.vecmath.Color3b;
import javax.vecmath.Quat4d;
import javax.vecmath.Vector3d;

import util.TwoComplement;

import cern.colt.matrix.impl.DenseDoubleMatrix1D;

public class RenderParameter {

	private final static int CAMERA_DISTANCE = 0;

	private final static int OBJECT_SCALE = 1;

	private final static int OBJECT_POSITION_X = 2;
	private final static int OBJECT_POSITION_Y = 3;

	private final static int OBJECT_ROTATION_X = 4;
	private final static int OBJECT_ROTATION_Y = 5;
	private final static int OBJECT_ROTATION_Z = 6;
	private final static int OBJECT_ROTATION_W = 7;

	private final static int AMBIENT_COLOR_R = 8;
	private final static int AMBIENT_COLOR_G = 9;
	private final static int AMBIENT_COLOR_B = 10;

	private final static int DIRECTED_LIGHT_COLOR_R = 11;
	private final static int DIRECTED_LIGHT_COLOR_G = 12;
	private final static int DIRECTED_LIGHT_COLOR_B = 13;

	private final static int DIRECTED_LIGHT_DIRECTION_X = 14;
	private final static int DIRECTED_LIGHT_DIRECTION_Y = 15;
	private final static int DIRECTED_LIGHT_DIRECTION_Z = 16;
	private final static int DIRECTED_LIGHT_DIRECTION_W = 17;

	private final static int COLOR_OFFSET_R = 18;
	private final static int COLOR_OFFSET_G = 19;
	private final static int COLOR_OFFSET_B = 20;

	private final static int COLOR_GAIN_R = 21;
	private final static int COLOR_GAIN_G = 22;
	private final static int COLOR_GAIN_B = 23;

	private final static int OBJECT_SHININESS = 24;

	private final static int PARAM_NUMBER = OBJECT_SHININESS +1;


	private DenseDoubleMatrix1D matrix = new DenseDoubleMatrix1D(PARAM_NUMBER);

	public RenderParameter() {
		//Insert plausible default value here.
	}

	public DenseDoubleMatrix1D getMatrix() {
		return matrix;
	}

	public void setMatrix(DenseDoubleMatrix1D matrix) {
		this.matrix = matrix;
	}

	public double getCameraDistance() {
		return matrix.getQuick(CAMERA_DISTANCE);
	}

	public void setCameraDistance(double cameraDistance) {
		matrix.setQuick(CAMERA_DISTANCE, cameraDistance);
	}

	public double getObjectScale() {
		return matrix.getQuick(OBJECT_SCALE);
	}

	public void setObjectScale(double objectScale) {
		matrix.setQuick(OBJECT_SCALE, objectScale);
	}

	public Vector3d getObjectPosition() {
		return new Vector3d(matrix.getQuick(OBJECT_POSITION_X),
												matrix.getQuick(OBJECT_POSITION_Y),
												0.0);
	}

	public void setObjectPosition(Vector3d objectPosition) {
		matrix.setQuick(OBJECT_POSITION_X, objectPosition.x);
		matrix.setQuick(OBJECT_POSITION_Y, objectPosition.y);
	}

	public Quat4d getObjectRotation() {
		return new Quat4d(matrix.getQuick(OBJECT_ROTATION_X),
											matrix.getQuick(OBJECT_ROTATION_Y),
											matrix.getQuick(OBJECT_ROTATION_Z),
											matrix.getQuick(OBJECT_ROTATION_W));
	}

	public void setObjectRotation(Quat4d objectRotation) {
		matrix.setQuick(OBJECT_ROTATION_X, objectRotation.x);
		matrix.setQuick(OBJECT_ROTATION_Y, objectRotation.y);
		matrix.setQuick(OBJECT_ROTATION_Z, objectRotation.z);
		matrix.setQuick(OBJECT_ROTATION_W, objectRotation.w);
	}

	public Color3b getAmbientLightColor() {
		return new Color3b(TwoComplement.from2complement(matrix.getQuick(AMBIENT_COLOR_R)),
											 TwoComplement.from2complement(matrix.getQuick(AMBIENT_COLOR_G)),
											 TwoComplement.from2complement(matrix.getQuick(AMBIENT_COLOR_B)));
	}

	public void setAmbientLightColor(Color3b ambientLightColor) {
		matrix.setQuick(AMBIENT_COLOR_R, TwoComplement.to2complement(ambientLightColor.x));
		matrix.setQuick(AMBIENT_COLOR_G, TwoComplement.to2complement(ambientLightColor.y));
		matrix.setQuick(AMBIENT_COLOR_B, TwoComplement.to2complement(ambientLightColor.z));
	}

	public Color3b getDirectedLightColor() {
		return new Color3b(TwoComplement.from2complement(matrix.getQuick(DIRECTED_LIGHT_COLOR_R)),
											 TwoComplement.from2complement(matrix.getQuick(DIRECTED_LIGHT_COLOR_G)),
											 TwoComplement.from2complement(matrix.getQuick(DIRECTED_LIGHT_COLOR_B)));
	}

	public void setDirectedLightColor(Color3b directedLightColor) {
		matrix.setQuick(DIRECTED_LIGHT_COLOR_R, TwoComplement.to2complement(directedLightColor.x));
		matrix.setQuick(DIRECTED_LIGHT_COLOR_G, TwoComplement.to2complement(directedLightColor.y));
		matrix.setQuick(DIRECTED_LIGHT_COLOR_B, TwoComplement.to2complement(directedLightColor.z));
	}

	public Quat4d getDirectedLightDirection() {
		return new Quat4d(matrix.getQuick(DIRECTED_LIGHT_DIRECTION_X),
											matrix.getQuick(DIRECTED_LIGHT_DIRECTION_Y),
											matrix.getQuick(DIRECTED_LIGHT_DIRECTION_Z),
											matrix.getQuick(DIRECTED_LIGHT_DIRECTION_W));
	}

	public void setDirectedLightDirection(Quat4d directedLightDirection) {
		matrix.setQuick(DIRECTED_LIGHT_DIRECTION_X, directedLightDirection.x);
		matrix.setQuick(DIRECTED_LIGHT_DIRECTION_Y, directedLightDirection.y);
		matrix.setQuick(DIRECTED_LIGHT_DIRECTION_Z, directedLightDirection.z);
		matrix.setQuick(DIRECTED_LIGHT_DIRECTION_W, directedLightDirection.w);
	}

	public float[] getColorsOffsets() {
		float[] tab = new float[3];
		tab[0] = (float) matrix.getQuick(COLOR_OFFSET_R);
		tab[1] = (float) matrix.getQuick(COLOR_OFFSET_G);
		tab[2] = (float) matrix.getQuick(COLOR_OFFSET_B);
		return tab;
	}

	public void setColorsOffsets(float[] colorsOffsets) {
		if(colorsOffsets.length != 3)
			throw new IllegalArgumentException("Unexpected array length for colors offsets.");
		matrix.setQuick(COLOR_OFFSET_R, colorsOffsets[0]);
		matrix.setQuick(COLOR_OFFSET_G, colorsOffsets[1]);
		matrix.setQuick(COLOR_OFFSET_B, colorsOffsets[2]);
	}

	public float[] getColorsGains() {
		float[] tab = new float[3];
		tab[0] = (float) matrix.getQuick(COLOR_GAIN_R);
		tab[1] = (float) matrix.getQuick(COLOR_GAIN_G);
		tab[2] = (float) matrix.getQuick(COLOR_GAIN_B);
		return tab;
	}

	public void setColorsGains(float[] colorsGains) {
		if(colorsGains.length != 3)
			throw new IllegalArgumentException("Unexpected array length for colors gains.");
		matrix.setQuick(COLOR_GAIN_R, colorsGains[0]);
		matrix.setQuick(COLOR_GAIN_G, colorsGains[1]);
		matrix.setQuick(COLOR_GAIN_B, colorsGains[2]);
	}

	public float getObjectShininess() {
		return (float) matrix.getQuick(OBJECT_SHININESS);
	}

	public void setObjectShininess(float objectShininess) {
		matrix.setQuick(OBJECT_SHININESS, objectShininess);
	}
}
