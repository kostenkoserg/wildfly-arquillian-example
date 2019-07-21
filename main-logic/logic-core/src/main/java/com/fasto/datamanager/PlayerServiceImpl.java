package com.fasto.datamanager;

import com.fasto.datamanager.dto.PlayerDto;
import com.fasto.datamanager.exception.PlayerNotFoundException;
import com.fasto.datamanager.exception.PlayerRegistrationException;
import com.fasto.datamanager.model.PlayerEntity;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author nkravchenko
 */
@Remote(PlayerService.class)
@Stateless(name = PlayerService.PLAYER_SERVICE_NAME)
public class PlayerServiceImpl implements PlayerService {

    final static Log logger = LogFactory.getLog(PlayerServiceImpl.class);

    @PersistenceContext
    EntityManager entityManager;

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public long createPlayer(PlayerDto playerDto) {
        PlayerEntity playerEntity = new PlayerEntity();
        playerEntity.setTimeCreated(new Date());
        playerEntity.setRating(playerDto.getRating());
        playerEntity.setBalance(playerDto.getBalance());
        playerEntity.setOperatorId(playerDto.getOperatorId());
        playerEntity.setAlias(playerDto.getAlias());

        entityManager.persist(playerEntity);
        return playerEntity.getPlayerId();
    }

    @Override
    public long createPlayer(String alias) {
        PlayerEntity entity = new PlayerEntity();
        entity.setAlias(alias);
        entityManager.persist(entity);
        return entity.getPlayerId();
    }

    @Override
    public PlayerDto getPlayerByAlias(String alias) {
        Query q = entityManager.createQuery("FROM PlayerEntity p WHERE p.alias = :alias");
        q.setParameter("alias", alias);

        List<PlayerEntity> players = q.getResultList();

        return players.isEmpty() ? null : players.get(0).convert();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public long register(String email, String password, String alias) throws PlayerRegistrationException {
        
        List<PlayerEntity> players = entityManager.createQuery("FROM PlayerEntity p WHERE p.email = :email OR p.alias = :alias", PlayerEntity.class)
                        .setParameter("email", email)
                        .setParameter("alias", alias)
                        .getResultList();

        if(!players.isEmpty()) {
            if (players.get(0).getEmail().equals(email)) {
                throw new PlayerRegistrationException(PlayerRegistrationException.CODE.EMAIL_EXIST, String.format("Player with email %s already registered.", email));
            } else {
                throw new PlayerRegistrationException(PlayerRegistrationException.CODE.ALIAS_EXIST, String.format("Player with alias %s already registered.", alias));
            }
        }

        PlayerEntity playerEntity = new PlayerEntity();
        playerEntity.setTimeCreated(new Date());
        playerEntity.setEmail(email);
        playerEntity.setPassword(password);
        playerEntity.setAlias(alias);
        playerEntity.setBalance(10000);

        entityManager.persist(playerEntity);
        return playerEntity.getPlayerId();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public boolean changePassword(String email, String password, String newPassword) {

        int result = entityManager.createQuery("UPDATE PlayerEntity p SET p.password = :newPassword  WHERE p.email = :email AND p.password = :password")
                .setParameter("email", email)
                .setParameter("password", password)
                .setParameter("newPassword", newPassword)
                .executeUpdate();

        if (result > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean forgotPassword(String email) throws PlayerNotFoundException {
        
        List<PlayerEntity> players = entityManager.createQuery("FROM PlayerEntity p WHERE p.email = :email", PlayerEntity.class)
                        .setParameter("email", email)
                        .getResultList();

        if(players.isEmpty()) {
            throw new PlayerNotFoundException(String.format("Account with provided email not found.", email));
        }
        logger.warn("Not supported yet.");
        return false;
    }

    @Override
    public PlayerDto login(String email, String password) {

        List<PlayerEntity> players = entityManager.createQuery("FROM PlayerEntity p WHERE p.email = :email AND p.password = :password", PlayerEntity.class)
                        .setParameter("email", email)
                        .setParameter("password", password)
                        .getResultList();

        if(players.isEmpty()) {
            return null; // @TODO: throw PlayerServiceException
        }
        
        PlayerDto dto = players.get(0).convert();
        return dto;
    }
}
