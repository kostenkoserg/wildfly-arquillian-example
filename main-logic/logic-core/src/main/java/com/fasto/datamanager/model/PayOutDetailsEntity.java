package com.fasto.datamanager.model;

import com.fasto.datamanager.model.converters.Convertible;
import com.fasto.datamanager.dto.PayOutDetailsDto;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author kostenko
 */
@Entity
@Table(name = "PAY_OUT_DETAILS")
public class PayOutDetailsEntity implements Convertible<PayOutDetailsDto> {

    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "START_PLACE", nullable = false)
    private int startPlace;

    @Column(name = "END_PLACE", nullable = false)
    private int endPlace;

    @Column(name = "PERCENTAGE", nullable = false)
    private float percentage;

    @ManyToOne
    @JoinColumn(name = "PAY_OUT_ID", nullable = false)
    private PayOutEntity payOutEntity;
    
    @Column(name = "NUMBER_OF_ENTRIES", nullable = false)
    private int numberOfEntries;
    

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

    public PayOutEntity getPayOutEntity() {
        return payOutEntity;
    }

    public void setPayOutEntity(PayOutEntity payOutEntity) {
        this.payOutEntity = payOutEntity;
    }

    public int getNumberOfEntries() {
        return numberOfEntries;
    }

    public void setNumberOfEntries(int numberOfEntries) {
        this.numberOfEntries = numberOfEntries;
    }
    

    @Override
    public PayOutDetailsDto convert() {
        PayOutDetailsDto dto = new PayOutDetailsDto();
        dto.setId(this.id);
        dto.setStartPlace(this.startPlace);
        dto.setEndPlace(this.endPlace);
        dto.setPercentage(this.percentage);
        dto.setNumberOfEntries(this.numberOfEntries);
        return dto;
    }
    
}
