package engineTester;

import audio.AudioMaster;
import audio.Source;
import entities.*;
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRendering.TextMaster;
import guis.GuiRenderer;
import guis.GuiTexture;
import models.TexturedModel;
import normalMappingObjConverter.NormalMappedObjLoader;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import particles.Particle;
import particles.ParticleMaster;
import particles.ParticleSystem;
import particles.ParticleTexture;
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

    private Loader loader;
    private MasterRenderer renderer;
    private Camera camera;
    private MousePicker mousePicker;
    private GuiRenderer guiRenderer;
    private WaterRenderer waterRenderer;
    private List<Entity> normalMappedEntities = new ArrayList<>(), entities = new ArrayList<>();
    private List<Light> lights = new ArrayList<>();
    private List<JButton> buttons = new ArrayList<>();
    private List<Terrain> terrains = new ArrayList<>();
    private List<Terrain> renderedTerrains = new ArrayList<>();
    private List<Entity> renderedEntities = new ArrayList<>(), renderedNormalMappedEntities = new ArrayList<>();
    private List<GuiTexture> guis=new ArrayList<>();
    private List<GUIText> texts = new ArrayList<>();
    private TerrainTexturePack texturePack;
    private TerrainTexture blendMap;
    private Terrain current;
    private TexturedModel tree;
    private TexturedModel grass;
    private TexturedModel fern;
    private TexturedModel flower;
    private TexturedModel lowPolyTree;
    private TexturedModel boulder;
    private TexturedModel lamp;
    private Player player;
    private WaterFrameBuffers fbos;
    private WaterShader waterShader;
    private List<WaterTile> waters=new ArrayList<>();
    private Source starter;
    private Source source;
    private Source source2;
    private Source water;
    private int bard;
    private int forest;
    private int splash;
    private int flow;
    private int waves;
    private boolean sounds = true;
    private boolean particles = true;
    private ParticleSystem snow;
    private ParticleTexture particleTexture;

    public static void main(String[] args) throws FileNotFoundException {

        DisplayManager.createDisplay();
        Loader loader=new Loader();
        TerrainTexture backgroundTexture=new TerrainTexture(loader.loadTexture("grassy2"));
        TerrainTexture rTexture=new TerrainTexture(loader.loadTexture("mud"));
        TerrainTexture gTexture=new TerrainTexture(loader.loadTexture("grassFlowers"));
        TerrainTexture bTexture=new TerrainTexture(loader.loadTexture("path"));
        TerrainTexturePack texturePack=new TerrainTexturePack(backgroundTexture,rTexture,gTexture,bTexture);
        TerrainTexture blendMap=new TerrainTexture(loader.loadTexture("blendMap"));
        TexturedModel tree=new TexturedModel(OBJLoader.loadObjModel("pine", loader,false),new ModelTexture(loader.loadTexture("pine")));
        TexturedModel grass=new TexturedModel(OBJLoader.loadObjModel("grassModel",loader,false),new ModelTexture(loader.loadTexture("grassTexture")));
        TexturedModel fern=new TexturedModel(OBJLoader.loadObjModel("fern",loader,false),new ModelTexture(loader.loadTexture("fern")));
        TexturedModel flower=new TexturedModel(OBJLoader.loadObjModel("grassModel",loader,false),new ModelTexture(loader.loadTexture("flower")));
        TexturedModel lowPolyTree=new TexturedModel(OBJLoader.loadObjModel("lowPolyTree",loader,false),new ModelTexture(loader.loadTexture("lowPolyTree")));
        TexturedModel boulder = new TexturedModel(NormalMappedObjLoader.loadOBJ("boulder",loader),new ModelTexture((loader.loadTexture("boulder"))));
        TexturedModel lamp=new TexturedModel(OBJLoader.loadObjModel("lamp",loader,false),new ModelTexture(loader.loadTexture("lamp")));
        Player player=new Player(new TexturedModel(OBJLoader.loadObjModel("stanfordBunny",loader,true),new ModelTexture(loader.loadTexture("white"))),new Vector3f(160000,0,160000),0,0,0,0.75f);
        MasterRenderer renderer=new MasterRenderer(loader);
        Camera camera=new Camera(player);
        MousePicker mousePicker=new MousePicker(camera,renderer.getProjectionMatrix());
        GuiRenderer guiRenderer=new GuiRenderer(loader);
        WaterFrameBuffers fbos=new WaterFrameBuffers();
        WaterShader waterShader=new WaterShader();
        WaterRenderer waterRenderer=new WaterRenderer(loader,waterShader,renderer.getProjectionMatrix(),fbos);
        new MainGameLoop(loader,renderer,camera,mousePicker,guiRenderer,waterRenderer,texturePack,blendMap,tree,grass,fern,flower,lowPolyTree,boulder,lamp,player,fbos,waterShader);

    }

    private MainGameLoop(Loader loader, MasterRenderer renderer, Camera camera, MousePicker mousePicker, GuiRenderer guiRenderer, WaterRenderer waterRenderer, TerrainTexturePack texturePack, TerrainTexture blendMap, TexturedModel tree, TexturedModel grass, TexturedModel fern, TexturedModel flower, TexturedModel lowPolyTree, TexturedModel boulder, TexturedModel lamp, Player player, WaterFrameBuffers fbos, WaterShader waterShader) {
        this.loader = loader;
        this.renderer = renderer;
        this.camera = camera;
        this.mousePicker = mousePicker;
        this.guiRenderer = guiRenderer;
        this.waterRenderer = waterRenderer;
        this.texturePack = texturePack;
        this.blendMap = blendMap;
        this.tree = tree;
        this.grass = grass;
        this.fern = fern;
        this.flower = flower;
        this.lowPolyTree = lowPolyTree;
        this.boulder = boulder;
        this.lamp = lamp;
        this.player = player;
        this.fbos = fbos;
        this.waterShader = waterShader;
        AudioMaster.init();
        AudioMaster.setListenerData(0,0,0);
        this.source = new Source();
        this.source2 = new Source();
        this.water = new Source();
        this.starter = new Source();
        this.bard = AudioMaster.loadSound("audio/bard.wav");
        this.forest = AudioMaster.loadSound("audio/forest.wav");
        this.splash = AudioMaster.loadSound("audio/splashShort.wav");
        this.flow = AudioMaster.loadSound("audio/flow.wav");
        this.waves = AudioMaster.loadSound("audio/waves.wav");
        ParticleMaster.init(loader,renderer.getProjectionMatrix());
        this.particleTexture = new ParticleTexture(loader.loadTexture("particleAtlas"),4,false);
        this.snow = new ParticleSystem(particleTexture,40,10,0.1f,1,1.6f);
        snow.setLifeError(0.1f);
        snow.setScaleError(0.5f);
        snow.setSpeedError(0.25f);
        snow.randomizeRotation();
        menu();
    }

    private void menu(){
        //main menu code goes here
        boolean close = false;
        boolean play = false;
        boolean setting = false;
        boolean quit = false;
        boolean down = true;
        TextMaster.init(loader);
        GUIText text = loadText("Play", 3,new Vector2f(0.25f,0.575f),0.5f,true,new Vector3f(1,1,1));
        GuiTexture texture = new GuiTexture(loader.loadTexture("texture"),new Vector2f(0,-0.25f),new Vector2f(0.25f,0.1f));
        JButton playButton = new JButton(text,texture,JButton.calculateTopLeft(texture.getPosition(),texture.getScale()),JButton.calculateBottomRight(texture.getPosition(),texture.getScale()),buttons);
        JButton settings = new JButton(loadText("Settings",3,new Vector2f(0.25f,0.7f),0.5f,true,new Vector3f(1,1,1)),new GuiTexture(loader.loadTexture("texture"), new Vector2f(0f,-0.5f),new Vector2f(0.25f,0.1f)),JButton.calculateTopLeft(new Vector2f(0f,-0.5f),new Vector2f(0.25f,0.1f)),JButton.calculateBottomRight(new Vector2f(0f,-0.5f),new Vector2f(0.25f,0.1f)),buttons);
        JButton quitButton = new JButton(loadText("Quit",3,new Vector2f(0.25f,0.825f),0.5f,true,new Vector3f(1,1,1)),new GuiTexture(loader.loadTexture("texture"),new Vector2f(0f,-0.75f),new Vector2f(0.25f,0.1f)),JButton.calculateTopLeft(new Vector2f(0f,-0.75f),new Vector2f(0.25f,0.1f)),JButton.calculateBottomRight(new Vector2f(0f,-0.75f),new Vector2f(0.25f,0.1f)),buttons);
        guis.add(new GuiTexture(loader.loadTexture("nightBack"),new Vector2f(0,0),new Vector2f(1,1)));
        guis.add(playButton.getTexture());
        guis.add(settings.getTexture());
        guis.add(quitButton.getTexture());
        loadText("The Night Is Dark",6,new Vector2f(0.1f,0.1f),0.8f,true,new Vector3f(1,1,1));
        while(!play){
            mousePicker.update();
            if(Mouse.isButtonDown(0)) {
                if(!down) {
                    play = MousePicker.checkIfOnButton(playButton);
                    setting = MousePicker.checkIfOnButton(settings);
                    quit = MousePicker.checkIfOnButton(quitButton);
                    if (sounds) {
                        if (play || quit) {
                            starter.play(bard);
                        }
                    }
                }
                down = true;
            }else{
                down = false;
            }
            for(JButton button:buttons) {
                if(MousePicker.checkIfOnButton(button)){
                    guiRenderer.render(guis, 0.1f, guis.indexOf(button.getTexture()));
                    break;
                } else {
                    guiRenderer.render(guis);
                }
            }
            TextMaster.render();
            DisplayManager.updateDisplay();
            if(Display.isCloseRequested() || quit) {
                close = true;
                break;
            }else if(setting){
                break;
            }
        }
        source.setLooping(false);
        if(!close) {
            playButton.deleteButton(buttons);
            settings.deleteButton(buttons);
            quitButton.deleteButton(buttons);
            guis.clear();
            clearTexts();
            texts.clear();
            if(setting){
                settings(true );
            }else {
                initGame();
            }
        }else{
            exit();
        }
    }

    private void initGame(){
        initEntitiies();
        loadText("Hello world!",1,new Vector2f(0,0),1,false,new Vector3f(1,1,1));
        source.play(forest);
        gameLogic();
        exit();
    }

    private void gameLogic(){
        if(sounds) {
            source.setLooping(true);
            source.resume();
        }else{
            source.pause();
        }
        boolean paused = false;
        boolean hitWater = true;
        float rate=0.15f;
        float old=lights.get(0).getColour().x;
        current = Terrain.calculateTerrain(terrains, player.getPosition());
        updateGrid();
        renderedTerrains = Terrain.calculateGrid(terrains, current);
        calculateEntitiesToRender();
        while(!Display.isCloseRequested()){
            if(current!=Terrain.calculateTerrain(terrains,player.getPosition())) {
                current = Terrain.calculateTerrain(terrains, player.getPosition());
                updateGrid();
                renderedTerrains = Terrain.calculateGrid(terrains, current);
                calculateEntitiesToRender();
            }
            player.move(current);
            player.limitRotation();
            if(sounds) {
                if (player.getPosition().y <= 0) {
                    if (hitWater) {
                        water.play(splash);
                        hitWater = false;
                    } else if (!water.isPlaying()) {
                        water.play(flow);
                    }
                } else {
                    if(!hitWater){
                        water.play(waves);
                    }
                    hitWater = true;
                }
            }
            if(particles) {
                snow.generateParticles(new Vector3f(player.getPosition().x,player.getPosition().y + 10, player.getPosition().z));
                ParticleMaster.update(camera);
            }
            if(lights.get(0).getColour().x>0.7f){
                rate=-0.15f*DisplayManager.getDelta();
            }else if(lights.get(0).getColour().x<0f){
                rate=0.15f*DisplayManager.getDelta();
            }
            Light.updateLight(lights.get(0),player,old,rate);
            old = lights.get(0).getColour().x;
            camera.move(renderedTerrains);
            mousePicker.update();
            GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
            //reflection
            fbos.bindReflectionFrameBuffer();
            float distance=2*camera.getPosition().y-waters.get(0).getHeight();
            camera.getPosition().y-=distance;
            camera.invertPitch();
            renderer.renderScene(renderedNormalMappedEntities,renderedEntities,renderedTerrains,lights,camera,player,new Vector4f(0,1,0,-waters.get(0).getHeight()+1f));
            camera.getPosition().y+=distance;
            camera.invertPitch();
            //refraction
            fbos.bindRefractionFrameBuffer();
            renderer.renderScene(renderedNormalMappedEntities,renderedEntities,renderedTerrains,lights,camera,player,new Vector4f(0,-1,0,waters.get(0).getHeight()+1f));
            //regular
            GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
            fbos.unbindCurrentFrameBuffer();
            renderer.renderScene(renderedNormalMappedEntities,renderedEntities,renderedTerrains,lights,camera,player,new Vector4f(0,-1,0,100000));
            waterRenderer.render(waters,camera, lights.get(0));
            if(particles) {
                ParticleMaster.render(camera);
            }
            guiRenderer.render(guis);
            TextMaster.render();
            DisplayManager.updateDisplay();
            if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
                paused = true;
                break;
            }
        }
        if(paused){
            source.pause();
            source2.pause();
            water.pause();
            pause();
        }
    }

    private void generateTerrain(int worldX, int worldZ){
        Terrain terrain = new Terrain(worldX,worldZ,loader,texturePack,blendMap,"heightMap");
        float centerX = 800;
        float centerZ = 800;
        waters.add(new WaterTile(worldX*1600+centerX,worldZ*1600+centerZ,0));
        terrains.add(terrain);
        generateEntities(terrain);
    }

    private void updateGrid(){
        for(int i=-1;i<2;i++){
            for(int j=-1;j<2;j++){
                if(!Terrain.checkForTerrain(current.getX()/Terrain.SIZE+i,current.getZ()/Terrain.SIZE+j,terrains)){
                    generateTerrain((int)(current.getX()/Terrain.SIZE+i),(int)(current.getZ()/Terrain.SIZE+j));
                }
            }
        }
    }

    private void generateEntities(Terrain terrain){
        float x;
        float z;
        float y;
        float scale;
        Random random=new Random();
        for(int i=0;i<200;i++){
            if(i%5==0) {
                scale = random.nextFloat() * 4 + 1f;
            }else{
                scale = random.nextFloat() * 2 + 1f;
            }
            x = random.nextFloat() * 1600;
            z = random.nextFloat() * 1600;
            if(terrain.getX()!=0) {
                x += terrain.getX();
            }
            if(terrain.getZ()!=0) {
                z += terrain.getZ();
            }
            y = terrain.getHeightOfTerrain(x, z);
            normalMappedEntities.add(new Entity(boulder,new Vector3f(x,y+(random.nextFloat() * 4 - 2) * scale,z),random.nextFloat() * 360,random.nextFloat()*360,random.nextFloat()*360,scale));
            x = random.nextFloat() * 1600;
            z = random.nextFloat() * 1600;
            if(terrain.getX()!=0) {
                x += terrain.getX();
            }
            if(terrain.getZ()!=0) {
                z += terrain.getZ();
            }
            y = terrain.getHeightOfTerrain(x, z);
            if(i%5==0){
                entities.add(new Entity(lamp,new Vector3f(x,y,z),0,0,0,1));
                lights.add(new Light(new Vector3f(x,y+16,z),new Vector3f(2,2,0),new Vector3f(1f,0.01f,0.002f)));
            }
            x = random.nextFloat() * 1600;
            z = random.nextFloat() * 1600;
            if(terrain.getX()!=0) {
                x += terrain.getX();
            }
            if(terrain.getZ()!=0) {
                z += terrain.getZ();
            }
            y = terrain.getHeightOfTerrain(x, z);
            entities.add(new Entity(tree,new Vector3f(x,y,z),0,0,0,random.nextFloat()*4+1));
            x = random.nextFloat() * 1600;
            z = random.nextFloat() * 1600;
            if(terrain.getX()!=0) {
                x += terrain.getX();
            }
            if(terrain.getZ()!=0) {
                z += terrain.getZ();
            }
            y = terrain.getHeightOfTerrain(x, z);
            entities.add(new Entity(fern,random.nextInt(4),new Vector3f(x,y,z),0,random.nextFloat()*360,0,random.nextFloat()*0.5f+2));
            x = random.nextFloat() * 1600;
            z = random.nextFloat() * 1600;
            if(terrain.getX()!=0) {
                x += terrain.getX();
            }
            if(terrain.getZ()!=0) {
                z += terrain.getZ();
            }
            y = terrain.getHeightOfTerrain(x, z);
            entities.add(new Entity(lowPolyTree,new Vector3f(x,y,z),0,random.nextFloat()*360,0,random.nextFloat()*0.5f+1f));
        }
    }

    private void initEntitiies(){
        lights.add(new Light(new Vector3f(160000,100000,1600),new Vector3f(0.25f,0.25f,0.25f)));//,new Vector3f(0.001f,0.001f,0.001f)));
        //lights.add(new Light(new Vector3f(185,1000,-293),new Vector3f(1,1,1)));
        grass.getTexture().setHasTransparency(true);
        grass.getTexture().setUseFakeLighting(true);
        fern.getTexture().setHasTransparency(true);
        fern.getTexture().setNumberOfRows(2);
        flower.getTexture().setUseFakeLighting(true);
        flower.getTexture().setHasTransparency(true);
        lamp.getTexture().setUseFakeLighting(true);
        boulder.getTexture().setNormalMapID(loader.loadTexture("boulderNormal"));
        generateTerrain(100,100);
        generateTerrain(99,100);
        generateTerrain(100,99);
        generateTerrain(99,99);
    }

    private void calculateEntitiesToRender(){
        renderedEntities.clear();
        renderedNormalMappedEntities.clear();
        Vector2f topRight = new Vector2f(current.getX()+2*Terrain.SIZE,current.getZ()+2*Terrain.SIZE);
        Vector2f bottomLeft = new Vector2f(current.getX()-Terrain.SIZE,current.getZ()-Terrain.SIZE);
        for(Entity entity:entities){
            if (entity.getPosition().x <= topRight.x && entity.getPosition().x >= bottomLeft.x && entity.getPosition().z <= topRight.y && entity.getPosition().z >= bottomLeft.y) {
                renderedEntities.add(entity);
            }
        }
        for(Entity entity:normalMappedEntities){
            if (entity.getPosition().x <= topRight.x && entity.getPosition().x >= bottomLeft.x && entity.getPosition().z <= topRight.y && entity.getPosition().z >= bottomLeft.y) {
                renderedNormalMappedEntities.add(entity);
            }
        }
    }

    private void pause(){
        source2.setLooping(false);
        //temp
        texts.clear();
        boolean down = true;
        boolean close = false;
        boolean play = false;
        boolean setting = false;
        boolean quit = false;
        TextMaster.init(loader);
        GUIText text = loadText("Resume", 3,new Vector2f(0.25f,0.575f),0.5f,true,new Vector3f(1,1,1));
        GuiTexture texture = new GuiTexture(loader.loadTexture("texture"),new Vector2f(0,-0.25f),new Vector2f(0.25f,0.1f));
        JButton playButton = new JButton(text,texture,JButton.calculateTopLeft(texture.getPosition(),texture.getScale()),JButton.calculateBottomRight(texture.getPosition(),texture.getScale()),buttons);
        JButton settings = new JButton(loadText("Settings",3,new Vector2f(0.25f,0.7f),0.5f,true,new Vector3f(1,1,1)),new GuiTexture(loader.loadTexture("texture"), new Vector2f(0f,-0.5f),new Vector2f(0.25f,0.1f)),JButton.calculateTopLeft(new Vector2f(0f,-0.5f),new Vector2f(0.25f,0.1f)),JButton.calculateBottomRight(new Vector2f(0f,-0.5f),new Vector2f(0.25f,0.1f)),buttons);
        JButton quitButton = new JButton(loadText("Quit",3,new Vector2f(0.25f,0.825f),0.5f,true,new Vector3f(1,1,1)),new GuiTexture(loader.loadTexture("texture"),new Vector2f(0f,-0.75f),new Vector2f(0.25f,0.1f)),JButton.calculateTopLeft(new Vector2f(0f,-0.75f),new Vector2f(0.25f,0.1f)),JButton.calculateBottomRight(new Vector2f(0f,-0.75f),new Vector2f(0.25f,0.1f)),buttons);
        guis.add(new GuiTexture(loader.loadTexture("nightBack"),new Vector2f(0,0),new Vector2f(1,1)));
        guis.add(playButton.getTexture());
        guis.add(settings.getTexture());
        guis.add(quitButton.getTexture());
        loadText("Paused",6,new Vector2f(0.1f,0.1f),0.8f,true,new Vector3f(1,1,1));
        while(!play){
            mousePicker.update();
            if(Mouse.isButtonDown(0)) {
                if(!down) {
                    play = MousePicker.checkIfOnButton(playButton);
                    setting = MousePicker.checkIfOnButton(settings);
                    quit = MousePicker.checkIfOnButton(quitButton);
                    if (play || quit) {
                        if (sounds) {
                            starter.play(bard);
                        }
                    }
                }
                down = true;
            }else {
                down = false;
            }
            for(JButton button:buttons) {
                if(MousePicker.checkIfOnButton(button)){
                    guiRenderer.render(guis, 0.1f, guis.indexOf(button.getTexture()));
                    break;
                } else {
                    guiRenderer.render(guis);
                }
            }
            TextMaster.render();
            DisplayManager.updateDisplay();
            if(Display.isCloseRequested() || quit) {
                close = true;
                break;
            }else if(setting){
                break;
            }
        }
        starter.setLooping(false);
        if(!close) {
            playButton.deleteButton(buttons);
            settings.deleteButton(buttons);
            quitButton.deleteButton(buttons);
            guis.clear();
            clearTexts();
            texts.clear();
            if(setting){
                settings(false);
            }else {
                gameLogic();
            }
        }
        //pause menu code goes here
    }

    private void settings(boolean menu){
        boolean down = true;
        boolean close = false;
        boolean backToMenu = false;
        GUIText text = loadText("Sound", 3,new Vector2f(0.25f,0.575f),0.5f,true,new Vector3f(1,1,1));
        GuiTexture texture = new GuiTexture(loader.loadTexture("texture"),new Vector2f(0,-0.25f),new Vector2f(0.25f,0.1f));
        JButton sound = new JButton(text,texture,JButton.calculateTopLeft(texture.getPosition(),texture.getScale()),JButton.calculateBottomRight(texture.getPosition(),texture.getScale()),buttons);
        JButton particle = new JButton(loadText("Particles",3,new Vector2f(0.25f,0.7f),0.5f,true,new Vector3f(1,1,1)),new GuiTexture(loader.loadTexture("texture"),new Vector2f(0f,-0.5f),new Vector2f(0.25f,0.1f)),JButton.calculateTopLeft(new Vector2f(0f,-0.5f),new Vector2f(0.25f,0.1f)),JButton.calculateBottomRight(new Vector2f(0f,-0.5f),new Vector2f(0.25f,0.1f)),buttons);
        JButton back = new JButton(loadText("Back",3,new Vector2f(0.25f,0.825f),0.5f,true,new Vector3f(1,1,1)),new GuiTexture(loader.loadTexture("texture"), new Vector2f(0f,-0.75f),new Vector2f(0.25f,0.1f)),JButton.calculateTopLeft(new Vector2f(0f,-0.75f),new Vector2f(0.25f,0.1f)),JButton.calculateBottomRight(new Vector2f(0f,-0.75f),new Vector2f(0.25f,0.1f)),buttons);
        guis.add(new GuiTexture(loader.loadTexture("nightBack"),new Vector2f(0,0),new Vector2f(1,1)));
        guis.add(sound.getTexture());
        guis.add(particle.getTexture());
        guis.add(back.getTexture());
        loadText("The Night Is Dark",6,new Vector2f(0.1f,0.1f),0.8f,true,new Vector3f(1,1,1));
        GuiTexture soundGUI = new GuiTexture(loader.loadTexture("green"),new Vector2f(0.375f,-0.25f),new Vector2f(0.1f,0.1f));
        GuiTexture particleGUI = new GuiTexture(loader.loadTexture("green"),new Vector2f(0.375f,-0.5f),new Vector2f(0.1f,0.1f));
        List<GuiTexture> GUIList = new ArrayList<>();
        GUIList.add(soundGUI);
        GUIList.add(particleGUI);
        while(!backToMenu){
            mousePicker.update();
            if(Mouse.isButtonDown(0)) {
                if(!down) {
                    backToMenu = MousePicker.checkIfOnButton(back);
                    if (MousePicker.checkIfOnButton(sound)) {
                        sounds = !sounds;
                    }else if(MousePicker.checkIfOnButton(particle)){
                        particles = !particles;
                    }
                }
                down = true;
            }else{
                down = false;
            }
            for(JButton button:buttons) {
                if(MousePicker.checkIfOnButton(button)){
                    guiRenderer.render(guis, 0.1f, guis.indexOf(button.getTexture()));
                    break;
                } else {
                    guiRenderer.render(guis);
                }
            }
            if(sounds){
                soundGUI.setTexture(loader.loadTexture("green"));
            }else{
                soundGUI.setTexture(loader.loadTexture("red"));
            }
            if(particles){
                particleGUI.setTexture(loader.loadTexture("green"));
            }else{
                particleGUI.setTexture(loader.loadTexture("red"));
            }
            guiRenderer.render(GUIList);
            TextMaster.render();
            DisplayManager.updateDisplay();
            if(Display.isCloseRequested()) {
                close = true;
                break;
            }
        }
        sound.deleteButton(buttons);
        particle.deleteButton(buttons);
        back.deleteButton(buttons);
        clearTexts();
        texts.clear();
        guis.clear();
        if(!close){
            if(menu){
                menu();
            }else{
                pause();
            }
        }else{
            exit();
        }
    }

    private GUIText loadText(String words,float size, Vector2f position, float length, boolean center, Vector3f color){
        FontType font = new FontType(loader.loadTexture("harrington"),new File("res/harrington.fnt"));
        GUIText text = new GUIText(words,size,font,position,length,center);
        text.setColour(color.x,color.y,color.z);
        texts.add(text);
        return text;
    }

    private void removeText(GUIText text){
        TextMaster.removeText(text);
    }

    private void clearTexts(){
        for(GUIText text:texts){
            removeText(text);
        }
    }

    private void exit(){
        ParticleMaster.cleanUp();
        TextMaster.cleanUp();
        source.cleanUp();
        source2.cleanUp();
        water.cleanUp();
        starter.cleanUp();
        AudioMaster.cleanUp();
        fbos.cleanUp();
        buttons.clear();
        waterShader.cleanUp();
        guiRenderer.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }

}