package textures;

public class ModelTexture {

    private int textureID;

    private float shineDamper=1;
    private float reflectivity=1;

    public ModelTexture(int id){
        this.textureID=id;
    }

    public int getTextureID() {
        return textureID;
    }

    public float getShineDamper() {
        return shineDamper;
    }

    public void setShineDamper(float shineDamper) {
        this.shineDamper = shineDamper;
    }

    public float getRelflectivity() {
        return reflectivity;
    }

    public void setRelflectivity(float relflectivity) {
        this.reflectivity = relflectivity;
    }
}
