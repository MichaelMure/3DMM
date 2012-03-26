package app;

import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Geometry;
import javax.media.j3d.GeometryUpdater;
import javax.media.j3d.IndexedTriangleArray;
import javax.media.j3d.WakeupOnElapsedFrames;
import javax.vecmath.Point3d;

import model.FaceParameter;
import model.MorphableModel;

public class RandomMorphingUpdater implements GeometryUpdater {

	private final MorphableModel model;
	private final IndexedTriangleArray faceArray;
	private FaceParameter origin;
	private FaceParameter target;
	private double alpha;


	public RandomMorphingUpdater(MorphableModel model,
			                   			 IndexedTriangleArray faceArray) {
		this.model = model;
		this.faceArray = faceArray;
		this.origin = FaceParameter.getRandomFaceParameter(model.getSize());
		this.target = FaceParameter.getRandomFaceParameter(model.getSize());
		this.alpha = 0.0;
	}

	public void updateData() {
		faceArray.updateData(this);
	}

	@Override
	public void updateData(Geometry geometry) {
		IndexedTriangleArray array = (IndexedTriangleArray) geometry;
		IndexedTriangleArray targetArray = model.getFace(origin.linearApplication(target, alpha)).getGeometry();
		array.setCoordRefDouble(targetArray.getCoordRefDouble());
		array.setColorRefByte(targetArray.getColorRefByte());
		alpha += 0.05;

		/* If morphing is ended, create a new random target. */
		if(alpha >= 1.0) {
			origin = target;
			target = FaceParameter.getRandomFaceParameter(model.getSize());
			alpha = 0.0;
		}
	}

	public Behavior getUpdateBehavior() {
		Behavior runner = new Behavior() {
			WakeupOnElapsedFrames wakeup = new WakeupOnElapsedFrames(0, false);

			@Override
			public void initialize() {
				wakeupOn(wakeup);
			}

			@SuppressWarnings("rawtypes")
			@Override
			public void processStimulus(Enumeration criteria) {
				updateData();
				initialize();
			}
		};

		runner.setSchedulingBounds(new BoundingSphere(new Point3d(0, 0, 0), 100));
		return runner;
	}

}
