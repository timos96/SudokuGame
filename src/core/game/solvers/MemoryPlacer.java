package core.game.solvers;

import core.game.sudoku.Sudoku;

/** Implements a memory strategy for the Placer */
public class MemoryPlacer extends Placer {

    /** Current value */
    int v = 1;
    
    /**
     * Constructs the object with a given game and index
     * @param game  The game
     * @param i     The index
     */
    public MemoryPlacer(Sudoku game, int i) {
        super(game, i);
    }
    
    /**
     * Constructs the object with a given game and finding the next empty cell as index
     * @param game The game
     */
    public MemoryPlacer(Sudoku game) {
        super(game);
    }
    
    /**
     * Returns the next valid value for the selected cell
     * @return the next valid value for the selected cell or 0 if no valid value or when all valid values have been eliminated
     */
    @Override
    public int getNext() {
        Sudoku game = getGame();
        int d = game.getBoardDimension();
    
        int i = getIndex();
    
        for(; v <= d; ++v)
            if(game.canPlace(i, v))
                return v++;
    
        return 0;
    }
}
