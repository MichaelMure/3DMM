package render;

import java.awt.Color;

import javax.vecmath.Color3f;
import javax.vecmath.Quat4d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

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

	private final static int COLOR_OFFSET_R = 17;
	private final static int COLOR_OFFSET_G = 18;
	private final static int COLOR_OFFSET_B = 19;

	private final static int COLOR_GAIN_R = 20;
	private final static int COLOR_GAIN_G = 21;
	private final static int COLOR_GAIN_B = 22;

	private final static int OBJECT_SHININESS = 23;

	private final static int PARAMS_SIZE = OBJECT_SHININESS +1;


	private DenseDoubleMatrix1D matrix = new DenseDoubleMatrix1D(PARAMS_SIZE);

	public RenderParameter() {
		setCameraDistance(3.0);
		setObjectScale(1.0);
		setObjectPosition(new Vector3d());
		setObjectRotation(new Quat4d());
		setAmbientLightColor(new Color3f(Color.WHITE));
		setDirectedLightColor(new Color3f(Color.WHITE));
		setDirectedLightDirection(new Vector3f(0,1,0));

		float[] offsets = new float[4];
		offsets[0] = 1.0f;
		offsets[1] = 1.0f;
		offsets[2] = 1.0f;
		offsets[3] = 1.0f;
		setColorsOffsets(offsets);

		float[] gains = new float[4];
		gains[0] = 0.0f;
		gains[1] = 0.0f;
		gains[2] = 0.0f;
		gains[3] = 0.0f;
		setColorsGains(gains);

		setObjectShininess(1f);
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

	public Color3f getAmbientLightColor() {
		return new Color3f((float) matrix.getQuick(AMBIENT_COLOR_R),
				(float) matrix.getQuick(AMBIENT_COLOR_G),
				(float) matrix.getQuick(AMBIENT_COLOR_B));
	}

	public void setAmbientLightColor(Color3f ambientLightColor) {
		matrix.setQuick(AMBIENT_COLOR_R, ambientLightColor.x);
		matrix.setQuick(AMBIENT_COLOR_G, ambientLightColor.y);
		matrix.setQuick(AMBIENT_COLOR_B, ambientLightColor.z);
	}

	public Color3f getDirectedLightColor() {
		return new Color3f((float) matrix.getQuick(DIRECTED_LIGHT_COLOR_R),
				(float) matrix.getQuick(DIRECTED_LIGHT_COLOR_G),
				(float) matrix.getQuick(DIRECTED_LIGHT_COLOR_B));
	}

	public void setDirectedLightColor(Color3f directedLightColor) {
		matrix.setQuick(DIRECTED_LIGHT_COLOR_R, directedLightColor.x);
		matrix.setQuick(DIRECTED_LIGHT_COLOR_G, directedLightColor.y);
		matrix.setQuick(DIRECTED_LIGHT_COLOR_B, directedLightColor.z);
	}

	public Vector3f getDirectedLightDirection() {
		return new Vector3f((float) matrix.getQuick(DIRECTED_LIGHT_DIRECTION_X),
				(float) matrix.getQuick(DIRECTED_LIGHT_DIRECTION_Y),
				(float) matrix.getQuick(DIRECTED_LIGHT_DIRECTION_Z));
	}

	public void setDirectedLightDirection(Vector3f directedLightDirection) {
		matrix.setQuick(DIRECTED_LIGHT_DIRECTION_X, directedLightDirection.x);
		matrix.setQuick(DIRECTED_LIGHT_DIRECTION_Y, directedLightDirection.y);
		matrix.setQuick(DIRECTED_LIGHT_DIRECTION_Z, directedLightDirection.z);
	}

	public float[] getColorsOffsets() {
		float[] tab = new float[4];
		tab[0] = (float) matrix.getQuick(COLOR_OFFSET_R);
		tab[1] = (float) matrix.getQuick(COLOR_OFFSET_G);
		tab[2] = (float) matrix.getQuick(COLOR_OFFSET_B);
		tab[3] = 0.0f;
		return tab;
	}

	public void setColorsOffsets(float[] colorsOffsets) {
		if(colorsOffsets.length != 3 && colorsOffsets.length != 4)
			throw new IllegalArgumentException("Unexpected array length for colors offsets.");
					matrix.setQuick(COLOR_OFFSET_R, colorsOffsets[0]);
					matrix.setQuick(COLOR_OFFSET_G, colorsOffsets[1]);
					matrix.setQuick(COLOR_OFFSET_B, colorsOffsets[2]);
	}

	public float[] getColorsGains() {
		float[] tab = new float[4];
		tab[0] = (float) matrix.getQuick(COLOR_GAIN_R);
		tab[1] = (float) matrix.getQuick(COLOR_GAIN_G);
		tab[2] = (float) matrix.getQuick(COLOR_GAIN_B);
		tab[3] = 1.0f;
		return tab;
	}

	public void setColorsGains(float[] colorsGains) {
		if(colorsGains.length != 3 && colorsGains.length != 4)
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
