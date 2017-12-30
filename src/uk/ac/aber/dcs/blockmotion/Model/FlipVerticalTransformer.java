package uk.ac.aber.dcs.blockmotion.Model;

/**
 * Extended class from FlipTransformer.
 * It performs the flip transformation horizontally.
 * Created by Carlos Roldan on 03/04/2017.
 */
public class FlipVerticalTransformer extends FlipTransformer {
    public FlipVerticalTransformer() {}

    /**
     * method to distribute the location of the characters
     * @param first is the original Frame
     * @param second is the next Frame
     * @param row is an Integer which represents the number of rows within the Footage
     * @param col is an Integer which represents the number of columns within the Footage
     */
    public void distribution(Frame first, Frame second, int row, int col) {
        char temp = first.getChar(row, col); //get the char from the parameter row and col
        first.setChar(row, col, first.getChar(-row + first.getNumRows() - 1, col)); //sets the new characters in the frame From
        second.setChar(-row + first.getNumRows() - 1, col, temp);//sets the new characters in the Frame to
    }

    /**
     * method to transform the animation
     * tempFrame is a temporal Frame
     * @param frame is replaced by tempFrame
     */
    @Override
    public void transform(IFrame frame) {
        Frame tempFrame = new Frame(frame.getNumRows()); //creates new Frame with new number of rows
        int numRows = frame.getNumRows(); //numrows becomes equal to the frows in the parameter frame

        for(int row = 0; row < numRows; ++row) {
            for(int col = 0; col < numRows; ++col) {
                distribution((Frame) frame, tempFrame, row, col);
            }
        }

        tempFrame.replace(frame);//replace the original frame with the frame created within the method
    }
}
