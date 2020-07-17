package core.game.solvers;

import core.game.sudoku.Sudoku;

/** An interface for the sudoku solver */
public interface SudokuSolver {
    /**
     * Solves the given game
     * @param game The game
     * @return true if the game was solved, false otherwise
     */
    boolean solve(Sudoku game);
}
