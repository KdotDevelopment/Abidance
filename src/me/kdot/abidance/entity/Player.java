package me.kdot.abidance.entity;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.LWJGLException;
import org.lwjgl.glfw.*;

import me.kdot.abidance.client.GameSettings;
import me.kdot.abidance.client.renderer.DisplayManager;
import me.kdot.abidance.models.TexturedModel;
import me.kdot.abidance.world.Terrain;

public class Player extends Entity {
	
	private static final float RUN_SPEED = 40;
	private static final float GRAVITY = -50;
	private static final float JUMP_POWER = 20;
	
	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;
	private float upwardsSpeed = 0;
	
	private boolean isInAir = false;

	public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
		
	}
	
	public void move(Terrain terrain) {
		checkInputs();
		float distance = RUN_SPEED * DisplayManager.getFrameTimeSeconds();
		if(Keyboard.isKeyDown(GameSettings.forwardKeyBind)) {
			this.getPosition().x -= (distance * Math.sin(Math.toRadians(this.rotY)));
			this.getPosition().z -= (distance * Math.cos(Math.toRadians(this.rotY)));
			
		}
		if(Keyboard.isKeyDown(GameSettings.backwardKeyBind)) {
			this.getPosition().x += (distance * Math.sin(Math.toRadians(this.rotY)));
			this.getPosition().z += (distance * Math.cos(Math.toRadians(this.rotY)));
			
		}
		if(Keyboard.isKeyDown(GameSettings.leftKeyBind)) {
			this.getPosition().x += (distance * Math.sin(Math.toRadians(this.rotY - 90)));
			this.getPosition().z += (distance * Math.cos(Math.toRadians(this.rotY - 90)));
			
		}
		if(Keyboard.isKeyDown(GameSettings.rightKeyBind)) {
			this.getPosition().x += (distance * Math.sin(Math.toRadians(this.rotY + 90)));
			this.getPosition().z += (distance * Math.cos(Math.toRadians(this.rotY + 90)));
			
		}
		//super.increaseRotation(0, currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		
		/*float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
		float dx = -(float) (distance * Math.sin(Math.toRadians(super.getRotY())));
		float dz = -(float) (distance * Math.cos(Math.toRadians(super.getRotY())));
		super.increasePosition(dx, 0, dz);*/
		
		upwardsSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
		super.increasePosition(0,upwardsSpeed * DisplayManager.getFrameTimeSeconds(),0);
		float terrainHeight = terrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z);
		
		if(super.getPosition().y < terrainHeight) {
			upwardsSpeed = 0;
			isInAir = false;
			super.getPosition().y = terrainHeight;
		}
	}
	
	private void jump() {
		if(!isInAir) {
			this.upwardsSpeed = JUMP_POWER;
			isInAir = true;
		}
	}
	
	private void checkInputs() {
		if(Keyboard.isKeyDown(Keyboard.KEY_1)) {
			GL11.glLineWidth(1f);
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);//GL11.GL_FRONT_AND_BACK
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_2)) {
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		}
		
		if(Keyboard.isKeyDown(GameSettings.jumpKeyBind)) {
			jump();
		}
		float mouseDX = -Mouse.getDX();
		float mouseDY = Mouse.getDY();
		this.rotY += mouseDX * GameSettings.mouseSensitivity;
		this.rotX += mouseDY * GameSettings.mouseSensitivity;
		this.rotX = Math.max(-90, Math.min(90, this.rotX));
		
		if(Mouse.isInsideWindow() && Mouse.isButtonDown(0)) {
            Mouse.setGrabbed(true);
        }
        if(Keyboard.isKeyDown(GameSettings.escapeKeyBind)) {
            Mouse.setGrabbed(false);
        }
        
        if(Mouse.isGrabbed()) {
        	Mouse.setCursorPosition(GameSettings.FRAME_WIDTH/2, GameSettings.FRAME_HEIGHT/2);
        }
        
        if(Keyboard.isKeyDown(GameSettings.fullscreenOnKeyBind)) {
			try {
				Display.setFullscreen(true);
			} catch (LWJGLException e) {
				System.err.println("Could not enable Fullscreen");
				e.printStackTrace();
			}
		}
		if(Keyboard.isKeyDown(GameSettings.fullscreenOffKeyBind)) {
			try {
				Display.setFullscreen(false);
			} catch (LWJGLException e) {
				System.err.println("Could not disable Fullscreen");
				e.printStackTrace();
			}
		}
	}

}
