package core.gui;

import core.LanguageSettings;
import core.DiskManager;
import core.game.*;
import core.game.solvers.BacktrackingSolver;
import core.game.sudoku.Duidoku;
import core.game.sudoku.Sudoku;
import core.gui.localized.LocalizedJMenu;
import core.gui.localized.LocalizedJMenuItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;

/** The main window of the game */
public class MainForm extends JFrame {
    
    //The size of the window
    private static final Dimension WINDOW_SIZE = new Dimension(800, 800);
    
    //The game panel
    private GUISudokuBoard gamePanel    = null;
    //The user menu
    private final JMenu userMenu        = new LocalizedJMenu("user_menu_item");
    //The gui stats panel
    private final GUIStats statsPanel   = new GUIStats(null);
    
    //The current game
    private Sudoku game = null;
    //The current user
    private User   user = null;
    //The users that play the game
    private final Set<User> users = new HashSet<>();
    
    /**
     * Default construction of the object
     */
    public MainForm() {
        addWindowListener(new ClosingListener());   //Handle the closing event (Save users)
        setDefaultCloseOperation(EXIT_ON_CLOSE);    //Exit on close
        setSize(WINDOW_SIZE);                       //Set the size
        setLayout(null);                            //No layout
        setResizable(false);                        //Prevent resize
        
        {   //Menu bar
            JMenuBar menuBar = new JMenuBar();
            menuBar.setSize(getSize().width, 30);
            {   // Game menu
                JMenu menu = new LocalizedJMenu("game_menu_item");
                {   // New Sudoku Game
                    JMenuItem item =  new LocalizedJMenuItem("new_sudoku_game_item");
                    item.addActionListener(e -> loadNewSudoku());
                    menu.add(item);
                }
                {   // New Killer sudoku Game
                    JMenuItem item = new LocalizedJMenuItem("new_killer_sudoku_game_item");
                    item.addActionListener(e -> loadNewKillerSudoku());
                    menu.add(item);
                }
                {   // New Duidoku Game
                    JMenuItem item = new LocalizedJMenuItem("new_duidoku_game_item");
                    item.addActionListener(e -> loadNewDuidoku());
                    menu.add(item);
                }
                menuBar.add(menu);
            }
            {   // User menu
                JMenu menu = userMenu;
                {   // New user item
                    JMenuItem item = new LocalizedJMenuItem("new_user_menu_item");
                    item.addActionListener(e -> createNewUser(JOptionPane.showInputDialog("Username:")));
                    menu.add(item);
                }
                menuBar.add(menu);
                {   // Load Users
                    users.addAll(DiskManager.loadAllUsers());
                    for(User user : users) {
                        JMenuItem item = new JMenuItem(user.getName());
                        item.addActionListener(e -> selectUser(user));
                        menu.add(item);
                    }
                }
            }
            {   // Solve menu
                JMenu menu = new LocalizedJMenu("solve");
                {
                    JMenuItem item = new LocalizedJMenuItem("solve");
                    item.addActionListener(e -> solveCurrent());
                    menu.add(item);
                }
                menuBar.add(menu);
            }
            {   // Wordoku menu
                JMenu menu = new JMenu("Wordoku");
                { // Enable/Disable
                    JMenuItem item = new LocalizedJMenuItem("wordoku_enable_disable");
                    item.addActionListener(e -> wordokuToggle());
                    menu.add(item);
                }
                menuBar.add(menu);
            }
            {   // Language menu
                JMenu menu = new LocalizedJMenu("language_menu_item");
                { // English item
                    JMenuItem item = new JMenuItem("English");
                    item.addActionListener(e -> changeDisplayedLanguage(new Locale("en")));
                    menu.add(item);
                }
                { // Greek item
                    JMenuItem item = new JMenuItem("Ελληνικά");
                    item.addActionListener(e -> changeDisplayedLanguage(new Locale("gr")));
                    menu.add(item);
                }
                menuBar.add(menu);
            }
            add(menuBar);
        }

        
        statsPanel.setLocation(10, 40);
        add(statsPanel);
        
        selectUser(null);           //Select default user
        updateDisplayLanguage();    //Update the display language
    }
    
    
    /**
     * Solve the current game
     */
    protected void solveCurrent() {
        if (game == null)   // If no game yet return
            return;
        if(JOptionPane.showConfirmDialog(null, LanguageSettings.getMessageBundle().getString("auto_solve_message")) == JOptionPane.YES_OPTION) { //Double check if the user is ok with it
            if( game instanceof Duidoku ) { //If game is a duidoku variant then solve has no meaning
                JOptionPane.showMessageDialog(null, LanguageSettings.getMessageBundle().getString("solvable_duidoku"));
                return;
            }
            
            
            if(new BacktrackingSolver().solve(game))    //Else solve game and if succeed update UI
                gamePanel.updateUI();
            else
                JOptionPane.showMessageDialog(null, LanguageSettings.getMessageBundle().getString("not_solvable_sudoku"));
        }
    }
    
