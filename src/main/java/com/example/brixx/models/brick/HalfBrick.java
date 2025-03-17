package com.example.brixx.models.brick;

import com.example.brixx.models.Course;

public class HalfBrick extends Brick {
    public static final double WIDTH = 100.0;

    public HalfBrick(double xPos, double yPos) {
        super(xPos, yPos, WIDTH);
    }

    public HalfBrick(double xPos, double yPos, Course parent) {
        super(xPos, yPos, WIDTH, parent);
    }
}
