package com.github.coderodde.javafx;

import java.time.Duration;
import java.util.Random;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
 
public class PieChart3DDemo extends Application {
    
    private static final double DIMENSION = 500.0;
    private static final double CANVAS_DIMENSION = 400.0;
    private static final int MAXIMUM_NUMBER_OF_SECTORS = 20;
    private static final double MAXIMUM_VALUE = 200.0;
    private static final double FULL_ANGLE = 360.0;
    private static final DemoTask DEMO_TASK = new DemoTask();
    
    public static void main(String[] args) {
        Runtime.getRuntime()
               .addShutdownHook(
                       new Thread(() -> {
                           DEMO_TASK.stop();
                       }));
        
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("PieChart3D demo");
        StackPane root = new StackPane();
        
        primaryStage.setScene(
                new Scene(root, 
                          DIMENSION,
                          DIMENSION));
        
        DEMO_TASK.setRoot(root);
        DEMO_TASK.setStage(primaryStage);
        
        new Thread(DEMO_TASK).start();
        
        primaryStage.show();
    }
    
    private static Color getRandomColor(Random random) {
        return new Color(random.nextDouble(),
                         random.nextDouble(),
                         random.nextDouble(),
                         1.0);
    }
    
    private static int getRandomNumberOfSectors(Random random) {
        return random.nextInt(MAXIMUM_NUMBER_OF_SECTORS + 1);
    }
    
    private static double getRandomValue(Random random) {
        return MAXIMUM_VALUE * random.nextDouble();
    }
    
    private static double getRandomAngleOffset(Random random) {
        return FULL_ANGLE * random.nextDouble();
    }
    
    static PieChart3D getRandomChart(Random random) {
        double angleOffset = getRandomAngleOffset(random);
        int numberOfSectors = getRandomNumberOfSectors(random);
        
        PieChart3D chart = new PieChart3D(CANVAS_DIMENSION);
        
        chart.setAngleOffset(angleOffset);
        chart.setOriginalIntensityColor(getRandomColor(random));
        
        for (int i = 0; i < numberOfSectors; i++) {
            PieChart3DEntry entry = 
                    new PieChart3DEntry()
                        .withSectorAngleValue(getRandomValue(random))
                        .withSectorColorIntensityValue(getRandomValue(random))
                        .withSectorRadiusValue(getRandomValue(random));
            
            chart.add(entry);
        }
        
        return chart;
    }
}

final class DemoTask extends Task<Void> {

    private static final long SLEEP_DURATION_SECONDS = 2L;
    
    private volatile boolean doRun = true;
    private final Random random = new Random();
    private StackPane root;
    private Stage stage;
    
    void stop() {
        doRun = false;
    }
    
    void setRoot(StackPane root) {
        this.root = root;
    }
    
    void setStage(Stage stage) {
        this.stage = stage;
    }
    
    @Override
    protected Void call() throws Exception {
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            
            @Override
            public void handle(WindowEvent t) {
                System.out.println("Exiting...");
                Platform.exit();
                System.exit(0);
            }
        });
        
        int iterations = 0;
                
        while (doRun) {
            try {
                Thread.sleep(Duration.ofSeconds(SLEEP_DURATION_SECONDS).toMillis());
            } catch (InterruptedException ex) {}
            
            PieChart3D chart = PieChart3DDemo.getRandomChart(random);
            
            Platform.runLater(() -> {
                root.getChildren().clear();
                root.getChildren().add(chart);
                chart.draw();
            });
            
            System.out.println("Iterated: " + iterations++);
        }
        
        return null;
    }
}