package com.fasto.zootester.api;

import com.fasto.commons.TimeUtil;
import com.fasto.datamanager.dto.SlateDto;
import com.fasto.datamanager.dto.StockExchangeDto;
import com.fasto.datamanager.dto.TournamentDto;
import com.fasto.datamanager.dto.TournamentTemplateDto;

/**
 *
 * @author kostenko
 */
public class FakeFactory {
    
    private static int counter = 0;

    public static TournamentDto fakeTournament() {

        TournamentDto t = new TournamentDto();
        t.setName("FakeTournament_" + counter++);
        t.setTournamentTemplateDto(new TournamentTemplateDto());
        //t.setSlateDto(new SlateDto());
        t.setType("public");
        t.setStructure("structure");
        //t.setDuration(5);
        t.setStartTimestamp(TimeUtil.getCurrentDate());
        t.setVisibleInLobbyTimestamp(TimeUtil.getCurrentDate());
        
        return t;
    }
    
    public static TournamentTemplateDto fakeTournamentTemplate() {
        
        TournamentTemplateDto tt = new TournamentTemplateDto();
        tt.setName("FakeTemplate_" + counter++);
        tt.setBuyIn(0);
        tt.setRake(0);
        tt.setGuaranteed(false);
        tt.setMinPlayers(1);
        tt.setMaxPlayers(Integer.MAX_VALUE);
        tt.setMinEntries(1);
        tt.setMaxEntries(Integer.MAX_VALUE);
        tt.setMaxEntriesPerPlayer(Integer.MAX_VALUE);

        return tt;
    }

    public static SlateDto fakeSlate() {
        
        SlateDto s = new SlateDto();
        s.setName("FakeSlate_" + counter++);
        s.setEnabled(Boolean.TRUE);
        s.setStockExchangeDto(new StockExchangeDto());

        return s;
    }
    
    public static StockExchangeDto fakeStockExchange() {
        
        StockExchangeDto se = new StockExchangeDto();
        se.setName("FakeStockExchange_" + counter++);
        se.setEnabled(true);
        
        return se;
    }
}
