package uk.ac.aber.dcs.blockmotion.Model;

/**
 * Extended class from SlideTransformer.
 * It performs the slide transformation vertically.
 * Created by Carlos Roldan on 03/04/2017.
 */
public class SlideVerticallyTransformer extends SlideTransformer {
    /**
     * constructor of the class
     * @param amount refers to the super class SlideTransformer
     */
    public SlideVerticallyTransformer(int amount) {
        super(amount);
    }

    /**
     * Method to Slide the animations either in ascending order
     * or descending order
     * It basically redistributes the order of characters in a
     * temporary frame, in a mathematical way
     * @param frame the frame to transform
     */
    @Override
    public void transform(IFrame frame) {
        int eachOne = Math.abs(amount);

        for(int mainColumn = 0; mainColumn < frame.getNumRows(); mainColumn++) {
            for(int i = 0; i < eachOne; i++) {
                char temporary;
                int thisToGo;

                if(this.amount < 0) {
                    temporary = frame.getChar(0, mainColumn);

                    for(thisToGo = 0; thisToGo < frame.getNumRows() - 1; thisToGo++) {
                        frame.setChar(thisToGo, mainColumn, frame.getChar(thisToGo + 1, mainColumn));
                    }

                    frame.setChar(frame.getNumRows() - 1, mainColumn, temporary);
                } else {
                    temporary = frame.getChar(frame.getNumRows() - 1, mainColumn);

                    for(thisToGo = frame.getNumRows() - 1; thisToGo > 0; thisToGo--) {
                        frame.setChar(thisToGo, mainColumn, frame.getChar(thisToGo - 1, mainColumn));
                    }

                    frame.setChar(0, mainColumn, temporary);
                }
            }
        }
    }
}

