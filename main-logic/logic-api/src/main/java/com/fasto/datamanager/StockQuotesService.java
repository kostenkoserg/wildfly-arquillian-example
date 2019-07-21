package com.fasto.datamanager;

import com.fasto.datamanager.dto.StockQuotesDto;
import com.fasto.datamanager.dto.wrappers.ResponseWrapper;
import java.util.Date;
import java.util.List;

/**
 * @author nkravchenko
 */
public interface StockQuotesService {

  String STOCK_QUOTES_SERVICE_NAME = "stock-quotes-service";
  String STOCK_QUOTES_SERVICE_JNDI =
      "java:global/datamanager/datamanager-core/" + STOCK_QUOTES_SERVICE_NAME;

  ResponseWrapper<StockQuotesDto> getQuotesStocksByStockIds(List<Long> stockIds);

  void saveQuotesStocks(long dataFeedProfivderId, List<StockQuotesDto> stockQuotesDto);
  
  List<StockQuotesDto> getQuotesBetween(Date from, Date to, long slateId);
}
