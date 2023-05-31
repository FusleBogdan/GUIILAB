package org.gui.lab3;


import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;

import javax.swing.*;
import java.awt.*;

public class ChesBoard extends JFrame implements GLEventListener {

    private final Integer windowWidth = 800;
    private final Integer windowHeight = 800;

    public ChesBoard() {
        super("ChessBoard Exercise");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setSize(windowWidth, windowHeight);

        this.initializeJogl();

        this.setVisible(true);
    }

    private void initializeJogl() {
        // Creating a new GL profile.
        GLProfile glprofile = GLProfile.getDefault();
        // Creating an object to manipulate OpenGL parameters.
        GLCapabilities capabilities = new GLCapabilities(glprofile);

        // Setting some OpenGL parameters.
        capabilities.setHardwareAccelerated(true);
        capabilities.setDoubleBuffered(true);

        // Try to enable 2x anti aliasing. It should be supported on most hardware.
        capabilities.setNumSamples(2);
        capabilities.setSampleBuffers(true);

        // Creating an OpenGL display widget -- canvas.
        GLCanvas canvas = new GLCanvas(capabilities);

        // Adding the canvas in the center of the frame.
        this.getContentPane().add(canvas);

        // Adding an OpenGL event listener to the canvas.
        canvas.addGLEventListener(this);


    }

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();

        gl.glEnable(GL2.GL_CULL_FACE);

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(-1f, 1f, -1f, 1f, -1f, 1f);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();


        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {

    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);

        chessboard(gl);
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glViewport(0, 0, windowWidth, windowHeight);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        float aspect = (float) windowWidth / (float) windowHeight;
        if (windowWidth <= windowHeight) {
            gl.glOrtho(-1.0, 1.0, -1.0 / aspect, 1.0 / aspect, -1.0, 1.0);
        } else {
            gl.glOrtho(-1.0 * aspect, 1.0 * aspect, -1.0, 1.0, -1.0, 1.0);
        }
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    private void chessboard(GL2 gl){
        final int squares = 8;

        boolean white = true;
        float posX = -1f, posY = 1.0f;
        float size = 2f/squares;

        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
        GUI_Utils.setGlColor(gl, Color.WHITE);
        for(int i=0; i<squares; i++){
            for(int j=0; j<squares; j++){
                gl.glCullFace(white ? GL.GL_FRONT : GL.GL_BACK);
                white = !white;
                gl.glRectf(posX, posY, posX+size, posY-size);
                posX+=size;
            }
            white = !white;
            posX = -1f;
            posY -= size;
        }

    }
    public static void main(String[] args) {
        GLCanvas canvas = new GLCanvas();
        //canvas.addGLEventListener(new Lab3.ChesBoard());
    }
}
