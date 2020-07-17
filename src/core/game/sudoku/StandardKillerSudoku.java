package core.game.sudoku;

import core.game.User;

import java.util.Set;

/** The standard implementation of the killer sudoku game */
public class StandardKillerSudoku extends KillerSudoku {
    
    /** The Standard Duidoku Board Size */
    public static int BOARD_DIMENSION_SIZE = 9;
    /** The Standard Duidoku Box Size */
    public static int BOX_DIMENSION_SIZE   = 3;
    
    /**
     * Constructs a Standard killer sudoku game
     * @param areas The initial areas of the game
     */
    public StandardKillerSudoku(String name, User user, Set<Area> areas) {
        super(name, user, areas, BOARD_DIMENSION_SIZE, BOX_DIMENSION_SIZE);
    }
}
