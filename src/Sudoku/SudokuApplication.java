package Sudoku;

import Sudoku.buildlogic.SudokuBuildLogic;
import Sudoku.userInterface.IUserInterfaceContract;
import Sudoku.userInterface.UserInterfaceImpl;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This class is the Root Container (the thing which attends to all of the primary objects which must communicate when
 * the program is running (a running program is called a "process").
 */

public class SudokuApplication extends Application {
    // Prefix the User Interface
    private IUserInterfaceContract.View uiImpl;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Represents User Interface
        // primaryStage is a simple window that java gives us to modify it
        //Get SudokuGame object for a new game
        uiImpl = new UserInterfaceImpl(primaryStage);
        try {
            // SudokuBuildLogic setup for build logic which is used for dependency and wires the things together
            SudokuBuildLogic.build(uiImpl);
        }
        catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
