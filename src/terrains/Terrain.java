package terrains;

import models.RawModel;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.Loader;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.Maths;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class Terrain {

    public static final float SIZE=1600;
    private float [][] heights;
    private static final int VERTEX_COUNT = 128;
    private static final int SEED = new Random().nextInt(1000000000);

    private HeightsGenerator generator;

    private float x;
    private float z;
    private RawModel model;
    private TerrainTexturePack texturePack;
    private TerrainTexture blendMap;

    public Terrain(int gridX, int gridZ, Loader loader, TerrainTexturePack texturePack,TerrainTexture blendMap, String heightMap){
        this.texturePack =texturePack;
        this.blendMap=blendMap;
        this.x=gridX*SIZE;
        this.z=gridZ*SIZE;
        generator = new HeightsGenerator(gridX,gridZ,VERTEX_COUNT,SEED);
        this.model=generateTerrain(loader);
    }

    public float getX() {
        return x;
    }

    public float getZ() {
        return z;
    }

    public RawModel getModel() {
        return model;
    }

    public float getSize(){
        return SIZE;
    }

    public TerrainTexturePack getTexturePack() {
        return texturePack;
    }

    public TerrainTexture getBlendMap() {
        return blendMap;
    }

    public float getHeightOfTerrain(float worldX,float worldZ){
        float terrainX=worldX-this.x;
        float terrainZ=worldZ-this.z;
        float gridSquareSize=SIZE/((float)heights.length-1);
        int gridX=(int)Math.floor(terrainX/gridSquareSize);
        int gridZ=(int)Math.floor(terrainZ/gridSquareSize);
        if(gridX>=heights.length-1||gridZ>=heights.length-1||gridX<0||gridZ<0){
            return 0;
        }
        float xCoord=(terrainX%gridSquareSize)/gridSquareSize;
        float zCoord=(terrainZ%gridSquareSize)/gridSquareSize;
        float answer;
        if(xCoord<=1-zCoord){
            answer = Maths.barryCentric(new Vector3f(0, heights[gridX][gridZ], 0), new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(0, heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
        }else {
            answer = Maths.barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(1, heights[gridX + 1][gridZ + 1], 1), new Vector3f(0, heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
        }
        return answer;
    }
    private RawModel generateTerrain(Loader loader){
        heights=new float[VERTEX_COUNT][VERTEX_COUNT];
        int count = VERTEX_COUNT * VERTEX_COUNT;
        float[] vertices = new float[count * 3];
        float[] normals = new float[count * 3];
        float[] textureCoords = new float[count*2];
        int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
        int vertexPointer = 0;
        for(int i=0;i<VERTEX_COUNT;i++){
            for(int j=0;j<VERTEX_COUNT;j++){
                vertices[vertexPointer*3] = (float)j/((float)VERTEX_COUNT - 1) * SIZE;
                float height=getHeight(j,i,generator);
                heights[j][i]=height;
                vertices[vertexPointer*3+1] = getHeight(j,i,generator);
                vertices[vertexPointer*3+2] = (float)i/((float)VERTEX_COUNT - 1) * SIZE;
                Vector3f normal=calculateNormal(j,i,generator);
                normals[vertexPointer*3] = normal.x;
                normals[vertexPointer*3+1] = normal.y;
                normals[vertexPointer*3+2] = normal.z;
                textureCoords[vertexPointer*2] = (float)j/((float)VERTEX_COUNT - 1);
                textureCoords[vertexPointer*2+1] = (float)i/((float)VERTEX_COUNT - 1);
                vertexPointer++;
            }
        }
        int pointer = 0;
        for(int gz=0;gz<VERTEX_COUNT-1;gz++){
            for(int gx=0;gx<VERTEX_COUNT-1;gx++){
                int topLeft = (gz*VERTEX_COUNT)+gx;
                int topRight = topLeft + 1;
                int bottomLeft = ((gz+1)*VERTEX_COUNT)+gx;
                int bottomRight = bottomLeft + 1;
                indices[pointer++] = topLeft;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = topRight;
                indices[pointer++] = topRight;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = bottomRight;
            }
        }
        return loader.loadToVAO(vertices, textureCoords, indices, normals);
    }

    private Vector3f calculateNormal(int x, int z, HeightsGenerator heightsGenerator){
        float heightL=getHeight(x-1,z,heightsGenerator);
        float heightR=getHeight(x+1,z,heightsGenerator);
        float heightU=getHeight(x,z+1,heightsGenerator);
        float heightD=getHeight(x,z-1,heightsGenerator);
        Vector3f normal=new Vector3f(heightL-heightR,2f,heightD-heightU);
        normal.normalise();
        return normal;
    }

    private float getHeight(int x, int z, HeightsGenerator heightsGenerator){
        return heightsGenerator.generateHeight(x,z);
    }

    public static Terrain calculateTerrain(List<Terrain> terrains, Vector3f position){
        boolean x;
        boolean z;
        for(Terrain terrain:terrains) {
            x = false;
            z = false;
            if (terrain.getX() == 0) {
                if(position.x >= 0 && position.x<SIZE){
                    x = true;
                }
            }else if(Math.abs((int)position.x / (int)terrain.getX()) == 0 && terrain.getX()<0 || (int)position.x / (int)terrain.getX()== 1 && terrain.getX()>0){
                x = true;
            }
            if(terrain.getZ() == 0) {
                if (position.z >= 0 && position.z<SIZE){
                    z = true;
                }
            }else if(Math.abs((int)position.z / (int)terrain.getZ()) == 0 && terrain.getZ()<0 || (int)position.z / (int)terrain.getZ()== 1 && terrain.getZ()>0){
                z = true;
            }
            if(x && z){
                return terrain;
            }
        }
        return terrains.get(0);
    }

}
