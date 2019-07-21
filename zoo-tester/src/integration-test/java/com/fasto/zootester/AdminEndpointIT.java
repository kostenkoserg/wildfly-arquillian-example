package com.fasto.zootester;

import com.fasto.datamanager.dto.TournamentDto;
import com.fasto.datamanager.dto.TournamentTemplateDto;
import com.fasto.zootester.common.BaseIT;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * To run test use:
 * <pre>
 *      gradle integrationTest --tests com.fasto.zootester.AdminEndpointIT -info
 * </pre>
 *
 * @author kostenko
 */
@RunWith(Arquillian.class)
public class AdminEndpointIT extends BaseIT {

    @Test
    @InSequence(1)
    public void shouldCreateTournamentTemplate() throws Exception {
        TournamentTemplateDto tournamentTemplate = new TournamentTemplateDto();
        tournamentTemplate.setName("AdminEndpointIT_TournamentTemplate");
        testAPI().createTournamentTemplate(tournamentTemplate);
        
        Assert.assertEquals(1, testAPI().selectCount("FROM TOURNAMENT_TEMPLATE WHERE NAME = 'AdminEndpointIT_TournamentTemplate'"));
    }

    @Test
    @InSequence(2)
    public void shouldCreateTournament() throws Exception {
        TournamentDto tournament = new TournamentDto();
        tournament.setName("AdminEndpointIT_TestTournament");
        testAPI().createTournament(tournament);
        
        Assert.assertEquals(1, testAPI().selectCount("FROM TOURNAMENT WHERE NAME = 'AdminEndpointIT_TestTournament'"));
    }

}
