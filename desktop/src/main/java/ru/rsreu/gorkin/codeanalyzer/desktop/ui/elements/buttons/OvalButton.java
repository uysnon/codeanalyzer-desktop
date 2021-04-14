package ru.rsreu.gorkin.codeanalyzer.desktop.ui.elements.buttons;

//file OvalButton.java

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

class OvalButton extends CustomButton {

    OvalButton(String title) {
        super(title);
    }

    public void paintComponent(Graphics g) {
        g.setColor(getParent().getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        Graphics2D g2D = (Graphics2D) g;
        if (hit == true) {
            g2D.setColor(Color.green);
        } else {
            g2D.setColor(Color.yellow);
        }
        ;
        g2D.fillOval(0, 0, getWidth() - 2, getHeight() - 2);
        g2D.setColor(Color.black);
        g2D.drawOval(0, 0, getWidth() - 2, getHeight() - 2);
        g2D.drawString(title, 10, getHeight() / 2);
    }

    public static void main(String[] args) {//TEST
        JFrame jFrame = new JFrame();
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        OvalButton button = new OvalButton("Special button");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("I am clicked!");
            }
        });
        Container cont = jFrame.getContentPane();
        cont.setLayout(new FlowLayout());
        cont.add(new JLabel("TEST ME:"));
        cont.add(button);
        jFrame.pack();
        jFrame.setVisible(true);
    }

}//