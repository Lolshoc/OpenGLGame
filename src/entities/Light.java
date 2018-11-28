package entities;

import org.lwjgl.util.vector.Vector3f;
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

    public List<Light> findNearestLights(Vector3f position, List<Light> lights){
        float light1=100000000;
        float light2=100000000;
        float light3=100000000;
        int index[]=new int[3];
        float difference;
        List<Light> fourLights=new ArrayList<>();
        for(Light light:lights){
            difference=Maths.pythagorean(Math.abs(Math.abs(light.getPosition().x)-Math.abs(position.x)),Math.abs(Math.abs(light.getPosition().z)-Math.abs(position.z)));
            if(difference<light1){
                light1=difference;
                index[0]=lights.indexOf(light);
            }else if(difference<light2){
                light2=difference;
                index[1]=lights.indexOf(light);
            }else if(difference<light3){
                light3=difference;
                index[2]=lights.indexOf(light);
            }
        }
        fourLights.add(lights.get(index[0]));
        fourLights.add(lights.get(index[1]));
        fourLights.add(lights.get(index[2]));
        fourLights.add(lights.get(0));
        return fourLights;
    }
}
