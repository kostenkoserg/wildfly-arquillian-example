package com.fasto.datamanager;

import com.fasto.datamanager.model.converters.Convertible;
import com.fasto.datamanager.dto.SlateDto;
import com.fasto.datamanager.dto.wrappers.ResponseWrapper;
import com.fasto.datamanager.model.SlateEntity;
import com.fasto.datamanager.model.StockExchangeEntity;
import com.fasto.datamanager.paging.DefaultFilterFieldToEntityFieldMapper;
import com.fasto.datamanager.paging.PageHelper;
import com.fasto.datamanager.paging.QueryRequest;
import com.fasto.datamanager.paging.QueryResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Remote(SlateService.class)
@Stateless(name = SlateService.SLATE_SERVICE_NAME)
public class SlateServiceImpl implements SlateService {

    final static Log logger = LogFactory.getLog(SlateServiceImpl.class);

    @PersistenceContext
    EntityManager entityManager;

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public long createSlate(String name, long stockExchangeId, boolean enabled) {

        SlateEntity entity = new SlateEntity();
        entity.setName(name);
        entity.setStockExchangeEntity(entityManager.find(StockExchangeEntity.class, stockExchangeId));
        entity.setEnabled(enabled);

        entityManager.persist(entity);

        return entity.getSlateId();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public ResponseWrapper<SlateDto> getSlates(QueryRequest queryRequest) {
        PageHelper pageHelper = PageHelper.builder(entityManager)
                .withEntity(SlateEntity.class)
                .withQueryRequest(queryRequest)
                .withFilterFieldToEntityFieldMapper(new DefaultFilterFieldToEntityFieldMapper())
                .build();
        
        QueryResponse queryResponse = pageHelper.buildPage();
        
        return new ResponseWrapper<>(Convertible.convert(queryResponse.getData()), queryResponse.getTotalPageCount());
    }
}
