package uk.ac.aber.dcs.blockmotion.Model;

/**
 * Extended class from FlipTransformer.
 * It performs the flip transformation horizontally.
 * Created by Carlos Roldan on 03/04/2017.
 */
public class FlipHorizontalTransformer extends FlipTransformer {

    public FlipHorizontalTransformer() {}
    /**
     * method to distribute the location of the characters
     * @param first is the original Frame
     * @param second is the next Frame
     * @param row is an Integer which represents the number of rows within the Footage
     * @param col is an Integer which represents the number of columns within the Footage
     */
    public void distribution(Frame first, Frame second, int row, int col)
    {
        char temp = first.getChar(row, col);
        first.setChar(row, col, first.getChar(row, -col + first.getNumRows() - 1));
        second.setChar(row, -col + first.getNumRows() - 1, temp);
    }
    /**
     * method to transform the animation
     * tempFrame is a temporal Frame
     * @param frame is replaced by tempFrame
     */
    @Override
    public void transform(IFrame frame) {
        Frame tempFrame = new Frame(frame.getNumRows());
        int numRows = frame.getNumRows();
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numRows; col++) {
                distribution((Frame)frame, tempFrame, row, col);
            }
        }
        tempFrame.replace(frame);
    }
}