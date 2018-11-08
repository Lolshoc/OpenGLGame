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

public class MainGameLoop {

    public static void main(String[] args) throws FileNotFoundException {

        DisplayManager.createDisplay();
        Loader loader=new Loader();



        RawModel model= OBJLoader.loadObjModel("tree", loader);
        TexturedModel texturedModel=new TexturedModel(model,new ModelTexture(loader.loadTexture("tree")));
        ModelTexture texture=texturedModel.getTexture();
        texture.setShineDamper(10);
        texture.setRelflectivity(1);
        Entity entity=new Entity(texturedModel,new Vector3f(0,0,-25),0,0,0,1);
        Light light=new Light(new Vector3f(2000,2000,2000),new Vector3f(1,1,1));
        Terrain terrain=new Terrain(0,-1,loader,new ModelTexture(loader.loadTexture("grass")));
        Terrain terrain2=new Terrain(-1,-1,loader,new ModelTexture(loader.loadTexture("grass")));
        Camera camera=new Camera();

        MasterRenderer renderer=new MasterRenderer();
        while(!Display.isCloseRequested()){
            if (entity.getPosition().y > 0.5) {
                entity.setPosition(new Vector3f(entity.getPosition().x,0.5f,entity.getPosition().z));
            } else if(entity.getPosition().y==0.5) {
                entity.setPosition(new Vector3f(entity.getPosition().x, 0f, entity.getPosition().z));
            }else{
                entity.increasePosition(0,(float)Math.random(),0);
            }
            camera.move();
            renderer.processTerrain(terrain);
            renderer.processTerrain(terrain2);
            renderer.processEntity(entity);
            renderer.render(light,camera);
            DisplayManager.updateDisplay();
        }
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();

    }

}