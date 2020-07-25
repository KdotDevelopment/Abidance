package me.kdot.abidance.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import me.kdot.abidance.client.renderer.DisplayManager;
import me.kdot.abidance.client.renderer.Loader;
import me.kdot.abidance.client.renderer.MasterRenderer;
import me.kdot.abidance.client.renderer.gui.GuiRenderer;
import me.kdot.abidance.client.renderer.gui.GuiTexture;
import me.kdot.abidance.client.renderer.textures.ModelTexture;
import me.kdot.abidance.client.renderer.textures.TerrainTexture;
import me.kdot.abidance.client.renderer.textures.TerrainTexturePack;
import me.kdot.abidance.entity.Camera;
import me.kdot.abidance.entity.Entity;
import me.kdot.abidance.entity.Light;
import me.kdot.abidance.entity.Player;
import me.kdot.abidance.models.RawModel;
import me.kdot.abidance.models.TexturedModel;
import me.kdot.abidance.models.objloader.ModelData;
import me.kdot.abidance.models.objloader.OBJFileLoader;
import me.kdot.abidance.utils.MousePicker;
import me.kdot.abidance.world.Terrain;
import me.kdot.abidance.world.water.WaterRenderer;
import me.kdot.abidance.world.water.WaterShader;
import me.kdot.abidance.world.water.WaterTile;

public class Abidance {
	
	public static void main(String[] args) {
		DisplayManager.createDisplay();
		
		Loader loader = new Loader();	
		
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grass"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		
		ModelData treeData = OBJFileLoader.loadOBJ("Tree 02/Tree");
		ModelData grassData = OBJFileLoader.loadOBJ("plant");
		ModelData leavesData = OBJFileLoader.loadOBJ("Tree 02/Leaves");
		
		RawModel treeModel = loader.loadToVAO(treeData);
		TexturedModel staticTreeModel = new TexturedModel(treeModel,new ModelTexture(loader.loadTexture("Tree 02/bark_0004")));
		ModelTexture treeTexture = staticTreeModel.getTexture();
		
		RawModel leavesModel = loader.loadToVAO(leavesData);
		TexturedModel staticLeavesModel = new TexturedModel(leavesModel,new ModelTexture(loader.loadTexture("Tree 02/DB2X2_L01")));
		ModelTexture leavesTexture = staticLeavesModel.getTexture();
		leavesTexture.setTransparent(true);
		
		RawModel grassModel = loader.loadToVAO(grassData);
		TexturedModel staticGrassModel = new TexturedModel(grassModel,new ModelTexture(loader.loadTexture("grassTexture")));
		ModelTexture grassTexture = staticGrassModel.getTexture();
		
		treeTexture.setShineDamper(10);
		treeTexture.setReflectivity(0);
		
		staticGrassModel.getTexture().setTransparent(true);
		staticGrassModel.getTexture().setUseFakeLighting(true);
		
		Entity entity = new Entity(staticTreeModel, new Vector3f(0,0,-50),0,0,0,10);
		
		List<Light> lights = new ArrayList<Light>();
		Light sun = new Light(new Vector3f(20000,20000,20000), new Vector3f(1.5f,1.5f,1.5f));
		lights.add(sun);
		//lights.add(new Light(new Vector3f(-200,10,-200), new Vector3f(3,0,3), new Vector3f(1,0.01f,0.002f)));
		
		Terrain terrain = new Terrain(-1,-1,loader,texturePack, blendMap, "heightmap2");
		
		Random random = new Random();
		List<Entity> entities = new ArrayList<Entity>();
		
		List<GuiTexture> guis = new ArrayList<GuiTexture>();
		GuiTexture crosshair = new GuiTexture(loader.loadTexture("crosshair"), new Vector2f(1/(GameSettings.FRAME_WIDTH/2),1/(GameSettings.FRAME_HEIGHT/2)), new Vector2f(0.01f,0.018f));
		guis.add(crosshair);
		
		GuiRenderer guiRenderer = new GuiRenderer(loader);
		
		for(int i = 0; i < 800; i++) {
			float x = random.nextFloat() * 1600 - 1600;
			float z = random.nextFloat() * -1600;
			float e = random.nextFloat() * 10; //lol good luck future me
			float y = terrain.getHeightOfTerrain(x, z);
			entities.add(new Entity(staticTreeModel, new Vector3f(x,y,z),0,0,0,(e)+2));
			entities.add(new Entity(staticLeavesModel, new Vector3f(x,y,z),0,0,0,(e)+2));
		}
		for(int i = 0; i < 80000; i++) {
			float x = random.nextFloat() * 1600 - 1600;
			float z = random.nextFloat() * -1600;
			float y = terrain.getHeightOfTerrain(x, z);
			//entities.add(new Entity(staticGrassModel, new Vector3f(x,y,z),0,0,0,2.5f));
		}
		
		Player player = new Player(staticTreeModel, new Vector3f(-200,0,-200),0,0,0,0);
		
		Camera camera = new Camera(player);
		camera.setPosition(new Vector3f(-200,6,-200));
		
		MasterRenderer renderer = new MasterRenderer(loader);
		
		MousePicker picker = new MousePicker(camera,renderer.getProjectionMatrix());
		
		List<Terrain> terrains = new ArrayList<Terrain>();
		terrains.add(terrain);
		
		WaterShader waterShader = new WaterShader();
		WaterRenderer waterRenderer = new WaterRenderer(loader,waterShader,renderer.getProjectionMatrix());
		List<WaterTile> waters = new ArrayList<WaterTile>();
		waters.add(new WaterTile(75,-75,0));
		
		while(!Display.isCloseRequested()) {
			player.move(terrain);
			camera.move();
			picker.update();
			
			if(!GameSettings.isThirdPerson) {
				camera.setPosition(new Vector3f(player.getPosition().x,player.getPosition().y+7,player.getPosition().z));
				camera.setYaw(-player.getRotY());
				camera.setPitch(-player.getRotX());
			}
			renderer.processEntity(player);
			
			renderer.renderScene(entities, terrains, lights, camera);
			waterRenderer.render(waters, camera);
			guiRenderer.render(guis);
			DisplayManager.updateDisplay();
		}
		
		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
	
}
