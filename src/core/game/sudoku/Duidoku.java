package core.game.sudoku;

import core.game.User;
import core.game.solvers.SimplePlacer;

public class Duidoku extends Sudoku {
    /** The Standard Duidoku Board Size */
    public static int BOARD_DIMENSION_SIZE = 4;
    /** The Standard Duidoku Box Size */
    public static int BOX_DIMENSION_SIZE   = 2;
    /** Denotes if the player is to place or the AI */
    private boolean playerMove = true;
    
    /**
     * Constructs a Standard duidoku game
     */
    public Duidoku(User user) {
        super("", user, new int[BOARD_DIMENSION_SIZE*BOARD_DIMENSION_SIZE], BOARD_DIMENSION_SIZE, BOX_DIMENSION_SIZE);
    }
    
    /** Places the next AI move */
    public boolean nextAIMove() {
        int d = getBoardDimension();
        int dd = d*d;
        
        for(int i = 0; i < dd; ++i)                     //Linear search
            if(new SimplePlacer(this, i).placeNext())   //Place where ever
                return true;                            //If any place happens return true
        return false;                                   //Else false
    }
    
    //Places for the AI as well if its not player move
    @Override
    public boolean place(int i, int v) {
        if(super.place(i, v)) {         //If move was successful
            playerMove = !playerMove;   //Toggle playerMove
            if(!playerMove)             //If it was a player move
                nextAIMove();           //Make an AI move
            return true;                //Return true since the move was legal
        }
        return false;                   //Else false
    }
    
    @Override
    public boolean canPlace(int i, int j, int v) {
        return isCellEmpty(get(i,j)) && super.canPlace(i, j, v);
    }
    
    @Override
    public void reset(int i) {
        //Empty
    }
    
    @Override
    public boolean replace(int i, int v) {
        return place(i, v);//Since you cant replace on duidoku
    }
    
    @Override
    public boolean isComplete() {
        return super.isComplete() || !canMakeProgress();
    }
    
    @Override
    protected void onComplete() {
        if(playerMove)
            getUser().won();
        else
            getUser().lost();
    }
    
    public boolean isPlayerMove() {
        return playerMove;
    }
    
    private boolean canMakeProgress() {
        int d = getBoardDimension();
        
        for(int j = 0; j < d; ++j)
            for(int i = 0; i < d; ++i)
                for(int v = 1; v <= d; ++v)
                    if(canPlace(i, j, v))
                        return true;
                    
        return false;
    }
}
