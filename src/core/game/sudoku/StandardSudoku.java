package core.game.sudoku;

import core.game.User;

/** The standard implementation of the sudoku game */
public class StandardSudoku extends Sudoku {
    
    /** The Standard Duidoku Board Size */
    public static int BOARD_DIMENSION_SIZE = 9;
    /** The Standard Duidoku Box Size */
    public static int BOX_DIMENSION_SIZE   = 3;
    
    /**
     * Constructs a Standard sudoku game
     * @param board The initial board
     */
    public StandardSudoku(String name, User user, int[] board) {
        super(name, user, board, BOARD_DIMENSION_SIZE, BOX_DIMENSION_SIZE);
    }
}
