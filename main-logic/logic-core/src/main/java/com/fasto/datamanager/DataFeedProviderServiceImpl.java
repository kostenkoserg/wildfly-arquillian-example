package com.fasto.datamanager;

import com.fasto.datamanager.model.converters.Convertible;
import com.fasto.datamanager.dto.DataFeedProviderDto;
import com.fasto.datamanager.dto.StockExchangeDto;
import com.fasto.datamanager.dto.wrappers.ResponseWrapper;
import com.fasto.datamanager.model.DataFeedProviderEntity;
import com.fasto.datamanager.model.DataFeedProviderToStockExchangeMapEntity;
import com.fasto.datamanager.model.StockExchangeEntity;
import com.fasto.datamanager.paging.DefaultFilterFieldToEntityFieldMapper;
import com.fasto.datamanager.paging.PageHelper;
import com.fasto.datamanager.paging.QueryRequest;
import com.fasto.datamanager.paging.QueryResponse;
import java.util.HashMap;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;

/**
 * @author kostenko
 */
@Remote(DataFeedProviderService.class)
@Stateless(name = DataFeedProviderService.DATA_FEED_PROVIDER_SERVICE_NAME)
public class DataFeedProviderServiceImpl implements DataFeedProviderService {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public ResponseWrapper<DataFeedProviderDto> getDataFeedProviders(QueryRequest queryRequest) {

        PageHelper pageHelper = PageHelper.builder(entityManager)
                .withEntity(DataFeedProviderEntity.class)
                .withQueryRequest(queryRequest)
                .withFilterFieldToEntityFieldMapper(new DefaultFilterFieldToEntityFieldMapper())
                .build();

        QueryResponse queryResponse = pageHelper.buildPage();

        List<DataFeedProviderEntity> dataFeedProviderEntities = queryResponse.getData();
        return new ResponseWrapper<>(Convertible.convert(dataFeedProviderEntities), queryResponse.getTotalPageCount());
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public long createDataFeedProvider(String name, String siteUrl, String dataUrl, String credential, String additionalJson) {

        DataFeedProviderEntity entity = new DataFeedProviderEntity();
        entity.setName(name);
        entity.setSiteUrl(siteUrl);
        entity.setDataUrl(dataUrl);
        entity.setCredentials(credential);
        entity.setAdditionalInfoJSON(additionalJson);

        entityManager.persist(entity);

        return entity.getDataFeedProviderId();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public long updateDataFeedProvider(String name, String siteUrl, String dataUrl, String credential, String additionalJson, long dataFeedProviderId) {
        DataFeedProviderEntity dataFeedProviderEntity = entityManager.find(DataFeedProviderEntity.class, dataFeedProviderId);
        dataFeedProviderEntity.setAdditionalInfoJSON(additionalJson);
        dataFeedProviderEntity.setCredentials(credential);
        dataFeedProviderEntity.setDataUrl(dataUrl);
        dataFeedProviderEntity.setName(name);
        dataFeedProviderEntity.setSiteUrl(siteUrl);
        entityManager.merge(dataFeedProviderEntity);

        return dataFeedProviderEntity.getDataFeedProviderId();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void removeDataFeedProviderById(int dataFeedProviderId) {
        DataFeedProviderEntity dataFeedProviderEntity = entityManager.find(DataFeedProviderEntity.class, dataFeedProviderId);
        entityManager.remove(dataFeedProviderEntity);
    }

    @Override
    public Map<Long, Long> getDataFeedProviderStockExchangeMap() {

        Map<Long, Long> result = new HashMap<>();

        List<DataFeedProviderToStockExchangeMapEntity> list = entityManager
                .createQuery("FROM DataFeedProviderToStockExchangeMapEntity")
                .getResultList();
        
        for (DataFeedProviderToStockExchangeMapEntity entity : list) {
            result.put(
                    entity.getDataFeedProviderEntity().getDataFeedProviderId(), 
                    entity.getStockExchangeEntity().getStockExchangeId()
            );
        }
        return result;
    }

}
