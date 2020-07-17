package core;

import core.gui.MainForm;

/** Entry class */
public final class Main {
    
    /** Entry point */
    public static void main(String[] args) {
        new MainForm().setVisible(true);
    }
    
    //Prevent instantiation
    private Main() {}
}
