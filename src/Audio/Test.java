package audio;

public class Test {

    public static void main(String[] args){

        Audio.AudioMaster.init();
        Audio.AudioMaster.setListenerData();
        int buffer = Audio.AudioMaster.loadSound("audio/bounce.wav");
        Audio.Source source = new Audio.Source();
        for (int i=0;i<10;i++){
            source.play(buffer);
        }

        source.cleanUp();
        Audio.AudioMaster.cleanUp();

    }

}
