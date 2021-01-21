package ru.rsreu.gorkin.codeanalyzer.desktop.elements.panels;

import ru.rsreu.gorkin.codeanalyzer.desktop.elements.graphics.Arrow;
import ru.rsreu.gorkin.codeanalyzer.desktop.elements.utils.PathFinder;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.List;

public class ClassDiagramPanel extends JPanel {
    Graphics2D graphics;
    List<JButton> classes;
    Color arrowColor = new Color(41, 50, 65 );

    public ClassDiagramPanel(List<JButton> classes) {
        super(new GridLayout(0, 2, 40, 40));
        setBorder(new EmptyBorder(30, 30, 30, 30));
        this.classes = classes;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        graphics = (Graphics2D) g;
        for (JButton button : classes) {
//            drawArrow(
//                    0,
//                    0,
//                    button.getX(),
//                    button.getY() + (double) button.getHeight() / 2);


            List<Point2D> points = new PathFinder().findPath(
                    new Point2D.Double(0, getHeight()/2.0),
//                    classes.stream().filter(jButton -> jButton != button).toArray(JComponent[]::new),
                    classes.toArray(new JButton[0]),
                    button
            );

            Color oldColor = g.getColor();
            g.setColor(arrowColor);
            for (int i = 0; i < points.size() - 1; i++) {
                g.drawLine((int) points.get(i).getX(),
                        (int) points.get(i).getY(),
                        (int) points.get(i + 1).getX(),
                        (int) points.get(i + 1).getY());
                if (i == points.size() - 2){
                    drawArrow(
                            (int) points.get(i).getX(),
                            (int) points.get(i).getY(),
                            (int)points.get(i+1).getX(),
                            (int)points.get(i+1).getY());
                }
            }
            g.setColor(oldColor);
        }


//        drawArrow(50, 50, 100, 100);
//        drawArrow(50, 50, 50, 100);

    }

    private void drawArrow(double x1, double y1, double x2, double y2) {
        if (graphics != null) {
            Arrow.draw(graphics,
                    new Point2D.Double(x1, y1),
                    new Point2D.Double(x2, y2),
                    new BasicStroke(1),
                    new BasicStroke(1),
                    10,
                    arrowColor);
        }
    }
}
