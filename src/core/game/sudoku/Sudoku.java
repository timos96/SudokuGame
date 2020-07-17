package core.game.sudoku;

import core.game.User;

/**
 * A generic Sudoku implementation
 */
public class Sudoku {
    
    /** A cell with that value is considered empty */
    public static final int EMPTY_VALUE = 0;
    
    /** The name of the sudoku game */
    private final String name;
    /** The user playing the game */
    private final User user;
    /** The board of the game */
    private final int[] board;
    /** The dimensions of the board */
    private final int boardDimension;
    /** The dimensions of the box */
    private final int boxDimension;
    
    /**
     * Constructs a new {@link Sudoku} game
     *
     * @param name              The name of the board   (by reference)
     * @param user              The user playing        (by reference)
     * @param board             The board of the game   (by reference)
     * @param boardDimension    The board dimension     (by reference)
     * @param boxDimension      The box dimension       (by reference)
     *
     * @throws NullPointerException if the name, user or board is null
     * @throws IllegalArgumentException if the board or box size is negative or the boxes do not evenly divide the board
     * @throws IllegalArgumentException if the prefixed values of the board are not valid
     */
    public Sudoku(String name, User user, int[] board, int boardDimension, int boxDimension) {
        //Assign the values to the object by reference
        this.name           = name;
        this.user           = user;
        this.board          = board;
        this.boardDimension = boardDimension;
        this.boxDimension   = boxDimension;
        
        //Safety checks
        if( name == null )
            throw new NullPointerException("Name is null");
        
        if( user == null )
            throw new NullPointerException("User is null");
        
        if( board == null )
            throw new NullPointerException("Board is null");
        
        if( boardDimension < 0 )
            throw new IllegalArgumentException("Board size is negative");
    
        if( boxDimension < 0 )
            throw new IllegalArgumentException("Box size is negative");
        
        if (board.length != boardDimension * boardDimension )
            throw new IllegalArgumentException("Board is not a square");
        
        if( boardDimension % boxDimension != 0 )
            throw new IllegalArgumentException("Board cannot be evenly divided by the boxes");
        
        for(int cell : board)
            if( !isCellValid(cell) )
                throw new IllegalArgumentException("Board cells are not all valid");
    }
    
    /**
     * Checks if a value is valid.
     * <br>
     * A value is considered valid when it belongs in [1, boardDimension]
     *
     * @param v The value to check
     *
     * @return true if the value is valid, false otherwise
     */
    public boolean isValueValid(int v) {
        return 0 < v && v <= boardDimension;
    }
    
    /**
     * Checks if a cell is valid.
     * <br>
     * Cells are valid when their values are valid or if they are empty
     *
     * @param c The cell to check
     *
     * @return true if the cell is valid, false otherwise
     */
    public boolean isCellValid(int c) {
        return isCellEmpty(c) || isValueValid(c);
    }
    
    /**
     * Checks if a part of a 2D index is valid.
     * <br>
     * An index is considered valid when it belongs in [0, boardDimension)
     *
     * @param i The index to check
     *
     * @return true if the index is valid, false otherwise
     */
    public boolean isIndexValid(int i) {
        return isValueValid(i+1);
    }
    
    /**
     * Checks if the cell is empty
     *
     * @param c The cell to check
     *
     * @return true if its empty, false otherwise
     */
    public boolean isCellEmpty(int c) { // Could be static
        return c == EMPTY_VALUE;
    }
    
    /**
     * Converts a 2D index to a Row-Major 1D index
     *
     * @param i The column index
     * @param j The row index
     *
     * @return Row-Major 1D index
     *
     * @throws IndexOutOfBoundsException if i or j is not a valid index
     * @see #isIndexValid(int)
     */
    protected int asRawIndex(int i, int j) {
        if( !isIndexValid(i) )
            throw new IndexOutOfBoundsException("Index i was out of bounds");
        if( !isIndexValid(j) )
            throw new IndexOutOfBoundsException("Index j was out of bounds");
        return i + j*boardDimension; //Row-Major
    }
    
    /**
     * Returns the cell at a given 1D index
     *
     * @param i The raw index of the cell
     *
     * @return the selected cell
     */
    public int get(int i) {
        return board[i];
    }
    
    /**
     * Returns the cell at a given 2D index.
     * <br>
     * Calls {@link #get(int)} converting the i and j to a raw index
     *
     * @param i The column index of the cell
     * @param j The row index of the cell
     *
     * @return the selected cell
     *
     * @throws IndexOutOfBoundsException if {@link #asRawIndex(int, int)} throws
     * @see #asRawIndex(int, int)
     */
    public final int get(int i, int j) {
        return get(asRawIndex(i, j));
    }
    
    /**
     * Resets the selected cell to the {@link #EMPTY_VALUE}
     * @param i The raw index of the cell
     *
     * @throws IndexOutOfBoundsException if i exceeds the limits of the array
     */
    public void reset(int i) {
        board[i] = EMPTY_VALUE;
    }
    
    /**
     * Resets the selected cell to the {@link #EMPTY_VALUE}
     * <br>
     * Calls {@link #reset(int)} converting the i and j to a raw index
     *
     * @param i - The column index
     * @param j - The row index
     *
     * @throws IndexOutOfBoundsException if {@link #asRawIndex(int, int)} throws
     * @see #asRawIndex(int, int)
     */
    public final void reset(int i, int j) {
        reset(asRawIndex(i, j));
    }
    
    /**
     * Attempts to set the selected cell to a given value
     * <br>
     * A cell can be set to a value, only if its empty and if the {@link Sudoku} rules allow it
     *
     * @param i The raw index of the cell
     * @param v The value to set the cell
     *
     * @return true if the placement was accepted, false otherwise
     */
    public boolean place(int i, int v) {
        if( !canPlace(i, v) )
            return false;
    
        board[i] = v;
    
        if(isComplete())
            onComplete();
    
        return true;
    }
    
