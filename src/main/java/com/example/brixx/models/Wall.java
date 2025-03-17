package com.example.brixx.models;

import com.example.brixx.models.brick.Brick;

import java.util.ArrayList;
import java.util.List;

public class Wall {
    ///  The planned width of the wall
    double width;
    ///  The planned (maximum) height of the wall
    double height;
    ///  References to individual courses
    List<Course> courses;

    public Wall(double width, double height) {
        this.width = width;
        this.height = height;
        courses = new ArrayList<>();
    }

    public List<Course> getCourses() {
        return courses;
    }

    /**
     * Calculate the count of all planned bricks in all courses within a wall
     * @return count of all bricks in the wall
     */
    public int getBrickCount(){
        int brickCount = 0;
        for(Course course : courses){
            brickCount += course.bricksCount();
        }
        return brickCount;
    }

    /**
     * Returns the course that's currently the top layer of the wall
     * @return top course of the wall
     */
    public Course getTopCourse()
    {
        if(!courses.isEmpty()) {
            return courses.getLast();
        } else {
            return null;
        }
    }

    public double getWidth(){
        return width;
    }
    public double getHeight(){ return height;}



    /**
     * Creates and appends a new course on top of current top course
     * @return the newly created course
     */
    public Course createNewCourse() {
        Course newCourse;
        // If the course we place is the first one, we cannot refer to the top course
        Course topCourse = getTopCourse();
        if(topCourse != null) {
            // We plan the course on top of another course.
            double base = topCourse.getYPos();
            // We level the robot up by height of
            double newBase = base + Course.COURSE_HEIGHT;
            // Check if we grow too far
            if (newBase + Brick.BRICK_HEIGHT > height) {
                throw new IllegalStateException("Cannot plan this course placement on this wall. The layer would exceed height of the wall.");
            }
            newCourse = new Course(newBase, width, topCourse);
        } else {
            // Edge case: Single-layered wall. Like if you have really tall bricks or whatever
            if (height < Brick.BRICK_HEIGHT){
                throw new IllegalStateException("Cannot plan this course placement on this wall. The layer would exceed height of the wall.");
            }
            newCourse = new Course(0.0, width);
        }
        courses.add(newCourse);
        return newCourse;
    }


}
