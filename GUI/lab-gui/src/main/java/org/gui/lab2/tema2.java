package org.gui.lab2;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import com.jogamp.opengl.util.Animator;

import javax.swing.*;
import java.awt.*;
import java.util.Queue;

public class tema2 extends JFrame implements GLEventListener{
    public tema2(){
        super("Java OpenGL");

        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setSize(800, 600);

        // This method will be explained later
        this.initializeJogl();

        this.setVisible(true);
    }

    private void initializeJogl(){
        // Obtaining a reference to the default GL profile
        GLProfile glProfile = GLProfile.getDefault();
        // Creating an object to manipulate OpenGL parameters.
        GLCapabilities capabilities = new GLCapabilities(glProfile);

        // Setting some OpenGL parameters.
        capabilities.setHardwareAccelerated(true);
        capabilities.setDoubleBuffered(true);

        // Creating an OpenGL display widget -- canvas.
        this.canvas = new GLCanvas();

        // Adding the canvas in the center of the frame.
        this.getContentPane().add(this.canvas);

        // Adding an OpenGL event listener to the canvas.
        this.canvas.addGLEventListener(this);

        this.animator = new Animator();

        this.animator.add(this.canvas);

        this.animator.start();
    }

    public void init(GLAutoDrawable canvas){
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable){

    }

    public void display(GLAutoDrawable canvas){
        GL2 gl = canvas.getGL().getGL2();

        //drawSquareWithGL_LINES(gl);
        //drawSquareWithGL_LINE_STRIP(gl);
        //drawSquareWithGL_LINE_LOOP(gl);
        //drawCircle(gl, 0.5f, 0.5f, 0.25f);
        drawHouseWithGL_LINES(gl);

        gl.glFlush();
    }

    public void reshape(GLAutoDrawable canvas, int left, int top, int width, int height){
        GL2 gl = canvas.getGL().getGL2();

        // Select the viewport -- the display area -- to be the entire widget.
        gl.glViewport(0, 0, width, height);

        // Determine the width to height ratio of the widget.
        double ratio = (double) width / (double) height;

        // Select the Projection matrix.
        gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);

        gl.glLoadIdentity();

        // Select the view volume to be x in the range of 0 to 1, y from 0 to 1 and z from -1 to 1.
        // We are careful to keep the aspect ratio and enlarging the width or the height.
        if (ratio < 1)
            gl.glOrtho(0, 1, 0, 1 / ratio, -1, 1);
        else
            gl.glOrtho(0, 1 * ratio, 0, 1, -1, 1);

        // Return to the Modelview matrix.
        gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
    }

    private void drawSquareWithGL_LINES(GL2 gl){
        gl.glBegin(GL.GL_LINES);
            gl.glVertex2f(.1f, .5f);
            gl.glVertex2f(.5f, .5f);

            gl.glVertex2f(.5f, .5f);
            gl.glVertex2f(.5f, .1f);

            gl.glVertex2f(.5f, .1f);
            gl.glVertex2f(.1f, .1f);

            gl.glVertex2f(.1f, .1f);
            gl.glVertex2f(.1f, .5f);
        gl.glEnd();
    }

    private void drawSquareWithGL_LINE_STRIP(GL2 gl){
        gl.glBegin(GL.GL_LINE_STRIP);
            gl.glVertex2f(.1f, .5f);
            gl.glVertex2f(.5f, .5f);
            gl.glVertex2f(.5f, .1f);
            gl.glVertex2f(.1f, .1f);
            gl.glVertex2f(.1f, .5f);
        gl.glEnd();
    }

    private void drawSquareWithGL_LINE_LOOP(GL2 gl){
        gl.glBegin(GL.GL_LINE_LOOP);
            gl.glVertex2f(.1f, .5f);
            gl.glVertex2f(.5f, .5f);
            gl.glVertex2f(.5f, .1f);
            gl.glVertex2f(.1f, .1f);
        gl.glEnd();
    }

    private void drawCircle(GL2 gl, float xCenter, float yCenter, float radius){
        double x, y, angle;

        gl.glBegin(GL2.GL_LINE_LOOP);
            for (int i = 0; i < 360; i++) {
                angle = Math.toRadians(i);
                x = radius * Math.cos(angle);
                y = radius * Math.sin(angle);
                gl.glVertex2d(xCenter + x, yCenter + y);
            }
        gl.glEnd();
    }

    private void drawHouseWithGL_LINES(GL2 gl){
        // draw the square for the house
        gl.glColor3f(0.0f, 1.0f, 0.0f);
        gl.glBegin(GL.GL_LINES);
            gl.glVertex2f(.1f, .6f);
            gl.glVertex2f(.6f, .6f);

            gl.glVertex2f(.6f, .6f);
            gl.glVertex2f(.6f, .1f);

            gl.glVertex2f(.6f, .1f);
            gl.glVertex2f(.1f, .1f);

            gl.glVertex2f(.1f, .1f);
            gl.glVertex2f(.1f, .6f);
        gl.glEnd();

        // draw the triangle for the roof
        gl.glColor3f(1.0f, 0.0f, 0.0f);
        gl.glBegin(GL.GL_LINES);
            gl.glVertex2f(.05f, .6f);
            gl.glVertex2f(.65f, .6f);

            gl.glVertex2f(.35f, .8f);
            gl.glVertex2f(.05f, .6f);

            gl.glVertex2f(.35f, .8f);
            gl.glVertex2f(.65f, .6f);
        gl.glEnd();

        // draw the sun
        gl.glColor3f(1.0f, 1.0f, 0.0f);
        gl.glBegin(GL.GL_LINE_LOOP);
            int num_segments = 50;
            float radius = 0.1f;
            for(int i = 0; i < num_segments; i++){
                float theta = (float)i / (float)num_segments * 2.0f * (float)Math.PI;
                float x = .9f + radius * (float)Math.cos(theta); // x-coordinate
                float y = .8f + radius * (float)Math.sin(theta); // y-coordinate
                gl.glVertex2f(x, y);
            }
        gl.glEnd();
    }

    private GLCanvas canvas;
    private Animator animator;
}