package com.fasto.datamanager.model;

import com.fasto.datamanager.model.converters.Convertible;
import com.fasto.datamanager.dto.DataFeedProviderDto;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Present information about feed data providers.
 *
 * @author kostenko
 */
@Entity
@Table(name = "DATA_FEED_PROVIDER")
@NamedQuery(name = "DataFeedProviderEntity.getAll", query = "SELECT d FROM DataFeedProviderEntity d")
public class DataFeedProviderEntity implements Convertible<DataFeedProviderDto> {

    @Id
    @Column(name = "DATA_FEED_PROVIDER_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long dataFeedProviderId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "SITE_URL")
    private String siteUrl;

    @Column(name = "DATA_URL")
    private String dataUrl;

    @Column(name = "CREDENTIALS")
    private String credentials;

    @Column(name = "ADDITIONAL_INFO_JSON")
    private String additionalInfoJSON;

    @OneToOne
    @JoinColumn(name = "DATA_FEED_SCHEDULES_ID")
    private DataFeedSchedulesEntity dataFeedSchedules;

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

    public DataFeedSchedulesEntity getDataFeedSchedules() {
        return dataFeedSchedules;
    }

    public void setDataFeedSchedules(DataFeedSchedulesEntity dataFeedSchedules) {
        this.dataFeedSchedules = dataFeedSchedules;
    }

    @Override
    public DataFeedProviderDto convert() {
        DataFeedProviderDto dto = new DataFeedProviderDto();
        dto.setCredentials(this.credentials);
        dto.setDataFeedProviderId(this.dataFeedProviderId);
        dto.setDataUrl(this.dataUrl);
        dto.setName(this.name);
        dto.setSiteUrl(this.siteUrl);
        dto.setAdditionalInfoJSON(this.additionalInfoJSON);

        dto.setDataFeedSchedulesDto(getDataFeedSchedules().convert());
        
        return dto;
    }
}
