package com.example.brixx.models.wallbuilder;

import com.example.brixx.models.Course;
import com.example.brixx.models.Robot;
import com.example.brixx.models.Wall;
import com.example.brixx.models.brick.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FlemishBondBuilder extends WallBuilder {
    int bricksToBePlaced = 0;

    public FlemishBondBuilder(Wall wall, Robot robot) {
        super(wall, robot);
        createWallStructure();
        robot.moveTo(0,0);
        strideBuildOrders = new ArrayList<>();
        bricksToBePlaced = wall.getBrickCount();
    }

    /**
     * Plan a complete Stretcher bond wall structure
     * This method does NOT place bricks, it plans the layout
     */
    private void createWallStructure() {

        // Create base course with a full brick
        Course baseCourse = wall.createNewCourse();
        baseCourse.addFullBrick();

        // Add additional bricks to each course and create new courses as needed
        while (true) {
            Course currentCourse = wall.getTopCourse();
            double spaceLeft = currentCourse.spaceLeft();
            if (spaceLeft >= FullBrick.WIDTH && !(currentCourse.getBricks().getLast() instanceof FullBrick)) {
                currentCourse.addFullBrick();
            } else if (spaceLeft >= HalfBrick.WIDTH /*!(currentCourse.getBricks().getLast() instanceof HalfBrick*/) {
//                if(spaceLeft >= QueenCloser.WIDTH + HalfBrick.WIDTH + Brick.HEAD_JOINT_WIDTH && spaceLeft <= FullBrick.WIDTH){
//                    currentCourse.addQueenCloser();
//                }
                currentCourse.addHalfBrick();
            } else if (spaceLeft >= QueenCloser.WIDTH) {
                currentCourse.addQueenCloser();
            } else {
                try {
                    Course newCourse = wall.createNewCourse();
                    // Alternate between full and half bricks for the first brick
                    // in each course
                    if (currentCourse.getBricks().getFirst() instanceof HalfBrick) {
                        //newCourse.addQueenCloser();
                        newCourse.addFullBrick();
                        // If a half-brick is the first one, it needs
                        // a queen closer next to it
                    } else {
                        newCourse.addHalfBrick();
                        newCourse.addQueenCloser();
                    }
                } catch (IllegalStateException e) {
                    // Cannot create another course, wall height reached
                    break;
                }
            }
        }
    }
    /**
     * Creates the wall structure and plans the build order
     */
    @Override
    public void buildWall() {
        direction = Direction.RIGHT;
        while(bricksToBePlaced > 0) {
            List<Brick> stride = createStride();
            if(!stride.isEmpty()) {
                buildOrder.addAll(stride);
                spanList.add(robot.getSpan());
            }
            previousDirection = direction;
            double deltax = 0;
            double deltay = 0;

            if (direction == Direction.UP && robot.getSpan().x_min <= 0) {
                direction = Direction.RIGHT;
            } else if (direction == Direction.UP && robot.getSpan().x_max >= wall.getWidth()){
                direction = Direction.LEFT;
            } else if(robot.getSpan().x_max >= wall.getWidth() && previousDirection == Direction.RIGHT){
                previousDirection = direction;
                direction = Direction.UP;
            } else if (robot.getSpan().x_min <= 0 && previousDirection == Direction.LEFT){
                previousDirection = direction;
                direction = Direction.UP;
            }

            switch (direction) {
                case RIGHT -> deltax = QueenCloser.WIDTH + HalfBrick.WIDTH + 2 * Brick.HEAD_JOINT_WIDTH;
                case LEFT  -> deltax = -(QueenCloser.WIDTH + HalfBrick.WIDTH + 2 * Brick.HEAD_JOINT_WIDTH);
                case UP    -> deltay = 3 * Course.COURSE_HEIGHT;
            }

            if(!stride.isEmpty() && stride.getFirst().getPosition_y() > wall.getHeight() - 3 * Course.COURSE_HEIGHT) {
                deltax *= 2;
            }

            if(stride.isEmpty())
                deltax /= 4;
            if(robot.getSpan().y_max >= wall.getHeight())
                deltay = 0;
            robot.move(deltax, deltay);
        }
        robot.moveTo(0,0);
    }

    List<Brick> bricksWithinRobotRange(Course c){
        List<Brick> bricks = new ArrayList<>(c.getBricks());
        if(direction == Direction.LEFT || previousDirection == Direction.LEFT){
            Collections.reverse(bricks);
        }
        List<Brick> filtered =  c.getBricks().stream()
                .filter(robot::isWithinRange)
                .filter(b -> b.getStride() == -1 )
                .filter(b -> b.getBricksBelow().isEmpty() ||
                         b.getBricksBelow().stream().allMatch(x -> x.getStride() != -1)
                ).toList();
        if (filtered.size() == 1 && !(filtered.getFirst() instanceof FullBrick) &&
                !filtered.getFirst().equals(bricks.getFirst()) && !filtered.getFirst().equals(bricks.getLast())){
            return Collections.emptyList();
        }
        return filtered;
    }

    public List<Brick> createStride(){
        List<Brick> stride = new ArrayList<>();
        int idx = strideBuildOrders.size();
        for(Course c : wall.getCourses()){
            List<Brick> bricksWithinRobotRange = bricksWithinRobotRange(c);
            List<Brick> courseStride = new ArrayList<>();
            for(Brick b : bricksWithinRobotRange){
                courseStride.add(b);
                b.setStride(idx);
                bricksToBePlaced--;
            }
            stride.addAll(courseStride);
        }
        if(!stride.isEmpty())
            strideBuildOrders.add(stride);
        return stride;
    }
}
