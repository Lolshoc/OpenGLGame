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
        fern.getTexture().setNumberOfRows(2);
        TexturedModel flower=new TexturedModel(OBJLoader.loadObjModel("grassModel",loader,false),new ModelTexture(loader.loadTexture("flower")));
        flower.getTexture().setUseFakeLighting(true);
        flower.getTexture().setHasTransparency(true);
        TexturedModel lowPolyTree=new TexturedModel(OBJLoader.loadObjModel("lowPolyTree",loader,false),new ModelTexture(loader.loadTexture("lowPolyTree")));
        Light light=new Light(new Vector3f(20000,20000,2000),new Vector3f(1,1,1));
        Terrain terrain=new Terrain(0,-1,loader,texturePack,blendMap, "heightmap");
        Terrain terrain2=new Terrain(-1,-1,loader,texturePack,blendMap, "heightmap");
        Terrain terrain3=new Terrain(0,0,loader,texturePack,blendMap,"heightmap");
        Terrain terrain4=new Terrain(-1,0,loader,texturePack,blendMap,"heightmap");
        List<Entity> entities=new ArrayList<Entity>();
        Random random=new Random();
        float y;
        for(int i=0;i<500;i++){
            float x=random.nextFloat()*1600-800;
            float z=random.nextFloat()*1600-800;
            if(x>terrain.getX()&&z<terrain3.getZ()) {
                y = terrain.getHeightOfTerrain(x, z);
            }else if(x<terrain.getX()&&z<terrain3.getZ()){
                y = terrain2.getHeightOfTerrain(x,z);
            }else if(x<terrain.getX()&&z>terrain3.getZ()){
                y=terrain4.getHeightOfTerrain(x,z);
            }else{
                y=terrain3.getHeightOfTerrain(x,z);
            }
            entities.add(new Entity(tree,new Vector3f(x,y,z),0,0,0,random.nextFloat()*1+4));
            x=random.nextFloat()*1600-800;
            z=random.nextFloat()*1600-800;
            if(x>terrain.getX()&&z<terrain3.getZ()) {
                y = terrain.getHeightOfTerrain(x, z);
            }else if(x<terrain.getX()&&z<terrain3.getZ()){
                y = terrain2.getHeightOfTerrain(x,z);
            }else if(x<terrain.getX()&&z>terrain3.getZ()){
                y=terrain4.getHeightOfTerrain(x,z);
            }else{
                y=terrain3.getHeightOfTerrain(x,z);
            }
            entities.add(new Entity(fern,random.nextInt(4),new Vector3f(x,y,z),0,0,0,0.9f));
            x=random.nextFloat()*1600-800;
            z=random.nextFloat()*1600-800;
            if(x>terrain.getX()&&z<terrain3.getZ()) {
                y = terrain.getHeightOfTerrain(x, z);
            }else if(x<terrain.getX()&&z<terrain3.getZ()){
                y = terrain2.getHeightOfTerrain(x,z);
            }else if(x<terrain.getX()&&z>terrain3.getZ()){
                y=terrain4.getHeightOfTerrain(x,z);
            }else{
                y=terrain3.getHeightOfTerrain(x,z);
            }
            entities.add(new Entity(lowPolyTree,new Vector3f(x,y,z),0,0,0,0.6f));
        }
        MasterRenderer renderer=new MasterRenderer();
        TexturedModel bunny=new TexturedModel(OBJLoader.loadObjModel("stanfordBunny",loader,true),new ModelTexture(loader.loadTexture("white")));
        Player player=new Player(bunny,new Vector3f(100,0,-50),0,180,0,0.75f);
        Camera camera=new Camera(player);
        while(!Display.isCloseRequested()){
            if(player.getPosition().x>terrain.getX()&&player.getPosition().z<terrain3.getZ()) {
                player.move(terrain);
            }else if(player.getPosition().x<terrain.getX()&&player.getPosition().z<terrain3.getZ()){
                player.move(terrain2);
            }else if(player.getPosition().x<terrain.getX()&&player.getPosition().z>terrain3.getZ()){
                player.move(terrain4);
            }else {
                player.move(terrain3);
            }
            camera.move();
            renderer.processEntity(player);
            for(Entity entity:entities){
                renderer.processEntity(entity);
            }
            renderer.processTerrain(terrain);
            renderer.processTerrain(terrain2);
            renderer.processTerrain(terrain3);
            renderer.processTerrain(terrain4);
            renderer.render(light,camera);
            DisplayManager.updateDisplay();
        }
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();

    }

}