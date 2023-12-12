package src.desktop_ui.pages;

import java.awt.Dimension;

import javax.swing.*;

public class HomePage implements Page {

    private JPanel mainPanel;
    private JLabel sizeLabel = new JLabel();

    public HomePage() {
        mainPanel = new JPanel(true);

        Thread threadName = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                Dimension size = mainPanel.getSize();
                sizeLabel.setText("( " + size.width + ", " + size.height + " )");
            }
        });
        threadName.start();
    }

    @Override
    public JPanel GetPageUI() {

        mainPanel.add(sizeLabel);

        return mainPanel;
    }

    @Override
    public String GetPageName() {
        return "Home Page";
    }

}
