package com.github.coderodde.javafx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
 
public class PieChart3DDemo extends Application {
    
    private static final double DIMENSION = 500.0;
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("PieChart3D demo");
        StackPane root = new StackPane();
        PieChart3D chart = new PieChart3D(DIMENSION);
        chart.setAngleOffset(30.0);
        
        PieChart3DEntry entry1 = new PieChart3DEntry();
        PieChart3DEntry entry2 = new PieChart3DEntry();
        PieChart3DEntry entry3 = new PieChart3DEntry();
        
        entry1.withSectorAngleValue(1.0)
              .withSectorColorIntensityValue(100.0)
              .withSectorRadiusValue(1.0);
        
        entry2.withSectorAngleValue(1.0)
              .withSectorColorIntensityValue(70.0)
              .withSectorRadiusValue(0.6);
        
        entry3.withSectorAngleValue(1.0)
              .withSectorColorIntensityValue(85.0)
              .withSectorRadiusValue(0.2);
        
        chart.add(entry1);
        chart.add(entry2);
        chart.add(entry3);
        
        root.getChildren().add(chart);
        
        chart.draw();
        
        primaryStage.setScene(
                new Scene(root, 
                          DIMENSION,
                          DIMENSION));
        
        primaryStage.show();
    }
}