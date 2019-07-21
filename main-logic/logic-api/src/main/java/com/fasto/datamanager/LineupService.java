package com.fasto.datamanager;

import com.fasto.datamanager.dto.LineupDto;
import com.fasto.datamanager.dto.StockDto;
import com.fasto.datamanager.dto.wrappers.ResponseWrapper;
import com.fasto.datamanager.paging.QueryRequest;
import java.util.List;
import java.util.Map;

public interface LineupService {

  String LINEUP_SERVICE_NAME = "lineup-service";
  String LINEUP_SERVICE_JNDI = "java:global/datamanager/datamanager-core/" + LINEUP_SERVICE_NAME;

  ResponseWrapper<LineupDto> getLineups(QueryRequest queryRequest);

  ResponseWrapper<LineupDto> getLineupsByTournamentIdAndPlayerId(QueryRequest queryRequest,
      long playerId);

  void updateLineup(List<Long> stockIdsList, long tournamentId, long playerId, long id, String name);

  void createLineup(long tournamentId, long playerId, List<Long> stockIdsList, String name);

  void lockLineup(long lineupId, long playerId, long tournamentId);

  Map<Long, Integer> getLineupIdToEntriesCountMap(long playerId, long tournamentId);
  
  Map<Long, List<StockDto>> getLineupIdStocksMap(List<Long> lineupIds);

  void lockLineups(List<LineupDto> lineups);
}
