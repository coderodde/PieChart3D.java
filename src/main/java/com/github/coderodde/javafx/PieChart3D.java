package com.github.coderodde.javafx;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

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
    
    private static final Color DEFAULT_BOX_COLOR = Color.WHITE;
    private static final Color DEFAULT_CHART_BACKGROUND_COLOR = Color.WHITE;
    private static final Color DEFAULT_ORIGINAL_INTENSITY_COLOR = Color.BLACK;
    
    private Color boxColor               = DEFAULT_BOX_COLOR;
    private Color chartBackgroundColor   = DEFAULT_CHART_BACKGROUND_COLOR;
    private Color originalIntensityColor = DEFAULT_ORIGINAL_INTENSITY_COLOR;
    private double angleOffset           = 0.0;
    
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
            angleOffset += 360.0;
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
        drawEntirePieChart(gc);
        
        if (!entries.isEmpty()) {
            // Once here, we have entries to draw:
            drawChart(gc);
        }
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
        double sumOfRelativeAngles        = computeSumOfRelativeAngles();
        double maximumRadiusValue         = getMaximumRadiusValue();
        double maximumColorIntensityValue = getMaximumColorIntensity();
        double startAngle                 = 90.0 - angleOffset;
        
        for (PieChart3DEntry entry : entries) {
            
            double actualAngle = 360.0 * entry.getSectorAngleValue()
                                       / sumOfRelativeAngles;
            
            double actualRadius =
                    (getHeight() / 2.0) * (entry.getSectorRadiusValue() 
                                        / maximumRadiusValue);
                                        
            Color actualColor = 
                    obtainColor(entry.getSectorColorIntensityValue() / 
                                maximumColorIntensityValue);
            
            double sectorStartAngle = startAngle - actualAngle;
            
            startAngle -= actualAngle;
            
            drawSector(gc,
                       sectorStartAngle,
                       actualAngle,
                       actualRadius,
                       actualColor);
        }
    }
    
    /**
     * Draws a single sector.
     * 
     * @param gc           the graphics context.
     * @param startAngle   the start angle.
     * @param angle     the end angle.
     * @param actualRadius the radius of the sector.
     * @param color        the color of the sector.
     */
    private void drawSector(GraphicsContext gc, 
                            double startAngle,
                            double angle,
                            double actualRadius,
                            Color color) {
        
        double canvasDimension = getHeight();
        double centerX = canvasDimension / 2.0;
        double centerY = canvasDimension / 2.0;
        
        gc.setFill(color);
        gc.fillArc(centerX - actualRadius, 
                   centerY - actualRadius,
                   2.0 * actualRadius,
                   2.0 * actualRadius,
                   startAngle,
                   angle,
                   ArcType.ROUND);
    }
    
    private double computeSumOfRelativeAngles() {
        double angleSum = 0.0;
        
        for (PieChart3DEntry entry : entries) {
            angleSum += entry.getSectorAngleValue();
        }
        
        return angleSum;
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
                entries.stream().max((e1, e2) -> {
            return Double.compare(e1.getSectorRadiusValue(), 
                                  e2.getSectorRadiusValue()); 
        });
        
        return optional.get().getSectorRadiusValue();
    }
    
    private double getMaximumColorIntensity() {
        Optional<PieChart3DEntry> optional = 
                entries.stream().max((e1, e2) -> {
                    return Double.compare(e1.getSectorColorIntensityValue(), 
                                          e2.getSectorColorIntensityValue());
                });
        
        return optional.get().getSectorColorIntensityValue();
    }
    
    private Color obtainColor(double intensity) {
        double r = originalIntensityColor.getRed();
        double g = originalIntensityColor.getGreen();
        double b = originalIntensityColor.getBlue();
        
        r += (1.0 - r) * (1.0 - intensity);
        g += (1.0 - g) * (1.0 - intensity);
        b += (1.0 - b) * (1.0 - intensity);
        
        return new Color(r, g, b, 1.0);
    }
}
