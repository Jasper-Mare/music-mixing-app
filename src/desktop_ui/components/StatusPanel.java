package src.desktop_ui.components;

import java.awt.Container;

import javax.swing.JLabel;

import src.desktop_ui.pages.Page;

public class StatusPanel extends Container {

    // tracked data

    private Page currentPage;

    // components

    private JLabel windowTitle;

    public StatusPanel(Page currentPage) {
        this.currentPage = currentPage;

        windowTitle = new JLabel(currentPage.GetPageName());
        this.add(windowTitle);
    }

    public Page getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Page currentPage) {
        this.currentPage = currentPage;
    }

}