    /**
     * Attempts to set the selected cell to a given value
     * <br>
     * Calls {@link #place(int,int)} converting the i and j to a raw index
     * <br>
     * A cell can be set to a value, only if its empty and if the {@link Sudoku} rules allow it
     *
     * @param i The column index of the cell
     * @param j The row index of the cell
     * @param v The value to set the cell
     *
     * @return true if the placement was accepted, false otherwise
     */
    public final boolean place(int i, int j, int v) {
        return place(asRawIndex(i, j), v);
    }
    
    /**
     * Attempts to replace a value of a cell to a given new value
     * <br>
     * If the selected cell is empty, then a replace action is the same as a place action
     *
     * @param i - The Raw index of the cell
     * @param v - The new value
     *
     * @return true if the replace was a success, false otherwise
     */
    public boolean replace(int i, int v) {
        int old = get(i);
        
        // If it was empty replace = place
        if (isCellEmpty(old))
            return place(i, v);
        
        // Else reset
        reset(i);
        // Attempt to place new and return true on success
        if( place(i, v) )
            return true;
        
        // Place old and return false since replace failed
        place(i, old);
        return false;
    }
    
    /**
     * Attempts to replace a value of a cell to a given new value
     * <br>
     * Calls {@link #replace(int, int)} converting i and j to a raw index
     * <br>
     * If the selected cell is empty, then a replace action is the same as a place action
     *
     * @param i - The column index of the cell
     * @param j - The row index of the cell
     * @param v - The new value
     *
     * @return true if the replace was a success, false otherwise
     */
    public final boolean replace(int i, int j, int v) {
        return replace(asRawIndex(i, j), v);
    }
    
    
    /**
     * Checks if a given value can be placed on the selected Cell
     * <br>
     * A cell can be set to a value, only if its empty and if the current {@link Sudoku} variant rules allow it
     *
     * @param i The column index of the cell
     * @param j The row index of the cell
     * @param v The value to check if can be placed on the cell
     *
     * @return true if the placement would be accepted, false otherwise
     */
    public boolean canPlace(int i, int j, int v) {
        if( !isValueValid(v) )
            return false;
        
        return canPlaceOnRow(j, v) && canPlaceOnColumn(i, v) && canPlaceOnBox(i, j, v);
    }
    
    /**
     * Checks if a given value can be placed on the selected Cell
     * <br>
     * Calls {@link #canPlace(int, int, int)} converting the raw index to a 2D RowMajor index
     * <br>
     * A cell can be set to a value, only if its empty and if the current {@link Sudoku} variant rules allow it
     *
     * @param i The raw index of the cell
     * @param v The value to check if can be placed on the cell
     *
     * @return true if the placement would be accepted, false otherwise
     */
    public final boolean canPlace(int i, int v) {
        return canPlace(i%boardDimension, i/boardDimension, v);
    }
    
    /**
     * Checks if a value can be placed on a selected row
     *
     * @param j The row index
     * @param v The value to check
     *
     * @return true if the value can be placed on the selected row, false otherwise
     */
    private boolean canPlaceOnRow(int j, int v) {
        for(int i = 0; i < boardDimension; ++i) // For all columns on this row
            if( get(i, j) == v )                // If any cell has the value v in it
                return false;                   // The value cannot be placed on this row
        return true;                            // Else it can
    }
    
    /**
     * Checks if a value can be placed on a selected column
     *
     * @param i The column index
     * @param v The value to check
     *
     * @return true if the value can be placed on the selected column, false otherwise
     */
    private boolean canPlaceOnColumn(int i, int v) {
        for(int j = 0; j < boardDimension; ++j) // For all rows on this column
            if( get(i, j) == v )                // If any cell has the value v in it
                return false;                   // The value cannot be placed on this column
        return true;                            // Else it can
    }
    
    /**
     * Checks if a value can be placed on a selected box
     *
     * @param i The cell's column index
     * @param j The cell's row index
     * @param v The value to check
     *
     * @return true if the value can be placed on the selected box, false otherwise
     */
    private boolean canPlaceOnBox(int i, int j, int v) {
        int bi = (i/boxDimension)*boxDimension;    // Leftmost column of the box
        int bj = (j/boxDimension)*boxDimension;    // Upmost row of the box
        
        for(int lj = 0; lj < boxDimension; ++lj)    // For all rows of the box
            for(int li = 0; li < boxDimension; ++li)// For all columns of the row
                if( get(li+bi, lj+bj) == v)         // If any cell in the box has the value v
                    return false;                   // The value cannot be placed in the box
        return true;                                // Else it can
    }
    
    /**
     * Checks if the board is complete
     *
     * @return true if no cell is empty, false otherwise
     */
    public boolean isComplete() {
        for(int i = 0; i < boardDimension*boardDimension; ++i)
            if( isCellEmpty(get(i)) )
                return false;
        return true;
    }
    
    /**
     * Procedure to be executed on completion
     */
    protected void onComplete() {
        user.played(this);
    }
    
    /**
     * Getter for the {@link #boardDimension}
     * @return the board dimension
     */
    public int getBoardDimension() {
        return boardDimension;
    }
    
    /**
     * Getter for the {@link #boxDimension}
     * @return the box dimension
     */
    public int getBoxDimension() {
        return boxDimension;
    }
    
    /**
     * Getter for the {@link #name}
     * @return the name of the game
     */
    public String getName() {
        return name;
    }
    
    /**
     * Getter for the {@link #user}
     * @return the user playing the game
     */
    public User getUser() {
        return user;
    }
}
