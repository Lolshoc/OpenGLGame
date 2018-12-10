package entities;

import fontMeshCreator.GUIText;
import fontRendering.TextMaster;
import guis.GuiTexture;
import org.lwjgl.util.vector.Vector2f;

import java.util.List;

public class JButton {

    private GUIText text;
    private GuiTexture texture;

    Vector2f topLeft;
    Vector2f bottomeRight;

    public JButton(GUIText text, GuiTexture texture, Vector2f topLeft, Vector2f bottomeRight, List<JButton> list){
        this.text = text;
        this.texture = texture;
        this.topLeft = topLeft;
        this.bottomeRight = bottomeRight;
        list.add(this);
        TextMaster.loadText(text);
    }

    public void deleteButton(){
        TextMaster.removeText(text);
    }

    public GuiTexture getTexture(){
        return texture;
    }

    public void setText(String string){
        text.setText(string);
    }

    public static Vector2f calculateTopLeft(Vector2f position, Vector2f length){
        return new Vector2f(position.x-length.x,position.y-length.y);
    }

    public static Vector2f calculateBottomRight(Vector2f position, Vector2f length){
        return new Vector2f(position.x+length.x,position.y+length.y);
    }

    public Vector2f getTopLeft() {
        return topLeft;
    }

    public Vector2f getBottomeRight() {
        return bottomeRight;
    }

    public void upload(List<GuiTexture> guis, List<GUIText> texts){
        guis.add(texture);
        texts.add(text);
        TextMaster.loadText(text);
    }
}
