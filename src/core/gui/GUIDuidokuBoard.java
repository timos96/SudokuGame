package core.gui;

import core.LanguageSettings;
import core.game.sudoku.Duidoku;

import javax.swing.*;

/** A GUI for the Duidoku game */
public class GUIDuidokuBoard extends GUISudokuBoard {
    
    public GUIDuidokuBoard(MainForm parent) {
        super(parent);
        if(!(parent.getGame() instanceof Duidoku))
            throw new IllegalArgumentException("The game is not Duidoku");
    }
    
    //Prints the analogous message on win or lose
    @Override
    protected void onCompletion() {
        if(((Duidoku)getGame()).isPlayerMove())
            JOptionPane.showMessageDialog(null, LanguageSettings.getMessageBundle().getString("lost"));
        else
            JOptionPane.showMessageDialog(null, LanguageSettings.getMessageBundle().getString("won"));
    }
}
