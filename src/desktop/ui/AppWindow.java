package src.desktop.ui;

import java.awt.Container;
import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;

import javax.swing.*;
import javax.imageio.ImageIO;

import src.desktop.ui.components.StatusPanel;
import src.desktop.ui.pages.Page;
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

        try {
            frame.setIconImage(ImageIO.read(new File("res/icon256.png")));
        } catch (IOException e) {
        }

        Container contentPane = frame.getContentPane();

        contentPane.add(statusPanel, BorderLayout.PAGE_START);
        contentPane.add(currentPage.GetPageUI(), BorderLayout.CENTER);

        frame.setSize(appPrefs.getPreferedDimension());

        frame.setLocationRelativeTo(null);

        // Display the window.
        frame.setVisible(true);
    }

}
