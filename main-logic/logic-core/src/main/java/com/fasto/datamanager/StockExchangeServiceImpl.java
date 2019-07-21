package com.fasto.datamanager;

import com.fasto.datamanager.model.converters.Convertible;
import com.fasto.datamanager.dto.StockExchangeDto;
import com.fasto.datamanager.dto.wrappers.ResponseWrapper;
import com.fasto.datamanager.model.StockEntity;
import com.fasto.datamanager.model.StockExchangeEntity;
import com.fasto.datamanager.paging.DefaultFilterFieldToEntityFieldMapper;
import com.fasto.datamanager.paging.PageHelper;
import com.fasto.datamanager.paging.QueryRequest;
import com.fasto.datamanager.paging.QueryResponse;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

/**
 * @author nkravchenko
 */
@Remote(StockExchangeService.class)
@Stateless(name = StockExchangeService.STOCK_EXCHANGE_SERVICE_NAME)
public class StockExchangeServiceImpl implements StockExchangeService {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public ResponseWrapper<StockExchangeDto> getStockExchanges(QueryRequest queryRequest) {

        PageHelper<StockEntity> pageHelper = PageHelper.builder(entityManager)
                .withEntity(StockExchangeEntity.class)
                .withQueryRequest(queryRequest)
                .withFilterFieldToEntityFieldMapper(new DefaultFilterFieldToEntityFieldMapper())
                .build();

        QueryResponse queryResponse = pageHelper.buildPage();

        List<StockExchangeEntity> stockExchangeEntities = queryResponse.getData();
        return new ResponseWrapper<>(Convertible.convert(stockExchangeEntities), queryResponse.getTotalPageCount());
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public long createStockExchange(String name, String shortName, boolean enabled, Date startTime, Date closeTime) {

        StockExchangeEntity entity = new StockExchangeEntity();
        entity.setName(name);
        entity.setShortName(shortName);
        entity.setEnabled(enabled);
        entity.setStartTimeUtc(startTime);
        entity.setCloseTimeUtc(closeTime);

        entityManager.persist(entity);

        return entity.getStockExchangeId();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public long updateStockExchange(Long id, String name, String shortName, Boolean enabled, Date startTime, Date closeTime) {
        StockExchangeEntity entity = entityManager.find(StockExchangeEntity.class, id);
        entity.setName(name);
        entity.setShortName(shortName);
        entity.setEnabled(enabled);
        entity.setStartTimeUtc(startTime);
        entity.setCloseTimeUtc(closeTime);

        entityManager.merge(entity);
        return entity.getStockExchangeId();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void removeStockExchangeById(Long stockExchangeId) {
        StockExchangeEntity entity = entityManager.find(StockExchangeEntity.class, stockExchangeId);
        entityManager.remove(entity);
    }
}
