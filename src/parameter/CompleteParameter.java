package parameter;

import render.FittingScene;

import com.jme3.scene.Mesh;

import model.Model;

public class CompleteParameter {

	private ModelParameter modelParams;
	private RenderParameter renderParams;

	public CompleteParameter() {
		this.modelParams = new ModelParameter();
		this.renderParams = new RenderParameter();
	}

	public CompleteParameter(CompleteParameter cp) {
		this.modelParams = new ModelParameter(cp.modelParams);
		this.renderParams = new RenderParameter(cp.renderParams);
	}

	public void copy(CompleteParameter cp) {
		this.modelParams.copy(cp.modelParams);
		this.renderParams.copy(cp.renderParams);
	}

	@Override
	public String toString() {
		return modelParams.toString() + "\n" + renderParams.toString();
	}

	public String getDataString() {
		return modelParams.getDataString() + "\t" + renderParams.getDataString();
	}

	public RenderParameter getRenderParameter() {
		return renderParams;
	}

	public ModelParameter getModelParameter() {
		return modelParams;
	}

	/** Utility shortcut to retrieve a Model from a ModelParameter */
	public Model getModel() {
		return modelParams.getModel();
	}

	/** Utility shortcut to retrieve a mesh from a ModelParameter */
	public Mesh getMesh() {
		return modelParams.getMesh();
	}

	/** Utility shortcut to update a mesh from a ModelParameter */
	public void updateMesh(Mesh mesh) {
		modelParams.updateMesh(mesh);
	}

	public void updateScene(FittingScene scene) {
		scene.update(renderParams);
	}
}
