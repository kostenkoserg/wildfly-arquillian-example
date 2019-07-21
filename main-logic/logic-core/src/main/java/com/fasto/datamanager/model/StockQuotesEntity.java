package com.fasto.datamanager.model;

import com.fasto.datamanager.model.converters.Convertible;
import com.fasto.datamanager.dto.StockDto;
import com.fasto.datamanager.dto.StockQuotesDto;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author kostenko
 */
@Entity
@Table(name = "STOCK_QUOTES")
public class StockQuotesEntity implements Convertible<StockQuotesDto> {

    @Id
    @Column(name = "STOCK_QUOTES_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long stockQuotesId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STOCK_ID")
    private StockEntity stockEntity;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EVENT_DATE")
    private Date eventDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TRADE_DATE")
    private Date tradeDate;

    @Column(name = "VALUE")
    private BigDecimal value;

    public static StockQuotesEntity convertToEntity(StockQuotesDto dto) {
        if (dto != null) {
            StockQuotesEntity stockQuotesEntity = new StockQuotesEntity();
            stockQuotesEntity.setEventDate(dto.getEventDate());

            StockEntity stockEntity = StockEntity.convertToEntity(dto.getStockDto());
            stockQuotesEntity.setStockEntity(stockEntity);
            stockQuotesEntity.setStockQuotesId(dto.getStockQuotesId());
            stockQuotesEntity.setTradeDate(dto.getTradeDate());
            stockQuotesEntity.setValue(dto.getValue());

            return stockQuotesEntity;
        } else {
            return null;
        }
    }


    public long getStockQuotesId() {
        return stockQuotesId;
    }

    public void setStockQuotesId(long stockQuotesId) {
        this.stockQuotesId = stockQuotesId;
    }

    public StockEntity getStockEntity() {
        return stockEntity;
    }

    public void setStockEntity(StockEntity stockEntity) {
        this.stockEntity = stockEntity;
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

    @Override
    public StockQuotesDto convert() {
        StockQuotesDto dto = new StockQuotesDto();
        dto.setEventDate(this.eventDate);

        StockDto stockDto = this.stockEntity.convert();
        dto.setStockDto(stockDto);
        dto.setStockQuotesId(this.stockQuotesId);
        dto.setTradeDate(this.tradeDate);
        dto.setValue(this.value);

        return dto;
    }
}
