/**
 * Created by tyler on 1/11/2017.
 */

public class Image {
    public Image(){}
    public Image(int height, int width){
        numOfCols = width;
        numOfRows = height;
        image = new Pixel[height][width];
        indexOfNextRow = 0;
        indexOfNextCol = 0;
    }
    //here we keep track of the next available spot in the array as we push back pixels
    void push_back(Pixel newPixel){
        try {
            if(indexOfNextCol < numOfCols && indexOfNextRow < numOfRows) {
                image[indexOfNextRow][indexOfNextCol] = newPixel;
                ++indexOfNextCol;//increment
                //checking to see if we are at the end of a row of pixels
                if (indexOfNextCol == numOfCols) {
                    ++indexOfNextRow;//incremented only after we do max number of columns
                    indexOfNextCol = 0;//reset to zero
                }
            }
        }catch(NullPointerException e){
            System.out.println(e.getMessage().toString());
        }
    }
    //getters
    int getNumOfCols(){
        return this.numOfCols;
    }
    int getNumOfRows(){
        return this.numOfRows;
    }
    Pixel[][] getImage(){
        return this.image;
    }
    //setter
    void setImage(Pixel[][] newImage) {this.image = newImage;}

    private Pixel[][] image;
    private int numOfCols;
    private int numOfRows;
    private int indexOfNextRow;
    private int indexOfNextCol;
}
