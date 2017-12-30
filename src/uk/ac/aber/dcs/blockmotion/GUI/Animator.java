package uk.ac.aber.dcs.blockmotion.GUI;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import uk.ac.aber.dcs.blockmotion.Model.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.util.Scanner;

/**
 * Main class that runs the animation
 * and performs multiple important functions within the program.
 * Created by Carlos Roldan on 03/04/2017.
 */
public class Animator extends Application implements EventHandler<ActionEvent> {
    private String dataFile = "";
    private String fileName;
    private Button[][] gridArray;
    private GridPane grid;
    private Thread animation;
    private boolean doRun;
    private Footage footage;
    private Stage stage;
    private Stage firstMenuStage;
    private Stage secondMenuStage;
    private Stage initialStage;
    private Scene scene;
    private Scanner in;
    private Transformer flipVerticalTransformer;
    private Transformer flipHorizontalTransformer;
    private Transformer slideVerticallyDownTransformer;
    private Transformer slideHorizontallyRightTransformer;
    private Transformer slideHorizontallyLeftTransformer;
    private Transformer slideVerticallyUpTransformer;
    private int slideHorizontalLeftNum = -1;
    private int slideHorizontalRightNum = 1;
    private int slideVerticalUpNum = -1;
    private int slideVerticalDownNum = 1;
    private int inputAmountInt;
    private Button buttonForLoad;
    private Button buttonForSave;
    private Button buttonForSaveAs;
    private Button buttonForRun;
    private Button buttonForTerminate;
    private Button buttonForEditMenu;
    private Button buttonForTerminalVers;
    private Button buttonForVisualVers;
    private Button buttonForCreateFootage;
    private Button buttonToFlipHor;
    private Button buttonToFlipVer;
    private Button buttonToSlideLeft;
    private Button buttonToSlideRight;
    private Button buttonToSlideUp;
    private Button buttonToSlideDown;
    private Button buttonToSlideLeftInt;
    private Button buttonToSlideRightInt;
    private Button buttonToSlideUpInt;
    private Button buttonToSlideDownInt;
    private Button buttonToSave;
    private Button buttonToExit;
    private ImageView image;
    private Text statusText;
    private TextField textToIntroduce;
//*********************************-------------TERMINAL VERSION------------************************************

    //--------------------------------------MENUS-----------------------------

    /**
     * method to print the first menu
     * in the terminal version
     */
    private void printFirstMenu() {
        System.out.println("l  - Load footage file");
        System.out.println("s  - Save footage file");
        System.out.println("sa - Save as footage file");
        System.out.println("a  - Run footage animation");
        System.out.println("t  - Stop footage animation");
        System.out.println("c  - Create new footage (will save and then replace current footage)");
        System.out.println("e  - Edit current footage (will do a create if there is no current footage)");
        System.out.println("q  - Quit");
    }

    /**
     * method to print the second menu lines from the terminal version
     * this menu is related with the edit menu
     */
    private void printSecondMenu() {
        System.out.println("fh  - Flip  horizontal");
        System.out.println("fv  - Flip  vertical");
        System.out.println("sl  - slideJustOne left");
        System.out.println("sr  - slideJustOne right");
        System.out.println("su  - slideJustOne up");
        System.out.println("sd  - slideJustOne down");
        System.out.println("nr  - slideJustOne right number. Currently = " + slideHorizontalRightNum);
        System.out.println("nl  - slideJustOne left number. Currently = " + Math.abs(slideHorizontalLeftNum));
        System.out.println("nd  - slideJustOne down number. Currently = " + slideVerticalDownNum);
        System.out.println("nu  - slideJustOne up number. Currently = " + Math.abs(slideVerticalUpNum));
        System.out.println("r   - Repeat last operation");
        System.out.println("Q   - Quit editing");
    }

