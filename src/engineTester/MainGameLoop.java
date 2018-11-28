package engineTester;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import guis.GuiRenderer;
import guis.GuiTexture;
import models.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
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
        List<Entity> entities=new ArrayList<Entity>();

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
        TexturedModel lamp=new TexturedModel(OBJLoader.loadObjModel("lamp",loader,false),new ModelTexture(loader.loadTexture("lamp")));
        lamp.getTexture().setUseFakeLighting(true);
        Terrain terrain=new Terrain(0,-1,loader,texturePack,blendMap, "heightmap");
        Terrain terrain2=new Terrain(-1,-1,loader,texturePack,blendMap, "heightmap");
        Terrain terrain3=new Terrain(0,0,loader,texturePack,blendMap,"heightmap");
        Terrain terrain4=new Terrain(-1,0,loader,texturePack,blendMap,"heightmap");
        List<Light> lights=new ArrayList<>();
        lights.add(new Light(new Vector3f(2000,1000,-7000),new Vector3f(1f,1f,1f),new Vector3f(0.001f,0.001f,0.001f)));
        lights.add(new Light(new Vector3f(185,terrain.getHeightOfTerrain(185,-293),-293),new Vector3f(2,2,0),new Vector3f(1,0.01f,0.002f)));
        //lights.add(new Light(new Vector3f(-3200,10,-3200),new Vector3f(10,0,0)));
        //lights.add(new Light(new Vector3f(3200,10,-3200),new Vector3f(0,10,0)));
        //lights.add(new Light(new Vector3f(0,10,3200),new Vector3f(0,0,10)));
        float x;
        float z;
        float y;
        /*if(x>terrain.getX()&&z<terrain3.getZ()) {
            y = terrain.getHeightOfTerrain(x, z);
        }else if(x<terrain.getX()&&z<terrain3.getZ()){
            y = terrain2.getHeightOfTerrain(x,z);
        }else if(x<terrain.getX()&&z>terrain3.getZ()){
            y=terrain4.getHeightOfTerrain(x,z);
        }else{
            y=terrain3.getHeightOfTerrain(x,z);
        }
        entities.add(new Entity(lamp,new Vector3f(x,y,z),0,0,0,1));
        x=370;
        z=-300;
        if(x>terrain.getX()&&z<terrain3.getZ()) {
            y = terrain.getHeightOfTerrain(x, z);
        }else if(x<terrain.getX()&&z<terrain3.getZ()){
            y = terrain2.getHeightOfTerrain(x,z);
        }else if(x<terrain.getX()&&z>terrain3.getZ()){
            y=terrain4.getHeightOfTerrain(x,z);
        }else{
            y=terrain3.getHeightOfTerrain(x,z);
        }
        entities.add(new Entity(lamp,new Vector3f(x,y,z),0,0,0,1));
        x=293;
        z=-305;
        if(x>terrain.getX()&&z<terrain3.getZ()) {
            y = terrain.getHeightOfTerrain(x, z);
        }else if(x<terrain.getX()&&z<terrain3.getZ()){
            y = terrain2.getHeightOfTerrain(x,z);
        }else if(x<terrain.getX()&&z>terrain3.getZ()){
            y=terrain4.getHeightOfTerrain(x,z);
        }else{
            y=terrain3.getHeightOfTerrain(x,z);
        }
        entities.add(new Entity(lamp,new Vector3f(x,y,z),0,0,0,1));*/
        Random random=new Random();
        for(int i=0;i<500;i++){
            if(i%2==0){
                x=random.nextFloat()*3200-1600;
                z=random.nextFloat()*3200-1600;
            }else {
                x = random.nextFloat() * 1600 - 800;
                z = random.nextFloat() * 1600 - 800;
            }
            if(x>terrain.getX()&&z<terrain3.getZ()) {
                y = terrain.getHeightOfTerrain(x, z);
            }else if(x<terrain.getX()&&z<terrain3.getZ()){
                y = terrain2.getHeightOfTerrain(x,z);
            }else if(x<terrain.getX()&&z>terrain3.getZ()){
                y=terrain4.getHeightOfTerrain(x,z);
            }else{
                y=terrain3.getHeightOfTerrain(x,z);
            }
            if(i%5==0){
                entities.add(new Entity(lamp,new Vector3f(x,y,z),0,0,0,1));
                lights.add(new Light(new Vector3f(x,y+16,z),new Vector3f(2,2,0),new Vector3f(1f,0.01f,0.002f)));
            }
            if(i%2==0){
                x=random.nextFloat()*3200-1600;
                z=random.nextFloat()*3200-1600;
            }else {
                x = random.nextFloat() * 1600 - 800;
                z = random.nextFloat() * 1600 - 800;
            }
            if(x>terrain.getX()&&z<terrain3.getZ()) {
                y = terrain.getHeightOfTerrain(x, z);
            }else if(x<terrain.getX()&&z<terrain3.getZ()){
                y = terrain2.getHeightOfTerrain(x,z);
            }else if(x<terrain.getX()&&z>terrain3.getZ()){
                y=terrain4.getHeightOfTerrain(x,z);
            }else{
                y=terrain3.getHeightOfTerrain(x,z);
            }
            entities.add(new Entity(tree,new Vector3f(x,y,z),0,0,0,random.nextFloat()*4+12));
            if(i%2==0){
                x=random.nextFloat()*3200-1600;
                z=random.nextFloat()*3200-1600;
            }else {
                x = random.nextFloat() * 1600 - 800;
                z = random.nextFloat() * 1600 - 800;
            }
            if(x>terrain.getX()&&z<terrain3.getZ()) {
                y = terrain.getHeightOfTerrain(x, z);
            }else if(x<terrain.getX()&&z<terrain3.getZ()){
                y = terrain2.getHeightOfTerrain(x,z);
            }else if(x<terrain.getX()&&z>terrain3.getZ()){
                y=terrain4.getHeightOfTerrain(x,z);
            }else{
                y=terrain3.getHeightOfTerrain(x,z);
            }
            entities.add(new Entity(fern,random.nextInt(4),new Vector3f(x,y,z),0,random.nextFloat()*360,0,random.nextFloat()*0.5f+2));
            if(i%2==0){
                x=random.nextFloat()*3200-1600;
                z=random.nextFloat()*3200-1600;
            }else {
                x = random.nextFloat() * 1600 - 800;
                z = random.nextFloat() * 1600 - 800;
            }
            if(x>terrain.getX()&&z<terrain3.getZ()) {
                y = terrain.getHeightOfTerrain(x, z);
            }else if(x<terrain.getX()&&z<terrain3.getZ()){
                y = terrain2.getHeightOfTerrain(x,z);
            }else if(x<terrain.getX()&&z>terrain3.getZ()){
                y=terrain4.getHeightOfTerrain(x,z);
            }else{
                y=terrain3.getHeightOfTerrain(x,z);
            }
            entities.add(new Entity(lowPolyTree,new Vector3f(x,y,z),0,random.nextFloat()*360,0,random.nextFloat()*0.5f+1f));
        }
        MasterRenderer renderer=new MasterRenderer();
        TexturedModel bunny=new TexturedModel(OBJLoader.loadObjModel("stanfordBunny",loader,true),new ModelTexture(loader.loadTexture("white")));
        Player player=new Player(bunny,new Vector3f(100,0,-50),0,180,0,0.75f);
        Camera camera=new Camera(player);

        List<GuiTexture> guis=new ArrayList<GuiTexture>();
        //GuiTexture gui=new GuiTexture(loader.loadTexture("texture"),new Vector2f(0.5f,0.5f),new Vector2f(0.25f,0.25f));
        //guis.add(gui);
        GuiRenderer guiRenderer=new GuiRenderer(loader);
        float r=0.03f;
        float g=0.03f;
        float b=0.03f;
        float oldr=0;
        float oldg=0;
        float oldb=0;
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
            if(player.getRotY()>180){
                player.setRotY(player.getRotY()-360);
            }else if(player.getRotY()<-180){
                player.setRotY(player.getRotY()+360);
            }/*
            System.out.println(player.getRotY());
            if(player.getRotY()<=45&&player.getRotY()>=-45){
                lights.get(1).setPosition(new Vector3f(player.getPosition().x-100,10,player.getPosition().z+100));
                lights.get(2).setPosition(new Vector3f(player.getPosition().x,10,player.getPosition().z-100));
                lights.get(3).setPosition(new Vector3f(player.getPosition().x+100,player.getPosition().y+10,player.getPosition().z+100));
            }else if(player.getRotY()>=45&&player.getRotY()<=135){
                lights.get(1).setPosition(new Vector3f(player.getPosition().x-100,10,player.getPosition().z+100));
                lights.get(2).setPosition(new Vector3f(player.getPosition().x+100,player.getPosition().y+10,player.getPosition().z));
                lights.get(3).setPosition(new Vector3f(player.getPosition().x-100,player.getPosition().y+10,player.getPosition().z-100));
            }else if(player.getRotY()>=-135&&player.getRotY()<=-45){
                lights.get(1).setPosition(new Vector3f(player.getPosition().x,10,player.getPosition().z+100));
                lights.get(2).setPosition(new Vector3f(player.getPosition().x+100,player.getPosition().y+10,player.getPosition().z-100));
                lights.get(3).setPosition(new Vector3f(player.getPosition().x-100,player.getPosition().y+10,player.getPosition().z-100));
            }else{
                lights.get(1).setPosition(new Vector3f(player.getPosition().x-100,player.getPosition().y+10,player.getPosition().z-100));
                lights.get(2).setPosition(new Vector3f(player.getPosition().x,10,player.getPosition().z+100));
                lights.get(3).setPosition(new Vector3f(player.getPosition().x+100,player.getPosition().y+10,player.getPosition().z-100));

            }*/
            lights.get(0).setPosition(new Vector3f(player.getPosition().x,player.getPosition().y+100,player.getPosition().z));
            lights.get(0).setColour(new Vector3f(oldr+r,oldg+g,oldb+b));
            if(lights.get(0).getColour().x>5){
                r=-0.04f;
                g=-0.04f;
                b=-0.04f;
            }else if(lights.get(0).getColour().x<0){
                r=0.04f;
                g=0.04f;
                b=0.04f;
            }
            oldr = lights.get(0).getColour().x;
            oldg = lights.get(0).getColour().y;
            oldb = lights.get(0).getColour().z;
            camera.move();
            renderer.processEntity(player);
            for(Entity entity:entities){
                renderer.processEntity(entity);
            }
            renderer.processTerrain(terrain);
            renderer.processTerrain(terrain2);
            renderer.processTerrain(terrain3);
            renderer.processTerrain(terrain4);
            renderer.render(lights.get(0).findNearestLights(player.getPosition(),lights),camera);
            guiRenderer.render(guis);
            DisplayManager.updateDisplay();
        }
        guiRenderer.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();

    }

}