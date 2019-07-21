package com.fasto.datamanager;

import com.fasto.datamanager.dto.StockQuotesDto;
import com.fasto.datamanager.dto.wrappers.ResponseWrapper;
import com.fasto.datamanager.model.StockEntity;
import com.fasto.datamanager.model.StockExchangeEntity;
import com.fasto.datamanager.model.StockQuotesEntity;
import com.fasto.datamanager.model.converters.Convertible;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
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
@Remote(StockQuotesService.class)
@Stateless(name = StockQuotesService.STOCK_QUOTES_SERVICE_NAME)
public class StockQuotesServiceImpl implements StockQuotesService {

    @EJB
    StockService stockService;

    @PersistenceContext
    private EntityManager entityManager;
    final static Log logger = LogFactory.getLog(StockQuotesServiceImpl.class);

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public ResponseWrapper<StockQuotesDto> getQuotesStocksByStockIds(List<Long> stockIds) {
        logger.debug("start get StockQuotesEntity List");
        Query query = entityManager
                .createQuery("from StockQuotesEntity sqe LEFT JOIN FETCH sqe.stockEntity WHERE sqe.stockEntity.stockId in (:stockIds) order by sqe.tradeDate DESC");
        query.setParameter("stockIds", stockIds);
        List<StockQuotesEntity> stockQuotesEntities = query.getResultList();
        logger.debug("finish get StockQuotesEntity List");
        return new ResponseWrapper(Convertible.convert(stockQuotesEntities),
                stockQuotesEntities.size());

    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void saveQuotesStocks(long dataFeedProviderId, List<StockQuotesDto> stockQuotesDtoList) {

        long stockExchangeId = 0l;
        String sql = "SELECT m.stockExchangeEntity FROM DataFeedProviderToStockExchangeMapEntity m WHERE m.dataFeedProviderEntity.dataFeedProviderId = :dataFeedProviderId";
        Query query = entityManager.createQuery(sql).setParameter("dataFeedProviderId", dataFeedProviderId);
        List<StockExchangeEntity> queryResultList = query.getResultList();
        if (!queryResultList.isEmpty()) {
            stockExchangeId = queryResultList.get(0).getStockExchangeId();
        }

        List<StockEntity> stocks = entityManager.createQuery("from StockEntity").getResultList();
        for (StockQuotesDto stockQuotesDto : stockQuotesDtoList) {
            StockQuotesEntity stockQuotesEntity = StockQuotesEntity.convertToEntity(stockQuotesDto);
            for (StockEntity stock : stocks) {
                if (stock.getSymbol().equals(stockQuotesDto.getStockDto().getSymbol())
                        && stock.getStockExchangeEntity().getStockExchangeId() == stockExchangeId) {

                    stockQuotesEntity.setStockEntity(stock);
                    entityManager.persist(stockQuotesEntity);
                }
            }
        }
    }

    @Override
    public List<StockQuotesDto> getQuotesBetween(Date from, Date to, long slateId) {

        String sql = "SELECT q FROM StockQuotesEntity q "
                + "JOIN SlateEntity s "
                + "ON (s.stockExchangeEntity.stockExchangeId = q.stockEntity.stockExchangeEntity.stockExchangeId) "
                + "LEFT JOIN FETCH q.stockEntity "
                + "WHERE q.eventDate BETWEEN :from AND :to AND s.slateId=:slateId";

        List<StockQuotesEntity> resultList = entityManager.createQuery(sql)
                .setParameter("from", from)
                .setParameter("to", to)
                .setParameter("slateId", slateId)
                .getResultList();

        return Convertible.convert(resultList);
    }
}
