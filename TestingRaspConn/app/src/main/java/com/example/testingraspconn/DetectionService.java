package com.example.testingraspconn;

import java.util.Date;

public class DetectionService {
    private final int methaneLower = 750;
    private final int AcetoneLower = 200;
    private final int AcetoneUpper = 500;
    private final int CO2Lower = 30;
    private final int CO2Upper = 100;

    public DetectedGas ConvertPPM(int ppm){
        Date date = new Date();
        GasType gasType = GasType.CleanAir;
        if(ppm >= methaneLower)
           gasType = GasType.Methane;
        else if(ppm >= AcetoneLower && ppm<=AcetoneUpper)
            gasType = GasType.Acetone;
        else if(ppm >= CO2Lower && ppm <= CO2Upper)
            gasType = GasType.CO2;

        return new DetectedGas(gasType,ppm,date);
    }
}
