package Sudoku.userInterface;

import javafx.scene.control.TextField;

public class SudokuTextField extends TextField {
    private final int x;
    private final int y;

    public SudokuTextField(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    // if you don't override, you will get strange behaviour
    // e.g. when the user enters num from 0-9 into SudokuTxtBox, you will get repeat numbers
    @Override
    public void replaceText(int i, int i1, String s){
        // regular expression = regex, used for matching strings
        // e.g. if s matches any numbers from 0-9 that expr will be true
        // Regex is used for matching text
        if(!s.matches("[0-9]")) {
            super.replaceText(i, i1, s);
        }
    }

    @Override
    public void replaceSelection(String s) {
        if (!s.matches("[0-9]")) {
            super.replaceSelection(s);
        }
    }
}
