package org.gui.Proiect;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.util.Stack;

public class Proiect {
    private JFrame frame;
    private DrawArea drawArea;
    private String drawShape = "Pencil";
    private int thickness = 4;

    public Proiect() {
        frame = new JFrame("Proiect");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        drawArea = new DrawArea();

        JToolBar toolbar = new JToolBar();
        JButton redButton = new JButton("Red");
        JButton blueButton = new JButton("Blue");
        JButton yellowButton = new JButton("Yellow");
        JButton greenButton = new JButton("Green");
        JButton eraserButton = new JButton("Eraser");
        JButton squareButton = new JButton("Square");
        JButton circleButton = new JButton("Circle");
        JButton triangleButton = new JButton("Triangle");

        JSlider thicknessSlider = new JSlider(1, 10, 4);
        thicknessSlider.addChangeListener((ChangeEvent e) -> thickness = thicknessSlider.getValue());

        redButton.addActionListener((ActionEvent e) -> {
            drawArea.setColor(Color.RED);
            drawShape = "Pencil";
        });
        blueButton.addActionListener((ActionEvent e) -> {
            drawArea.setColor(Color.BLUE);
            drawShape = "Pencil";
        });
        yellowButton.addActionListener((ActionEvent e) -> {
            drawArea.setColor(Color.YELLOW);
            drawShape = "Pencil";
        });
        greenButton.addActionListener((ActionEvent e) -> {
            drawArea.setColor(Color.GREEN);
            drawShape = "Pencil";
        });

        eraserButton.addActionListener((ActionEvent e) -> drawShape = "Eraser");
        squareButton.addActionListener((ActionEvent e) -> drawShape = "Square");
        circleButton.addActionListener((ActionEvent e) -> drawShape = "Circle");
        triangleButton.addActionListener((ActionEvent e) -> drawShape = "Triangle");

        toolbar.add(redButton);
        toolbar.add(blueButton);
        toolbar.add(yellowButton);
        toolbar.add(greenButton);
        toolbar.add(eraserButton);
        toolbar.add(squareButton);
        toolbar.add(circleButton);
        toolbar.add(triangleButton);
        toolbar.add(thicknessSlider);

        frame.add(drawArea, BorderLayout.CENTER);
        frame.add(toolbar, BorderLayout.NORTH);
        frame.setVisible(true);
    }

    public class DrawArea extends JComponent {
        private Image image;
        private Graphics2D g2;
        private int currentX, currentY, oldX, oldY;
        private Stack<Image> undoStack = new Stack<>();
        private boolean isDrawing = false;

        public DrawArea() {
            setDoubleBuffered(false);

            addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    oldX = e.getX();
                    oldY = e.getY();
                    if (!drawShape.equals("Pencil") && !drawShape.equals("Eraser")) {
                        drawShape(e);
                    }
                }
            });

            addMouseMotionListener(new MouseMotionAdapter() {
                public void mouseDragged(MouseEvent e) {
                    currentX = e.getX();
                    currentY = e.getY();

                    if (g2 != null && (drawShape.equals("Pencil") || drawShape.equals("Eraser"))) {
                        g2.setStroke(new BasicStroke(thickness));
                        if (drawShape.equals("Eraser")) {
                            g2.setPaint(Color.white);
                        }
                        g2.drawLine(oldX, oldY, currentX, currentY);
                        repaint();
                        oldX = currentX;
                        oldY = currentY;
                        if (drawShape.equals("Eraser")) {
                            g2.setPaint(Color.black);
                        }
                    }
                }
            });

            // Add key binding for Ctrl+Z to perform undo
            getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK), "Undo");
            getActionMap().put("Undo", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    undoLastOperation();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (image == null) {
                image = createImage(getSize().width, getSize().height);
                g2 = (Graphics2D) image.getGraphics();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                clear();
            } else {
                // Push the current image into the stack before drawing
                undoStack.push(copyImage(image));
            }

            g.drawImage(image, 0, 0, null);
        }

        public void setColor(Color color) {
            g2.setPaint(color);
        }

        public void clear() {
            g2.setPaint(Color.white);
            g2.fillRect(0, 0, getSize().width, getSize().height);
            g2.setPaint(Color.black);
            repaint();
        }

        private void drawShape(MouseEvent e) {
            g2.setStroke(new BasicStroke(1));  // Set stroke width to 1 for shape outline
            Color currentColor = g2.getColor(); // Memorize current color

            isDrawing = true;
            switch (drawShape) {
                case "Square":
                    g2.fill(new Rectangle2D.Double(oldX - 25 * thickness, oldY - 25 * thickness, 50 * thickness, 50 * thickness));
                    break;
                case "Circle":
                    g2.fill(new Ellipse2D.Double(oldX - 25 * thickness, oldY - 25 * thickness, 50 * thickness, 50 * thickness));
                    break;
                case "Triangle":
                    Path2D triangle = new Path2D.Double();
                    triangle.moveTo(oldX, oldY - 25 * thickness);
                    triangle.lineTo(oldX - 25 * thickness, oldY + 25 * thickness);
                    triangle.lineTo(oldX + 25 * thickness, oldY + 25 * thickness);
                    triangle.closePath();
                    g2.fill(triangle);
                    break;
            }
            isDrawing = false;

            g2.setColor(currentColor); // Reset to the previous color
            g2.setStroke(new BasicStroke(thickness));  // Reset stroke width for drawing
            repaint();
        }

        private void undoLastOperation() {
            if (!undoStack.isEmpty()) {
                image = undoStack.pop();
                repaint();
            }
        }

        private Image copyImage(Image img) {
            BufferedImage copy = new BufferedImage(getSize().width, getSize().height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = copy.createGraphics();
            g.drawImage(img, 0, 0, null);
            g.dispose();
            return copy;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Proiect::new);
    }
}
