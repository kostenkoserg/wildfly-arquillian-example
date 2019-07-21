package com.fasto.datamanager;

import com.fasto.datamanager.dto.PlayerDto;
import com.fasto.datamanager.exception.PlayerNotFoundException;
import com.fasto.datamanager.exception.PlayerRegistrationException;

public interface PlayerService {

    String PLAYER_SERVICE_NAME = "player-service";
    String PLAYER_SERVICE_JNDI = "java:global/datamanager/datamanager-core/" + PLAYER_SERVICE_NAME;

    long createPlayer(PlayerDto playerDto);

    long createPlayer(String alias);

    PlayerDto getPlayerByAlias(String alias);

    public long register(String email, String password, String alias) throws PlayerRegistrationException;
    
    public boolean changePassword(String email, String password, String newPassword);
    
    public boolean forgotPassword(String email) throws PlayerNotFoundException;
    
    public PlayerDto login(String email, String password);

}
