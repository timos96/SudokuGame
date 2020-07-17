package core.game.solvers;

import core.game.sudoku.Sudoku;

/** Solves the sudoku game by the recursive backtracking approach */
public class BacktrackingSolver implements SudokuSolver {
    
    public boolean solve(Sudoku game) {
        Placer placer = new MemoryPlacer(game);
        int i = placer.getIndex();
        
        if( i == -1 )
            return true;
        
        while(placer.placeNext())
            if(solve(game))
                return true;
            else
                placer.reset();
            
        return false;
    }
    
}
