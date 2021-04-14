package ru.rsreu.gorkin.codeanalyzer.desktop.ui.elements.buttons;

import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;

public abstract class CustomButton extends JPanel
        implements MouseListener{

    String title = null;
    private Vector listeners = null;
    boolean hit = false;

    public CustomButton (String title){
        super();
        this.title = title;
        listeners = new Vector();
        addMouseListener(this);
    }

    public Dimension getPreferredSize(){return new Dimension(120,80);}

    public abstract void paintComponent(Graphics g);


    public void mousePressed(MouseEvent e){
        hit=true;
        repaint();
    }

    public void mouseReleased(MouseEvent e){
        hit=false;
        repaint();
    }

    public void mouseClicked(MouseEvent e){
        fireEvent(new ActionEvent(this,0,title));
    }

    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}

    public void addActionListener(ActionListener listener){
        listeners.addElement(listener);
    }

    public void removeActionListener(ActionListener listener){
        listeners.removeElement(listener);
    }

    private void fireEvent(ActionEvent event){
        for (int i = 0;i<listeners.size() ;i++ ){
            ActionListener listener = (ActionListener)listeners.elementAt(i);
            listener.actionPerformed(event);
        };
    }

}