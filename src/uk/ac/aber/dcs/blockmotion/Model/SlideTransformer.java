package uk.ac.aber.dcs.blockmotion.Model;

/**
 * Abstract class inherited from Transformer.
 * It carries out the side transformations.
 * Created by Carlos Roldan on 03/04/2017.
 */
public abstract class SlideTransformer implements Transformer {
    int amount;

    /**
     * Constructor that initialises and integer
     * @param amount is an integer that represents the amount to be slided
     */
    public SlideTransformer(int amount) {
        this.amount = amount;
    }

    /**
     * Get method that returns the int amount
     * @return the amount of times to be slided
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Set method that sets the int amount
     * @param amount is set
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }
}
