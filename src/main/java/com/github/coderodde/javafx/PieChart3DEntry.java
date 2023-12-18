package com.github.coderodde.javafx;

/**
 *
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Dec 18, 2023)
 * @since 1.6 (Dec 18, 2023)
 */
public final class PieChart3DEntry {
    
    private double radiusValue;
    private double sectorValue;
    private double colorIntensityValue;
    
    public double getRadiusValue() {
        return radiusValue;
    }

    public double getSectorValue() {
        return sectorValue;
    }

    public double getColorIntensityValue() {
        return colorIntensityValue;
    }

    public void setRadiusValue(double radiusValue) {
        checkValue(radiusValue);
        this.radiusValue = radiusValue;
    }

    public void setSectorValue(double sectorValue) {
        checkValue(sectorValue);
        this.sectorValue = sectorValue;
    }

    public void setColorIntensityValue(double colorIntensityValue) {
        checkValue(colorIntensityValue);
        this.colorIntensityValue = colorIntensityValue;
    }

    private static void checkValue(double value) {
        if (Double.isNaN(value)) {
            throw new IllegalArgumentException("The input value is NaN.");
        }
        
        if (Double.isInfinite(value)) {
            throw new IllegalArgumentException("The input value is infinite.");
        }
        
        if (value < 0.0) {
            throw new IllegalArgumentException("The input value is negative.");
        }
    }
}
