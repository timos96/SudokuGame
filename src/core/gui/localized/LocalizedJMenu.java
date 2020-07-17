package core.gui.localized;

import core.LanguageSettings;

import javax.swing.*;

public class LocalizedJMenu extends JMenu {
    private final String key;
    
    public LocalizedJMenu(String key) {
        this.key = key;
        updateDisplayedLanguage();
    }
    
    public void updateDisplayedLanguage() {
        setText(LanguageSettings.getGuiBundle().getString(key));
    }
}
