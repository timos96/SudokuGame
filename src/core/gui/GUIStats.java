package core.gui;

import core.LanguageSettings;
import core.game.User;

import javax.swing.*;

/** The panel for stats */
public class GUIStats extends JPanel {
    
    //The user which stats are displayed
    private User user;
    //The labels for wins and losses
    private final JLabel wins = new JLabel(),
            losses = new JLabel();
    
    /**
     * Constructs the panel with the given user
     * @param user The user
     */
    public GUIStats(User user) {
        add(wins);
        add(losses);
        setUser(user);
    }
    
    /**
     * Sets the user and updates the ui
     * @param user The user
     */
    public void setUser(User user) {
        if(user == null)
            user = User.ANONYMOUS;
        this.user = user;
        updateUI();
    }
    
    @Override
    public void updateUI() {
        updateStats();
        super.updateUI();
    }
    
    /**
     * Updates the stats of the panel
     */
    public void updateStats() {
        if(user == null)
            return;
        setBorder(BorderFactory.createTitledBorder(user.getName()));
        wins.setText(String.format("%s: %d", LanguageSettings.getGuiBundle().getString("wins"), user.getWins()));
        losses.setText(String.format("%s: %d", LanguageSettings.getGuiBundle().getString("losses"), user.getLosses()));
        setSize(getPreferredSize());
    }
    
}
