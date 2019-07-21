package com.fasto.datamanager.model;

import com.fasto.datamanager.model.converters.Convertible;
import com.fasto.datamanager.dto.EntryDto;
import com.fasto.datamanager.dto.LineupDto;
import com.fasto.datamanager.dto.PlayerDto;
import com.fasto.datamanager.dto.TournamentDto;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "ENTRY")
public class EntryEntity implements Convertible<EntryDto> {

    @Id
    @Column(name = "ENTRY_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long entryId;

    @ManyToOne
    @JoinColumn(name = "PLAYER_ID")
    private PlayerEntity playerEntity;

    @ManyToOne
    @JoinColumn(name = "TOURNAMENT_ID", nullable = false)
    private TournamentEntity tournamentEntity;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TIMESTAMP", nullable = true)
    private Date timestamp;

    @Column(name = "STATUS_ID", nullable = true)
    private int statusId;

    @Column(name = "RANK", nullable = true)
    private int rank;

    @Column(name = "PAYOUT", nullable = true)
    private int payout;
    
    @ManyToOne
    @JoinColumn(name = "LINEUP_ID", nullable = true)
    private LineupEntity lineupEntity;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_TIME_STATUS_CHANGE", nullable = true)
    private Date lastTimeStatusChange;

    @Column(name = "SCORE", nullable = true)
    private double score;

    public long getEntryId() {
        return entryId;
    }

    public void setEntryId(long entryId) {
        this.entryId = entryId;
    }


    public PlayerEntity getPlayerEntity() {
        return playerEntity;
    }

    public void setPlayerEntity(PlayerEntity playerEntity) {
        this.playerEntity = playerEntity;
    }

    public TournamentEntity getTournamentEntity() {
        return tournamentEntity;
    }

    public void setTournamentEntity(TournamentEntity tournamentEntity) {
        this.tournamentEntity = tournamentEntity;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getPayout() {
        return payout;
    }

    public void setPayout(int payout) {
        this.payout = payout;
    }

    public LineupEntity getLineupEntity() {
        return lineupEntity;
    }

    public void setLineupEntity(LineupEntity lineupEntity) {
        this.lineupEntity = lineupEntity;
    }

    public Date getLastTimeStatusChange() {
        return lastTimeStatusChange;
    }

    public void setLastTimeStatusChange(Date lastTimeStatusChange) {
        this.lastTimeStatusChange = lastTimeStatusChange;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntryEntity that = (EntryEntity) o;
        return entryId == that.entryId &&
                Objects.equals(playerEntity, that.playerEntity) &&
                Objects.equals(tournamentEntity, that.tournamentEntity) &&
                Objects.equals(timestamp, that.timestamp) &&
                Objects.equals(statusId, that.statusId) &&
                Objects.equals(rank, that.rank) &&
                Objects.equals(payout, that.payout) &&
                Objects.equals(lineupEntity, that.lineupEntity) &&
                Objects.equals(lastTimeStatusChange, that.lastTimeStatusChange) &&
                Objects.equals(score, that.score);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entryId, playerEntity, tournamentEntity, timestamp, statusId, rank, payout, lineupEntity, lastTimeStatusChange, score);
    }

    @Override
    public EntryDto convert() {

        EntryDto dto = new EntryDto();
        dto.setEntryId(entryId);

        PlayerDto playerDto = this.playerEntity.convert();
        dto.setPlayerDto(playerDto);

        TournamentDto tournamentDto = this.tournamentEntity.convert();
        dto.setTournamentDto(tournamentDto);

        dto.setTimestamp(this.timestamp);
        dto.setStatusId(this.statusId);
        dto.setRank(this.rank);
        dto.setPayout(this.payout);
        dto.setScore(this.score);
        dto.setLastTimeStatusChange(this.lastTimeStatusChange);

        Optional<LineupEntity> optionalLineupEntity = Optional.ofNullable(this.lineupEntity);
        LineupDto lineupDto = optionalLineupEntity.isPresent() ? optionalLineupEntity.get().convert() : null;
        dto.setLineupDto(lineupDto);

        return dto;
    }

}
