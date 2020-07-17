package core;

import java.util.Locale;
import java.util.ResourceBundle;

/** A Singleton that handles the resource bundles */
public final class LanguageSettings {
    
    //Keys
    private static final String GUI_BUNDLE_KEY        = "gui_bundle";
    private static final String MESSAGES_BUNDLE_KEY   = "message_bundle";
    
    
    //Bundles
    private static ResourceBundle guiBundle     = ResourceBundle.getBundle(GUI_BUNDLE_KEY);
    private static ResourceBundle messageBundle = ResourceBundle.getBundle(MESSAGES_BUNDLE_KEY);
    
    /**
     * Gets the current gui bundle
     * @return the current gui bundle
     */
    public static ResourceBundle getGuiBundle() {
        return guiBundle;
    }
    
    /**
     * Loads new gui bundle based on a locale
     * @param locale The locale
     */
    public static void loadGuiBundle(Locale locale) {
        guiBundle = ResourceBundle.getBundle(GUI_BUNDLE_KEY, locale);
    }
    
    /**
     * Gets the current message bundle
     * @return the current message bundle
     */
    public static ResourceBundle getMessageBundle() {
        return messageBundle;
    }
    
    /**
     * Loads new message bundle based on a locale
     * @param locale The locale
     */
    public static void loadMessageBundle(Locale locale) {
        messageBundle = ResourceBundle.getBundle(MESSAGES_BUNDLE_KEY, locale);
    }
    
    //Prevent instantiation
    private LanguageSettings() {}
}
