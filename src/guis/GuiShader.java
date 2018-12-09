package guis;

import org.lwjgl.util.vector.Matrix4f;

import shaders.ShaderProgram;

public class GuiShader extends ShaderProgram{

    private static final String VERTEX_FILE = "src/guis/guiVertexShader.txt";
    private static final String FRAGMENT_FILE = "src/guis/guiFragmentShader.txt";

    private int location_transformationMatrix;
    private int location_highlight;

    public GuiShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    public void loadTransformation(Matrix4f matrix){
        super.loadMatrix(location_transformationMatrix, matrix);
    }

    public void loadHighlight(float highlight){
        super.loadFloat(location_highlight,highlight);
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_highlight = super.getUniformLocation("highlight");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }




}