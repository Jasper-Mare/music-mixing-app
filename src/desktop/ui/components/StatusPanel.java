package src.desktop.ui.components;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

import src.desktop.ui.pages.Page;
import src.prefs.ColourScheme;
import src.prefs.PreferencesManager;

public class StatusPanel extends JPanel {

    // tracked data

    private Page currentPage;

    // components

    private JLabel windowTitle;

    // constructors

    public StatusPanel(Page currentPage) {
        super();
        this.currentPage = currentPage;

        ColourScheme cs = PreferencesManager.statInstance.getColourScheme();

        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        windowTitle = new JLabel(currentPage.GetPageName());
        windowTitle.setSize(100, this.getHeight());
        windowTitle.setBackground(cs.getStatusPanelWindowTitleBackgroundColour());
        windowTitle.setForeground(cs.getStatusPanelWindowTitleForegroundColour());
        CompoundBorder titleBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 2, cs.getStatusPanelWindowTitleBorderColour()),
                BorderFactory.createEmptyBorder(2, 2, 2, 2));
        windowTitle.setBorder(titleBorder);
        windowTitle.setOpaque(true);

        this.add(windowTitle);
        this.setBackground(cs.getStatusPanelBackgroundColour());
        this.setBorder(BorderFactory.createMatteBorder(1, 0, 2, 0, cs.getStatusPanelBorderColour()));

    }

    // getters and setters

    public Page getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Page currentPage) {
        this.currentPage = currentPage;
        this.windowTitle.setText(currentPage.GetPageName());
    }

    // functionality

}
