package com.github.coderodde.javafx;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 *
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Dec 18, 2023)
 * @since 1.6 (Dec 18, 2023)
 */
public final class PieChart3D extends Canvas {
    
    private Color boxColor = Color.WHITE;
    private Color chartBackgroundColor = Color.WHITE;
    private Color originalIntensityColor;

    public Color getBoxBackgroundColor() {
        return boxColor;
    }

    public Color getChartBackgroundColor() {
        return chartBackgroundColor;
    }

    public Color getOriginalIntensityColor() {
        return originalIntensityColor;
    }

    public void setBoxBackgroundColor(Color boxColor) {
        this.boxColor = 
                Objects.requireNonNull(boxColor, "The input color is null.");
    }

    public void setChartBackgroundColor(Color chartBackgroundColor) {
        this.chartBackgroundColor = 
                Objects.requireNonNull(
                        chartBackgroundColor, 
                        "The input color is null.");
    }

    public void setOriginalIntensityColor(Color originalIntensityColor) {
        this.originalIntensityColor =
                Objects.requireNonNull(
                        originalIntensityColor,
                        "The input color is null.");
    }
    
    private final List<PieChart3DEntry> entries = new ArrayList<>();
    
    public PieChart3D(double dimension) {
        checkDimension(dimension);
        super.setWidth(dimension);
        super.setHeight(dimension);
    }
    
    public PieChart3DEntry get(int index) {
        return entries.get(index);
    }
    
    public void set(int index, PieChart3DEntry entry) {
        entries.set(index, Objects.requireNonNull(entry, "The entry is null."));
    }
    
    public int size() {
        return entries.size();
    }
    
    public void add(PieChart3DEntry entry) {
        entries.add(Objects.requireNonNull(entry, "The entry is null."));
    }
    
    public void add(int index, PieChart3DEntry entry) {
        entries.add(index, Objects.requireNonNull(entry, "The entry is null."));
    }
    
    public void remove(int index) {
        entries.remove(index);
    }
    
    public void draw() {
        GraphicsContext gc = getGraphicsContext2D();
        gc.setFill(getBoxBackgroundColor());
        gc.fillRect(0, 0, getWidth(), getHeight());
        gc.setFill(getChartBackgroundColor());
        gc.fillOval(0.0, 0.0, getHeight(), getWidth());
    }
    
    private static void checkDimension(double dimension) {
        if (Double.isNaN(dimension)) {
            throw new IllegalArgumentException("Dimension is NaN.");
        }
        
        if (Double.isInfinite(dimension)) {
            throw new IllegalArgumentException("Dimension is infinite.");
        }
        
        if (dimension <= 0.0) {
            throw new IllegalArgumentException(
                    "Dimension too small: " + dimension);
        }
    }
    
    private double getMaximumRadiusValue() {
        Optional<PieChart3DEntry> optional = 
                entries.stream().max((PieChart3DEntry e1, 
                                      PieChart3DEntry e2) -> { 
                    
            return Double.compare(e1.getRadiusValue(), 
                                  e2.getRadiusValue()); 
        });
        
        if (optional.isEmpty()) {
            throw new IllegalStateException("No entries in this chart.");
        }
        
        return optional.get().getRadiusValue();
    }
}
