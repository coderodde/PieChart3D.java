package com.github.coderodde.javafx;

/**
 *
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Dec 18, 2023)
 * @since 1.6 (Dec 18, 2023)
 */
public final class PieChart3DEntry {
    
    /**
     * Holds the sector radius of this entry.
     */
    private double sectorRadiusValue;
    
    /**
     * Holds the sector angle of this entry.
     */
    private double sectorAngleValue;
    
    /**
     * Holds the sector color intensity of this entry.
     */
    private double sectorColorIntensityValue;
    
    public double getSectorRadiusValue() {
        return sectorRadiusValue;
    }

    public double getSectorAngleValue() {
        return sectorAngleValue;
    }

    public double getSectorColorIntensityValue() {
        return sectorColorIntensityValue;
    }

    public void setSectorRadiusValue(double sectorRadiusValue) {
        checkValue(sectorRadiusValue);
        this.sectorRadiusValue = sectorRadiusValue;
    }

    public void setSectorAngleValue(double sectorAngleValue) {
        checkValue(sectorAngleValue);
        this.sectorAngleValue = sectorAngleValue;
    }

    public void setSectorColorIntensityValue(double sectorColorIntensityValue) {
        checkValue(sectorColorIntensityValue);
        this.sectorColorIntensityValue = sectorColorIntensityValue;
    }
    
    @Override
    public String toString() {
        return "[sectorRadiusValue = " 
                + sectorRadiusValue
                + ", sectorAngleValue = " 
                + sectorAngleValue 
                + ", sectorColorIntensityValue = "
                + sectorColorIntensityValue
                + "]";
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
