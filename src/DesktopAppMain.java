package src;

import src.desktop_ui.AppWindow;
import src.desktop_ui.pages.*;
import src.prefs.PreferencesManager;

public class DesktopAppMain {

    public static void main(String[] args) {

        PreferencesManager.setupStatInstance();
        PreferencesManager prefsManager = PreferencesManager.statInstance;
        prefsManager.saveToFile();

        AppWindow window = new AppWindow(new HomePage());

        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                window.createAndShowGUI();
            }
        });

    }

}
