package com.fasto.datamanager.dto.wrappers;

import java.io.Serializable;
import java.util.Objects;

public class RequestDtoWrapper implements Serializable {

    public RequestDtoWrapper() {
    }

    public RequestDtoWrapper(String route, String token, Object data) {
        this.route = route;
        this.token = token;
        this.data = data;
    }

    private RequestDtoWrapperType type;
    private String route;
    private String token;
    private Object data;
    private String sessionId;
    private String userName;
    private String password;



    public RequestDtoWrapperType getType() {
        return type;
    }

    public void setType(RequestDtoWrapperType type) {
        this.type = type;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestDtoWrapper that = (RequestDtoWrapper) o;
        return Objects.equals(type, that.type) &&
                Objects.equals(route, that.route) &&
                Objects.equals(token, that.token) &&
                Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, route, token, data);
    }

    @Override
    public String toString() {
        return "RequestDtoWrapper{" +
                "type='" + type + '\'' +
                ", route='" + route + '\'' +
                ", token='" + token + '\'' +
                ", data=" + data +
                '}';
    }
}
