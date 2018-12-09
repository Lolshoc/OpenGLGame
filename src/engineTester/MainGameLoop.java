package engineTester;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRendering.TextMaster;
import guis.GuiRenderer;
import guis.GuiTexture;
import models.TexturedModel;
import normalMappingObjConverter.NormalMappedObjLoader;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import renderEngine.*;
import models.RawModel;
import shaders.StaticShader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.MousePicker;
import water.WaterFrameBuffers;
import water.WaterRenderer;
import water.WaterShader;
import water.WaterTile;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainGameLoop {

    public static void main(String[] args) throws FileNotFoundException {

        DisplayManager.createDisplay();
        Loader loader=new Loader();
        TextMaster.init(loader);

        FontType font = new FontType(loader.loadTexture("harrington"),new File("res/harrington.fnt"));
        GUIText text = new GUIText("Hello world!",1,font,new Vector2f(0,0),1f,false);
        text.setColour(1,1,1);

        List<Entity> entities=new ArrayList<Entity>();
        List<Entity> normalMappedEntities=new ArrayList<>();

        TerrainTexture backgroundTexture=new TerrainTexture(loader.loadTexture("grassy2"));
        TerrainTexture rTexture=new TerrainTexture(loader.loadTexture("mud"));
        TerrainTexture gTexture=new TerrainTexture(loader.loadTexture("grassFlowers"));
        TerrainTexture bTexture=new TerrainTexture(loader.loadTexture("path"));
        TerrainTexturePack texturePack=new TerrainTexturePack(backgroundTexture,rTexture,gTexture,bTexture);
        TerrainTexture blendMap=new TerrainTexture(loader.loadTexture("blendMap"));

        RawModel model= OBJLoader.loadObjModel("pine", loader,false);
        TexturedModel tree=new TexturedModel(model,new ModelTexture(loader.loadTexture("pine")));
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
        List<Terrain> terrains=new ArrayList<>();
        terrains.add(terrain);
        terrains.add(terrain2);
        terrains.add(terrain3);
        terrains.add(terrain4);

        List<Light> lights=new ArrayList<>();
        lights.add(new Light(new Vector3f(2000,1000,-7000),new Vector3f(1f,1f,1f),new Vector3f(0.001f,0.001f,0.001f)));
        //lights.add(new Light(new Vector3f(185,1000,-293),new Vector3f(1,1,1)));

        TexturedModel boulder = new TexturedModel(NormalMappedObjLoader.loadOBJ("boulder",loader),new ModelTexture((loader.loadTexture("boulder"))));
        boulder.getTexture().setNormalMapID(loader.loadTexture("boulderNormal"));

        float x;
        float z;
        float y;
        float scale;
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
            if(i%5==0) {
                scale = random.nextFloat() * 4 + 1f;
            }else{
                scale = random.nextFloat() * 2 + 1f;
            }
            normalMappedEntities.add(new Entity(boulder,new Vector3f(x,y+(random.nextFloat() * 4 - 2) * scale,z),random.nextFloat() * 360,random.nextFloat()*360,random.nextFloat()*360,scale));
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
            entities.add(new Entity(tree,new Vector3f(x,y,z),0,0,0,random.nextFloat()*4+1));
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
        MasterRenderer renderer=new MasterRenderer(loader);
        TexturedModel bunny=new TexturedModel(OBJLoader.loadObjModel("stanfordBunny",loader,true),new ModelTexture(loader.loadTexture("white")));
        Player player=new Player(bunny,new Vector3f(100,0,-50),0,180,0,0.75f);
        Camera camera=new Camera(player);

        List<GuiTexture> guis=new ArrayList<GuiTexture>();
        GuiRenderer guiRenderer=new GuiRenderer(loader);
        MousePicker mousePicker=new MousePicker(camera,renderer.getProjectionMatrix());
        WaterFrameBuffers fbos=new WaterFrameBuffers();
        WaterShader waterShader=new WaterShader();
        WaterRenderer waterRenderer=new WaterRenderer(loader,waterShader,renderer.getProjectionMatrix(),fbos);
        List<WaterTile> waters=new ArrayList<>();
        waters.add(new WaterTile(0,0,0));
        /*GuiTexture refraction=new GuiTexture(fbos.getRefractionTexture(),new Vector2f(0.5f,0.5f),new Vector2f(0.25f,0.25f));
        GuiTexture reflection=new GuiTexture(fbos.getReflectionTexture(),new Vector2f(-0.5f,0.5f),new Vector2f(0.25f,0.25f));
        guis.add(refraction);
        guis.add(reflection);*/
        float rate=0.03f;
        float old=lights.get(0).getColour().x;
        while(!Display.isCloseRequested()){
            player.move(player.calculateTerrain(terrains));
            player.limitRotation();
            if(lights.get(0).getColour().x>5){
                rate=-5/4f*DisplayManager.getDelta();
            }else if(lights.get(0).getColour().x<0){
                rate=5/4f*DisplayManager.getDelta();
            }
            Light.updateLight(lights.get(0),player,old,rate);
            old = lights.get(0).getColour().x;
            camera.move();
            mousePicker.update();
            GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
            //reflection
            fbos.bindReflectionFrameBuffer();
            float distance=2*camera.getPosition().y-waters.get(0).getHeight();
            camera.getPosition().y-=distance;
            camera.invertPitch();
            renderer.renderScene(normalMappedEntities,entities,terrains,lights,camera,player,new Vector4f(0,1,0,-waters.get(0).getHeight()+1f));
            camera.getPosition().y+=distance;
            camera.invertPitch();
            //refraction
            fbos.bindRefractionFrameBuffer();
            renderer.renderScene(normalMappedEntities,entities,terrains,lights,camera,player,new Vector4f(0,-1,0,waters.get(0).getHeight()+1f));
            //regular
            GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
            fbos.unbindCurrentFrameBuffer();
            renderer.renderScene(normalMappedEntities,entities,terrains,lights,camera,player,new Vector4f(0,-1,0,100000));
            waterRenderer.render(waters,camera, lights.get(0));
            guiRenderer.render(guis);
            TextMaster.render();
            DisplayManager.updateDisplay();
        }
        TextMaster.cleanUp();
        fbos.cleanUp();
        waterShader.cleanUp();
        guiRenderer.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();

    }

}