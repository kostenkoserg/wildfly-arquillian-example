package com.fasto.datamanager.dto;

import java.io.Serializable;

/**
 *
 * @author kostenko
 */
public class PayOutDetailsDto implements Serializable {

    private long id;
    private int startPlace;
    private int endPlace;
    private float percentage;
    private int numberOfEntries;

    public PayOutDetailsDto() {
        
    }
    
    public PayOutDetailsDto(long id, int startPlace, int endPlace, int percentage, int numberOfEntries) {
        this.id = id;
        this.startPlace = startPlace;
        this.endPlace = endPlace;
        this.percentage = percentage;
        this.numberOfEntries = numberOfEntries;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getStartPlace() {
        return startPlace;
    }

    public void setStartPlace(int startPlace) {
        this.startPlace = startPlace;
    }

    public int getEndPlace() {
        return endPlace;
    }

    public void setEndPlace(int endPlace) {
        this.endPlace = endPlace;
    }

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    public int getNumberOfEntries() {
        return numberOfEntries;
    }

    public void setNumberOfEntries(int numberOfEntries) {
        this.numberOfEntries = numberOfEntries;
    }

}
