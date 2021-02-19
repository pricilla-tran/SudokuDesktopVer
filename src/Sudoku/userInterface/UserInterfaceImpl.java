package Sudoku.userInterface;

import Sudoku.problemdomain.Coordinates;
import Sudoku.constants.GameState;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import Sudoku.problemdomain.SudokuGame;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.util.HashMap;

//Manages the window, and displays a pop up notification when the user completes the puzzle.
public class UserInterfaceImpl implements IUserInterfaceContract.View,

        EventHandler<KeyEvent> {

    private final Stage stage;
    private final Group root;

    // using Hashnap to keep track of all 81 different text fields
    //This HashMap stores the Hash Values (a unique identifier which is automatically generated;
    // see java.lang.object in the documentation) of each TextField by their Coordinates. When a SudokuGame
    //is given to the updateUI method, we iterate through it by X and Y coordinates and assign the values to the
    //appropriate TextField therein. This means we don't need to hold a reference variable for every god damn
    //text field in this app; which would be awful.
    //The Key (<Key, Value> -> <Coordinates, Integer>) will be the HashCode of a given InputField for ease of lookup

    private HashMap<Coordinates, SudokuTextField> textFieldCoordinates;

    // EventLister is the controller/presenter, handles events
    private IUserInterfaceContract.EventListener listener;

    private static final double WINDOW_Y = 732;
    private static final double WINDOW_X = 668;
    private static final double BOARD_PADDING = 50;
    private static final double BOARD_X_AND_Y = 576;

    private static final Color WINDOW_BACKGROUND_COLOUR = Color.rgb(0,150,136);
    private static final Color BOARD_BACKGROUND_COLOUR = Color.rgb(224,242,241);
    // This is for the title
    private static final String SUDOKU = "Sudoku";

    // Constructor
    public UserInterfaceImpl(Stage stage){
        this.stage = stage;
        this.root = new Group();
        this.textFieldCoordinates = new HashMap<>();
        initializeUserInterface();
    }

    private void initializeUserInterface() {
        drawBackground(root);
        drawTitle(root);
        drawSudokuBoard(root);
        drawTextFields(root);
        drawGridLines(root);
        stage.show();
    }

    private void drawGridLines(Group root) {
        int xandY = 114;
        int index = 0;
        while (index < 8) {
            int thickness;
            if (index == 2 || index == 5){
                thickness = 3;
            }
            else {
                thickness = 2;
            }

            Rectangle verticalLine = getLine(
                 xandY + 64 * index,
                 BOARD_PADDING,
                 BOARD_X_AND_Y,
                 thickness
            );

            Rectangle horizontalLine = getLine(
                    BOARD_PADDING,
                    xandY + 64 * index,
                    thickness,
                    BOARD_X_AND_Y
            );

            root.getChildren().addAll(verticalLine, horizontalLine);

            index++;
        }
    }

    public Rectangle getLine(double x, double y, double height, double width) {
        Rectangle line = new Rectangle();

        line.setX(x);
        line.setY(y);
        line.setHeight(height);
        line.setWidth(width);
        line.setFill(Color.BLACK);

        return line;
    }

    private void drawTextFields(Group root) {
        final int xOrigin = 50;
        final int yOrigin = 50;

        // Change in x and y value
        final int XandYDelta = 64;

        // O(n^2) Runtime Complexity
        for (int xIndex = 0; xIndex < 9; xIndex++){
            for (int yIndex = 0; yIndex < 9; yIndex++) {
                int x = xOrigin + xIndex * XandYDelta;
                int y = yOrigin + yIndex * XandYDelta;
                //draw it
                SudokuTextField tile = new SudokuTextField(xIndex, yIndex);

                styleSudokuTile(tile,x,y);

                //Note: Note that UserInterfaceImpl implements EventHandler<ActionEvent> in the class declaration.
                //By passing "this" (which means the current instance of UserInterfaceImpl), when an action occurs,
                //it will jump straight to "handle(ActionEvent actionEvent)" down below.
                tile.setOnKeyPressed(this);

                textFieldCoordinates.put(new Coordinates(xIndex, yIndex), tile);

                root.getChildren().add(tile);

            }
        }

    }

    /**
     * Helper method for styling a sudoku tile number
     * @param tile
     * @param x
     * @param y
     */

    private void styleSudokuTile(SudokuTextField tile, double x, double y) {
        Font numberFont = new Font(32);

        tile.setFont(numberFont);
        tile.setAlignment(Pos.CENTER);

        tile.setLayoutX(x);
        tile.setLayoutY(y);
        tile.setPrefHeight(64);
        tile.setPrefWidth(64);

        tile.setBackground(Background.EMPTY);
    }

    private void drawSudokuBoard(Group root) {
        Rectangle boardBackground = new Rectangle();
        boardBackground.setX(BOARD_PADDING);
        boardBackground.setY(BOARD_PADDING);
        boardBackground.setWidth(BOARD_X_AND_Y);
        boardBackground.setHeight(BOARD_X_AND_Y);
        boardBackground.setFill(BOARD_BACKGROUND_COLOUR);
        root.getChildren().add(boardBackground);
    }

    private void drawTitle(Group root) {
        Text title = new Text(235, 690, SUDOKU);
        title.setFill(Color.WHITE);
        Font titleFont = new Font(43);
        title.setFont(titleFont);
        root.getChildren().add(title);
    }

    private void drawBackground(Group root) {
        Scene scene = new Scene(root, WINDOW_X, WINDOW_Y);
        scene.setFill(WINDOW_BACKGROUND_COLOUR);
        stage.setScene(scene);
    }

    @Override
    public void setListener(IUserInterfaceContract.EventListener listener) {
        this.listener = listener;
    }

    /**
     * Each time the user makes an input (which can be 0 to delete a number), we update the user
     * interface appropriately.
     */

    @Override
    public void updateSquare(int x, int y, int input) {
        SudokuTextField tile = textFieldCoordinates.get(new Coordinates(x, y));
        String value = Integer.toString(
                input
        );

        if (value.equals("0")) value = "";

        tile.textProperty().setValue(value);

    }

    @Override
    public void updateBoard(SudokuGame game) {
        for (int xIndex = 0; xIndex < 9; xIndex++) {
            for (int yIndex = 0; yIndex < 9; yIndex++) {
                TextField tile = textFieldCoordinates.get(new Coordinates(xIndex, yIndex));

                String value = Integer.toString(
                        // Create immutable copy
                        game.getCopyOfGridState()[xIndex][yIndex]
                );

                if (value.equals("0")) value = "";
                tile.setText(
                        value
                );

                //If a given tile has a non-zero value and the state of the game is GameState.NEW, then mark
                //the tile as read only. Otherwise, ensure that it is NOT read only.
                if (game.getGameState() == GameState.NEW){
                    if (value.equals("")) {
                        tile.setStyle("-fx-opacity: 1;");
                        tile.setDisable(false);
                    } else {
                        tile.setStyle("-fx-opacity: 0.8;");
                        tile.setDisable(true);
                    }
                }
            }
        }
    }

    @Override
    public void showDialog(String message) {
        // Basically alert for if you want to play again
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.OK);
        // show and wait will wait for interaction from user
        dialog.showAndWait();
        // Dialog was clicked and user wants to play, listener will figure out what to do
        if (dialog.getResult() == ButtonType.OK) listener.onDialogClick();
    }

    @Override
    public void showError(String message) {
        Alert dialog = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        dialog.showAndWait();
    }

    @Override
    public void handle(KeyEvent event) {
        // Checks the key pressed
        if (event.getEventType() == KeyEvent.KEY_PRESSED) {
            if (event.getText().equals("0")
                    || event.getText().equals("1")
                    || event.getText().equals("2")
                    || event.getText().equals("3")
                    || event.getText().equals("4")
                    || event.getText().equals("5")
                    || event.getText().equals("6")
                    || event.getText().equals("7")
                    || event.getText().equals("8")
                    || event.getText().equals("9")
            ) {
                int value = Integer.parseInt(event.getText());
                handleInput(value, event.getSource());
                // 0 or Backspace is used to clear it
            } else if (event.getCode() == KeyCode.BACK_SPACE) {
                handleInput(0, event.getSource());
            } else {
                ((TextField)event.getSource()).setText("");
            }
        }

        event.consume();
    }

    /**
     * @param value  expected to be an integer from 0-9, inclusive
     * @param source the textfield object that was clicked.
     */

    private void handleInput(int value, Object source) {
        listener.onSudokuInput(
                ((SudokuTextField) source).getX(),
                ((SudokuTextField) source).getY(),
                value
        );
    }
}
