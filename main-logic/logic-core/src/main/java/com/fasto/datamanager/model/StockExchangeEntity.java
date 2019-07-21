package com.fasto.datamanager.model;

import com.fasto.datamanager.model.converters.Convertible;
import com.fasto.datamanager.dto.StockExchangeDto;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "STOCK_EXCHANGE")
public class StockExchangeEntity implements Convertible<StockExchangeDto> {

    @Id
    @Column(name = "STOCK_EXCHANGE_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long stockExchangeId;

    @Column(name = "NAME", nullable = false, length = 256)
    private String name;

    @Column(name = "SHORTNAME", nullable = false, length = 16)
    private String shortName;

    @Column(name = "START_TIME_UTC", nullable = false)
    private Date startTimeUtc;

    @Column(name = "CLOSE_TIME_UTC", nullable = false)
    private Date closeTimeUtc;

    @Column(name = "ENABLED", nullable = false)
    private boolean enabled;

    public long getStockExchangeId() {
        return stockExchangeId;
    }

    public void setStockExchangeId(long stockExchangeId) {
        this.stockExchangeId = stockExchangeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortname) {
        this.shortName = shortname;
    }

    public Date getStartTimeUtc() {
        return startTimeUtc;
    }

    public void setStartTimeUtc(Date startTimeUtc) {
        this.startTimeUtc = startTimeUtc;
    }

    public Date getCloseTimeUtc() {
        return closeTimeUtc;
    }

    public void setCloseTimeUtc(Date closeTimeUtc) {
        this.closeTimeUtc = closeTimeUtc;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StockExchangeEntity that = (StockExchangeEntity) o;
        return stockExchangeId == that.stockExchangeId
                && enabled == that.enabled
                && Objects.equals(name, that.name)
                && Objects.equals(shortName, that.shortName)
                && Objects.equals(startTimeUtc, that.startTimeUtc)
                && Objects.equals(closeTimeUtc, that.closeTimeUtc);
    }

    @Override
    public int hashCode() {

        return Objects.hash(stockExchangeId, name, shortName, startTimeUtc, closeTimeUtc, enabled);
    }

    @Override
    public StockExchangeDto convert() {
        StockExchangeDto dto = new StockExchangeDto();
        dto.setStockExchangeId(this.stockExchangeId);
        dto.setName(this.name);
        dto.setShortName(this.shortName);
        dto.setStartTimeUtc(this.startTimeUtc);
        dto.setCloseTimeUtc(this.closeTimeUtc);
        dto.setEnabled(this.enabled);

        return dto;
    }
}
