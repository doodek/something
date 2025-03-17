package com.example.brixx.models.brick;

import com.example.brixx.models.Course;

public class FullBrick extends Brick {
    public static final double WIDTH = 210.0;

    public FullBrick(double xPos, double yPos) {
        super(xPos, yPos, WIDTH);
    }
    public FullBrick(double xPos, double yPos, Course parent) {
        super(xPos, yPos, WIDTH, parent);
    }

}
