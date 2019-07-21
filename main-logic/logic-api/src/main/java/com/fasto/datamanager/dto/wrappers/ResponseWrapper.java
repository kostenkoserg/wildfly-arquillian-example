package com.fasto.datamanager.dto.wrappers;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;

public class ResponseWrapper<T extends Serializable> implements Serializable {

  private int totalPageCount;
  private String error;
  private RequestDtoWrapperType type;
  private String route;
  private String token;
  private List<T> data;
  private String sessionId;
  private boolean isGeneratedByServer;

  public ResponseWrapper() {
  }

  public ResponseWrapper(String error) {
    this.error = error;
  }

  public ResponseWrapper(List dtoList, int totalPageCount) {
    this.data = dtoList;
    this.totalPageCount = totalPageCount;
  }

  public void initDataFromRequest(RequestDtoWrapper requestDtoWrapper) {
    this.route = requestDtoWrapper.getRoute();
    this.token = requestDtoWrapper.getToken();
    this.type = requestDtoWrapper.getType();
  }

  public void setType(RequestDtoWrapperType type) {
    this.type = type;
  }

  public void setGeneratedByServer(boolean generatedByServer) {
    isGeneratedByServer = generatedByServer;
  }

  @JsonProperty("isGeneratedByServer")
  public boolean isGeneratedByServer() {
    return isGeneratedByServer;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public List<T> getData() {
    return data;
  }

  public int getTotalPageCount() {
    return totalPageCount;
  }

  public RequestDtoWrapperType getType() {
    return type;
  }

  public String getRoute() {
    return route;
  }

  public String getToken() {
    return token;
  }

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }


}
