package core.gui.localized;

import core.LanguageSettings;

import javax.swing.*;

public class LocalizedJMenuItem extends JMenuItem {
    
    private final String key;
    
    public LocalizedJMenuItem(String key) {
        this.key = key;
        updateDisplayedLanguage();
    }
    
    public void updateDisplayedLanguage() {
        setText(LanguageSettings.getGuiBundle().getString(key));
    }
}
