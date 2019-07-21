package com.fasto.datamanager.model;

import com.fasto.datamanager.model.converters.Convertible;
import com.fasto.datamanager.dto.DataFeedSchedulesDto;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Calendar-Based Timer Expressions
 *
 * https://docs.oracle.com/javaee/6/tutorial/doc/bnboy.html
 *
 * @author kostenko
 */
@Entity
@Table(name = "DATA_FEED_SCHEDULES")
public class DataFeedSchedulesEntity implements Convertible<DataFeedSchedulesDto> {

    @Id
    @Column(name = "DATA_FEED_SCHEDULES_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long deataFeedSchedulesId;

    @Column(name = "SECOND")
    private String second;

    @Column(name = "MINUTE")
    private String minute;

    @Column(name = "HOUR")
    private String hour;

    @Column(name = "DAY_OF_MONTH")
    private String dayOfMonth;

    @Column(name = "MONTH")
    private String month;

    @Column(name = "DAY_OF_WEEK")
    private String dayOfWeek;

    @Column(name = "YEAR")
    private String year;

    public long getDeataFeedSchedulesId() {
        return deataFeedSchedulesId;
    }

    public void setDeataFeedSchedulesId(long deataFeedSchedulesId) {
        this.deataFeedSchedulesId = deataFeedSchedulesId;
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

    @Override
    public DataFeedSchedulesDto convert() {
        DataFeedSchedulesDto schDto = new DataFeedSchedulesDto();
        schDto.setDataFeedSchedulesId(getDeataFeedSchedulesId());
        schDto.setDayOfMonth(getDayOfMonth());
        schDto.setDayOfWeek(getDayOfWeek());
        schDto.setHour(getHour());
        schDto.setMinute(getMinute());
        schDto.setMonth(getMonth());
        schDto.setSecond(getSecond());
        schDto.setYear(getYear());

        return schDto;
    }
}
