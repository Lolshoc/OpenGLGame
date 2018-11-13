package shaders;

import entities.Camera;
import entities.Light;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Maths;

import java.util.List;

public class StaticShader extends ShaderProgram {

    private static final int MAX_LIGHTS=4;

    private static final String VERTEX_FILE="src/shaders/vertexShader.txt";
    private static final String FRAGMENT_FILE="src/shaders/fragmentShader.txt";

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_lightPosition[];
    private int location_lightColour[];
    private int location_shineDamper;
    private int location_reflectivity;
    private int location_useFakeLighting;
    private int location_skyColour;
    private int location_numberOfRows;
    private int location_offset;

    public StaticShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0,"position");
        super.bindAttribute(1,"textureCoords" );
        super.bindAttribute(2,"normal");
    }

    @Override
    protected void getAllUniformLocations(){
        location_transformationMatrix=super.getUniformLocation("transformationMatrix");
        location_projectionMatrix=super.getUniformLocation("projectionMatrix");
        location_viewMatrix=super.getUniformLocation("viewMatrix");
        location_shineDamper=super.getUniformLocation("shineDamper");
        location_reflectivity=super.getUniformLocation("reflectivity");
        location_useFakeLighting=super.getUniformLocation("useFakeLighting");
        location_skyColour=super.getUniformLocation("skyColour");
        location_numberOfRows=super.getUniformLocation("numberOfRows");
        location_offset=super.getUniformLocation("offset");
        location_lightPosition=new int[MAX_LIGHTS];
        location_lightColour=new int[MAX_LIGHTS];
        for(int i=0;i<MAX_LIGHTS;i++){
            location_lightPosition[i]=super.getUniformLocation("lightPosition["+i+"]");
            location_lightColour[i]=super.getUniformLocation("lightColour["+i+"]");
        }
    }

    public void loadNumberOfRows(int rows){
        super.loadFloat(location_numberOfRows,rows);
    }

    public void loadOffset(float x,float y){
        super.load2DVector(location_offset,new Vector2f(x,y));
    }

    public void loadSkyColour(float r, float g, float b){
        super.loadVecter(location_skyColour,new Vector3f(r,g,b));
    }

    public void loadFakeLighting(boolean useFake){
        super.loadBoolean(location_useFakeLighting,useFake);
    }

    public void loadShineValue(float damper,float reflectivity){
        super.loadFloat(location_shineDamper,damper);
        super.loadFloat(location_reflectivity,reflectivity);
    }

    public void loadTransformationMatrix(Matrix4f matrix){
        super.loadMatrix(location_transformationMatrix,matrix);
    }

    public void loadLight(List<Light> light){
        for(int i=0;i<MAX_LIGHTS;i++){
            if(i<light.size()){
                super.loadVecter(location_lightPosition[i],light.get(i).getPosition());
                super.loadVecter(location_lightColour[i],light.get(i).getColour());
            }else{
                super.loadVecter(location_lightPosition[i],new Vector3f(0.0f,0.0f,0.0f));
                super.loadVecter(location_lightColour[i],new Vector3f(0.0f,0.0f,0.0f));
            }
        }
    }

    public void loadViewMatrix(Camera camera){
        Matrix4f viewMatrix= Maths.createViewMatrix(camera);
        super.loadMatrix(location_viewMatrix,viewMatrix);
    }

    public void loadProjectionMatrix(Matrix4f projection){
        super.loadMatrix(location_projectionMatrix,projection);
    }
}
