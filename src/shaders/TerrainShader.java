package shaders;

import entities.Camera;
import entities.Light;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Maths;

public class TerrainShader extends ShaderProgram {

    private static final String VERTEX_FILE="src/shaders/terrainVertexShader.txt";
    private static final String FRAGMENT_FILE="src/shaders/terrainFragmentShader.txt";

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_lightPosition;
    private int location_lightColour;
    private int location_shineDamper;
    private int location_reflectivity;
    private int location_skyColour;

    public TerrainShader() {
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
        location_lightPosition=super.getUniformLocation("lightPosition");
        location_lightColour=super.getUniformLocation("lightColour");
        location_shineDamper=super.getUniformLocation("shineDamper");
        location_reflectivity=super.getUniformLocation("reflectivity");
        location_skyColour=super.getUniformLocation("skyColour");
    }

    public void loadSkyColour(float r,float g, float b){
        super.loadVecter(location_skyColour,new Vector3f(r,g,b));
    }

    public void loadShineValue(float damper,float reflectivity){
        super.loadFloat(location_shineDamper,damper);
        super.loadFloat(location_reflectivity,reflectivity);
    }

    public void loadTransformationMatrix(Matrix4f matrix){
        super.loadMatrix(location_transformationMatrix,matrix);
    }

    public void loadLight(Light light){
        super.loadVecter(location_lightPosition, light.getPosition());
        super.loadVecter(location_lightColour,light.getColour());
    }

    public void loadViewMatrix(Camera camera){
        Matrix4f viewMatrix= Maths.createViewMatrix(camera);
        super.loadMatrix(location_viewMatrix,viewMatrix);
    }

    public void loadProjectionMatrix(Matrix4f projection){
        super.loadMatrix(location_projectionMatrix,projection);
    }
}
