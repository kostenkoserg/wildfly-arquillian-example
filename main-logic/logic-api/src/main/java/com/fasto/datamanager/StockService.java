package com.fasto.datamanager;

import com.fasto.datamanager.dto.StockDto;
import com.fasto.datamanager.dto.wrappers.ResponseWrapper;
import com.fasto.datamanager.paging.QueryRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 * @author nkravchenko
 */
public interface StockService {

  String STOCK_SERVICE_NAME = "stock-service";
  String STOCK_SERVICE_JNDI = "java:global/datamanager/datamanager-core/" + STOCK_SERVICE_NAME;

  ResponseWrapper<StockDto> getStocks(QueryRequest queryRequest);

  long createStock(String name, String symbol, String url, boolean enabled, long stockExchangeId);

  long createStockToSlateMap(long stockId, long slateId);

  long createDataFeedProviderToStockExchangeMap(long dataFeedProviderId, long stockExchangeId);

  @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
  long updateStock(long stockId, String name, String symbol, String url, boolean enabled,
      long stockExchangeId);

  @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
  long removeStockById(long stockId);

  StockDto getStocksByID(Long stockID);

  List<StockDto> getStocksByLineupId(Long lineupId);

  Map<Long, List<StockDto>> getLineupIdToStocksMap();

  Map<Long, Set<StockDto>> getStocksBySlateIds(List<Long> slateIds);
}
