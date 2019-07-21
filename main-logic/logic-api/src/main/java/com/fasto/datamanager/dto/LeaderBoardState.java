package com.fasto.datamanager.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kostenko
 */
public class LeaderBoardState implements Serializable {

    private List all = new ArrayList<>();
    private List day = new ArrayList<>();
    private List week = new ArrayList<>();
    private List month = new ArrayList<>();

    public List getAll() {
        return all;
    }

    public void setAll(List all) {
        this.all = all;
    }

    public List getDay() {
        return day;
    }

    public void setDay(List day) {
        this.day = day;
    }

    public List getWeek() {
        return week;
    }

    public void setWeek(List week) {
        this.week = week;
    }

    public List getMonth() {
        return month;
    }

    public void setMonth(List month) {
        this.month = month;
    }

    public static class AllTimeWinnersUnit implements Serializable {

        private List<LeaderBoardUnit> allTimeWinners;

        public AllTimeWinnersUnit(List<LeaderBoardUnit> allTimeWinners) {
            this.allTimeWinners = allTimeWinners;
        }

        public List<LeaderBoardUnit> getAllTimeWinners() {
            return allTimeWinners;
        }

        public void setAllTimeWinners(List<LeaderBoardUnit> allTimeWinners) {
            this.allTimeWinners = allTimeWinners;
        }
    }

    public static class PlayersUnit implements Serializable {

        private Integer duration;
        private List<LeaderBoardUnit> players;

        public PlayersUnit(Integer duration, List<LeaderBoardUnit> players) {
            this.duration = duration;
            this.players = players;
        }
        
        public Integer getDuration() {
            return duration;
        }

        public void setDuration(Integer duration) {
            this.duration = duration;
        }

        public List<LeaderBoardUnit> getPlayers() {
            return players;
        }

        public void setPlayers(List<LeaderBoardUnit> players) {
            this.players = players;
        }
    }
}
