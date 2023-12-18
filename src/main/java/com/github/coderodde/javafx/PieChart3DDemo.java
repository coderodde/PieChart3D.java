package com.github.coderodde.javafx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
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
        chart.setBoxBackgroundColor(Color.GREY);
        chart.setChartBackgroundColor(Color.TOMATO);
        
        PieChart3DEntry entry1 = new PieChart3DEntry();
        PieChart3DEntry entry2 = new PieChart3DEntry();
        PieChart3DEntry entry3 = new PieChart3DEntry();
        
        entry1.setSectorColorIntensityValue(0.5);
        entry1.setSectorRadiusValue(100.0);
        entry1.setSectorAngleValue(100.0);
        
        entry2.setSectorColorIntensityValue(1);
        entry2.setSectorRadiusValue(70.0);
        entry2.setSectorAngleValue(50.0);
        
        entry3.setSectorColorIntensityValue(0.3);
        entry3.setSectorRadiusValue(50.0);
        entry3.setSectorAngleValue(60.0);
        
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