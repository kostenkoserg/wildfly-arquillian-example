package com.fasto.zootester;

import com.fasto.commons.TimeUtil;
import com.fasto.datamanager.DataFeedProviderService;
import com.fasto.datamanager.PlayerService;
import com.fasto.datamanager.SlateService;
import com.fasto.datamanager.StockExchangeService;
import com.fasto.datamanager.StockService;
import com.fasto.datamanager.TournamentService;
import com.fasto.datamanager.dto.EntryDto;
import com.fasto.datamanager.dto.PlayerDto;
import com.fasto.datamanager.dto.TournamentDto;
import com.fasto.datamanager.dto.TournamentStatus;
import com.fasto.datamanager.dto.wrappers.ResponseWrapper;
import com.fasto.zootester.common.BaseIT;
import org.apache.log4j.Logger;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * To run test use:
 * <pre>
 *      gradle integrationTest --tests com.fasto.zootester.TournamentFlowIT -info
 * </pre>
 */
@RunWith(Arquillian.class)
public class TournamentFlowIT extends BaseIT {

    private static final Logger logger = Logger.getLogger(TournamentFlowIT.class);

    //@Test
    //@InSequence(1)
    public void shouldCreateTournamentWithSpecificPlayer() throws Exception {
        //create player
        PlayerDto playerDto = new PlayerDto();
        playerDto.setPlayerId(1l);
        playerDto.setBalance(100);
        playerDto.setRating(10);
        testAPI().createPlayer(playerDto);
        Assert.assertEquals(1, testAPI().selectCount(" FROM PLAYER"));

        //create tournament
        TournamentDto tournament = new TournamentDto();
        tournament.setTournamentId(2l);
        tournament.setName("TournamentFlowIT_TestTournament_1");
        testAPI().createTournament(tournament);
        Assert.assertEquals(1, testAPI().selectCount(" FROM TOURNAMENT WHERE NAME = 'TournamentFlowIT_TestTournament_1'"));

        //create entry
        EntryDto entryDto = new EntryDto();
        entryDto.setScore(10);
        entryDto.setTournamentDto(tournament);
        entryDto.setPayout(20);
        entryDto.setRank(10);
        entryDto.setPlayerDto(playerDto);
        testAPI().createEntry(entryDto);
        Assert.assertEquals(1, testAPI().selectCount(" FROM ENTRY"));
        ResponseWrapper<TournamentDto> upcomingTournaments = testAPI().getUpcomingTournaments(playerDto.getPlayerId());
        Assert.assertEquals(1, upcomingTournaments.getData().size());
        Assert.assertEquals(1, upcomingTournaments.getTotalPageCount());

    }

    @Test
    @InSequence(20)
    public void testRunTournament() throws Exception {
        
        PlayerService playerService = testAPI().getPlayerService();
        TournamentService tournamentService = testAPI().getTournamentService();
        SlateService slateService = testAPI().getSlateService();
        StockService stockService = testAPI().getStockService();
        StockExchangeService stockExchangeService = testAPI().getStockExchangeService();
        DataFeedProviderService dataFeedProviderService = testAPI().getDataFeedProviderService();
        
        long playerId = playerService.createPlayer("player1");
        
        long payOutId = tournamentService.createPayOut("payout1");
        tournamentService.createPayOutDetails(payOutId, 1, 1, 50);
        tournamentService.createPayOutDetails(payOutId, 2, 2, 30);
        tournamentService.createPayOutDetails(payOutId, 3, 3, 20);

        long stockExchangeId = stockExchangeService.createStockExchange("name", "shortName", Boolean.TRUE, TimeUtil.getCurrentDate(), TimeUtil.getCurrentDate());
        long dataFeedProviderId = dataFeedProviderService.createDataFeedProvider("name", "http://", "http://localhost:8080/mock-tradier-0.1/rest/sandboxtradier", "Bearer CCeXAAQv7G0LG16QIrmmLuYclzHc", "{}");
        stockService.createDataFeedProviderToStockExchangeMap(dataFeedProviderId, stockExchangeId);

        long stockId = stockService.createStock("name", "BAC", "url", true, stockExchangeId);
        long slateId = slateService.createSlate("name", stockExchangeId, true);
        stockService.createStockToSlateMap(stockId, slateId);

        long templateId = tournamentService.createTournamentTemplate("name", 10, 2, false, 0, Integer.MAX_VALUE, 0, Integer.MAX_VALUE, Integer.MAX_VALUE);
        long tourmentId = tournamentService.createTournament("name", templateId, slateId, payOutId, "public", "structure", 2, TimeUtil.getCurrentDate(), TimeUtil.getCurrentDate());
        tournamentService.setTournamentStatus(tourmentId, TournamentStatus.OPEN);
        
        long lineUpId = tournamentService.createLineUp("lineup1", playerId, slateId);
        tournamentService.createStockToLineUpMap(stockId, lineUpId);
        
        tournamentService.createEntry(playerId, tourmentId, lineUpId);
        tournamentService.createEntry(playerId, tourmentId, lineUpId);
        tournamentService.createEntry(playerId, tourmentId, lineUpId);

        testAPI().waitForTournamentRunnerShouldRun();
        Thread.sleep(500);
        testAPI().waitForFinishBatches();
        Thread.sleep(500);

        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        Thread.sleep(30 * 1000);
    }
}
