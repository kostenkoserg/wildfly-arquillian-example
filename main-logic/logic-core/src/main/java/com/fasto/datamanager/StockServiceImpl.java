package com.fasto.datamanager;

import com.fasto.datamanager.dto.StockDto;
import com.fasto.datamanager.dto.StockQuotesDto;
import com.fasto.datamanager.dto.wrappers.ResponseWrapper;
import com.fasto.datamanager.model.DataFeedProviderEntity;
import com.fasto.datamanager.model.DataFeedProviderToStockExchangeMapEntity;
import com.fasto.datamanager.model.SlateEntity;
import com.fasto.datamanager.model.StockEntity;
import com.fasto.datamanager.model.StockExchangeEntity;
import com.fasto.datamanager.model.StockToLineupMapEntity;
import com.fasto.datamanager.model.StockToSlateMapEntity;
import com.fasto.datamanager.model.converters.Convertible;
import com.fasto.datamanager.paging.DefaultFilterFieldToEntityFieldMapper;
import com.fasto.datamanager.paging.PageHelper;
import com.fasto.datamanager.paging.QueryRequest;
import com.fasto.datamanager.paging.QueryResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * @author nkravchenko
 */
@Remote(StockService.class)
@Stateless(name = StockService.STOCK_SERVICE_NAME)
public class StockServiceImpl implements StockService {

  @PersistenceContext
  private EntityManager entityManager;

  @EJB(lookup = StockQuotesService.STOCK_QUOTES_SERVICE_JNDI)
  private StockQuotesService stockQuotesService;

  @Override
  @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
  public ResponseWrapper<StockDto> getStocks(QueryRequest queryRequest) {

    PageHelper<StockEntity> pageHelper = PageHelper.builder(entityManager)
        .withEntity(StockEntity.class)
        .withQueryRequest(queryRequest)
        .withFilterFieldToEntityFieldMapper(new DefaultFilterFieldToEntityFieldMapper())
        .build();

    QueryResponse queryResponse = pageHelper.buildPage();

    List<StockEntity> stockEntities = queryResponse.getData();
    return new ResponseWrapper<>(Convertible.convert(stockEntities),
        queryResponse.getTotalPageCount());
  }

  @Override
  @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
  public StockDto getStocksByID(Long stockID) {
    StockEntity stockEntity = entityManager.find(StockEntity.class, stockID);

    return stockEntity.convert();
  }

  @Override
  public List<StockDto> getStocksByLineupId(Long lineupId) {
    Query query = entityManager.createQuery("SELECT distinct s.stockEntity FROM "
        + "StockToLineupMapEntity s WHERE s.lineupEntity.lineupId = :lineupId");
    query.setParameter("lineupId", lineupId);
    List<StockEntity> stockEntities = query.getResultList();

    List<StockDto> stockDtos = Convertible.convert(stockEntities);
    setAdditionalParameters(stockDtos);
    return stockDtos;
  }

  @Override
  public Map<Long, List<StockDto>> getLineupIdToStocksMap() {
    Query query = entityManager.createQuery("SELECT distinct sle FROM "
        + "StockToLineupMapEntity sle");

    List<StockToLineupMapEntity> resultList = query.getResultList();
    Map<Long, List<StockDto>> listMap = new HashMap<>();
    resultList.stream().forEach(stockToLineupMapEntity -> {
      long lineupId = stockToLineupMapEntity.getLineupEntity().getLineupId();
      List<StockDto> stockDtos = listMap.get(lineupId);

      if (stockDtos == null) {
        stockDtos = new ArrayList<>();
      }
      stockDtos.add(stockToLineupMapEntity.getStockEntity().convert());
      listMap.put(lineupId, stockDtos);
    });

    return listMap;
  }

