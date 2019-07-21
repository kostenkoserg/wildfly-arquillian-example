package com.fasto.admin.model;

/**
 * PayOutDetail
 */
public class PayOutDetail {

    private Long id;
    private Integer startPlace;
    private Integer endPlace;
    private Float percentage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStartPlace() {
        return startPlace;
    }

    public void setStartPlace(Integer startPlace) {
        this.startPlace = startPlace;
    }

    public Integer getEndPlace() {
        return endPlace;
    }

    public void setEndPlace(Integer endPlace) {
        this.endPlace = endPlace;
    }

    public Float getPercentage() {
        return percentage;
    }

    public void setPercentage(Float percentage) {
        this.percentage = percentage;
    }

}
