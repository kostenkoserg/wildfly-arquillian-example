package com.fasto.datamanager;

import com.fasto.datamanager.dto.SlateDto;
import com.fasto.datamanager.dto.wrappers.ResponseWrapper;
import com.fasto.datamanager.paging.QueryRequest;

public interface SlateService {
    String SLATE_SERVICE_NAME = "slate-service";
    String SLATE_SERVICE_JNDI_NAME = "java:global/datamanager/datamanager-core/" + SLATE_SERVICE_NAME;


    ResponseWrapper<SlateDto> getSlates(QueryRequest queryRequest);

    long createSlate(String name, long stockExchangeId, boolean enabled);
}
