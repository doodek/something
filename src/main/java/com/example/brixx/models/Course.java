package com.example.brixx.models;

import com.example.brixx.models.brick.Brick;
import com.example.brixx.models.brick.FullBrick;
import com.example.brixx.models.brick.HalfBrick;
import com.example.brixx.models.brick.QueenCloser;

import java.util.ArrayList;
import java.util.List;

public class Course {
    ///  Height of a course
    public static final double COURSE_HEIGHT = Brick.BED_JOINT_HEIGHT + Brick.BRICK_HEIGHT;
    ///  Vertical position of the course
    double yPos;
    ///  Planned / Maximum width of the course
    double widthPlanned;
    ///  List of bricks within a single course
    List<Brick> bricks;

    Course below;

    public Course(double yPos, double widthPlanned) {
        this.yPos = yPos;
        this.widthPlanned = widthPlanned;
        bricks = new ArrayList<>();
    }

    public Course(double yPos, double widthPlanned, Course below) {
        this.yPos = yPos;
        this.widthPlanned = widthPlanned;
        bricks = new ArrayList<>();
        this.below = below;
    }

    public void addBrick(Brick brick) {
        bricks.add(brick);
    }

    public List<Brick> getBricks() {
        return bricks;
    }

    public int bricksCount() {
        return bricks.size();
    }

    public double getYPos() {
        return yPos;
    }

    public Brick addFullBrick(){
        Brick brick;
        if(bricks.isEmpty()) {
            brick = new FullBrick(0.0, yPos, this);
        } else {
            Brick lastBrick = bricks.getLast();
            double left_wall_coordinate = lastBrick.getPosition_x() + lastBrick.getWidth() + 10.0;
            brick = new FullBrick(left_wall_coordinate, yPos, this);
            if(brick.getPosition_x() + brick.getWidth() > widthPlanned) {
                throw new IllegalStateException("Brick position out of bounds");
            }
        }
        bricks.add(brick);
        return brick;
    }

    public Brick addHalfBrick(){
        Brick brick;
        if(bricks.isEmpty()) {
            brick = new HalfBrick(0.0, yPos, this);
        } else {
            Brick lastBrick = bricks.getLast();
            double left_wall_coordinate = lastBrick.getPosition_x() + lastBrick.getWidth() + Brick.HEAD_JOINT_WIDTH;
            brick = new HalfBrick(left_wall_coordinate, yPos, this);
            if(brick.getPosition_x() + brick.getWidth() > widthPlanned) {
                throw new IllegalStateException("Brick position out of bounds");
            }
        }
        bricks.add(brick);
        return brick;
    }

    public double spaceLeft(){
        if(bricks.isEmpty()) {
            return widthPlanned;
        }
        Brick last = bricks.getLast();
        return widthPlanned - last.getPosition_x() - last.getWidth() - Brick.HEAD_JOINT_WIDTH;
    }

    public Brick addQueenCloser(){
        Brick brick;
        if(bricks.isEmpty()) {
            brick = new QueenCloser(0.0, yPos, this);
        } else {
            Brick lastBrick = bricks.getLast();
            double left_wall_coordinate = lastBrick.getPosition_x() + lastBrick.getWidth() + Brick.HEAD_JOINT_WIDTH;
            brick = new QueenCloser(left_wall_coordinate, yPos, this);
            if(brick.getPosition_x() + brick.getWidth() > widthPlanned) {
                throw new IllegalStateException("Brick position out of bounds");
            }
        }
        bricks.add(brick);
        return brick;
    }

    public Course getBelow() {
        return below;
    }
}
