package core.gui.localized;

import core.LanguageSettings;

import javax.swing.*;

public class LocalizedJLabel extends JLabel {
    
    private final String key;
    
    public LocalizedJLabel(String key) {
        this.key = key;
        updateDisplayedLanguage();
    }
    
    public void updateDisplayedLanguage() {
        setText(LanguageSettings.getGuiBundle().getString(key));
    }

}
