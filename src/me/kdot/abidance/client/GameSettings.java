package me.kdot.abidance.client;

import org.lwjgl.input.Keyboard;

public class GameSettings {
	
	public static int FOV = 300;
	public static float NEAR_PLANE = 0.1f;
	public static float FAR_PLANE = 1000;
	
	public static float RUN_SPEED = 20;
	public static float TURN_SPEED = 160;
	public static float GRAVITY = -50;
	public static float JUMP_POWER = 20;
	public static float mouseSensitivity = 0.1f;
	
	public static int FPS_LIMIT = 165;
	public static int FRAME_WIDTH = 1280;
	public static int FRAME_HEIGHT = 720;
	
	public static boolean isThirdPerson = false;
	
	//Controls
	public static int forwardKeyBind = Keyboard.KEY_W;
	public static int backwardKeyBind = Keyboard.KEY_S;
	public static int leftKeyBind = Keyboard.KEY_A;
	public static int rightKeyBind = Keyboard.KEY_D;
	public static int jumpKeyBind = Keyboard.KEY_SPACE;
	public static int changePerspectiveKeyBind = Keyboard.KEY_C;
	public static int escapeKeyBind = Keyboard.KEY_ESCAPE;
	public static int fullscreenOnKeyBind = Keyboard.KEY_F11;
	public static int fullscreenOffKeyBind = Keyboard.KEY_F12;
	
	public static int gameTime = 0;
	
}
