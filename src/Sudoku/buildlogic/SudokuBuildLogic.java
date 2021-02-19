package Sudoku.buildlogic;

import Sudoku.computationlogic.GameLogic;
import Sudoku.persistence.LocalStorageImpl;
import Sudoku.problemdomain.IStorage;
import Sudoku.problemdomain.SudokuGame;
import Sudoku.userInterface.IUserInterfaceContract;
import Sudoku.userInterface.logic.ControlLogic;

import java.io.IOException;

public class SudokuBuildLogic {
    /**
     * This class takes in the uiImpl object which is tightly-coupled to the JavaFX framework,
     * and binds that object to the various other objects necessary for the application to function.
     */
    public static void build(IUserInterfaceContract.View userInterface) throws IOException {
        SudokuGame initialState;
        IStorage storage = new LocalStorageImpl();

        try {
            // will throw exception if no game data is found in local storage
            initialState = storage.getGameData();
        }
        catch (IOException e) {
            // ask for new game
            initialState = GameLogic.getNewGame();
            //this method below will also throw an IOException
            //if we cannot update the game data. At this point
            //the application is considered unrecoverable
            // update storage
            storage.updateGameData(initialState);
        }

        // bind everything together
        // First creating UI Logic class, control logic class
        // Requires storage and ref to user interface
        IUserInterfaceContract.EventListener uiLogic = new ControlLogic(storage, userInterface);

        // Bind control logic to User Interface
        userInterface.setListener(uiLogic);
        // update board with initial state
        userInterface.updateBoard(initialState);

    }
}
