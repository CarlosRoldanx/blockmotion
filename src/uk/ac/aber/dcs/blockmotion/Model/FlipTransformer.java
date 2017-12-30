package uk.ac.aber.dcs.blockmotion.Model;

/**
 * Abstract class inherited from Transformer.
 * it carries out flip transformations
 * Created by Carlos Roldan on 03/04/2017.
 */
public abstract class FlipTransformer implements Transformer
{
    public FlipTransformer() {}

    /**
     * method to flip the animation
     * either vertically or horizontally
     * @param frame the frame to transform
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
        frame.replace(tempFrame);
    }

    /**
     * method to distribute the location of the characters
     * @param first is the original Frame
     * @param second is the next Frame
     * @param frames is an Integer which represents the number of rows within the Footage
     * @param rows is an Integer which represents the number of columns within the Footage
     */
    public abstract void distribution(Frame first, Frame second, int frames, int rows);
}

