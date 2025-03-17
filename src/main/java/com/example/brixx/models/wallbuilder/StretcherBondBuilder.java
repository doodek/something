package com.example.brixx.models.wallbuilder;
import com.example.brixx.models.Course;
import com.example.brixx.models.Robot;
import com.example.brixx.models.Wall;
import com.example.brixx.models.brick.Brick;
import com.example.brixx.models.brick.FullBrick;
import com.example.brixx.models.brick.HalfBrick;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StretcherBondBuilder extends WallBuilder {
    ///  Counts how many bricks we have left to place
    int bricksToBePlaced = 0;

    public StretcherBondBuilder(Wall wall, Robot robot) {
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
            // If there is enough space for a full brick, place it
            Course currentCourse = wall.getTopCourse();
            double spaceLeft = currentCourse.spaceLeft();
            if (spaceLeft >= FullBrick.WIDTH) {
                currentCourse.addFullBrick();
            } else if (spaceLeft >= HalfBrick.WIDTH) {
                currentCourse.addHalfBrick();
            } else {
                try {
                    Course newCourse = wall.createNewCourse();
                    // Alternate between full and half bricks for the first brick in each course
                    if (currentCourse.getBricks().getFirst() instanceof FullBrick) {
                        newCourse.addHalfBrick();
                    } else {
                        newCourse.addFullBrick();
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
        // The robot walks in a snake-like pattern
        while(bricksToBePlaced > 0) {
            List<Brick> stride = createStride();
            buildOrder.addAll(stride);
            // The spanList is there for visualization of where the robot was at which step
            spanList.add(robot.getSpan());
            previousDirection = direction;
            double x_step = 0;
            double y_step = 0;

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
                case RIGHT -> x_step = FullBrick.WIDTH + Brick.HEAD_JOINT_WIDTH;
                case LEFT  -> x_step = -(FullBrick.WIDTH + Brick.HEAD_JOINT_WIDTH);
                case UP    -> y_step = 5 * Course.COURSE_HEIGHT;
            }
            // In case we are close enough to the top, we double the horizontal step, to minimize unused potential
            // of the robot span
            if(!stride.isEmpty() && stride.getFirst().getPosition_y() > wall.getHeight() - 3 * Course.COURSE_HEIGHT) {
                x_step *= 2;
            }
            if(robot.getSpan().y_max >= wall.getHeight())
                y_step = 0;
            robot.move(x_step, y_step);
        }
        robot.moveTo(0,0);
    }

    List<Brick> bricksWithinRobotRange(Course c){
        List<Brick> bricks = c.getBricks();
        if(direction == Direction.LEFT || previousDirection == Direction.LEFT){
            Collections.reverse(bricks);
        }
        List<Brick> filtered =  c.getBricks().stream()
                .filter(robot::isWithinRange)
                .filter(b -> b.getStride() == -1 )
                .filter(b -> b.getBricksBelow().isEmpty() ||
                         b.getBricksBelow().stream().allMatch(x -> x.getStride() != -1)
                ).toList();
        if (filtered.size() == 1 && filtered.get(0) instanceof HalfBrick) {
            return Collections.emptyList();
        }
        return filtered;
    }

    /**
     * Return all "placable" bricks, from the perspective of the robot
     * at the current walls' state
     * @return
     */
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
        strideBuildOrders.add(stride);
        return stride;
    }


}
