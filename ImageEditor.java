/**
 * Created by tyler on 1/11/2017.
 */
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.File;
import java.io.PrintWriter;

public class ImageEditor {
    public static void main(String[] args){
        ImageEditor ie = new ImageEditor();
        String inputFileName = args[0];
        String outputFileName = args[1];
        ie.createPicture(inputFileName,outputFileName,args);
    }
    //where we create create a 2D array of class Image made up of Class Pixel
    boolean createPicture(String inputFileName, String outputFileName, String[] args){
        int height,width;
        Scanner scan;
        File file = new File(inputFileName);
        try {
            scan = new Scanner(file).useDelimiter("((#[^\\n]*\\n)|(\\s+))+");
        }catch(FileNotFoundException e){System.out.println("No Such File Name! Exiting Program"); return false;}

        scan.next();//grabs the P3
        width = scan.nextInt();//grabs the width
        height = scan.nextInt();//grabs the height
        int numOfPixels = width * height;
        Image picture = new Image(height, width);
        rowLength = picture.getNumOfRows();//initialize class variables
        colLength = picture.getNumOfCols();
        scan.nextInt();//get rid of maximum value of colors, dont need to store

        //create our pixel objects and put them in our image
        for (int i = 0; i < numOfPixels; i++) {
            int red = 0, green = 0, blue = 0;
            red = scan.nextInt();
            green = scan.nextInt();
            blue = scan.nextInt();
            Pixel p = new Pixel(red,green, blue);
            picture.push_back(p);
        }
        scan.close();

        if(beginEditing(args, picture)) {//if no errors
            File outFile = new File(outputFileName);
            if (outputPicture(picture,outFile)) {
                return true;//picture created
            }
            else{System.out.println("Picture not created, failure.");return false;}
        }
        return false;
    }

    boolean beginEditing(String[] args,Image newPic){
        String input = args[2].toLowerCase();
        switch(input){
            case"grayscale":
                grayScale(newPic);
                break;
            case"emboss":
                emboss(newPic);
                break;
            case"invert":
                invertColors(newPic);
                break;
            case"motionblur":
                int num;
                try {
                    num = Integer.parseInt(args[3]);
                    motionBlur(num, newPic);
                } catch(IndexOutOfBoundsException  e){System.out.println("No blur length Parameter given!"); return false;}
                break;
            default:
                System.out.println("No/Incorrect Parameter!");
                return false;
        }
        return true;
    }
    boolean outputPicture(Image newPic, File outFile){
        try {
            PrintWriter writer = new PrintWriter(outFile);
            StringBuilder output = new StringBuilder();
            Pixel[][] arr = newPic.getImage();
            output.append("P3 " + colLength + " " + rowLength + " " + 255 + " ");
            for(int i = 0; i < rowLength; i++){
                for(int j = 0; j < colLength; j++){
                    output.append(arr[i][j].getRed() + " ");
                    output.append(arr[i][j].getGreen() + " ");
                    output.append(arr[i][j].getBlue() + " ");
                }
            }
            writer.print(output.toString());
            writer.close();
        }catch(Exception e){System.out.println(e.getMessage().toString()); return false;}
        return true;
    }

    int getDifference(Pixel[][] arr, int i, int j){
        int maxDif = 0;
        int num = 0;
        int redDif = arr[i][j].getRed() - arr[i-1][j-1].getRed();
        int greenDif =  arr[i][j].getGreen() - arr[i-1][j-1].getGreen();
        int blueDif = arr[i][j].getBlue() - arr[i-1][j-1].getBlue();
        //see where the greatest difference lies
        if(Math.abs(redDif) > Math.abs(maxDif)){maxDif = redDif;}
        if(Math.abs(greenDif) > Math.abs(maxDif)){maxDif = greenDif;}
        if(Math.abs(blueDif) > Math.abs(maxDif)){maxDif = blueDif;}
        num = maxDif + 128;
        //check boundaries
        if (num < 0) {
            return 0;
        }
        else if (num > 255) {
            return 255;
        }
        return num;
    }
    //Here we wll do the embossing
    void emboss(Image newPic){
        Pixel[][] arr = newPic.getImage();//get the image array
        for(int i = rowLength - 1; i >= 0; i--) {
            for (int j = colLength - 1; j >= 0; j--) {//goes through every pixel and embosses the image
                if(i == 0 || j == 0 ){
                    arr[i][j].setRed(128);
                    arr[i][j].setGreen(128);
                    arr[i][j].setBlue(128);
                }
                else{
                    int v = getDifference(arr,i,j);
                    //emboss the image
                    arr[i][j].setRed(v);
                    arr[i][j].setGreen(v);
                    arr[i][j].setBlue(v);
                }
            }
        }
        newPic.setImage(arr);
    }
    //helper method for invertColors which inverts numbers
    int inverter(int num){
        int newNum = 255 - num;
        return newNum;
    }

    //Here we will invert the colors
    void invertColors(Image newPic){
        Pixel[][] arr = newPic.getImage();//get the image array
        for(int i = 0; i < rowLength; i++){
            for(int j = 0; j < colLength; j++){//goes through every pixel and inverts the RGB values
                arr[i][j].setRed(inverter(arr[i][j].getRed()));
                arr[i][j].setGreen(inverter(arr[i][j].getGreen()));;
                arr[i][j].setBlue(inverter(arr[i][j].getBlue()));
            }
        }
        newPic.setImage(arr);
    }

    //The value of each color of each pixel is the average of that
    //color value for n pixels (from the current pixel to n-1) horizontally
    void motionBlur(int blurLength,Image newPic){
        Pixel[][] arr = newPic.getImage();//get the image array
        //goes through every pixel and blurs the image
        for(int i = 0; i < rowLength; i++) {
            for (int j = 0; j < colLength; j++) {
                int k,redAvg = 0, blueAvg = 0, greenAvg = 0, edger = 0;//when at edge, edger subtracts  out of bounds positions from divisor "k-edger"
                for(k = 0; k < blurLength; k++){//averages from k to blurlength-1
                    if(!((j + k) >= colLength)) {//if our column number plus the given k, is not greater then our width
                        redAvg += arr[i][j + k].getRed();
                        greenAvg += arr[i][j + k].getGreen();
                        blueAvg += arr[i][j + k].getBlue();
                    }
                    else{//skip out of bounds numbers
                        edger++;
                    }
                }
                int divisor = k - edger;//gets our divisor
                if(divisor <= 0){divisor = 1;}//set to one so we don't do division by zero or by negative numbers
                arr[i][j].setRed( (redAvg / divisor) );
                arr[i][j].setGreen( (greenAvg / divisor) );
                arr[i][j].setBlue( (blueAvg / divisor) );
            }
        }
        newPic.setImage(arr);
    }

    void grayScale(Image newPic){
        Pixel[][] arr = newPic.getImage();//get the image array
        for(int i = 0; i < rowLength; i++){
            for(int j = 0; j < colLength; j++){//goes through every pixel and inverts the RBG values
                int sum = arr[i][j].getBlue() + arr[i][j].getRed() + arr[i][j].getGreen();
                int average = sum / 3;
                arr[i][j].setRed(average);
                arr[i][j].setBlue(average);
                arr[i][j].setGreen(average);
            }
        }
        newPic.setImage(arr);
    }

    private int rowLength;
    private int colLength;
}
