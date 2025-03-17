package com.example.brixx.models;

import com.example.brixx.models.brick.Brick;

public final class Robot {

    public static class Span {
        public double x_min;
        public double y_min;
        public double x_max;
        public double y_max;

        public Span(double x_min, double y_min, double x_max, double y_max) {
            this.x_min = x_min;
            this.y_min = y_min;
            this.x_max = x_max;
            this.y_max = y_max;
        }
    }

    public static class Position {
        public double x;
        public double y;
    }

    public double envelope_width = 800.0f;
    public double envelope_height = 1300.0f;
    private Position position = new Position();

    public Robot(double x, double y) {
        this.position.x = x;
        this.position.y = y;
    }

    public Robot(double x, double y, double envelope_width, double envelope_height) {
        this.position.x = x;
        this.position.y = y;
        this.envelope_width = envelope_width;
        this.envelope_height = envelope_height;
    }


    public double getX() {
        return position.x;
    }

    public double getY() {
        return position.y;
    }

    public double getEnvelope_width() {
        return envelope_width;
    }

    public double getEnvelope_height() {
        return envelope_height;
    }

    public void move(double x, double y){
        this.position.x += x;
        this.position.y += y;
    }

    public void moveTo(double x, double y){
        this.position.x = x;
        this.position.y = y;
    }

    public void moveTo(Position position){
        this.position.x = position.x;
        this.position.y = position.y;
    }

    public Span getSpan() {
        return new Span(position.x, position.y, position.x + envelope_width, position.y + envelope_height);
    }

    public boolean isWithinHorizontalRange(Brick brick) {
        return (brick.getPosition_x() + brick.getWidth() <= this.getSpan().x_max)
                && brick.getPosition_x() >= this.getSpan().x_min;
    }

    public boolean isWithinVerticalRange(Brick brick) {
        return (brick.getPosition_y() + brick.getHeight() <= this.getSpan().y_max)
                && brick.getPosition_y() >= this.getSpan().y_min;
    }

    public boolean isWithinRange(Brick brick) {
        return isWithinHorizontalRange(brick) && isWithinVerticalRange(brick);
    }

    public Position getPosition() {
        return position;
    }
}
