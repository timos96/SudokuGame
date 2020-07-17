package core.game.solvers;

import core.game.sudoku.Sudoku;

/** An object that places a value on a Sudoku board */
public abstract class Placer {
    
    /**
     * Finds the next Empty cell on the board
     * @param game The board
     * @return The raw index of the empty cell on the board or -1 if no empty cell
     */
    private static int findNextEmptyCell(Sudoku game) {
        int d = game.getBoardDimension();
        
        for(int i = 0; i < d*d; ++i)
            if(game.isCellEmpty(game.get(i)))
                return i;
        return -1;
    }
    
    /** The game */
    private final Sudoku game;
    /** The raw index to place at */
    private final int i;
    
    /**
     * Constructs the object given a game and a raw index
     * @param game  The game
     * @param i     The raw index
     */
    public Placer(Sudoku game, int i) {
        this.game = game;
        this.i = i;
    }
    
    /**
     * Constructs the object given a game and finding the next empty cell
     * @param game The game
     */
    public Placer(Sudoku game) {
        this(game, findNextEmptyCell(game));
    }
    
    /**
     * Gets the next value to place on the selected cell
     * @return the next value to place on the selected cell
     */
    public abstract int getNext();
    
    /**
     * Resets the given cell
     */
    public void reset() {
        game.reset(i);
    }
    
    /**
     * Places the value from {@link #getNext()} to the given cell
     * @return true if the place is possible, false otherwise
     */
    public boolean placeNext() {
        return game.place(i, getNext());
    }
    
    /**
     * Getter for the game
     * @return the game
     */
    public Sudoku getGame() {
        return game;
    }
    
    /**
     * Getter for the index
     * @return the index
     */
    public int getIndex() {
        return i;
    }
    
}
