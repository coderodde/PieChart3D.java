package com.github.coderodde.javafx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
 
public class PieChart3DDemo extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("JavaFX3DPieChart demo");
        StackPane root = new StackPane();
        PieChart3D chart = new PieChart3D(300.0);
        chart.setBoxBackgroundColor(Color.GREY);
        chart.setChartBackgroundColor(Color.TOMATO);
        
        PieChart3DEntry entry1 = new PieChart3DEntry();
        PieChart3DEntry entry2 = new PieChart3DEntry();
        PieChart3DEntry entry3 = new PieChart3DEntry();
        
        entry1.setColorIntensityValue(0.5);
        entry1.setRadiusValue(100.0);
        entry1.setSectorValue(100.0);
        
        entry2.setColorIntensityValue(1);
        entry2.setRadiusValue(70.0);
        entry2.setSectorValue(50.0);
        
        entry3.setColorIntensityValue(0.3);
        entry3.setRadiusValue(50.0);
        entry3.setSectorValue(60.0);
        
        chart.add(entry1);
        chart.add(entry2);
        chart.add(entry3);
        
        root.getChildren().add(chart);
        chart.draw();
        primaryStage.setScene(
                new Scene(root, 
                          chart.getWidth(),
                          chart.getHeight()));
        
        primaryStage.show();
    }
}