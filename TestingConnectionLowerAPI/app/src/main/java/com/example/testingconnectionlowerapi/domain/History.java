package com.example.testingconnectionlowerapi.domain;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class History {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    private String typeOfGas;
    private int ppm;

    public History(String typeOfGas, int ppm) {
        this.typeOfGas = typeOfGas;
        this.ppm = ppm;
    }

    public String getTypeOfGas() {
        return typeOfGas;
    }

    public void setTypeOfGas(String typeOfGas) {
        this.typeOfGas = typeOfGas;
    }

    public int getPpm() {
        return ppm;
    }

    public void setPpm(int ppm) {
        this.ppm = ppm;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
