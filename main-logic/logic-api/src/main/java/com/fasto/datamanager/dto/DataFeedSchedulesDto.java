package com.fasto.datamanager.dto;

import java.io.Serializable;

/**
 *
 * @author kostenko
 */
public class DataFeedSchedulesDto implements Serializable {

    private long dataFeedSchedulesId;

    private String second;

    private String minute;

    private String hour;

    private String dayOfMonth;

    private String month;

    private String dayOfWeek;

    private String year;

    public long getDataFeedSchedulesId() {
        return dataFeedSchedulesId;
    }

    public void setDataFeedSchedulesId(long dataFeedSchedulesId) {
        this.dataFeedSchedulesId = dataFeedSchedulesId;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(String dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
