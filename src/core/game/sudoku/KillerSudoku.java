package core.game.sudoku;

import core.game.User;

import java.util.*;

/** The killer sudoku variant of the sudoku game */
public class KillerSudoku extends Sudoku {
    
    final Set<Area>  areas;
    
    /**
     * Constructs a {@link KillerSudoku} game from the given areas and the board/box sizes
     *
     * @param areas                 The set of non-overlapping areas of the game
     * @param boardDimensionSize    The board dimension
     * @param boxDimensionSize      The box dimension
     *
     * @throws RuntimeException if {@link Sudoku} fails construction
     * @throws NullPointerException if areas is null
     */
    public KillerSudoku(String name, User user, Set<Area> areas, int boardDimensionSize, int boxDimensionSize) {
        super(name, user, new int[boardDimensionSize*boardDimensionSize], boardDimensionSize, boxDimensionSize);
        
        if (areas == null)
            throw new NullPointerException("Areas is null");
        
        this.areas = Collections.unmodifiableSet(areas);
    }
    
    @Override
    public boolean isComplete() {
        if(!super.isComplete())
            return false;
        
        for(Area area : areas) {
            int sum = 0;
            for(int idx : area.getIndices())
                sum += get(idx);
            if( sum != area.getSum() )
                return false;
        }
        
        return true;
    }
    
    @Override
    public boolean canPlace(int i, int j, int v) {
        return canPlaceInArea(i, j, v) && super.canPlace(i, j, v); // Check also if can place in area
    }
    
    /**
     * Checks if a value can be placed on a selected area
     *
     * @param i The cell's column index
     * @param j The cell's row index
     * @param v The value to check
     *
     * @return true if the value can be placed on the selected area, false otherwise
     */
    private boolean canPlaceInArea(int i, int j, int v) {
        int idx = asRawIndex(i, j);
        
        Area selected = null; // Find area that has the index
        for(Area area : areas) {
            if( area.getIndices().contains(idx) ) {
                selected = area;
                break;
            }
        }
        
        if( selected == null ) // If no area the cant place in any area
            return false;
        
        int sum = 0;    // Sum of all values in the area
        int empty = 0;  // Number of empty cells in the area
        for( int index : selected.getIndices() ) {
            int c = get(index);
            if( isCellEmpty(c) )
                empty++;
            else
                sum += c;
        }
        
        
        if( empty == 1 )    // If only one empty then the sum+v must equal the needed sum
            return sum + v == selected.getSum();
        else                // Else it must be lower than the sum
            return sum + v < selected.getSum();
    }
    
    /**
     * Getter for the areas
     * @return the areas
     */
    public Set<Area> getAreas() {
        return areas;
    }
    
    /**
     * An implementation of an area in the {@link KillerSudoku} game
     */
    public static class Area {
        
        /** The expected sum of each value in this area */
        private final int sum;
        /** The indices that comprise an area */
        private final Set<Integer> indices;
    
        /**
         * Constructs an area with the given sum and given indices
         *
         * @param sum       The sum of the area
         * @param indices   The indices of the area
         *
         * @throws IllegalArgumentException if the sum is not positive
         * @throws NullPointerException if the indices is null
         */
        public Area(int sum, Set<Integer> indices) {
            if(sum <= 0)
                throw new IllegalArgumentException("Sum is not positive");
            if(indices == null)
                throw new NullPointerException("Indices is null");
            
            this.sum = sum;
            this.indices = Collections.unmodifiableSet(indices);
        }
    
        /**
         * Returns the sum of the areas
         * @return the sum of the areas
         */
        public int getSum() {
            return sum;
        }
    
        /**
         * Returns the indices as an {@link java.util.Collections.UnmodifiableSet UnmodifiableSet}
         * @return the indices
         */
        public Set<Integer> getIndices() {
            return indices;
        }
    
        /**
         * Checks if an area is equal to an other object
         *
         * @param obj The other object
         * @return true if the other object is {@link Area} and have at least one common cell with this area
         */
        @Override
        public boolean equals(Object obj) {
            if( !(obj instanceof Area) )
                return false;

            return equals((Area)obj);
        }
    
        /**
         * Checks if an area is equal to an other
         *
         * @param other The other area
         * @return true if the two areas have have at least one common cell, false otherwise
         */
        private boolean equals(Area other) {
            if(this == other)
                return true;
            for (int i : other.indices)
                if (indices.contains(i))
                    return true;
            return false;
        }
    }
    
}
