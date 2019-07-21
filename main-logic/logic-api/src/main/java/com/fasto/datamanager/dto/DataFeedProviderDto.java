package com.fasto.datamanager.dto;

import java.io.Serializable;

/**
 * @author kostenko
 */
public class DataFeedProviderDto implements Serializable {

    private static final long serialVersionUID = -5409531117957299690L;

    private long dataFeedProviderId;

    private String name;

    private String siteUrl;

    private String dataUrl;

    private String credentials;

    private String additionalInfoJSON;
    
    private DataFeedSchedulesDto dataFeedSchedulesDto;

    public long getDataFeedProviderId() {
        return dataFeedProviderId;
    }

    public void setDataFeedProviderId(long dataFeedProviderId) {
        this.dataFeedProviderId = dataFeedProviderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public String getDataUrl() {
        return dataUrl;
    }

    public void setDataUrl(String dataUrl) {
        this.dataUrl = dataUrl;
    }

    public String getCredentials() {
        return credentials;
    }

    public void setCredentials(String credentials) {
        this.credentials = credentials;
    }

    public String getAdditionalInfoJSON() {
        return additionalInfoJSON;
    }

    public void setAdditionalInfoJSON(String additionalInfoJSON) {
        this.additionalInfoJSON = additionalInfoJSON;
    }

    public DataFeedSchedulesDto getDataFeedSchedulesDto() {
        return dataFeedSchedulesDto;
    }

    public void setDataFeedSchedulesDto(DataFeedSchedulesDto dataFeedSchedulesDto) {
        this.dataFeedSchedulesDto = dataFeedSchedulesDto;
    }
    
}
