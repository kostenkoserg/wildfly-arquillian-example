package com.fasto.datamanager;

import com.fasto.datamanager.dto.DataFeedProviderDto;
import com.fasto.datamanager.dto.wrappers.ResponseWrapper;
import com.fasto.datamanager.paging.QueryRequest;
import java.util.List;
import java.util.Map;


/**
 *
 * @author kostenko
 */
public interface DataFeedProviderService {

    String DATA_FEED_PROVIDER_SERVICE_NAME = "datafeed-provider-service";
    String DATA_FEED_PROVIDER_SERVICE_JNDI = "java:global/datamanager/datamanager-core/" + DATA_FEED_PROVIDER_SERVICE_NAME;

    ResponseWrapper<DataFeedProviderDto> getDataFeedProviders(QueryRequest queryRequest);
    
    long createDataFeedProvider(String name, String siteUrl, String dataUrl, String credential, String additionalJson);

    long updateDataFeedProvider(String name, String siteUrl, String dataUrl, String credential, String additionalJson, long dataFeedProviderId);

    void removeDataFeedProviderById(int dataFeedProviderId);
    
    Map<Long, Long> getDataFeedProviderStockExchangeMap ();
}
