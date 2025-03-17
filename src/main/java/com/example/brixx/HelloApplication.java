package com.example.brixx;
// JavaFX Version
import com.example.brixx.models.*;
import com.example.brixx.models.brick.Brick;
import com.example.brixx.models.wallbuilder.FlemishBondBuilder;
import com.example.brixx.models.wallbuilder.StretcherBondBuilder;
import com.example.brixx.models.wallbuilder.WallBuilder;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HelloApplication extends Application {
    // This is hardcoded on the app-level, however, the Wall class
    // is designed to support many sizes (in theory...)
    private final static int WALL_WIDTH = 2300;
    private final static int WALL_HEIGHT = 2000;
    private final static int SCALE_FACTOR = 4;
    // The order, in which the bricks will be placed.
    // A brick can be just planned, or planned and placed
    Map<Brick, Rectangle> displayMap = new HashMap<>();
    Rectangle robotEnvelopeVisualization;
    WallBuilder stretcher;

    @Override
    public void start(Stage stage) throws IOException {
        // JavaFX setup.
        // Basically creates a scene and reflects it in y-axis,
        // so that we can work in cartesian coordinates (yippee!)
        Pane root = new Pane();
        Scene scene = new Scene(root, WALL_WIDTH / SCALE_FACTOR + 1 , WALL_HEIGHT / SCALE_FACTOR + 1);
        stage.setTitle("brixx by doodek for Monumental");
        stage.setScene(scene);
        stage.show();
        // magic! reflection using scale and translation vectors
        root.getTransforms().addAll(
                new Scale(1, -1),
                new Translate(0, -1 * (WALL_HEIGHT / SCALE_FACTOR + 1))
        );

        Wall wall = new Wall(WALL_WIDTH, WALL_HEIGHT);
        Robot robot = new Robot(0, 0, 800, 1300);
        stretcher = new StretcherBondBuilder(wall, robot);
        // Here all stuff happens. The WallBuilder plans the layout of the wall
        // and order in which the bricks should be placed
        stretcher.buildWall();
        setUpWallDisplay(wall, root);

        robotEnvelopeVisualization = new Rectangle(robot.getX() / 4, robot.getY() / 4, robot.getEnvelope_width() / 4, robot.getEnvelope_height() / 4);
        robotEnvelopeVisualization.setFill(Color.RED);
        robotEnvelopeVisualization.setOpacity(0.2);
        root.getChildren().add(robotEnvelopeVisualization);

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                updateBrickDisplay(stretcher.placeNextBrick());
            }
        });

    }

    public static void main(String[] args) {
        launch();
    }

    public void setUpWallDisplay(Wall wall, Pane root){
        Rectangle wallRectangle = new Rectangle((double) WALL_WIDTH / 4, (double) WALL_HEIGHT / 4);
        wallRectangle.setFill(Color.GRAY);
        root.getChildren().add(wallRectangle);
        for(Course c : wall.getCourses()){
            for(Brick b : c.getBricks()){
                if(!displayMap.containsKey(b)){
                    double scaledX = b.getPosition_x() / 4.0;
                    double scaledY = b.getPosition_y() / 4.0;
                    double scaledW = b.getWidth() / 4.0;
                    double scaledH = b.getHeight() / 4.0;
                    Rectangle r = new Rectangle(scaledX, scaledY, scaledW, scaledH);
                    if(b.getStride() % 2 == 0) {
                        r.setFill(Color.WHEAT);
                    } else {
                        r.setFill(Color.BEIGE);
                    }
                    root.getChildren().add(r);
                    Text strideText = new Text(String.valueOf(b.getStride()));
                    strideText.setFill(Color.BLACK);
                    strideText.setX(scaledX + scaledW / 2.0);
                    strideText.setY(scaledY + scaledH / 2.0);
                    root.getChildren().add(strideText);
                    strideText.setX(strideText.getX() - strideText.getLayoutBounds().getWidth() / 2.0);
                    strideText.setY(strideText.getY() + strideText.getLayoutBounds().getHeight() / 4.0);
                    strideText.setScaleY(-1);
                    displayMap.put(b, r);
                }
            }
        }
    }

    public void updateBrickDisplay(Brick b){
        Rectangle r = displayMap.get(b);
        Robot.Span strideSpan = stretcher.getSpanForBrick(b);
        robotEnvelopeVisualization.setX(strideSpan.x_min / 4.0);
        robotEnvelopeVisualization.setY(strideSpan.y_min / 4.0);
        if(r != null){
            r.setFill(Color.BROWN);
        }
    }
}
