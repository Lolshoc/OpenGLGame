package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.*;

public class DisplayManager {

    private static final int WIDTH=1280;
    private static final int HEIGHT=720;
    private static final int FPS_CAP=120;

    private static long lastFrameTime;
    private static float delta;

    public static void createDisplay(){

        try {
            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            Display.create();
            Display.setTitle("The Night Is Dark!");
        }catch (LWJGLException e){
            e.printStackTrace();
        }

        GL11.glViewport(0,0,WIDTH,HEIGHT);
        lastFrameTime=getCurrentTime();
    }

    public static void updateDisplay(){

        Display.sync(FPS_CAP);
        Display.update();
        long currentFrameTime=getCurrentTime();
        delta=(currentFrameTime-lastFrameTime)/1000f;
        lastFrameTime=currentFrameTime;

    }

    public static float getDelta(){
        return delta;
    }

    public static void closeDisplay(){

        Display.destroy();

    }

    public static long getCurrentTime(){
        return Sys.getTime()*1000/Sys.getTimerResolution();
    }

}