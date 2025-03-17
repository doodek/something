package com.example.brixx.models.brick;

import com.example.brixx.models.Course;

import java.util.ArrayList;
import java.util.List;

public abstract class Brick {

    double position_x;
    double position_y;
    double width;
    double height;
    boolean placed = false;
    int stride = -1;
    Course parent;

    public static final double BRICK_HEIGHT = 50.0;
    public static final double BED_JOINT_HEIGHT = 12.5;
    public static final double HEAD_JOINT_WIDTH = 10.0;


    public Brick(double position_x, double position_y, double width, int stride) {
        this.position_x = position_x;
        this.position_y = position_y;
        this.width = width;
        this.height = 50.0;
        this.stride = stride;
    }


    public Brick(double position_x, double position_y, double width, Course parent) {
        this.position_x = position_x;
        this.position_y = position_y;
        this.width = width;
        this.height = 50.0;
        this.parent = parent;
    }

    public Brick(double position_x, double position_y, double width) {
        this.position_x = position_x;
        this.position_y = position_y;
        this.width = width;
        this.height = 50.0;
    }

    public double getPosition_x() {
        return position_x;
    }

    public double getPosition_y() {
        return position_y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public boolean isPlaced() {
        return placed;
    }

    public void place() {
        placed = true;
    }

    public void setStride(int stride) {
        this.stride = stride;
    }

    public int getStride() {
        return stride;
    }

    public Course getParent() {
        return parent;
    }

    public List<Brick> getBricksBelow(){
        if(parent == null || parent.getBelow() == null || parent.getBelow().getBricks() == null){
            return new ArrayList<>();
        }
        List<Brick> bricks = new ArrayList<>(this.parent.getBelow().getBricks());
        bricks.removeIf(brick -> (brick.position_x + brick.width < this.position_x || brick.position_x > this.position_x + this.width));
        return bricks;
    }
}
