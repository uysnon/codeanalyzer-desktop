package ru.rsreu.gorkin.codeanalyzer.desktop;

import ru.rsreu.gorkin.codeanalyzer.desktop.frames.StartWindow;

public class SwingApp {
    public static void main(String[] args) {
        SwingApp app = new SwingApp();
        app.run();
    }

    public void run() {
        StartWindow startWindow = new StartWindow();
    }
}
