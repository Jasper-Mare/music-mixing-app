package src.desktop_ui.components;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

import src.desktop_ui.pages.Page;

public class StatusPanel extends JPanel {

    // tracked data

    private Page currentPage;

    // components

    private JLabel windowTitle;

    // constructors

    public StatusPanel(Page currentPage) {
        super();
        this.currentPage = currentPage;

        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        windowTitle = new JLabel(currentPage.GetPageName());
        windowTitle.setSize(100, this.getHeight());
        windowTitle.setBackground(Color.DARK_GRAY);
        windowTitle.setForeground(Color.WHITE);
        CompoundBorder titleBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 2, Color.BLACK),
                BorderFactory.createEmptyBorder(2, 2, 2, 2));
        windowTitle.setBorder(titleBorder);
        windowTitle.setOpaque(true);

        this.add(windowTitle);
        this.setBackground(Color.LIGHT_GRAY);
        this.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, Color.BLACK));

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
