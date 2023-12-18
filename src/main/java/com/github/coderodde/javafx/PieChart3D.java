package com.github.coderodde.javafx;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * This class implements a pie chart that can communicate data points in three
 * dimensions:
 * <ol>
 * <li>the radius of a sector,</li>
 * <li>the angle of a sector,</li>
 * <li>color intensity of a sector.</li>
 * </ol>
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Dec 18, 2023)
 * @since 1.6 (Dec 18, 2023)
 */
public final class PieChart3D extends Canvas {
    
    private Color boxColor = Color.WHITE;
    private Color chartBackgroundColor = Color.WHITE;
    private Color originalIntensityColor;
    private double angleOffset = 0.0;
    private final List<PieChart3DEntry> entries = new ArrayList<>();
    
    public PieChart3D(double dimension) {
        checkDimension(dimension);
        super.setWidth(dimension);
        super.setHeight(dimension);
    }
    
    public Color getBoxBackgroundColor() {
        return boxColor;
    }

    public Color getChartBackgroundColor() {
        return chartBackgroundColor;
    }

    public Color getOriginalIntensityColor() {
        return originalIntensityColor;
    }
    
    public double getAngleOffset() {
        return angleOffset;
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
    
    public void setAngleOffset(double angleOffset) {
        checkAngleOffset(angleOffset);
        angleOffset %= 360.0;
        
        if (angleOffset < 0.0) {
            System.out.print(angleOffset + " -> ");
            angleOffset += 360.0;
            System.out.println(angleOffset);
        }
        
        this.angleOffset = angleOffset;
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
        drawBoundingBox(gc);
        drawChart(gc);
    }
    
    private void drawBoundingBox(GraphicsContext gc) {
        gc.setFill(getBoxBackgroundColor());
        gc.fillRect(0.0,
                    0.0,
                    getWidth(), 
                    getHeight());
    }
    
    private void drawEntirePieChart(GraphicsContext gc) {
        gc.setFill(getChartBackgroundColor());
        gc.fillOval(0.0,
                    0.0,
                    getHeight(),
                    getWidth());       
    }
    
    private void drawChart(GraphicsContext gc) {
        double startAngle = 0.0;
        int entryIndex = 0;
        
        for (PieChart3DEntry entry : entries) {
            drawSector(gc, entry, startAngle);
            startAngle += computeStartAngleDelta(entryIndex++);
        }
    }
    
    private void drawSector(GraphicsContext gc, 
                            PieChart3DEntry entry,
                            double startAngle) {
        
    }
    
    private double computeStartAngleDelta(int entryIndex) {
        return 0.0;
    }
    
    private static void checkDimension(double dimension) {
        checkIsNotNaN(dimension, "The dimension is NaN.");
        checkIsNotInfinite(dimension,
                           "The dimention is infinite in absolute value.");
        
        if (dimension <= 0.0) {
            throw new IllegalArgumentException(
                    "The dimension is non-positive.");
        }
    }
    
    private static void checkAngleOffset(double angleOffset) {
        checkIsNotNaN(angleOffset, "The angle offset is NaN.");
        checkIsNotInfinite(angleOffset,
                           "The angle offset is infinite in absolute value.");
    }
    
    private static void checkIsNotNaN(double value, String exceptionMessage) {
        if (Double.isNaN(value)) {
            throw new IllegalArgumentException(exceptionMessage);
        }
    }
    
    private static void checkIsNotInfinite(double value, 
                                           String exceptionMessage) {
        if (Double.isInfinite(value)) {
            throw new IllegalArgumentException(exceptionMessage);
        }
    }
    
    private double getMaximumRadiusValue() {
        Optional<PieChart3DEntry> optional = 
                entries.stream().max((PieChart3DEntry e1, 
                                      PieChart3DEntry e2) -> { 
                    
            return Double.compare(e1.getSectorRadiusValue(), 
                                  e2.getSectorRadiusValue()); 
        });
        
        if (optional.isEmpty()) {
            throw new IllegalStateException("No entries in this chart.");
        }
        
        return optional.get().getSectorRadiusValue();
    }
    
    private static Color obtainColor(Color maximumColor, double intensity) {
        double r = maximumColor.getRed();
        double g = maximumColor.getGreen();
        double b = maximumColor.getBlue();
        
        r += (1.0 - r) * (1.0 - intensity);
        g += (1.0 - g) * (1.0 - intensity);
        b += (1.0 - b) * (1.0 - intensity);
        
        return new Color(r, g, b, 1.0);
    }
}
