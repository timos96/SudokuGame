package core.gui;

import core.game.sudoku.KillerSudoku;
import core.game.sudoku.Sudoku;

import javax.swing.*;
import java.awt.*;
import java.util.Set;

/** A GUI panel to handle the killer sudoku game */
public class GUIKillerSudokuBoard extends GUISudokuBoard {
    
    //The game
    private final KillerSudoku game;
    
    /**
     * Constructs the object
     * @param parent The parent window
     */
    public GUIKillerSudokuBoard(MainForm parent) {
        super(parent);
    
        Sudoku game = getGame();
        if( !(game instanceof KillerSudoku) )
            throw new IllegalArgumentException("Game is not killer sudoku");
        this.game = (KillerSudoku)game;
    }
    
    @Override
    public void updateUI() {
        super.updateUI();
        if(game != null) {
            for(KillerSudoku.Area area : game.getAreas()) { //For each area set color and text
            
                Set<Integer> indices = area.getIndices();
                int idx = Integer.MAX_VALUE;    //Find min = Upper left corner of area
                for( int index : indices )
                    if( index < idx )
                        idx = index;
            
                float h = idx / 20.f;
            
                Color backgroundColor = Color.getHSBColor(h, .5f, 1);
                Color foregoundColor  = Color.getHSBColor(h, 1, .5f);
            
                for(int i : area.getIndices()) {
                    int v = game.get(i);
                    JButton cell = (JButton) getComponent(i);
                    cell.setBackground(backgroundColor);
                
                    if(v != 0) {
                        cell.setForeground(Color.BLACK);
                    } else {
                        cell.setForeground(foregoundColor);
                        cell.setText(Integer.toString(area.getSum()));
                    }
                }
            }
        }
    }
}
