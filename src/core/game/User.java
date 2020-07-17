package core.game;

import core.game.sudoku.Sudoku;

import java.util.*;

/** Represents a user of the game */
public class User {
    
    //The default user
    public static final User ANONYMOUS = new User("Anonymous");
    
    //The name of the user
    private final String name;
    //The played games of the user
    private final List<String> played;
    //Wins and losses of the user from an AI
    private int wins, losses;
    
    /**
     * Constructs the user given the stats
     * @param name      The name of the user
     * @param wins      The wins of the user
     * @param losses    The losses of the user
     * @param played    The games played by the user
     */
    public User(String name, int wins, int losses, List<String> played) {
        this.name = name;
        this.wins = wins;
        this.losses = losses;
        this.played = new LinkedList<>(played);
    }
    
    /**
     * Constructs the user given only the name. Setting the stats to default
     * @param name The name of the user
     */
    public User(String name) {
        this(name, 0, 0, Collections.emptyList());
    }
    
    /**
     * Adds the game to the played list
     * @param game The game to add
     */
    public void played(Sudoku game) {
        played.add(game.getName());
    }
    
    /**
     * Increments the wins of the user
     */
    public void won() {
        wins++;
    }
    
    /**
     * Increments the losses of the user
     */
    public void lost() {
        losses++;
    }
    
    /**
     * Getter for the losses
     * @return the losses
     */
    public int getLosses() {
        return losses;
    }
    
    /**
     * Getter for the wins
     * @return the wins
     */
    public int getWins() {
        return wins;
    }
    
    /**
     * Getter for the played games
     * @return the played games
     */
    public List<String> getPlayed() {
        return played;
    }
    
    /**
     * Getter for the name
     * @return the name
     */
    public String getName() {
        return name;
    }
    
    //Equals only when names are equal
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof User))
            return false;
        if(obj == this)
            return true;
        return ((User) obj).name.equals(name);
    }
    
    //Hash same as name hash (for hash sets)
    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
