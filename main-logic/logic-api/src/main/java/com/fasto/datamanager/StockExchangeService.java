package com.fasto.datamanager;

import com.fasto.datamanager.dto.StockExchangeDto;
import com.fasto.datamanager.dto.wrappers.ResponseWrapper;
import com.fasto.datamanager.paging.QueryRequest;
import java.util.Date;

/**
 * @author nkravchenko
 */
public interface StockExchangeService {

    String STOCK_EXCHANGE_SERVICE_NAME = "stock-exchange-service";
    String STOCK_EXCHANGE_SERVICE_JNDI = "java:global/datamanager/datamanager-core/" + STOCK_EXCHANGE_SERVICE_NAME;

    ResponseWrapper<StockExchangeDto> getStockExchanges(QueryRequest queryRequest);

    long createStockExchange(String name, String shortName, boolean enabled, Date startTime, Date closeTime);

    long updateStockExchange(Long id, String name, String shortName, Boolean enabled, Date date, Date date1);

    void removeStockExchangeById(Long stockExchangeId);
}
