package uk.ac.aber.dcs.blockmotion.Model;

import java.io.PrintWriter;


/**
 * Inherited class from IFrame.
 * Represents a single frame made out of characters.
 * Created by Carlos Roldan on 03/04/2017.
 */
public class Frame implements IFrame {
    private char[][] code;
    private int numRows;

    /**
     * Constructor
     * @param nR becomes equal to numRows
     */
    public Frame(int nR) {
        numRows = nR;
        code = new char[numRows][numRows];
    }

    /**
     * Method to insert lines
     * lineToAnimate becomes equal to code[i][o]
     * @param lines an array of lines
     */
    @Override
    public void insertLines(String[] lines) {
        for(int row = 0; row < numRows; row++) { //for every line
            for (int col = 0; col < numRows; col++) {//for every line
                char lineToAnimate = lines[row].charAt(col);
                code[row][col] = lineToAnimate;
            }
        }
    }

    /**
     * method to get number of rows
     * @return number of rows
     */
    @Override
    public int getNumRows() {
            return numRows;
        }

    /**
     * method to print the characters in a file
     * @param outfile the open file to write to
     */
    @Override
    public void toFile(PrintWriter outfile) {
        for(int row = 0; row < numRows; ++row) { //for every line
            String lineToGo = "";
            for(int col = 0; col< numRows; ++col) { //for every line
                lineToGo = lineToGo + code[row][col];
            }
            outfile.println(lineToGo); //prints the actual line
        }

    }

    /**
     * method to return the characters
     * @param i the row
     * @param j the column
     * @return code[i][o]
     */
    @Override
    public char getChar(int i, int j) {
            return code[i][j];
        }

    /**
     * method to set new character
     * @param i the row
     * @param j the column
     * @param ch the character to set
     */
    @Override
    public void setChar(int i, int j, char ch) {
            code[i][j] = ch;
        }

    /**
     * method to copy the frame
     * @return the new frame once copied
     */
    @Override
    public IFrame copy() {
        Frame frame = new Frame(numRows);
        for(int i = 0; i < numRows; i++) {
            for(int o = 0; o < numRows; o++) {
                code[i][o] = code[i][o]; //makes it equal
            }
        }
        return (IFrame) frame;
    }

    /**
     * method to replace the
     * @param f the frame to insert
     */
    @Override
    public void replace(IFrame f) {
        for(int row = 0; row < numRows; row++) {//for every line
            for(int col = 0; col < numRows; col++) {//for every line
                ((Frame)f).code[row][col] = code[row][col];//replace the code for the Frame parameter one
            }
        }
    }

    /**
     * method to return information about the frames
     * @return s
     */
    @Override
    public String toString()
    {
        String s = "fr\n";
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numRows; j++)
                s = s + code[i][j];
            s = s + "\n";
        }
        return s;
    }

}


