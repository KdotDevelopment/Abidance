package me.kdot.abidance.world.water;

import org.lwjgl.util.vector.Matrix4f;

import me.kdot.abidance.client.renderer.shaders.ShaderProgram;
import me.kdot.abidance.entity.Camera;
import me.kdot.abidance.utils.AMath;

public class WaterShader extends ShaderProgram {

	private final static String VERTEX_FILE = "src/me/kdot/abidance/world/water/waterVertex.txt";
	private final static String FRAGMENT_FILE = "src/me/kdot/abidance/world/water/waterFragment.txt";

	private int location_modelMatrix;
	private int location_viewMatrix;
	private int location_projectionMatrix;

	public WaterShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		bindAttribute(0, "position");
	}

	@Override
	protected void getAllUniformLocations() {
		location_projectionMatrix = getUniformLocation("projectionMatrix");
		location_viewMatrix = getUniformLocation("viewMatrix");
		location_modelMatrix = getUniformLocation("modelMatrix");
	}

	public void loadProjectionMatrix(Matrix4f projection) {
		loadMatrix(location_projectionMatrix, projection);
	}
	
	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix = AMath.createViewMatrix(camera);
		loadMatrix(location_viewMatrix, viewMatrix);
	}

	public void loadModelMatrix(Matrix4f modelMatrix){
		loadMatrix(location_modelMatrix, modelMatrix);
	}

}
