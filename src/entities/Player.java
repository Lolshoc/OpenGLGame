package entities;

import models.TexturedModel;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import terrains.Terrain;

import java.util.List;

public class Player extends Entity {

    private static final float RUN_SPEED=80;
    private static final float TURN_SPEED=160;
    private static final float GRAVITY=-50;
    private static final float JUMP_POWER=30;

    private float currentSpeed=0;
    private float currentTurnSpeed=0;
    private float upwardsSpeed;

    private boolean isInAir=false;

    public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        super(model, position, rotX, rotY, rotZ, scale);
    }

    public void move(Terrain terrain){
        checkInputs();
        super.increaseRotation(0,currentTurnSpeed* DisplayManager.getDelta(),0);
        float distance=currentSpeed*DisplayManager.getDelta()*getScale();
        float dx=(float)(distance*Math.sin(Math.toRadians(super.getRotY())));
        float dz=(float)(distance*Math.cos(Math.toRadians(super.getRotY())));
        super.increasePosition(dx,0,dz);
        upwardsSpeed+=GRAVITY*DisplayManager.getDelta()*getScale();
        super.increasePosition(0,upwardsSpeed*DisplayManager.getDelta(),0);
        float terrainHeight=terrain.getHeightOfTerrain(super.getPosition().x,super.getPosition().z);
        if(super.getPosition().y<terrainHeight){
            upwardsSpeed=0;
            isInAir=false;
            super.getPosition().y=terrainHeight;
        }
    }

    private void jump(){
        if(!isInAir) {
            this.upwardsSpeed = JUMP_POWER*getScale();
            isInAir=true;
        }
    }

    private void checkInputs(){
        if(Keyboard.isKeyDown(Keyboard.KEY_W)){
            this.currentSpeed=RUN_SPEED;
        }else if(Keyboard.isKeyDown(Keyboard.KEY_S)){
            this.currentSpeed=-RUN_SPEED;
        }else{
            this.currentSpeed=0;
        }

        if(Keyboard.isKeyDown(Keyboard.KEY_D)){
            this.currentTurnSpeed=-TURN_SPEED;
        }else if(Keyboard.isKeyDown(Keyboard.KEY_A)){
            this.currentTurnSpeed=TURN_SPEED;
        }else{
            this.currentTurnSpeed=0;
        }

        if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
            jump();
        }
    }

    public Terrain calculateTerrain(List<Terrain> terrains){
        if(getPosition().x>terrains.get(0).getX()&&getPosition().z<terrains.get(2).getZ()) {
            return terrains.get(0);
        }else if(getPosition().x<terrains.get(0).getX()&&getPosition().z<terrains.get(2).getZ()){
            return terrains.get(1);
        }else if(getPosition().x<terrains.get(0).getX()&getPosition().z>terrains.get(2).getZ()){
            return terrains.get(3);
        }else {
            return terrains.get(2);
        }
    }

    public void limitRotation(){
        if(getRotY()>180){
            setRotY(getRotY()-360);
        }else if(getRotY()<-180){
            setRotY(getRotY()+360);
        }
    }}
