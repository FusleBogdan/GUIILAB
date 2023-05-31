package org.gui.lab3;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.Animator;

import javax.swing.*;
import java.awt.*;

public class HouseSun extends JFrame implements GLEventListener {

    private final Integer windowWidth = 800;
    private final Integer windowHeight = 600;

    private int house;
    private float sunPos = -1.2f;
    private float sunSpeed = 0.007f;

    public HouseSun() {
        super("House Exercise");

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

        Animator animator = new Animator();

        animator.add(canvas);

        animator.start();
    }

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();

        // Activate the GL_LINE_SMOOTH state variable. Other options include
        // GL_POINT_SMOOTH and GL_POLYGON_SMOOTH.
        gl.glEnable(GL.GL_LINE_SMOOTH);

        // Activate the GL_BLEND state variable. Means activating blending.
        gl.glEnable(GL.GL_BLEND);

        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);

        // Control GL_LINE_SMOOTH_HINT by applying the GL_DONT_CARE behavior.
        // Other behaviours include GL_FASTEST or GL_NICEST.
        gl.glHint(GL.GL_LINE_SMOOTH_HINT, GL.GL_DONT_CARE);

        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        house = gl.glGenLists(1);
        // Generate the Display List
        gl.glNewList(house, GL2.GL_COMPILE);
        drawHouse(gl);
        gl.glEndList();

    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {

    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);

        GUI_Utils.setGlColor(gl, Color.YELLOW);
        GUI_Utils.DrawCircle sun = new GUI_Utils.DrawCircle(sunPos, 0.7f, 50, 0.2f);
        sun.drawFill(gl);
        sunPos += sunSpeed;
        if(sunPos >= 1.2f || sunPos <= -1.2f){
            sunSpeed *= -1;
        }

        gl.glCallList(house);
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

    // Draw a house by using GL_TRIANGLES. The Sun should be a filled yellow circle and should move through the scene from left to right.
    // Fill each section of the house with a different color. Use display lists
    private void drawHouse(GL2 gl){
        gl.glBegin(GL2.GL_TRIANGLES);

        // First triangle (blue)
        gl.glColor3f(0.0f, 0.0f, 1.0f); // blue
        gl.glVertex2f(-0.25f, -0.25f);
        gl.glVertex2f(0.25f, -0.25f);
        gl.glVertex2f(-0.25f, 0.25f);

        // Second triangle (green)
        gl.glColor3f(0.0f, 1.0f, 0.0f); // green
        gl.glVertex2f(-0.25f, 0.25f);
        gl.glVertex2f(0.25f, -0.25f);
        gl.glVertex2f(0.25f, 0.25f);

        gl.glColor3f(1.0f, 0.0f, 1.0f);
        gl.glVertex2f(-0.25f, 0.25f);
        gl.glVertex2f(0.25f, -0.25f);
        gl.glVertex2f(0.25f, 0.25f);

        gl.glColor3f(1.0f, 0.0f, 0.0f);
        gl.glVertex2f(-0.25f, 0.25f);
        gl.glVertex2f(0.25f, 0.25f);
        gl.glVertex2f(0.0f, 0.50f);
        gl.glEnd();

    }
    public static void main(String[] args) {
        // create the GLCanvas and add this as a GLEventListener
        GLCanvas canvas = new GLCanvas();
        canvas.addGLEventListener(new HouseSun());
    }
}
