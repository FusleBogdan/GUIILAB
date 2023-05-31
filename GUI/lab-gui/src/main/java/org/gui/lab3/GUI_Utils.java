package org.gui.lab3;
import com.jogamp.opengl.GL2;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GUI_Utils {
    public static void setGlColor(GL2 gl, Color color){
        gl.glColor3f((float) color.getRed()/255.0f, (float) color.getGreen()/255.0f, (float) color.getBlue()/255.0f);
    }

    public static class Coordinates2D{
        public Float x;
        public Float y;

        public Coordinates2D(Float x, Float y){
            this.x = x;
            this.y = y;
        }

        public float[] getVectorValues(){
            return (new float[]{this.x, this.y, 0});
        }
    }

    public static class DrawRectangle implements DrawShape {
        List<Coordinates2D> squareCoordinates;

        public DrawRectangle(float x1, float y1, float x2, float y2){
            this.squareCoordinates = new ArrayList<>();
            squareCoordinates.add(new GUI_Utils.Coordinates2D(x1, y1));
            squareCoordinates.add(new GUI_Utils.Coordinates2D(x1, y2));
            squareCoordinates.add(new GUI_Utils.Coordinates2D(x2, y2));
            squareCoordinates.add(new GUI_Utils.Coordinates2D(x2, y1));
        }

        // Draw rectangle with GL_LINE_LOOP mode
        @Override
        public void draw(GL2 gl){
            //edge1,2,3,4
            gl.glBegin( GL2.GL_LINE_LOOP );
            gl.glVertex3f(squareCoordinates.get(0).x, squareCoordinates.get(0).y,0 );
            gl.glVertex3f(squareCoordinates.get(1).x, squareCoordinates.get(1).y,0 );
            gl.glVertex3f( squareCoordinates.get(2).x,squareCoordinates.get(2).y,0 );
            gl.glVertex3f( squareCoordinates.get(3).x,squareCoordinates.get(3).y,0 );
            gl.glEnd();
        }

        @Override
        public void drawFill(GL2 gl) {
            gl.glBegin( GL2.GL_POLYGON );
            gl.glVertex3f(squareCoordinates.get(0).x, squareCoordinates.get(0).y,0 );
            gl.glVertex3f(squareCoordinates.get(1).x, squareCoordinates.get(1).y,0 );
            gl.glVertex3f( squareCoordinates.get(2).x,squareCoordinates.get(2).y,0 );
            gl.glVertex3f( squareCoordinates.get(3).x,squareCoordinates.get(3).y,0 );
            gl.glEnd();
        }
    }

    public static class DrawCircle implements DrawShape {
        private final Integer verticsNumber;
        private final Float radius;
        private final GUI_Utils.Coordinates2D pos;

        public DrawCircle(float x, float y, int nrVertics, float radius){
            this.verticsNumber = nrVertics;
            this.radius = radius;
            this.pos = new GUI_Utils.Coordinates2D(x, y);
        }

        @Override
        public void draw(GL2 gl){
            gl.glBegin(GL2.GL_LINE_LOOP);
            for(int i=0; i<verticsNumber; i++){
                double angle = 2.0 * Math.PI * ((double) i / (double) verticsNumber);
                float x = (float) (radius * Math.cos(angle));
                float y = (float) (radius * Math.sin(angle));
                gl.glVertex2f(x + pos.x, y + pos.y);
            }
            gl.glEnd();
        }

        @Override
        public void drawFill(GL2 gl) {
            gl.glBegin(GL2.GL_POLYGON);
            for(int i=0; i<verticsNumber; i++){
                double angle = 2.0 * Math.PI * ((double) i / (double) verticsNumber);
                float x = (float) (radius * Math.cos(angle));
                float y = (float) (radius * Math.sin(angle));
                gl.glVertex2f(x + pos.x, y + pos.y);
            }
            gl.glEnd();
        }
    }

    public static class DrawTriangle implements DrawShape {
        protected List<GUI_Utils.Coordinates2D> triangleCoordinates;

        protected DrawTriangle(){ }

        public DrawTriangle(float x1, float y1, float x2, float y2) {
            this.triangleCoordinates = new ArrayList<>();
            triangleCoordinates.add(new GUI_Utils.Coordinates2D(x1, y1));
            triangleCoordinates.add(new GUI_Utils.Coordinates2D(x1 + (x2-x1)/2 , y2));
            triangleCoordinates.add(new GUI_Utils.Coordinates2D(x2, y1));
        }

        // Draw triangle with GL_LINES mode
        @Override
        public void draw(GL2 gl) {
            //edge1
            gl.glBegin(GL2.GL_LINES);
            gl.glVertex3f(triangleCoordinates.get(0).x, triangleCoordinates.get(0).y, 0);
            gl.glVertex3f(triangleCoordinates.get(1).x, triangleCoordinates.get(1).y, 0);
            gl.glEnd();

            //edge2
            gl.glBegin(GL2.GL_LINES);
            gl.glVertex3f(triangleCoordinates.get(1).x, triangleCoordinates.get(1).y, 0);
            gl.glVertex3f(triangleCoordinates.get(2).x, triangleCoordinates.get(2).y, 0);
            gl.glEnd();

            //edge3
            gl.glBegin(GL2.GL_LINES);
            gl.glVertex3f(triangleCoordinates.get(2).x, triangleCoordinates.get(2).y, 0);
            gl.glVertex3f(triangleCoordinates.get(0).x, triangleCoordinates.get(0).y, 0);
            gl.glEnd();
        }

        @Override
        public void drawFill(GL2 gl) {
            gl.glBegin(GL2.GL_TRIANGLES);
            gl.glVertex3fv(triangleCoordinates.get(0).getVectorValues(), 0);
            gl.glVertex3fv(triangleCoordinates.get(1).getVectorValues(), 0);
            gl.glVertex3fv(triangleCoordinates.get(2).getVectorValues(), 0);
            gl.glEnd();

        }
    }

    public static class DrawRightTriangle extends GUI_Utils.DrawTriangle{
        public DrawRightTriangle(float x1, float y1, float x2, float y2){
            super();
            this.triangleCoordinates = new ArrayList<>();
            triangleCoordinates.add(new GUI_Utils.Coordinates2D(x1, y1));
            triangleCoordinates.add(new GUI_Utils.Coordinates2D(x1, y2));
            triangleCoordinates.add(new GUI_Utils.Coordinates2D(x2, y1));
        }
    }
}