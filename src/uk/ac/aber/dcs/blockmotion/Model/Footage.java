package uk.ac.aber.dcs.blockmotion.Model;

import java.io.*;
import java.util.ArrayList;

/**
 * Inherited class from IFootage.
 * Represents the animation footage.
 * Created by Carlos Roldan on 03/04/2017.
 */
public class Footage implements IFootage {
    private int numFrames;
    private String fileName;
    private int rows;
    private ArrayList<Frame> frames = new ArrayList();

    /**
     * constructor of Footage
     * It initializes an array of Frames
     * @param rows is the amount of rows for every frame
     */
    public Footage(int rows) {
        this.rows = rows;
        frames = new ArrayList<>();
    }

    /**
     * constructor of Footage
     * It initializes an array of Frames
     */
    public Footage() {
        frames = new ArrayList<>();
    }

    /**
     * method to return number of frames
     * @return number of frames as an integer
     */
    @Override
    public int getNumFrames() {
        return numFrames;
    }

    /**
     * method to set number of frames
     * @param numFrames sets the new number of frames as an integer
     */
    public void setNumFrames(int numFrames) {
        this.numFrames = numFrames;
    }

    /**
     * method to set the number of rows
     * @param rows sets the new number
     * of rows as an integer
     */
    public void setRows(int rows) {
        this.rows = rows;
    }

    /**
     * method the get the number of Rows
     * @return the amount of Rows as an integer
     */
    @Override
    public int getNumRows() {
        return rows;
    }

    /**
     * method to get a specific frame
     * @param num the position of the frame starting from 0
     * @return the frame
     */
    @Override
    public IFrame getFrame(int num) {
        return (Frame) frames.get(num);

    }

    /**
     * method to add a frame into the Arraylist frames
      * @param f the frame to add
     */
    @Override
    public void add(IFrame f) {
        frames.add((Frame)f);
        numFrames = frameSize();
    }

    /**
     * method to load a footage from a file
     * @param fn the file name
     * @return the number of frames as a String + the amount of rows as a String
     * @throws IOException and prinsts the message error if not loaded
     */
    @Override
    public String load(String fn) throws IOException {
        frames.clear(); //clears the array
        BufferedReader readIn = new BufferedReader(new InputStreamReader(new FileInputStream(fn)));

        try {
            numFrames = Integer.parseInt(readIn.readLine()); //converts numframes to String
            rows = Integer.parseInt(readIn.readLine());//converts number of rows to String

            for(int e = 0; e < numFrames; ++e) { //for every number of frames
                Frame temporaryFrame = new Frame(rows);//creates a mew frame
                if(!addition(temporaryFrame, readIn)) { //if its true it breaks
                    break;
                }
                frames.add(temporaryFrame);//add the temporary frame to the Arraylist of Frames
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(readIn != null) {
                readIn.close(); //close the reader
            }

        }
        return numFrames + String.valueOf(rows); //return the number of Frames and number of Rows as a String
    }

    /**
     * method to get the size of the Arraylist frames
     * @return the size as an Integer of the Arraylist frames
     */
    public int frameSize() {
        numFrames = frames.size();//number of frames becomes equal to the size of the frames
        return frames.size();//then it is returned
    }

    /**
     * method to fill one when is True
     * @param frame to insert the String [] Lines into the Frame
     * @param br reads the line
     * @return the true state of the boolean when inserts the lines in the selected Frame
     */
    public boolean addition(Frame frame, BufferedReader br) {
        String[] lines = new String[rows]; //initialise an Array with the size of the rows

        try {
            for(int e = 0; e < rows; ++e) { //for each row
                lines[e] = br.readLine();//reads the line in the array
            }

            frame.insertLines(lines); //insert the lines in the selected frame
            return true; //makes it true
        } catch (Exception e) {
            e.printStackTrace();
            return false; //otherwise it will be false
        }
    }

    /**
     * method to save the footage
     * @param fn the file name
     * @throws IOException and prints the error message
     */
    @Override
    public void save(String fn) throws IOException {
        fileName = fn;//Globally String fileName becomes equal to the selected String
        PrintWriter outfile = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName)));//creates a printwriter

        try {
            outfile.println(numFrames); //prints into the file the number of Frames
            outfile.println(rows); //prints into the file the number of Rows

            for(int i = 0; i < numFrames; ++i) { //for every Frame
                Frame frame = frames.get(i); //get the number of frames
                frame.toFile(outfile);//print the frame
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(outfile != null) {
                try {
                    outfile.close();//close the print writer
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                outfile.close(); //close the print writer
            }

        }

    }

    /**
     * method to transform the footage
     * @param transformer the transformer goes for each loop
     */
    @Override
    public void transform(Transformer transformer) {
        for (Frame f: frames) { // for each frame
            transformer.transform(f); //execute a transformation
        }

    }

    /**
     * toString method to print all the frames
     * @return s
     */
    @Override
    public String toString() {
        String s = "frames";
        for (Frame f : frames)
            s = s + f.toString() + "\n\n\n**********************\n\n\n";
        return s;
    }
}
