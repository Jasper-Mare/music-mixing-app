package src.desktop_ui;

import javax.swing.*;

import src.desktop_ui.pages.Page;
import src.prefs.AppPreferences;

public class AppWindow {

    private Page currentPage;

    public AppWindow(Page startPage) {
        currentPage = startPage;
    }

    public void createAndShowGUI(AppPreferences appPrefs) {
        // Create and set up the window.
        JFrame frame = new JFrame("Music Mixer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.getContentPane().add(currentPage.GetPageUI());

        frame.setSize(appPrefs.getPreferedDimension());

        frame.setLocationRelativeTo(null);

        // Display the window.
        frame.setVisible(true);
    }

}