  //set gain, position
  private void setAdditionalParameters(List<StockDto> stockDtos) {
    List<StockQuotesDto> stockQuotesDtoList = stockQuotesService
        .getQuotesStocksByStockIds(stockDtos.stream().map(st -> st.getStockId()).collect(
            Collectors.toList())).getData();
    for (StockDto stock : stockDtos) {
      StockQuotesDto finalizedStockDtos = stockQuotesDtoList.stream()
          .filter(s -> s.getStockDto().getSymbol().equals(stock.getSymbol())).findFirst()
          .orElse(null);
      stockQuotesDtoList.remove(finalizedStockDtos);
      if (finalizedStockDtos != null) {
        BigDecimal lastValue = finalizedStockDtos.getValue();
        // SLV Should have called  getFinalizeQuotes() -->  getLastQuote()
        StockQuotesDto initStockValues = stockQuotesDtoList.stream()
            .filter(s -> s.getStockDto().getSymbol().equals(stock.getSymbol())).findFirst()
            .orElse(null);
        if (initStockValues != null) {
          BigDecimal initValue = initStockValues
              .getValue();// There is one qoute stored only , so findFirst is appropriate here
          BigDecimal stockScore = lastValue.subtract(initValue)
              .divide(initValue, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
          stock.setGain(stockScore.floatValue());
          //stock.setPosition(finalizedStockDtos.);
        }
      }
    }
  }


  @Override
  @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
  public long createStock(String name, String symbol, String url, boolean enabled,
      long stockExchangeId) {
    StockEntity entity = new StockEntity();
    entity.setName(name);
    entity.setSymbol(symbol);
    entity.setUrl(url);
    entity.setEnabled(enabled);
    entity.setStockExchangeEntity(entityManager.find(StockExchangeEntity.class, stockExchangeId));

    entityManager.persist(entity);

    return entity.getStockId();
  }

  @Override
  @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
  public long createStockToSlateMap(long stockId, long slateId) {

    StockToSlateMapEntity entity = new StockToSlateMapEntity();
    entity.setStockEntity(entityManager.find(StockEntity.class, stockId));
    entity.setSlateEntity(entityManager.find(SlateEntity.class, slateId));

    entityManager.persist(entity);

    return entity.getStockToSlateMapId();
  }

  @Override
  public long createDataFeedProviderToStockExchangeMap(long dataFeedProviderId,
      long stockExchangeId) {

    DataFeedProviderToStockExchangeMapEntity entity = new DataFeedProviderToStockExchangeMapEntity();
    entity.setDataFeedProviderEntity(
        entityManager.find(DataFeedProviderEntity.class, dataFeedProviderId));
    entity.setStockExchangeEntity(entityManager.find(StockExchangeEntity.class, stockExchangeId));

    entityManager.persist(entity);

    return entity.getDataFeedProviderToStockExchangeId();
  }

  @Override
  @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
  public long updateStock(long stockId, String name, String symbol, String url, boolean enabled,
      long stockExchangeId) {
    StockEntity stockEntity = entityManager.find(StockEntity.class, stockId);
    stockEntity.setName(name);
    stockEntity.setSymbol(symbol);
    stockEntity.setUrl(url);
    stockEntity.setEnabled(enabled);

    StockExchangeEntity stockExchangeEntity = entityManager
        .find(StockExchangeEntity.class, stockExchangeId);
    stockEntity.setStockExchangeEntity(stockExchangeEntity);
    entityManager.merge(stockEntity);
    return stockEntity.getStockId();
  }

  @Override
  @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
  public long removeStockById(long stockId) {
    StockEntity stockEntity = entityManager.find(StockEntity.class, stockId);
    entityManager.remove(stockEntity);
    return stockEntity.getStockId();
  }

  @Override
  public Map<Long, Set<StockDto>> getStocksBySlateIds(List<Long> slateIds) {

    Map<Long, Set<StockDto>> slateIdToStockDtoListMap = new HashMap<>();

    String sql = "SELECT m.slateEntity.slateId, m.stockEntity FROM StockToSlateMapEntity m  WHERE m.slateEntity.slateId in (:slateIds)";
    Query query = entityManager.createQuery(sql).setParameter("slateIds", slateIds);

    List<Object[]> queryResultList = query.getResultList();

    for (Object[] queryResult : queryResultList) {

      Long slateId = (Long) queryResult[0];
      StockEntity stock = (StockEntity) queryResult[1];

      Set<StockDto> stocks = slateIdToStockDtoListMap.get(slateId);
      if (stocks == null) {
        stocks = new HashSet<>();
        stocks.add(stock.convert());
        slateIdToStockDtoListMap.put(slateId, stocks);
      } else {
        stocks.add(stock.convert());
      }
    }

    return slateIdToStockDtoListMap;
  }
}
