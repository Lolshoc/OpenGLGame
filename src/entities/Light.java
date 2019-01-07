package entities;

import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import toolbox.Maths;

import java.util.ArrayList;
import java.util.List;

public class Light {

    private Vector3f position;
    private Vector3f colour;
    private Vector3f attenuation=new Vector3f(1,0,0);

    public Light(Vector3f position, Vector3f colour) {
        this.position = position;
        this.colour = colour;
    }

    public Light(Vector3f position, Vector3f colour, Vector3f attenuation) {
        this.position = position;
        this.colour = colour;
        this.attenuation=attenuation;
    }

    public Vector3f getAttenuation(){
        return attenuation;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getColour() {
        return colour;
    }

    public void setColour(Vector3f colour) {
        this.colour = colour;
    }

    public static List<Light> findNearestLights(Vector3f position, List<Light> lights){
        float[] distances=new float[]{100000000,100000000,100000000,100000000};
        float index[]=new float[4];
        float difference;
        List<Light> fourLights=new ArrayList<>();
        for(Light light:lights){
            difference=Maths.pythagorean(light.getPosition().x-position.x,light.getPosition().z-position.z);
            if(difference<distances[0]){
                Maths.shift(distances,0);
                Maths.shift(index,0);
                distances[0]=difference;
                index[0]=lights.indexOf(light);
            }else if(difference<distances[1]){
                Maths.shift(distances,1);
                Maths.shift(index,1);
                Maths.shift(distances,2);
                distances[1]=difference;
                index[1]=lights.indexOf(light);
            }else if(difference<distances[2]){
                Maths.shift(distances,2);
                Maths.shift(index,2);
                distances[2]=difference;
                index[2]=lights.indexOf(light);
            }else if(difference<distances[3]){
                distances[3]=difference;
                index[3]=lights.indexOf(light);
            }
        }
        fourLights.add(lights.get((int)index[0]));
        fourLights.add(lights.get((int)index[1]));
        fourLights.add(lights.get((int)index[2]));
        fourLights.add(lights.get(0));
        //fourLights.add(lights.get((int)index[3]));
        return fourLights;
    }

    public static void updateLight(Light light, Player player, float old, float rate){
        //light.setPosition(new Vector3f(player.getPosition().x,player.getPosition().y+100,player.getPosition().z));
        light.setColour(new Vector3f(old+rate,old+rate,old+rate));
    }
}
