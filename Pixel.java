/**
 * Created by tyler on 1/11/2017.
 */

public class Pixel{
    public Pixel(){}
    public Pixel(int red,int green,int blue){
       setRed(red);
       setBlue(blue);
       setGreen(green);
    }
    //getters
    int getRed(){
        return red;
    }
    int getBlue(){
        return blue;
    }
    int getGreen(){
        return green;
    }
    //setters
    void setRed(int red){ this.red = red;}
    void setBlue(int blue){
        this.blue = blue;
    }
    void setGreen(int green){
        this.green = green;
    }

    private int red;
    private int green;
    private int blue;
}
