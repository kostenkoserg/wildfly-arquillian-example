package com.fasto.datamanager.dto;

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StockExchangeDto implements Serializable {
    private static final long serialVersionUID = -2163557581420684874L;

    private long stockExchangeId;
    private String name;
    private String shortName;
    private Date startTimeUtc;
    private Date closeTimeUtc;
    private boolean enabled;
    private List<Long> stockIDsList = new ArrayList<>();
    private List<Long> dataFeedProviderIDs = new ArrayList<>();

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

    public void setShortName(String shortName) {
        this.shortName = shortName;
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

    public List<Long> getStockIDsList() {
        return stockIDsList;
    }

    public void setStockIDsList(List<Long> stockIDsList) {
        this.stockIDsList = stockIDsList;
    }

    public List<Long> getDataFeedProviderIDs() {
        return dataFeedProviderIDs;
    }

    public void setDataFeedProviderIDs(List<Long> dataFeedProviderIDs) {
        this.dataFeedProviderIDs = dataFeedProviderIDs;
    }
}
