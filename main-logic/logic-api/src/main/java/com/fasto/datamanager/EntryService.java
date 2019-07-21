package com.fasto.datamanager;

import com.fasto.datamanager.dto.EntryDto;
import com.fasto.datamanager.dto.LeaderBoardState;
import java.util.List;

public interface EntryService {

    String ENTRY_SERVICE_NAME = "entry-service";
    String ENTRY_SERVICE_JNDI = "java:global/datamanager/datamanager-core/" + ENTRY_SERVICE_NAME;

    List<EntryDto> getEntriesByTournamentId(Long tournamentId);

    List<EntryDto> getEntriesByTournamentIdAndPlayerId(long playerId, long tournamentId);

    void updateEntries(Long entries, long lineupId, long tournamentId, long playerOd) throws Exception;

    void updateEntry(long entryId, int payOut, int rank, double score);

    LeaderBoardState getCurrentLeaderBoardState();
}
