package engineTester;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.*;
import models.RawModel;
import shaders.StaticShader;
import terrains.Terrain;
import textures.ModelTexture;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainGameLoop {

    public static void main(String[] args) throws FileNotFoundException {

        DisplayManager.createDisplay();
        Loader loader=new Loader();



        RawModel model= OBJLoader.loadObjModel("tree", loader);
        TexturedModel tree=new TexturedModel(model,new ModelTexture(loader.loadTexture("tree")));
        TexturedModel grass=new TexturedModel(OBJLoader.loadObjModel("grassModel",loader),new ModelTexture(loader.loadTexture("grassTexture")));
        grass.getTexture().setHasTransparency(true);
        grass.getTexture().setUseFakeLighting(true);
        TexturedModel fern=new TexturedModel(OBJLoader.loadObjModel("fern",loader),new ModelTexture(loader.loadTexture("fern")));
        fern.getTexture().setHasTransparency(true);
        TexturedModel flower=new TexturedModel(OBJLoader.loadObjModel("grassModel",loader),new ModelTexture(loader.loadTexture("flower")));
        flower.getTexture().setUseFakeLighting(true);
        flower.getTexture().setUseFakeLighting(true);
        TexturedModel lowPolyTree=new TexturedModel(OBJLoader.loadObjModel("lowPolyTree",loader),new ModelTexture(loader.loadTexture("lowPolyTree")));
        Light light=new Light(new Vector3f(20000,20000,2000),new Vector3f(1,1,1));
        Terrain terrain=new Terrain(0,-1,loader,new ModelTexture(loader.loadTexture("grass")));
        Terrain terrain2=new Terrain(-1,-1,loader,new ModelTexture(loader.loadTexture("grass")));
        Camera camera=new Camera();
        List<Entity> entities=new ArrayList<Entity>();
        Random random=new Random();
        for(int i=0;i<500;i++){
            entities.add(new Entity(tree,new Vector3f(random.nextFloat()*800-400,0,random.nextFloat()*-600),0,0,0,5));
            entities.add(new Entity(grass,new Vector3f(random.nextFloat()*800-400,0,random.nextFloat()*-600),0,0,0,1));
            entities.add(new Entity(fern,new Vector3f(random.nextFloat()*800-400,0,random.nextFloat()*-600),0,0,0,0.6f));
            entities.add(new Entity(lowPolyTree,new Vector3f(random.nextFloat()*800-400,0,random.nextFloat()*-600),0,0,0,0.6f));
        }
        MasterRenderer renderer=new MasterRenderer();
        while(!Display.isCloseRequested()){
            camera.move();
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