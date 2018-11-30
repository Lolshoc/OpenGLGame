package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.MasterRenderer;

public class Camera {

    private float distanceFromPlayer=50;
    private float angleAroundPlayer=0;
    private static float defaultHieght=0;

    private Vector3f position=new Vector3f(100,30,0);
    private float pitch=20;
    private float yaw;
    private float roll;

    private Player player;

    public Camera(Player player){
        this.player=player;
    }

    public void move(){
        calculateZoom();
        calculatePitch();
        calculateAngle();
        float horizontalDistance=calculateHorizontalDistance();
        float verticalDistance=calculateVerticalDistance();
        calculateCameraPos(horizontalDistance,verticalDistance);
        this.yaw=180-(player.getRotY()+angleAroundPlayer);if(Keyboard.isKeyDown(Keyboard.KEY_W)){
            position.z-=0.2f;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_D)){
            position.x+=0.2f;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_A)){
            position.x-=0.2f;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_S)){
            position.z+=0.2f;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
            position.y+=0.2f;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
            position.y-=0.2f;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_Q)){
            yaw-=0.2f;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_E)){
            yaw+=0.2f;
        }
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }

    private void calculateCameraPos(float horizontalDistance,float verticalDistance){
        float theta =player.getRotY()+angleAroundPlayer;
        float offsetX=(float)(horizontalDistance*Math.sin(Math.toRadians(theta)));
        float offsetZ=(float)(horizontalDistance*Math.cos(Math.toRadians(theta)));
        position.x=player.getPosition().x-offsetX;
        position.z=player.getPosition().z-offsetZ;
        position.y=player.getPosition().y+verticalDistance+(defaultHieght*player.getScale());
    }

    private float calculateHorizontalDistance(){
        return (float)(distanceFromPlayer*Math.cos(Math.toRadians(pitch)));
    }

    private float calculateVerticalDistance(){
        return (float)(distanceFromPlayer*Math.sin(Math.toRadians(pitch)));
    }

    private void calculateZoom(){
        float zoomLevel= Mouse.getDWheel()*0.025f;
        distanceFromPlayer-=zoomLevel;
        if(distanceFromPlayer<10){
            distanceFromPlayer=10;
        }else if(distanceFromPlayer>200){
            distanceFromPlayer=200;
        }
    }

    private void calculatePitch(){
        if(Mouse.isButtonDown(0)){
            float pitchChange=Mouse.getDY()*0.1f;
            pitch-=pitchChange;
            if (pitch > 90) {
                pitch=90;
            }else if(pitch<1){
                pitch=1;
            }
        }
    }

    private void calculateAngle(){
        if(Mouse.isButtonDown(0)){
            float angleChange=Mouse.getDX()*0.3f;
            angleAroundPlayer-=angleChange;
        }
    }

    public static void setDefaultHieght(float defaultHieght) {
        Camera.defaultHieght = defaultHieght;
    }
}