    /** Toggle wordoku if a gamePanel exists */
    protected void wordokuToggle() {
        if(gamePanel != null)
            gamePanel.toggleWordoku();
    }
    
    /** Update display language by updating the ui recursively */
    public void updateDisplayLanguage() {
        setTitle(LanguageSettings.getGuiBundle().getString("title"));
        updateDisplayLanguage(this);
        statsPanel.updateUI();
    }
    
    /**
     * Update display language recursively
     * @param component The root of the recursion
     */
    protected void updateDisplayLanguage(Component component) {
        if(component == null)   // If component is null, nothing to do
            return;
        
        if (component instanceof JMenu) //If its a menu update all within the menu
            for( Component inner : ((JMenu)component).getMenuComponents() )
                updateDisplayLanguage(inner);
        if( component instanceof Container )    //If its a container update all within the container
            for( Component inner : ((Container)component).getComponents() )
                updateDisplayLanguage(inner);
            
        if( component instanceof LocalizedJMenu )   //If its a LocalizedJMenu execute updateDisplayedLanguage
            ((LocalizedJMenu)component).updateDisplayedLanguage();
        else if( component instanceof LocalizedJMenuItem ) //If its a LocalizedJMenuItem execute updateDisplayedLanguage
            ((LocalizedJMenuItem)component).updateDisplayedLanguage();
    }
    
    
    /** Load a new Sudoku game */
    private void loadNewSudoku() {
        if(gamePanel != null)   // Remove old gamePanel
            remove(gamePanel);
        game = DiskManager.loadSudoku(user);    //Load new game for the user
        
        if (game == null) { //If not new game found
            JOptionPane.showMessageDialog(null, LanguageSettings.getMessageBundle().getString("no_new_games"));
            return;
        }
        
        gamePanel = new GUISudokuBoard(this);   //New panel
        gamePanel.setLocation((getWidth() - gamePanel.getWidth())/2, (getHeight() - gamePanel.getHeight())/2);  //Center it
        add(gamePanel);
        SwingUtilities.updateComponentTreeUI(this);
    }
    
    /** Load a new KillerSudoku game */
    private void loadNewKillerSudoku() {
        if(gamePanel != null) // Remove old gamePanel
            remove(gamePanel);
        game = DiskManager.loadKillerSudoku(user);  //Load new game for the user
        
        if (game == null) { //If not new game found
            JOptionPane.showMessageDialog(null, LanguageSettings.getMessageBundle().getString("no_new_games"));
            return;
        }
        
        gamePanel = new GUIKillerSudokuBoard(this); //New panel
        gamePanel.setLocation((getWidth() - gamePanel.getWidth())/2, (getHeight() - gamePanel.getHeight())/2); //Center it
        add(gamePanel);
        SwingUtilities.updateComponentTreeUI(this);
    }
    
    /** Load new Duidoku game */
    private void loadNewDuidoku() {
        if(gamePanel != null) // Remove old gamePanel
            remove(gamePanel);
        
        game = new Duidoku(user);
        gamePanel = new GUIDuidokuBoard(this); //New panel
        gamePanel.setLocation((getWidth() - gamePanel.getWidth())/2, (getHeight() - gamePanel.getHeight())/2); //Center it
        add(gamePanel);
        SwingUtilities.updateComponentTreeUI(this);
    }
    
    /**
     * Changes and updates the displayed language given a locale
     * @param locale The locale
     */
    private void changeDisplayedLanguage(Locale locale) {
        LanguageSettings.loadGuiBundle(locale);
        LanguageSettings.loadMessageBundle(locale);
        updateDisplayLanguage();
    }
    
    /**
     * Creates a new user given a username
     * @param username The username
     */
    private void createNewUser(String username) {
        if (username == null)
            return;
        User user = new User(username);
        if(!users.add(user)) {
            JOptionPane.showMessageDialog(null, LanguageSettings.getMessageBundle().getString("username_taken"));
        } else {
            JMenuItem item = new JMenuItem(username);
            item.addActionListener(e -> selectUser(user));
            userMenu.add(item);
            selectUser(user);
        }
    }
    
    /**
     * Selects a user
     * @param user The user
     */
    private void selectUser(User user) {
        if(user == null)
            user = User.ANONYMOUS;
        this.user = user;
        
        statsPanel.setUser(user);
    }
    
    /**
     * Getter for the game
     * @return the game
     */
    public Sudoku getGame() {
        return game;
    }
    
    /**
     * Getter for the user
     * @return the user
     */
    public User getUser() {
        return user;
    }
    
    /** A Handler for the closing event */
    private class ClosingListener extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            DiskManager.saveUsers(users);
            super.windowClosing(e);
        }
    }
}
