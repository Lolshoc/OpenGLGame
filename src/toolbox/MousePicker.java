package toolbox;

import entities.Camera;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class MousePicker {

    private Vector3f currentRay;

    private Matrix4f viewMatrix;
    private Matrix4f projectionMatrix;
    private Camera camera;

    public MousePicker(Camera cam, Matrix4f projection){
        this.camera=cam;
        this.projectionMatrix=projection;
        this.viewMatrix=Maths.createViewMatrix(camera);
    }

    public Vector3f getCurrentRay(){
        return currentRay;
    }

    public void update(){
        viewMatrix=Maths.createViewMatrix(camera);
        currentRay=calculateMouseRay();
    }

    private Vector3f calculateMouseRay(){
        float mouseX=Mouse.getX();
        float mouseY=Mouse.getY();
        Vector2f normalizedCoords=getNormalizedDeviceCoords(mouseX,mouseY);
        Vector4f clipCoords=new Vector4f(normalizedCoords.x,normalizedCoords.y,-1,1);
        Vector4f eyeCoords=toEyeCoords(clipCoords);
        Vector3f worldCoords=toWorldCoords(eyeCoords);
        return worldCoords;
    }

    private Vector3f toWorldCoords(Vector4f eyeCoords){
        Matrix4f invertedView=Matrix4f.invert(viewMatrix,null);
        Vector4f rayWorld=Matrix4f.transform(invertedView,eyeCoords,null);
        Vector3f mouseRay=new Vector3f(rayWorld.x,rayWorld.y,rayWorld.z);
        mouseRay.normalise();
        return mouseRay;
    }

    private Vector4f toEyeCoords(Vector4f clipCoords){
        Matrix4f invertedProjection=Matrix4f.invert(projectionMatrix, null);
        Vector4f eyeCoords=Matrix4f.transform(invertedProjection,clipCoords,null);
        return new Vector4f(eyeCoords.x,eyeCoords.y,-1,0);
    }

    private Vector2f getNormalizedDeviceCoords(float mouseX,float mouseY){
        float x=(2f*mouseX)/ Display.getWidth()-1;
        float y=(2f*mouseY)/Display.getHeight()-1;
        return new Vector2f(x,y);
    }

    public static boolean checkIfOnButton(Vector2f topLeft, Vector2f bottomRight){
        float x = (float)Mouse.getX()/Display.getWidth();
        float y = (float)Mouse.getY()/Display.getHeight();
        x = x*2 - 1;
        y = y*2 - 1;
        return (x>topLeft.x && x<bottomRight.x && y<topLeft.y && y>bottomRight.y);
    }

}
