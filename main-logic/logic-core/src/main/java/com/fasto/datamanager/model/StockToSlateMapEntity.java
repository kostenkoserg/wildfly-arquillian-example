package com.fasto.datamanager.model;

import com.fasto.datamanager.model.converters.Convertible;
import com.fasto.datamanager.dto.SlateDto;
import com.fasto.datamanager.dto.StockDto;
import com.fasto.datamanager.dto.StockToSlateMapDto;
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
@Table(name = "STOCK_TO_SLATE_MAP")
public class StockToSlateMapEntity implements Convertible<StockToSlateMapDto> {
    private long stockToSlateMapId;
    private StockEntity stockEntity;
    private SlateEntity slateEntity;

    @Id
    @Column(name = "STOCK_TO_SLATE_MAP_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getStockToSlateMapId() {
        return stockToSlateMapId;
    }

    public void setStockToSlateMapId(long stockToSlateMapId) {
        this.stockToSlateMapId = stockToSlateMapId;
    }

    @ManyToOne
    @JoinColumn(name = "STOCK_ID", nullable = false)
    public StockEntity getStockEntity() {
        return stockEntity;
    }

    public void setStockEntity(StockEntity stockEntity) {
        this.stockEntity = stockEntity;
    }

    @ManyToOne
    @JoinColumn(name = "SLATE_ID", nullable = false)
    public SlateEntity getSlateEntity() {
        return slateEntity;
    }

    public void setSlateEntity(SlateEntity slateEntity) {
        this.slateEntity = slateEntity;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockToSlateMapEntity that = (StockToSlateMapEntity) o;
        return stockToSlateMapId == that.stockToSlateMapId &&
                stockEntity.equals(that.stockEntity) &&
                slateEntity.equals(that.slateEntity);
    }

    @Override
    public int hashCode() {

        return Objects.hash(stockToSlateMapId, stockEntity, slateEntity);
    }

    @Override
    public StockToSlateMapDto convert() {
        StockToSlateMapDto dto = new StockToSlateMapDto();
        dto.setStockToSlateMapId(this.stockToSlateMapId);

        StockDto stockDto = this.stockEntity.convert();
        dto.setStockDto(stockDto);

        SlateDto slateDto = this.slateEntity.convert();
        dto.setSlateDto(slateDto);

        return dto;
    }

}