    /**
     *
     */
    private void runMenu() {
        Thread commandLineThread = new Thread() {
            public void run() {
                grid = new GridPane();
                scene = new Scene(grid);
                initialiseTransformations();
                String option = "";
                in = new Scanner(System.in);

                do {
                    printFirstMenu();
                    System.out.println("Enter option: ");
                    option = in.nextLine().toLowerCase();
                    switch (option) {
                        case "a":
                            try {
                                runAnimation();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            break;
                        case "c":
                            createNewFootage();
                            break;
                        case "e":
                            editFootage();
                            break;
                        case "l":
                            loadTerminal();
                            break;
                        case "q":
                            doQuit(in);
                            break;
                        case "s":
                            save();
                            break;
                        case "t":
                            terminateAnimation();
                            break;
                        case "sa":
                            saveAs();
                            break;
                        default:
                            System.out.println("Option " + option + " unknown. Try again!");
                    }
                } while (!option.equals("q"));

                doRun = false;
            }
        };
        commandLineThread.start();
    }

    //--------------------------------------TRANSFORMATIONS-------------------

    /**
     * method to set the slide number to perform the amount
     * for the slide transformation
     *
     * @param which     is a String and it is printed before the selection
     * @param direction is an enum type object and indicates the constant
     *                  direction is going to follow the image
     */
    private void setSlideNumber(String which, Direction direction) {
        boolean loop = true;
        do {
            System.out.println(which);
            try {
                int ime = Math.abs(in.nextInt());
                switch (direction) {
                    case LEFT:
                        slideHorizontalLeftNum = -ime;
                        ((SlideTransformer) slideHorizontallyLeftTransformer).setAmount(slideHorizontalLeftNum);
                        footage.transform(slideHorizontallyLeftTransformer);
                        break;
                    case RIGHT:
                        slideHorizontalRightNum = ime;
                        ((SlideTransformer) slideHorizontallyRightTransformer).setAmount(slideHorizontalRightNum);
                        footage.transform(slideHorizontallyRightTransformer);
                        break;
                    case UP:
                        slideVerticalUpNum = -ime;
                        ((SlideTransformer) slideVerticallyUpTransformer).setAmount(slideVerticalUpNum);
                        footage.transform(slideVerticallyUpTransformer);
                        break;
                    case DOWN:
                        slideVerticalDownNum = ime;
                        ((SlideTransformer) slideVerticallyDownTransformer).setAmount(slideVerticalDownNum);
                        footage.transform(slideVerticallyDownTransformer);
                        break;
                }
                loop = false;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Please type a valid number");
            } finally {
                in.nextLine();
            }
        } while (loop);
    }

    /**
     * method to edit the footage
     * includes a terminal menu with every section
     * to perform one transformation or other
     */
    private void editFootage() {
        String option = "";
        String lastOption = "";
        String msg = "";
        String info = "Enter slide right as an integer";

        do {
            printSecondMenu(); //print menu
            System.out.println("Enter option: ");
            option = in.nextLine().toLowerCase(); //take input
            if (option.equals("r")) {
                if (lastOption.length() > 0) {
                    option = lastOption; //take las option input
                } else {
                    System.out.println("There was no previous option.");
                    continue;
                }

            }
            switch (option) {
                case "fh":
                    if (footage != null) {
                        footage.transform(flipHorizontalTransformer);
                    }
                    break;
                case "fv":
                    if (footage != null) {
                        footage.transform(flipVerticalTransformer);
                    }
                    break;
                case "sl":
                    if (footage != null) {
                        footage.transform(slideHorizontallyLeftTransformer);
                    }
                    break;
                case "sr":
                    if (footage != null) {
                        footage.transform(slideHorizontallyRightTransformer);
                    }
                    break;
                case "su":
                    if (footage != null) {
                        footage.transform(slideVerticallyUpTransformer);
                    }
                    break;
                case "sd":
                    if (footage != null) {
                        footage.transform(slideVerticallyDownTransformer);
                    }
                    break;
                case "nl":
                    msg = "Enter slide left as an integer ";
                    setSlideNumber(msg, Direction.LEFT);
                    break;
                case "nd":
                    msg = "Enter slide left as an integer ";
                    setSlideNumber(msg, Direction.DOWN);
                    break;
                case "nu":
                    msg = "Enter slide up as an integer ";
                    setSlideNumber(msg, Direction.UP);
                    break;
                case "nr":
                    msg = "Enter slide down as an integer ";
                    setSlideNumber(msg, Direction.RIGHT);
                    break;
                default:
                    System.out.println("Option " + option + " unknown. Try again!");
                    break;
            }
            lastOption = option;
        } while (!option.equals("q"));

    }

    //-----------------------------------------SAVES--------------------------

    /**
     * method to save the animation
     * it launches an error if it does not get to save it
     */
    private void save() {
        try {
            save(dataFile);
        } catch (IOException e) {
            System.out.println("Unable to save to file: " + dataFile + ": " + e.getMessage());
        }

    }

    /**
     * method to save the animation with other file name
     * and as a consequence to another file
     */
    private void saveAs() {
        boolean tryAgain = true;

        do {
            System.out.println("Enter fileName: ");
            String filename = in.nextLine();

            try {
                save(filename);
                dataFile = filename;
                tryAgain = false;
            } catch (IOException e) {
                System.out.println("Problem saving file: " + filename + " try again or type Q to quit the option: ");
            }
        } while (tryAgain || dataFile.equals("Q"));

    }

    /**
     * method to save the file into its original file
     *
     * @param filename A string object type that indicates the file name
     * @throws IOException if it does not save the file
     */
    private void save(String filename) throws IOException {
        if (!filename.equals("") && footage != null) {
            footage.save(filename);
        }
    }

    //----------------------------------------LOAD----------------------------

    /**
     * method to load the animation from a text file in the terminal version
     */
    private void loadTerminal() {
        boolean tryAgain = true;

        do {
            System.out.println("Enter fileName: ");
            String filename = in.nextLine();

            try {
                try {
                    save(this.dataFile);
                } catch (IOException e) {
                    System.out.println("Was unable to save file " + dataFile + " before loading. Abandoing" + " the loadTerminal operation. Problem: " + e.getMessage());
                    return;
                }

                footage = new Footage();
                dataFile = filename;
                footage.load(dataFile);
                final int e = footage.getNumRows();
                Platform.runLater(new Runnable() {
                    public void run() {
                        createGrid(e);
                    }
                });
                tryAgain = false;
            } catch (IOException ex) {
                System.out.println("Problem loading file: " + dataFile + " try again or type Q to quit the option: ");
            }
        } while (tryAgain || dataFile.equals("Q"));

    }

    //---------------------------------------MISC-----------------------------

    /**
     * method finish the animation
     * it stops the footage
     */
    private void terminateAnimation() {
        doRun = false;
        Platform.runLater(new Runnable() {
            public void run() {
                stage.setIconified(true);
            }
        });
    }

    /**
     * method to allow the user to save
     * the data when clicks to exit the program
     *
     * @param in is a Scanner object to take input
     */
    private void doQuit(Scanner in) {
        System.out.println("Do you want to save first (y/n)?");
        String answer = in.nextLine();
        if (answer.equals("y")) {
            try {
                if (!dataFile.equals("")) {
                    save(dataFile);
                } else {
                    saveAs();
                }
            } catch (IOException e) {
                System.out.println("Was not able to save to file " + dataFile + " because " + e.getMessage());
            }
        }

        Platform.exit();
    }

    /**
     * method to create a new Footage
     */
    private void createNewFootage() {
        try {
            if (!dataFile.equals("") && footage != null) {
                footage.save(dataFile);
            }
            footage = new Footage();
        } catch (IOException e) {
            System.out.println("Unable to save existing footage. Cannot create new footage. " + e.getMessage());
        }

    }

    /**
     * method to stop the animation
     */
    private void stopAnimation() {
        doRun = false;
    }

    /**
     * Static method main
     *
     * @param args are launched
     */
    public static void main(String[] args) {
        launch(args);
    }

//*************************************-----------------GUI VERSION------------************************************

    //--------------------------------------MENUS-----------------------------

    /**
     * JavaFx method that initially
     * gives the user the option
     * to run the program in a graphical version
     * or a terminal version
     */
    private void initialOptionWindow() {
        initialStage = new Stage(); //creates a stage for the window
        initialStage.setTitle("BLOCKMATION"); //sets the title of the stage
        imageFromAberystwyth();//creates the Images and sets it to the Image Viewer

        Text text = new Text("             Select between the 2 options"); //creates an informative text
        javafx.scene.text.Font font = Font.font("Serif", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 20);
        text.setFont(font);//sets the specific font previously created

        buttonForTerminalVers = new Button("Terminal version"); //creates an interactive button
        buttonForVisualVers = new Button("Visual version"); //creates another button

        buttonForTerminalVers.setOnAction(this); //creates the interaction
        buttonForVisualVers.setOnAction(this); //creates the interaction

        BorderPane root = getStructure(buttonForVisualVers, buttonForTerminalVers,text,image); //creates the structure

        adaptStage(scene,root,120,410,initialStage);//creates a new window
        //sets the minimum height, width and scene, the shows the stage
    }

    /**
     * JavaFX method to show the firsts options
     * in the graphical version of the program
     * @param text is text to be shown in the status bar
     */
    private void mainMenu(String text) {
        firstMenuStage = new Stage();//creates a new stage for the window
        firstMenuStage.setTitle("BLOCKMATION - Menu Option");//sets the tittle of the stage

        Text status = new Text("Status:");
        javafx.scene.text.Font font = Font.font("Serif", FontWeight.BOLD, FontPosture.REGULAR, 16);
        status.setFont(font);//setting the specific font for the text
        statusText = new Text(text);//creating text

        VBox buttonAreaLeft = new VBox(25);//creates a column area of buttons including the spacing amount
        VBox buttonAreaRight = new VBox(25);//creates another column area of buttons
        HBox statusArea = new HBox(10,status, statusText);
        mainButtonsCreation();//method to create the buttons

        buttonAreaLeft.getChildren().addAll(buttonForLoad, buttonForSave, buttonForSaveAs,buttonForCreateFootage);
        buttonAreaRight.getChildren().addAll(buttonForRun, buttonForTerminate, buttonForEditMenu);
        imageFromAberystwyth();

        BorderPane root = getStructure(buttonAreaLeft,buttonAreaRight,statusArea,image);//creating an basic structure

        adaptStage(scene,root,250,550,firstMenuStage);

        saveAsWhenClosed(firstMenuStage);
    }

    /**
     * JavaFX method to edit the footage that
     * includes a visual menu with every section
     * to perform one transformation or other
     */
    private void editWindow() {
        initialiseTransformations();
        secondMenuStage = new Stage();
        secondMenuStage.setTitle("BLOCKMATION - EDIT MENU");
        textToIntroduce = new TextField();
        textToIntroduce.setPromptText("Type a number to slide");

        creationOfEditButtons();
        imageFromAberystwyth();
        Text text = new Text("                        " +
                "                                   Close the Edit Menu to go BACK");

        Text textForTextField = new Text("Write down the number to slide ");
        VBox buttonAreaLeft = new VBox(25);
        buttonAreaLeft.getChildren().addAll(buttonToFlipHor, buttonToFlipVer, buttonToSlideLeft, buttonToSlideUp, buttonToSlideRight);
        VBox buttonAreaRight = new VBox(25);
        buttonAreaRight.getChildren().addAll(buttonToSlideDown,buttonToSlideLeftInt, buttonToSlideRightInt,buttonToSlideUpInt,buttonToSlideDownInt, textForTextField, textToIntroduce);
        BorderPane root = getStructure(buttonAreaLeft,buttonAreaRight,text,image);//creating an basic structure

        adaptStage(scene, root,400,600, secondMenuStage);

        goBackToVisualMenu(secondMenuStage);
    }

    /**
     * JavaFX methrod that creates a basic structure and returns it
     * @param node1 is a node object type and is set at the left of the structure
     * @param node2 is a node object type and is set at the right of the structure
     * @param node3 is a node object type and is set at the top of the structure
     * @param node4 is a node object type and is set at the top of the structure
     * @return the structure
     */
    private BorderPane getStructure(Node node1, Node node2, Node node3, Node node4) {
        BorderPane Structure = new BorderPane(); //creates an easy structure
        Structure.setLeft(node1); //sets node1 at the left
        Structure.setRight(node2);//sets node2 at the right
        Structure.setTop(node3);//sets node3 up to the top
        Structure.setCenter(node4);//sets node4 at the center
        return Structure;
    }

    /**
     * JavaFX method that allows to go back to the
     * main menu when the user click to exit the
     * edit menu
     * @param stage indicates the current stage to be closed
     */
    private void goBackToVisualMenu(Stage stage) {
        stage.setOnCloseRequest((evt) -> { //if close the stage
            evt.consume();
            stage.close(); //closes the current stage
            mainMenu("Edition finished"); // and it opens up the primary menu
        });
    }

    /**
     * JavaFX method that modifies and adapts a stage
     * @param scene is a Scene object type that is set to the Stage
     * @param structure is a BorderPane variable that indicates the structure of the Scene
     * @param height indicates the Scene height and the minimum height
     * @param width indicates the Scene width and its minimum
     * @param stage allows the window to be seen and sets the Scene to the Stage
     */
    private void adaptStage(Scene scene, BorderPane structure, int height, int width, Stage stage) {
        scene = new Scene(structure, width, height);
        stage.setScene(scene);
        stage.setMinHeight(height);
        stage.setMinWidth(width);
        stage.show();
    }

    //---------------------------------------BUTTONS--------------------------

    /**
     * JavaFX method to create the buttons for the first graphical menu
     */
    private void mainButtonsCreation() {
        //----------------creation of interactive buttons with text-----------------
        buttonForLoad = new Button("Load footage file");
        buttonForSave = new Button("Save footage file");
        buttonForSaveAs = new Button("Save as footage file");
        buttonForCreateFootage = new Button("Create footage");
        buttonForRun = new Button("Run footage animation");
        buttonForTerminate = new Button("Stop footage animation");
        buttonForEditMenu = new Button("Edit current footage");
        //---------------setting the interaction of the buttons---------------------
        interactionMainButtonsFirstColumn();
        interactionMainButtonsSecondColumn();
    }

    /**
     * JavaFX method that creates the interaction
     * of the first main column of buttons
     */
    private void interactionMainButtonsFirstColumn() {
        buttonForLoad.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                loadGUI();
                statusInfo("Footage NOT Loaded", "Footage " + dataFile.toUpperCase() + " successfully Loaded and ready to run");}});
        buttonForSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                save();
                statusInfo("Footage NOT Loaded", "Footage " + dataFile.toUpperCase() + " successfully saved");}});
        buttonForSaveAs.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (dataFile.equals("")) {
                    statusText.setText("Footage not even loaded");
                }else{
                    saveAsGUI("");
                    statusText.setText(dataFile + " Saved!");}}});
        buttonForCreateFootage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    if (!dataFile.equals("") && footage != null) {
                        footage.save(dataFile);}
                    footage = new Footage();
                } catch (IOException e) {}}});
    }

    /**
     * JavaFX method that creates the interaction
     * of the second main column of buttons
     */
    private void interactionMainButtonsSecondColumn() {
        buttonForRun.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    runAnimation();
                    statusInfo("Footage NOT LOADED!!", "Animation running");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();}}
        });
        buttonForTerminate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                terminateAnimation();
                statusInfo("Footage not loaded","Animation stopped");}
        });
        buttonForEditMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (dataFile.equals("")) {
                    statusText.setText("Load the footage first to edit it");
                } else {
                    editWindow();
                    firstMenuStage.close();}}
        });
    }

    /**
     * method to perform different actions
     * when a buttons are clicked
     *
     * @param event represents when the user performs an action
     */
    @Override
    public void handle(javafx.event.ActionEvent event) {
        if (event.getSource() == buttonForVisualVers) {
            initialStage.close();
            mainMenu("Welcome, this status bar shows the status of the program at any moment");
        } else if (event.getSource() == buttonToSave) {
            try {
                fileName = textToIntroduce.getText();
                save(fileName);
                System.exit(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (event.getSource() == buttonToExit) {
            System.exit(0);
        } else if (event.getSource() == buttonToSlideRight) {
            if (footage != null) {
                footage.transform(slideHorizontallyRightTransformer);
            }
        }
    }

    /**
     * method that creates the buttons and assigns the interaction of them
     */
    private void creationOfEditButtons() {
        buttonToFlipHor = new Button("Flip  horizontal");
        buttonToFlipVer = new Button("Flip  vertical");
        buttonToSlideLeft = new Button("Slide left");
        buttonToSlideRight = new Button("Slide right");
        buttonToSlideUp = new Button("Slide up");
        buttonToSlideDown = new Button("Slide down");
        buttonToSlideLeftInt = new Button("Slide left number. Currently = " + slideHorizontalLeftNum);
        buttonToSlideRightInt = new Button("Slide right number. Currently = " + Math.abs(slideHorizontalRightNum));
        buttonToSlideUpInt = new Button("Slide up number. Currently = " + slideVerticalUpNum);
        buttonToSlideDownInt = new Button("Slide Down number. Currently = " + Math.abs(slideVerticalDownNum));

        interactionEditButtons();
        interactionSlideButtons();
        interactionSlideNumButtons();
    }

    /**
     * JavaFX method that creates the interaction
     * of the buttons that slide the image with an
     * amount introduced by the user
     */
    private void interactionSlideNumButtons() {
        buttonToSlideLeftInt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                slideWithInteger(slideHorizontallyLeftTransformer,slideHorizontalLeftNum);
            }
        });
        buttonToSlideRightInt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                slideWithInteger(slideHorizontallyRightTransformer, slideHorizontalRightNum);
            }
        });
        buttonToSlideUpInt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                slideWithInteger(slideVerticallyUpTransformer, slideVerticalUpNum);
            }
        });
        buttonToSlideDownInt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                slideWithInteger(slideVerticallyDownTransformer, slideVerticalDownNum);
            }
        });
    }

    /**
     * JavaFX method that creates the interaction of
     * the flips Buttons + the slide left amount
     */
    private void interactionEditButtons() {
        buttonToFlipHor.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (footage != null) {
                    footage.transform(flipHorizontalTransformer);
                }
            }
        });
        buttonToFlipVer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (footage != null) {
                    footage.transform(flipVerticalTransformer);
                }
            }
        });
        buttonToSlideLeft.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                slideJustOne(slideHorizontallyLeftTransformer, -1);
            }
        });
    }

    /**
     * JavaFX method that creates the interaction
     * of the Slide Buttons
     */
    private void interactionSlideButtons() {
        buttonToSlideRight.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                slideJustOne(slideHorizontallyRightTransformer,1);
            }
        });
        buttonToSlideUp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                slideJustOne(slideVerticallyUpTransformer,-1);
            }
        });
        buttonToSlideDown.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                slideJustOne(slideVerticallyDownTransformer, 1);
            }
        });
    }

    //--------------------------------------TRANSFORMATIONS-------------------

    /**
     * JavaFX method that slides the image more thant just one step
     * @param transformer is the transformer, like the direction
     * @param amount is how much is going to be slided
     */
    private void slideWithInteger(Transformer transformer, int amount) {
            inputAmountInt = Integer.parseInt(textToIntroduce.getText());

            if (transformer == slideHorizontallyLeftTransformer || transformer == slideVerticallyUpTransformer) {
                amount = -inputAmountInt;
            }if (transformer == slideVerticallyDownTransformer || transformer == slideHorizontallyRightTransformer) {
                amount = inputAmountInt;
        }
            ((SlideTransformer) transformer).setAmount(amount);
            footage.transform(transformer);
    }

    /**
     * JavaFX method that slide the image just one step
     * @param transformer is the transformer, like the direction
     * @param amount is how much is going to be slided
     */
    private void slideJustOne(Transformer transformer, int amount){
        if (footage != null) {
            ((SlideTransformer) transformer).setAmount(amount);
            footage.transform(transformer);
        }
    }

    /**
     * method that initialize the transformations objects
     */
    private void initialiseTransformations() {
        flipVerticalTransformer = new FlipVerticalTransformer();
        flipHorizontalTransformer = new FlipHorizontalTransformer();
        slideVerticallyDownTransformer = new SlideVerticallyTransformer(slideVerticalDownNum);
        slideVerticallyUpTransformer = new SlideVerticallyTransformer(slideVerticalUpNum);
        slideHorizontallyRightTransformer = new SlideHorizontallyTransformer(slideHorizontalRightNum);
        slideHorizontallyLeftTransformer = new SlideHorizontallyTransformer(slideHorizontalLeftNum);
    }

    //-----------------------------------------SAVES--------------------------

    /**
     * JavaFX method that creates a window to allow
     * the user to save the footage with a different
     * file name
     * @param text is a text that appears in this window
     *             when it is closed, so code duplication
     *             is avoided
     */
    private void saveAsGUI(String text){
        textToIntroduce = new TextField();
        textToIntroduce.setPromptText("type here...");
        Text textToShow = new Text(text);
        Text secondText = new Text("Enter file name here");
        HBox saveArea = new HBox(20, secondText, textToIntroduce);
        initialStage = new Stage();
        initialStage.setTitle("BLOCKMATION - SAVE AS");
        imageFromAberystwyth();

        buttonToSave = new Button("save");
        buttonToSave.setOnAction(this);
        buttonToExit = new Button("Exit");
        buttonToExit.setOnAction(this);

        HBox buttonArea = new HBox(15, buttonToSave, buttonToExit);
        BorderPane root = getStructure(saveArea, buttonArea, textToShow,image);

        adaptStage(scene,root,100,650,initialStage);

        stage.setOnCloseRequest((evt) -> { //if close the stage
            evt.consume();
            firstMenuStage.close();
            stage.close();
        });
    }

    /**
     * JavaFX method that creates a window when the user tries
     * to close the program to offer the opportunity
     * to save the footage
     * @param stage indicates the current stage to be closed
     */
    private void saveAsWhenClosed(Stage stage) {
        stage.setOnCloseRequest((evt) -> { //if close the stage
            evt.consume();
            saveAsGUI("Do you want to save the file before closing the program?");
        });
    }

    //----------------------------------------LOAD----------------------------

    /**
     * JavaFX method to load a footage from a text file
     * through a visual interface by using JFilechooser
     */
    private void loadGUI() {
        footage = new Footage();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Open file to Load the Animation"); //sets the title of the stage
        FileNameExtensionFilter filterForTxt = new FileNameExtensionFilter("Text files", "txt");
        fileChooser.setFileFilter(filterForTxt);//reduce the filter to only .txt files
        if (fileChooser.showOpenDialog(fileChooser) == JFileChooser.APPROVE_OPTION) {//open the file chooser
            try {
                dataFile = fileChooser.getSelectedFile().getName();//gets the name of the file
                File file = fileChooser.getSelectedFile().getAbsoluteFile();//creates a file with the selected file
                footage.load(String.valueOf(file)); //load the string value of the file
                final int e = footage.getNumRows(); //gets a constatn number of rows of the footage from that file
                Platform.runLater(new Runnable() {
                    public void run() {
                        createGrid(e);
                    } //runs the animation
                });
            } catch (Exception error) {
                error.printStackTrace(); //return an error when needed
            }
        }

    }

    //-------------------------------------STATUS BAR-------------------------

    /**
     * prints a status message to the user if the footage is either loaded or not
     * @param noFootageMessage is a message if the footage is not loaded
     * @param yesFootageMessage is a message if the footage is loaded
     */
    private void statusInfo(String noFootageMessage, String yesFootageMessage) {
        if (dataFile.equals("")) {
            statusText.setText(noFootageMessage);
        } else {
            statusText.setText(yesFootageMessage);
        }
    }

    //---------------------------------------MISC-----------------------------

    /**
     * JavaFX that creates an image of Aberystwyth as part of flairs
     */
    private void imageFromAberystwyth() {
        image = new ImageView(); //creates the Image Viewer
        Image img = new Image("file:aber.png");//creates the image from the file link
        image.setImage(img);//sets the image to the Image Viewer
    }

    /**
     * JavaFX method to run the animation
     * before running the animation the
     * footage must be loaded
     *
     * @throws FileNotFoundException if the file is not found
     */
    private void runAnimation() throws FileNotFoundException {
        if (footage == null) {
            System.out.println("Load the footage first");
            statusText.setText("Load the footage first");

        } else {
            Runnable animationToRun = () -> {

                Background yellowBg = new Background(new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY));
                Background blackBg = new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY));
                Background blueBg = new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY));
                Background whiteBg = new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY));
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        stage.setIconified(false);
                    }
                });
                doRun = true;
                int numFrames = footage.getNumFrames();
                int currentFrameIndex = 0;
                Background bck = null;
                while (doRun) {
                    if (currentFrameIndex >= numFrames - 1) currentFrameIndex = 0;
                    IFrame currentFrame = footage.getFrame(currentFrameIndex);
                    // Iterate through the current frame displaying appropriate colour
                    for (int row = 0; row < footage.getNumRows(); row++) {
                        for (int col = 0; col < footage.getNumRows(); col++) {
                            switch (currentFrame.getChar(row, col)) {
                                case 'l':
                                    bck = yellowBg;
                                    break;
                                case 'r':
                                    bck = blackBg;
                                    break;
                                case 'b':
                                    bck = blueBg;
                                    break;
                                default:
                                    bck = whiteBg;
                            }
                            final int theRow = row;
                            final int theCol = col;
                            final Background backgroundColour = bck;
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    gridArray[theRow][theCol].setBackground(backgroundColour);
                                }
                            });

                        }
                    }
                    try {
                        // This is how we delay for 200th of a second
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                    }
                    currentFrameIndex++;
                }
            };
            animation = new Thread(animationToRun);
            animation.start();
        }
    }

    /**
     * JavaFX method to create a grid scene
     * and show the stage
     *
     * @param numRows is an integer that indicates the
     *                amount of rows from the animation
     */
    private void createGrid(int numRows) {
        grid = new GridPane();
        gridArray = new Button[numRows][numRows];
        Button displayButton = null;

        for (int row = 0; row < numRows; ++row) {
            for (int col = 0; col < numRows; ++col) {
                displayButton = new Button();
                gridArray[row][col] = displayButton;
                displayButton.setDisable(true);
                grid.add(displayButton, col, row);
            }
        }

        scene = new Scene(grid);
        stage.setScene(scene);
        scene.setRoot(grid);
        stage.setTitle("Current Animation");

        stage.show();
        stage.setMaxWidth(stage.getWidth());
        stage.setMaxHeight(stage.getHeight());
        stage.setMinWidth(stage.getWidth());
        stage.setMinHeight(stage.getHeight());
    }

    /**
     * JavaFX Start method for JavaFX
     *
     * @param primaryStage represents the Main Stage
     */
    public void start(Stage primaryStage) {
        stage = primaryStage;
        grid = new GridPane();
        scene = new Scene(grid);
        primaryStage.setScene(scene);
        scene.getStylesheets().add(Animator.class.getResource("styling.css").toExternalForm());
        initialOptionWindow();
        buttonForTerminalVers.setOnAction(new EventHandler<ActionEvent>() {
           @Override
           public void handle(ActionEvent event) {
               runMenu();
           }
       });
    }
//-----------------------------------------------------------------------------------
}
