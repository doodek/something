package com.example.brixx.models.wallbuilder;
import com.example.brixx.models.Robot;
import com.example.brixx.models.Wall;
import com.example.brixx.models.brick.Brick;

import java.util.*;

public abstract class WallBuilder {

    Queue<Brick> buildOrder;
    Robot robot;
    /// The spanList is there for visualization of where the robot was at which step
    List<Robot.Span> spanList = new ArrayList<>();

    Wall wall;
    List<List<Brick>> strideBuildOrders;
    Direction direction = Direction.RIGHT;
    Direction previousDirection = Direction.RIGHT;

    enum Direction {
        LEFT, RIGHT, UP;
    }

    public WallBuilder(Wall wall, Robot robot) {
        buildOrder = new LinkedList<>();
        this.robot = robot;
        this.wall = wall;
    }

    public void buildWall() {

    }

    public Brick placeNextBrick() {
        Brick b = buildOrder.poll();
        if(b != null) {
            b.place();
        }
        return b;
    }


    public Robot.Span getSpanForBrick(Brick b){
        return spanList.get(b.getStride());
    }

}
