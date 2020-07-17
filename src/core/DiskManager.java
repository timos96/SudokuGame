package core;

import core.game.sudoku.KillerSudoku;
import core.game.sudoku.StandardKillerSudoku;
import core.game.sudoku.StandardSudoku;
import core.game.User;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.util.*;

/** A Singleton that handles the disk operations */
public final class DiskManager {

    //Data directories
    private static final File dataFolder = new File("data");
    private static final File gameFolder = new File(dataFolder, "games");
    private static final File userFolder = new File(dataFolder, "users");
    
    
    /**
     * Loads the next sudoku game given a user
     * @param user The user to select the game for
     * @return A sudoku game for the given user or null if no sudoku game is found
     */
    public static StandardSudoku loadSudoku(User user) {
        gameFolder.mkdirs(); //Attempt to make sub dirs
        
        File[] gameFiles = gameFolder.listFiles(new SimpleFileNameFilter("sudoku")); // List all files with the suffix sudoku
        
        if(gameFiles == null || gameFiles.length == 0)  // If non existent or empty
            return null;                                // return null
        
        List<File> fileList = Arrays.asList(gameFiles);
        Collections.shuffle(fileList);
        
        File gameFile = null;   // Find the first game that the user hasn't played yet
        for(File gf : fileList)
            if(!user.getPlayed().contains(gf.getName())) {
                gameFile = gf;
                break;
            }
    
        if(gameFile == null)    // If no new game return null
            return null;
    
        int[] rawBoard = new int[StandardSudoku.BOARD_DIMENSION_SIZE*StandardSudoku.BOARD_DIMENSION_SIZE];  // new board
        
        try(BufferedReader reader = new BufferedReader(new FileReader(gameFile))) {
            String line;
            while( (line = reader.readLine()) != null) {    // Read each line
                String[] split = line.split(",");           // Split it
                int i = Integer.parseInt(split[0]);         // Parse first = i
                int j = Integer.parseInt(split[1]);         // Parse second = j
                int v = Integer.parseInt(split[2]);         // Parse third = v
                rawBoard[i + j*StandardSudoku.BOARD_DIMENSION_SIZE] = v;    // RowMajor conversion and assignment
            }
        } catch (Exception e) { //If anything fails return null
            e.printStackTrace();
            return null;
        }
        return new StandardSudoku(gameFile.getName(), user, rawBoard);  // Return a new Standard sudoku game
    }
    
    
    /**
     * Loads the next killer sudoku game given a user
     * @param user The user to select the game for
     * @return A killer sudoku game for the given user or null if no sudoku game is found
     */
    public static StandardKillerSudoku loadKillerSudoku(User user) {
        gameFolder.mkdirs(); //Attempt to make sub dirs
        
        File[] gameFiles = gameFolder.listFiles(new SimpleFileNameFilter("killer")); // List all files with the suffix killer
    
        if(gameFiles == null || gameFiles.length == 0) // If non existent or empty, return null
            return null;
        
        List<File> fileList = Arrays.asList(gameFiles);
        Collections.shuffle(fileList);
        
        File gameFile = null; // Find the first game that the user hasn't played yet
        for(File gf : fileList)
            if(!user.getPlayed().contains(gf.getName())) {
                gameFile = gf;
                break;
            }
        
        if(gameFile == null) // If no new game return null
            return null;
    
        Set<KillerSudoku.Area> areas = new HashSet<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(gameFile))) {
            String line;
            while( (line = reader.readLine()) != null) {            // Read each line
                String[] split = line.split(",");                   // Split it by comma
                int sum = Integer.parseInt(split[0]);               // first = sum
                Set<Integer> indices = new HashSet<>();             // rest = indices
                
                for(String str : Arrays.copyOfRange(split, 1, split.length))
                    indices.add(Integer.parseInt(str));
                
                areas.add(new KillerSudoku.Area(sum, indices));     // Construct a new area
            }
        } catch (Exception e) { //If anything fails return null
            e.printStackTrace();
            return null;
        }
        return new StandardKillerSudoku(gameFile.getName(), user, areas);   //Return the new killer sudoku game
    }
    
    /**
     * Loads a user from the disk
     * @param name The user name
     * @return A user by name from the disk or null if the user does not exist
     */
    public static User loadUser(String name) {
        userFolder.mkdirs();    //Attempt to make sub dirs
        File userFile = new File(userFolder, name); // Select the file with the given name
        
        if(!userFile.exists())  // If it does not exist return null
            return null;
    
        
        try {
            List<String> lines = Files.readAllLines(userFile.toPath()); // Read all lines
            
            if(lines.size() != 3)   // lines must be 3
                return null;
            
            List<String> played = Arrays.asList(lines.get(0).split(","));   // Split the first by comma and these are the played games
            
            int wins = Integer.parseInt(lines.get(1));      // Parse second = wins
            int losses = Integer.parseInt(lines.get(2));    // Parse third = losses
            
            return new User(name, wins, losses, played);    // Return the user
            
        } catch (Exception e) { // If anything fails print stack trace and return null
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Loads all users from the disk
     * @return A set with all the users
     */
    public static Set<User> loadAllUsers() {
        userFolder.mkdirs();                        // Attempt to make sub dirs
        File[] userFiles = userFolder.listFiles();  // List all files in that sub dir
        Set<User> users = new HashSet<>();
    
        if(userFiles != null) {
            for(File userFile : userFiles) {                //For each file
                User user = loadUser(userFile.getName());   //Attempt to load the user
                if(user == null)                            //If user not loaded discard it
                    continue;
                users.add(user);                            //Else add to the set
            }
        }
        
        return users;   // return the set of loaded users
    }
    
    /**
     * Saves a set of users to the disk
     * @param users The set of users
     */
    public static void saveUsers(Set<User> users) {
        if( users == null )
            return;
        
        userFolder.mkdirs();    //Attempt to make sub dirs
        
        for(User user : users) {                                                        //For each user in the set
            File userFile = new File(userFolder, user.getName());                       //Assign a file to it
            try(PrintStream out = new PrintStream(new FileOutputStream(userFile))) {    //Attempt to write on that file
                List<String> played = user.getPlayed();                                 //
                if(!played.isEmpty()) {                                                 //If played anything
                    for(int i=0; i<played.size()-1; ++i)                                //print on the first line
                        out.printf("%s,", played.get(i));                               //all of the played games comma seperated
                    out.println(played.get(played.size()-1));                           //last one does not end with a comma
                } else {
                    out.println();                                                      //If not played then print empty line
                }
                
                
                out.println(user.getWins());    // Print the wins
                out.print(user.getLosses());     // Print the losses
            } catch (Exception e) { //If anything fails print stack trace and return
                e.printStackTrace();
            }
        }
    }
    
    
    //Prevent Instantiation
    private DiskManager() {}
    
    /** Simple file filter */
    private static class SimpleFileNameFilter implements FilenameFilter {
    
        //The suffix of the file
        private final String suffix;
    
        /**
         * Construct the object with a given suffix
         * @param suffix The suffix
         */
        public SimpleFileNameFilter(String suffix) {
            this.suffix = suffix;
        }
        
        //Accept only files that end with the suffix (not case sensitive)
        @Override
        public boolean accept(File dir, String name) {
            return name.toLowerCase().endsWith(suffix.toLowerCase());
        }
    }
}
