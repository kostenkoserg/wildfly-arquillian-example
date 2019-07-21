package com.fasto.datamanager.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class StockQuotesDto implements Serializable {
    private static final long serialVersionUID = -1553218722913855124L;

    private long stockQuotesId;

    private StockDto stockDto;

    private Date eventDate;

    private Date tradeDate;

    private BigDecimal value;


    public long getStockQuotesId() {
        return stockQuotesId;
    }

    public void setStockQuotesId(long stockQuotesId) {
        this.stockQuotesId = stockQuotesId;
    }

    public StockDto getStockDto() {
        return stockDto;
    }

    public void setStockDto(StockDto stockDto) {
        this.stockDto = stockDto;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public Date getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(Date tradeDate) {
        this.tradeDate = tradeDate;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

}
