package com.fasto.datamanager.model;

import com.fasto.datamanager.model.converters.Convertible;
import com.fasto.datamanager.dto.PlayerDto;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "PLAYER")
public class PlayerEntity implements Convertible<PlayerDto> {

    @Id
    @Column(name = "PLAYER_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long playerId;

    @Column(name = "ALIAS", nullable = true, length = 45)
    private String alias;

    @Column(name = "OPERATOR_ID", nullable = true)
    private int operatorId;//? check is it necessary

    @Column(name = "PLAYER_FOREIGN_ID", nullable = true)
    private String playerForeignId;//why we need that column?

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "TIME_CREATED", nullable = true)
    private Date timeCreated;

    @Column(name = "RATING", nullable = true)
    private int rating;

    @Column(name = "BALANCE", nullable = true)
    private int balance;
    
    @Column(name = "PASSWORD")
    private String password;
    
    @Column(name = "EMAIL")
    private String email;
    

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public String getPlayerForeignId() {
        return playerForeignId;
    }

    public void setPlayerForeignId(String playerForeignId) {
        this.playerForeignId = playerForeignId;
    }

    public Date getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Date timeCreated) {
        this.timeCreated = timeCreated;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public PlayerDto convert() {
        PlayerDto dto = new PlayerDto();
        dto.setPlayerId(this.playerId);
        dto.setAlias(this.alias);
        dto.setBalance(this.balance);
        dto.setOperatorId(this.operatorId);
        dto.setPlayerForeignId(this.playerForeignId);
        dto.setTimeCreated(this.timeCreated);
        dto.setRating(this.rating);
        dto.setBalance(this.balance);
        dto.setPassword(this.password);
        dto.setEmail(this.email);

        return dto;
    }
}
