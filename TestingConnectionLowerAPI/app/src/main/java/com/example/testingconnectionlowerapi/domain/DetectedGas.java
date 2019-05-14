package com.example.testingconnectionlowerapi.domain;

import java.io.Serializable;
import java.util.Date;

public class DetectedGas implements Serializable {
    private GasType gasType;
    private int ppm;
    private Date detectionDate;

    public GasType getGasType() {
        return gasType;
    }

    public void setGasType(GasType gasType) {
        this.gasType = gasType;
    }

    public int getPpm() {
        return ppm;
    }

    public void setPpm(int ppm) {
        this.ppm = ppm;
    }

    public Date getDetectionDate() {
        return detectionDate;
    }

    public void setDetectionDate(Date detectionDate) {
        this.detectionDate = detectionDate;
    }

    public DetectedGas(GasType gasType, int ppm, Date detectionDate) {

        this.gasType = gasType;
        this.ppm = ppm;
        this.detectionDate = detectionDate;
    }
}
