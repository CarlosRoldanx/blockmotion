package uk.ac.aber.dcs.blockmotion.Model;

/**
 * Extended class from SlideTransformer.
 * It performs the slide transformation horizontally.
 * Created by Carlos Roldan on 03/04/2017.
 */
public class SlideHorizontallyTransformer extends SlideTransformer {
    /**
     * Constructor of the class
     * @param amount refer to the super class Slide Transformer
     */
    public SlideHorizontallyTransformer(int amount) {
        super(amount);
    }

    /**
     * Method to Slide the animations either in the right order
     * or the left order
     * It basically redistributes the order of characters in a
     * temporary frame, in a mathematical way
     * @param frame the frame to transform
     */
    @Override
    public void transform(IFrame frame) {
            int eachOne = Math.abs(amount);

            for(int mainRow = 0; mainRow < frame.getNumRows(); ++mainRow) {
                for(int i = 0; i < eachOne; i++) {
                    char tempor;
                    int thisToGo;
                    if(amount < 0) {
                        tempor = frame.getChar(mainRow, 0);

                        for(thisToGo = 0; thisToGo < frame.getNumRows() - 1; thisToGo++) {
                            frame.setChar(mainRow, thisToGo, frame.getChar(mainRow, thisToGo + 1));
                        }

                        frame.setChar(mainRow, frame.getNumRows() - 1, tempor);
                    } else {
                        tempor = frame.getChar(mainRow, frame.getNumRows() - 1);

                        for(thisToGo = frame.getNumRows() - 1; thisToGo > 0; thisToGo--) {
                            frame.setChar(mainRow, thisToGo, frame.getChar(mainRow, thisToGo - 1));
                        }

                        frame.setChar(mainRow, 0, tempor);
                    }
                }
            }

        }
    }