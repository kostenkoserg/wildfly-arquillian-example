package com.fasto.datamanager.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author kostenko
 */
@Entity
@Table(name = "DATAFEEDPROVIDER_TO_STOCKEXCHANGE_MAP")
public class DataFeedProviderToStockExchangeMapEntity {

    @Id
    @Column(name = "DATAFEEDPROVIDER_TO_STOCKEXCHANGE_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long dataFeedProviderToStockExchangeId;

    @ManyToOne
    @JoinColumn(name = "DATA_FEED_PROVIDER_ID", nullable = false)
    private DataFeedProviderEntity dataFeedProviderEntity;

    @ManyToOne
    @JoinColumn(name = "STOCK_EXCHANGE_ID", nullable = false)
    private StockExchangeEntity stockExchangeEntity;

    @Column(name = "PRIORITY")
    private int priority;

    public long getDataFeedProviderToStockExchangeId() {
        return dataFeedProviderToStockExchangeId;
    }

    public void setDataFeedProviderToStockExchangeId(long dataFeedProviderToStockExchangeId) {
        this.dataFeedProviderToStockExchangeId = dataFeedProviderToStockExchangeId;
    }

    public DataFeedProviderEntity getDataFeedProviderEntity() {
        return dataFeedProviderEntity;
    }

    public void setDataFeedProviderEntity(DataFeedProviderEntity dataFeedProviderEntity) {
        this.dataFeedProviderEntity = dataFeedProviderEntity;
    }

    public StockExchangeEntity getStockExchangeEntity() {
        return stockExchangeEntity;
    }

    public void setStockExchangeEntity(StockExchangeEntity stockExchangeEntity) {
        this.stockExchangeEntity = stockExchangeEntity;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }
}
