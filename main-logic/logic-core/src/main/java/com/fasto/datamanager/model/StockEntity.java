package com.fasto.datamanager.model;

import com.fasto.datamanager.model.converters.Convertible;
import com.fasto.datamanager.dto.StockDto;
import com.fasto.datamanager.dto.StockExchangeDto;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "STOCK")
public class StockEntity implements Convertible<StockDto> {

    @Id
    @Column(name = "STOCK_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long stockId;

    @Column(name = "SYMBOL", nullable = true, length = 45)
    private String symbol;

    @Column(name = "NAME", nullable = true, length = 255)
    private String name;

    @Column(name = "URL", nullable = true, length = 255)
    private String url;

    @Column(name = "ENABLED", nullable = false)
    private boolean enabled;

    @ManyToOne
    @JoinColumn(name = "STOCK_EXCHANGE_ID", nullable = true)
    private StockExchangeEntity stockExchangeEntity;

    public long getStockId() {
        return stockId;
    }

    public void setStockId(long stockId) {
        this.stockId = stockId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public StockExchangeEntity getStockExchangeEntity() {
        return stockExchangeEntity;
    }

    public void setStockExchangeEntity(StockExchangeEntity stockExchangeEntity) {
        this.stockExchangeEntity = stockExchangeEntity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockEntity that = (StockEntity) o;
        return stockId == that.stockId &&
                Objects.equals(symbol, that.symbol) &&
                Objects.equals(name, that.name) &&
                Objects.equals(stockExchangeEntity, that.stockExchangeEntity);
    }

    @Override
    public int hashCode() {

        return Objects.hash(stockId, symbol, name, stockExchangeEntity);
    }

    @Override
    public StockDto convert() {
        StockDto dto = new StockDto();
        dto.setStockId(this.stockId);
        dto.setSymbol(this.symbol);
        dto.setName(this.name);
        dto.setUrl(this.url);
        dto.setEnabled(this.enabled);

        if (stockExchangeEntity != null) {
            StockExchangeDto stockExchangeDto = this.stockExchangeEntity.convert();
            dto.setStockExchangeDto(stockExchangeDto);
        }

        return dto;
    }

    public static StockEntity convertToEntity(StockDto stockDto) {
        if (stockDto != null) {
            StockEntity stockEntity = new StockEntity();
            stockEntity.setStockId(stockDto.getStockId());
            stockEntity.setName(stockDto.getName());
            stockEntity.setUrl(stockDto.getUrl());
            stockEntity.setEnabled(stockDto.getEnabled());
            stockEntity.setSymbol(stockDto.getSymbol());
            //todo
            return stockEntity;
        }
        return null;
    }

}
