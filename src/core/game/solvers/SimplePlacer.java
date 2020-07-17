package core.game.solvers;

import core.game.sudoku.Sudoku;

/** Implements a simple strategy for the Placer */
public class SimplePlacer extends Placer {
    
    /**
     * Constructs the object with a given game and index
     * @param game  The game
     * @param i     The index
     */
    public SimplePlacer(Sudoku game, int i) {
        super(game, i);
    }
    
    /**
     * Constructs the object with a given game and finding the next empty cell as index
     * @param game The game
     */
    public SimplePlacer(Sudoku game) {
        super(game);
    }
    
    /**
     * Returns the first valid value for the selected cell
     * @return the first valid value for the selected cell or 0 if no valid value
     */
    @Override
    public int getNext() {
        Sudoku game = getGame();
        int d = game.getBoardDimension();
        
        int i = getIndex();
    
        for(int v = 1; v <= d; ++v)
            if(game.canPlace(i, v))
                return v;
            
        return 0;
    }
}
