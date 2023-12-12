package src.desktop_ui;

import java.awt.Container;
import java.awt.BorderLayout;

import javax.swing.*;

import src.desktop_ui.components.StatusPanel;
import src.desktop_ui.pages.Page;
import src.prefs.AppPreferences;
import src.prefs.PreferencesManager;

public class AppWindow {

    private Page currentPage;
    private StatusPanel statusPanel;

    private AppPreferences appPrefs;

    public AppWindow(Page startPage) {
        appPrefs = PreferencesManager.statInstance.getAppPrefs();
        currentPage = startPage;
        statusPanel = new StatusPanel(currentPage);
    }

    public void createAndShowGUI() {
        // Create and set up the window.
        JFrame frame = new JFrame("Music Mixer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container contentPane = frame.getContentPane();

        contentPane.add(statusPanel, BorderLayout.PAGE_START);
        contentPane.add(currentPage.GetPageUI(), BorderLayout.CENTER);

        frame.setSize(appPrefs.getPreferedDimension());

        frame.setLocationRelativeTo(null);

        // Display the window.
        frame.setVisible(true);
    }

}
