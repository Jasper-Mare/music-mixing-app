package src.desktop_ui;

import java.awt.Container;
import java.awt.BorderLayout;

import javax.swing.*;

import src.desktop_ui.components.StatusPanel;
import src.desktop_ui.pages.Page;
import src.prefs.AppPreferences;

public class AppWindow {

    private Page currentPage;
    private StatusPanel statusPanel;

    public AppWindow(Page startPage) {
        currentPage = startPage;
        statusPanel = new StatusPanel(currentPage);
    }

    public void createAndShowGUI(AppPreferences appPrefs) {
        // Create and set up the window.
        JFrame frame = new JFrame("Music Mixer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container contentPane = frame.getContentPane();

        contentPane.add(currentPage.GetPageUI(), BorderLayout.CENTER);
        contentPane.add(statusPanel, BorderLayout.PAGE_END);

        frame.setSize(appPrefs.getPreferedDimension());

        frame.setLocationRelativeTo(null);

        // Display the window.
        frame.setVisible(true);
    }

}
