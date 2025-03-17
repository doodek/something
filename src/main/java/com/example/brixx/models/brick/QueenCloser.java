package com.example.brixx.models.brick;

import com.example.brixx.models.Course;

public class QueenCloser extends Brick {
    public static final double WIDTH = 45.0;

    public QueenCloser(double xPos, double yPos) {
        super(xPos, yPos, WIDTH);
    }

    public QueenCloser(double xPos, double yPos, Course parent) {
        super(xPos, yPos, WIDTH, parent);
    }
}
