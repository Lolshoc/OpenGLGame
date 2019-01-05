package particles;


import entities.Camera;
import entities.Player;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;

public class Particle {

    private Vector3f position;
    private Vector3f velocity;
    private float gravityEffect;
    private float lifeLength;
    private float rotation;
    private float scale;

    private float elapsedTime = 0;

    private Vector2f textureOffset1 = new Vector2f();
    private Vector2f textureOffset2 = new Vector2f();
    private float blend;

    private ParticleTexture texture;
    private float distance;

    public Particle(ParticleTexture texture, Vector3f position, Vector3f velocity, float gravityEffect, float lifeLength, float rotation, float scale) {
        this.texture = texture;
        this.position = position;
        this.velocity = velocity;
        this.gravityEffect = gravityEffect;
        this.lifeLength = lifeLength;
        this.rotation = rotation;
        this.scale = scale;
        ParticleMaster.addParticle(this);
    }

    public Vector2f getTextureOffset1() {
        return textureOffset1;
    }

    public Vector2f getTextureOffset2() {
        return textureOffset2;
    }

    public float getBlend() {
        return blend;
    }

    public ParticleTexture getTexture() {
        return texture;
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getRotation() {
        return rotation;
    }

    public float getScale() {
        return scale;
    }

    public float getDistance() {
        return distance;
    }

    protected boolean update(Camera camera){
        velocity.y+= Player.GRAVITY * gravityEffect * DisplayManager.getDelta();
        Vector3f change = new Vector3f(velocity);
        change.scale(DisplayManager.getDelta());
        Vector3f.add(change,position,position);
        distance = Vector3f.sub(camera.getPosition(),position,null).lengthSquared();
        updateTextureInfo();
        elapsedTime+=DisplayManager.getDelta();
        return elapsedTime < lifeLength;
    }

    private void updateTextureInfo(){
        float lifeFactor = elapsedTime / lifeLength;
        int stage = texture.getNumberOfRows() * texture.getNumberOfRows();
        float atlasStage = lifeFactor * stage;
        int index1 = (int) Math.floor(atlasStage);
        int index2 = index1 < stage - 1 ? index1 + 1 : index1;
        this.blend = atlasStage % 1;
        setTextureOffset(textureOffset1, index1);
        setTextureOffset(textureOffset2, index2);
    }

    private void setTextureOffset(Vector2f offset, int index){
        int column = index % texture.getNumberOfRows();
        int row = index / texture.getNumberOfRows();
        offset.x = (float)column / texture.getNumberOfRows();
        offset.y = (float)row / texture.getNumberOfRows();
    }

}
