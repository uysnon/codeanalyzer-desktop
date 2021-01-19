package ru.rsreu.gorkin.codeanalyzer.desktop;

import ru.rsreu.gorkin.codeanalyzer.desktop.windows.StartWindow;

public class App {
    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    public void run() {
        StartWindow startWindow = new StartWindow();
    }
}
