package engineTester;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import models.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.*;
import models.RawModel;
import shaders.StaticShader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainGameLoop {

    public static void main(String[] args) throws FileNotFoundException {

        DisplayManager.createDisplay();
        Loader loader=new Loader();

        TerrainTexture backgroundTexture=new TerrainTexture(loader.loadTexture("grassy2"));
        TerrainTexture rTexture=new TerrainTexture(loader.loadTexture("mud"));
        TerrainTexture gTexture=new TerrainTexture(loader.loadTexture("grassFlowers"));
        TerrainTexture bTexture=new TerrainTexture(loader.loadTexture("path"));

        TerrainTexturePack texturePack=new TerrainTexturePack(backgroundTexture,rTexture,gTexture,bTexture);
        TerrainTexture blendMap=new TerrainTexture(loader.loadTexture("blendMap"));

        RawModel model= OBJLoader.loadObjModel("tree", loader,false);
        TexturedModel tree=new TexturedModel(model,new ModelTexture(loader.loadTexture("tree")));
        TexturedModel grass=new TexturedModel(OBJLoader.loadObjModel("grassModel",loader,false),new ModelTexture(loader.loadTexture("grassTexture")));
        grass.getTexture().setHasTransparency(true);
        grass.getTexture().setUseFakeLighting(true);
        TexturedModel fern=new TexturedModel(OBJLoader.loadObjModel("fern",loader,false),new ModelTexture(loader.loadTexture("fern")));
        fern.getTexture().setHasTransparency(true);
        TexturedModel flower=new TexturedModel(OBJLoader.loadObjModel("grassModel",loader,false),new ModelTexture(loader.loadTexture("flower")));
        flower.getTexture().setUseFakeLighting(true);
        flower.getTexture().setHasTransparency(true);
        TexturedModel lowPolyTree=new TexturedModel(OBJLoader.loadObjModel("lowPolyTree",loader,false),new ModelTexture(loader.loadTexture("lowPolyTree")));
        Light light=new Light(new Vector3f(20000,20000,2000),new Vector3f(1,1,1));
        Terrain terrain=new Terrain(0,-1,loader,texturePack,blendMap);
        Terrain terrain2=new Terrain(-1,-1,loader,texturePack,blendMap);
        List<Entity> entities=new ArrayList<Entity>();
        Random random=new Random();
        for(int i=0;i<300;i++){
            entities.add(new Entity(tree,new Vector3f(random.nextFloat()*800-400,0,random.nextFloat()*-600),0,0,0,random.nextFloat()*1+4));
            entities.add(new Entity(fern,new Vector3f(random.nextFloat()*800-400,0,random.nextFloat()*-600),0,0,0,0.9f));
            entities.add(new Entity(lowPolyTree,new Vector3f(random.nextFloat()*800-400,0,random.nextFloat()*-600),0,0,0,0.6f));
            if(i%2==0) {
                entities.add(new Entity(grass, new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * -600), 0, 0, 0, 1.8f));
                entities.add(new Entity(flower, new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * -600), 0, 0, 0, 2.3f));
            }
        }
        MasterRenderer renderer=new MasterRenderer();
        TexturedModel bunny=new TexturedModel(OBJLoader.loadObjModel("stanfordBunny",loader,true),new ModelTexture(loader.loadTexture("white")));
        Player player=new Player(bunny,new Vector3f(100,0,-50),0,180,0,0.75f);
        Camera camera=new Camera(player);
        while(!Display.isCloseRequested()){
            player.move();
            camera.move();
            renderer.processEntity(player);
            for(Entity entity:entities){
                renderer.processEntity(entity);
            }
            renderer.processTerrain(terrain);
            renderer.processTerrain(terrain2);
            renderer.render(light,camera);
            DisplayManager.updateDisplay();
        }
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();

    }

}