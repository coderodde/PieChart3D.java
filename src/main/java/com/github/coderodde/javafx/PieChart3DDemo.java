package com.github.coderodde.javafx;

import java.util.Random;
import java.util.Scanner;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
 
public class PieChart3DDemo extends Application {
    
    static final double DIMENSION = 500.0;
    private static final double CANVAS_DIMENSION = 400.0;
    private static final int MAXIMUM_NUMBER_OF_SECTORS = 20;
    private static final double MAXIMUM_VALUE = 200.0;
    private static final double FULL_ANGLE = 360.0;
    private static final Color CHART_BACKGROUND_COLOR = new Color(0.9, 
                                                                  0.9,
                                                                  0.9,
                                                                  1.0);
    private static final DemoTask DEMO_TASK = new DemoTask();
    private static final ControlledDemoTask CONTROLLED_DEMO_TASK = 
                     new ControlledDemoTask();
    
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
        
        Stage constructorStage = new Stage();
        StackPane constructorRoot = new StackPane();
        
        constructorStage.setScene(
                new Scene(constructorRoot,
                          DIMENSION, 
                          DIMENSION));
        
        constructorStage.setTitle("PieChart3D controlled demo");
        
        PieChart3D pieChart = new PieChart3D(DIMENSION);
        
        Rectangle2D screen = Screen.getPrimary().getBounds();
        double startY = (screen.getHeight() - DIMENSION) / 2.0;
        double startX1 = (screen.getWidth() - DIMENSION * 2.0 - 20.0) / 2.0;
        double startX2 = startX1 + DIMENSION + 20.0;
        
        primaryStage.setY(startY);
        primaryStage.setX(startX1);
        
        constructorStage.setY(startY);
        constructorStage.setX(startX2);
        
        CONTROLLED_DEMO_TASK.setRoot(constructorRoot);
        CONTROLLED_DEMO_TASK.setStage(constructorStage);
        CONTROLLED_DEMO_TASK.setChart(pieChart);
        
        new Thread(CONTROLLED_DEMO_TASK).start();
        
        primaryStage.show();
        constructorStage.show();
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
        
        chart.setChartBackgroundColor(CHART_BACKGROUND_COLOR);
        chart.setAngleOffset(angleOffset);
        chart.setOriginalIntensityColor(getRandomColor(random));
        chart.setChartBackgroundColor(CHART_BACKGROUND_COLOR);
        
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

    private static final long SLEEP_DURATION_IN_MILLISECONDS = 1L;
    private static final int FRAMES_PER_CHART = 2_000;
    
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
                Platform.exit();
                System.exit(0);
            }
        });
        
        while (doRun) {
            PieChart3D chart = PieChart3DDemo.getRandomChart(random);
            
            for (int i = 0; i < FRAMES_PER_CHART; i++) {

                Platform.runLater(() -> {    
                    root.getChildren().clear();
                    root.getChildren().add(chart);
                    
                    chart.setAngleOffset(chart.getAngleOffset() + 0.1);
                    chart.draw();
                });
                
                try {
                    Thread.sleep(SLEEP_DURATION_IN_MILLISECONDS);
                } catch (InterruptedException ex) {}
            }
        }
        
        return null;
    }
}

final class ControlledDemoTask extends Task<Void> {
    
    private Stage stage;
    private StackPane root;
    private PieChart3D chart;
    
    void setStage(Stage stage) {
        this.stage = stage;
    }
    
    void setRoot(StackPane root) {
        this.root = root;
    }
    
    void setChart(PieChart3D chart) {
        this.chart = chart;
    }
    
    @Override
    protected Void call() throws Exception {
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
        
        Scanner scanner = new Scanner(System.in);
        
        loop:
        while (true) {
            System.out.print("> ");
            
            String command = scanner.next().trim();
            
            switch (command) {
                case "quit":
                case "exit":
                    System.out.println("Bye!");
                    Platform.exit();
                    System.exit(0);
                    break loop;
                    
                case "add":
                    processAdd(chart, scanner);
                    break;
                    
                case "add-at":
                    processAddAt(chart, scanner);
                    break;
                    
                case "set":
                    processSet(chart, scanner);
                    break;
                    
                case "print":
                    processPrint(chart, scanner);
                    break;
                    
                case "size":
                    System.out.println(chart.size());
                    break;
                    
                case "remove":
                    processRemove(chart, scanner);
                    break;
                    
                case "set-box-color":
                    processSetBoxColor(chart, scanner);
                    break;
                    
                case "set-chart-background":
                    processSetChartBackground(chart, scanner);
                    break;
                    
                case "set-color":
                    processSetColor(chart, scanner);
                    break;
                    
                case "set-angle":
                    processSetAngle(chart, scanner);
                    break;
                    
                case "add-angle":
                    processAddAngle(chart, scanner);
                    break;
            }
            
            Platform.runLater(() -> {
                root.getChildren().clear();
                root.getChildren().add(chart);
                chart.draw();
            });
        }
        
        return null;
    }
    
    private static void processAdd(PieChart3D chart, Scanner scanner) {
        PieChart3DEntry entry = obtainEntry(scanner);
        chart.add(entry);
    }
    
    private static void processAddAt(PieChart3D chart, Scanner scanner) {
        int index             = scanner.nextInt();
        PieChart3DEntry entry = obtainEntry(scanner);
        
        chart.add(index, entry);
    }
    
    private static void processSet(PieChart3D chart, Scanner scanner) {
        int index = scanner.nextInt();
        PieChart3DEntry entry = obtainEntry(scanner);
        
        chart.set(index, entry);
    }
    
    private static void processPrint(PieChart3D chart, Scanner scanner) {
        int index = scanner.nextInt();
        System.out.println("> " + chart.get(index));
    }
    
    private static void processRemove(PieChart3D chart, Scanner scanner) {
        chart.remove(scanner.nextInt());
    }
    
    private static void processSetBoxColor(PieChart3D pieChart,
                                           Scanner scanner) {
        pieChart.setBoxBackgroundColor(obtainColor(scanner));
    }
    
    private static void processSetChartBackground(PieChart3D pieChart,
                                                  Scanner scanner) {
        pieChart.setChartBackgroundColor(obtainColor(scanner));
    }
    
    private static void processSetColor(PieChart3D pieChart, Scanner scanner) {
        pieChart.setOriginalIntensityColor(obtainColor(scanner));
    }
    
    private static void processSetAngle(PieChart3D pieChart, Scanner scanner) {
        double angle = scanner.nextDouble();
        pieChart.setAngleOffset(angle);
    }
    
    private static void processAddAngle(PieChart3D pieChart, Scanner scanner) {
        double angleDelta = scanner.nextDouble();
        pieChart.setAngleOffset(pieChart.getAngleOffset() + angleDelta);
    }
    
    private static PieChart3DEntry obtainEntry(Scanner scanner) {
        double radiusValue    = scanner.nextDouble();
        double angleValue     = scanner.nextDouble();
        double colorIntensity = scanner.nextDouble();
        
        return new PieChart3DEntry()
                          .withSectorRadiusValue(radiusValue)
                          .withSectorAngleValue(angleValue)
                          .withSectorColorIntensityValue(colorIntensity);
    }
    
    private static Color obtainColor(Scanner scanner) {
        String s = scanner.next();
        
        if (s.charAt(0) == '#') {
            s = s.substring(1);
        }
        
        if (s.length() != 3 && s.length() != 6) {
            throw new IllegalArgumentException("Invalid color string: " + s);
        }
        
        return Color.web(s);
    }
}