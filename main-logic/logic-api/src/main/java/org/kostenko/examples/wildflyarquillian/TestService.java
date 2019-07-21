package org.kostenko.examples.wildflyarquillian;

import com.fasto.datamanager.dto.PayOutDetailsDto;
import com.fasto.datamanager.dto.PayOutDto;
import com.fasto.datamanager.dto.TournamentDto;
import com.fasto.datamanager.dto.TournamentState;
import com.fasto.datamanager.dto.TournamentStatus;
import com.fasto.datamanager.dto.TournamentTemplateDto;
import com.fasto.datamanager.dto.wrappers.ResponseWrapper;
import com.fasto.datamanager.paging.QueryRequest;
import com.fasto.datamanager.paging.QueryResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author kostenko
 * @TODO: Should be move to tournament module
 */
public interface TestService {

  String TOURNAMENT_SERVICE_NAME = "tournament-service";
  String TOURNAMENT_SERVICE_JNDI =
      "java:global/datamanager/datamanager-core/" + TOURNAMENT_SERVICE_NAME;

  QueryResponse<TournamentDto> getTournaments(QueryRequest queryRequest, long playerId);

  QueryResponse<TournamentDto> getTournaments(QueryRequest queryRequest);

  long createTournament(String name, long templateId, long slateId, long payOutId, String type,
      String structure, int duration, Date startTimestamp, Date visibleInLobbyTimestamp);

  ResponseWrapper<TournamentTemplateDto> getTournamentTemplates(QueryRequest queryRequest);

  long createTournamentTemplate(String name, int buyIn, int rake, boolean guaranteed,
      int minPlayers, int maxPlayers, int minEntries, int maxEntries, int maxEntriesPerPlayer);

  ResponseWrapper<PayOutDto> getPayouts(QueryRequest queryRequest);

  long createPayOut(String name);

  long createPayOutDetails(long payOutId, int startPlace, int endPlace, float percentage);

  long createLineUp(String name, long playerId, long slateId);

  long createEntry(long playerId, long tournamentId, long lineUpId);

  long createStockToLineUpMap(long stockId, long lineUpId);

  void setTournamentStatus(long tournamentId, TournamentStatus status);

  TournamentState generateTournamentState(long tournamentId);

  int getCurrentPlayers(long tournamentId);

  int getMyEntriesCount(long playerId, Long tournamentId);

  TournamentDto getTournamentDetails(Long tournamentId);

  long updateTournament(String name, Long tournamentId, Long templateId, Long slateId,
      Long payOutId, String type, String structure, Integer duration, Date startTime,
      Date visibleInLobbyTime);

  void removeTournamentById(Long tournamentId);

  int getCurrentEntries(long tournamentId);

  List<PayOutDetailsDto> getPayOutDetailsByPayOutId(long payOutId);
  
  List<Long> getTournamentIdsByPlayerAndStatus(long playerId, TournamentStatus status);
  
  Map<Long, List<Integer>> getEntriesGroupedDataByPlayerAndTournaments(long playerId, List<Long> tournamentIds);
  
}
