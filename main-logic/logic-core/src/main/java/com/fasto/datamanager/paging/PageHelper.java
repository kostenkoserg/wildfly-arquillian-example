package com.fasto.datamanager.paging;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.log4j.Logger;

/**
 *
 * @author kostenko
 * @param <T>
 */
public class PageHelper<T> {

    private final static Logger LOGGER = Logger.getLogger(PageHelper.class);
    private final static String FETCH_GRAPH = "javax.persistence.fetchgraph";

    private final Class<T> clazz;
    private final FilterFieldToEntityFieldMapper<T> filterMapper;
    private final QueryRequest queryRequest;
    private final EntityManager entityManager;
    private final String namedEntityGraph;

    private PageHelper(Class<T> clazz, FilterFieldToEntityFieldMapper<T> filterMapper, QueryRequest queryRequest, EntityManager entityManager, String namedEntityGraph) {
        this.clazz = clazz;
        this.filterMapper = filterMapper;
        this.queryRequest = queryRequest;
        this.entityManager = entityManager;
        this.namedEntityGraph = namedEntityGraph;
    }

    public QueryResponse buildPage() {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = cb.createQuery(clazz);
        Root<T> root = criteriaQuery.from(clazz);

        // build where clause
        List<Predicate> predicates = new ArrayList<>();
        List<QueryRequest.FilterUnit> filterUnits = queryRequest.getFilterUnits();
        if (!filterUnits.isEmpty()) {
            for (QueryRequest.FilterUnit filterUnit : filterUnits) {
                if ((filterUnit.getValue() == null) || filterUnit.getValue().toString().trim().isEmpty()) {
                    continue;
                }
                Predicate predicate = null;
                Expression expr = filterMapper.getExpressionByFilterField(filterUnit.getField(), root);

                switch (filterUnit.getOperator()) {
                    case EQ: {
                        predicate = cb.equal(expr, filterUnit.getValue());
                        break;
                    }
                    case GT: {
                        if (expr.getJavaType().equals(Date.class)) {
                            Number nixTime = (Number) filterUnit.getValue();
                            predicate = cb.greaterThan(expr, new Date(nixTime.longValue()));
                        } else {
                            predicate = cb.gt(expr, (Number) filterUnit.getValue());
                        }
                        break;
                    }
                    case LT: {
                        predicate = cb.lt(expr, (Number) filterUnit.getValue());
                        break;
                    }
                    case BTW: {
                        List<Date> btwValues = (List) filterUnit.getValue();
                        predicate = cb.between(expr, btwValues.get(0), btwValues.get(1));
                        break;
                    }
                    case IN: {
                        predicate = expr.in((List) filterUnit.getValue());
                        break;
                    }
                }
                if (predicate != null) {
                    predicates.add(predicate);
                }
            }
        }
        criteriaQuery.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));

        //build ordering
        List<Order> orderList = new ArrayList();
        for (Map.Entry<String, QueryRequest.Sort> entry : queryRequest.getOrdering().entrySet()) {
            Expression orderField = filterMapper.getExpressionByFilterField(entry.getKey(), root);
            if (entry.getValue() == QueryRequest.Sort.ASC) {
                orderList.add(cb.asc(orderField));
            } else {
                orderList.add(cb.desc(orderField));
            }
        }
        if (!orderList.isEmpty()) {
            criteriaQuery.orderBy(orderList);
        }

        // build the query
        Query query = entityManager.createQuery(criteriaQuery);

        if (namedEntityGraph != null) {
            query.setHint(FETCH_GRAPH, entityManager.getEntityGraph(namedEntityGraph));
        }

        if (queryRequest.getFirstResult() != null && queryRequest.getMaxResults() != null) {
            query.setFirstResult(queryRequest.getFirstResult());
            query.setMaxResults(queryRequest.getMaxResults());
        }
        List<T> resultList = query.getResultList();

        // calculate total
        //CriteriaBuilder totalBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> totalCriteria = cb.createQuery(Long.class);
        Root totalRoot = totalCriteria.from(clazz);
        totalCriteria.select(cb.count(totalRoot));
        totalCriteria.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        TypedQuery<Long> totalQuery = entityManager.createQuery(totalCriteria);
        Long total = totalQuery.getSingleResult();

        QueryResponse response = new QueryResponse();
        response.setTotalPageCount(total.intValue());
        response.getData().addAll(resultList);

        return response;
    }

    public static IClazz builder(EntityManager entityManager) {
        return new Builder(entityManager);
    }

    public static IClazz builder(EntityManager entityManager, String namedEntityGraph) {
        return new Builder(entityManager, namedEntityGraph);
    }

    public static class Builder<T> implements IQueryRequest, IClazz, IFilterMapper, Build {

        private Class<T> clazz;
        private FilterFieldToEntityFieldMapper<T> filterMapper;
        private QueryRequest queryRequest;
        private EntityManager entityManager;
        private String namedEntityGraph;

        public Builder(EntityManager entityManager) {
            this.entityManager = entityManager;
        }

        public Builder(EntityManager entityManager, String namedEntityGraph) {
            this.entityManager = entityManager;
            this.namedEntityGraph = namedEntityGraph;
        }

        @Override
        public IFilterMapper withQueryRequest(QueryRequest queryRequest) {
            this.queryRequest = queryRequest;
            return this;
        }

        @Override
        public IQueryRequest withEntity(Class clazz) {
            this.clazz = clazz;
            return this;
        }

        @Override
        public Build withFilterFieldToEntityFieldMapper(FilterFieldToEntityFieldMapper filterMapper) {
            this.filterMapper = filterMapper;
            return this;
        }

        @Override
        public PageHelper build() {
            return new PageHelper(clazz, filterMapper, queryRequest, entityManager, namedEntityGraph);
        }

    }

    public interface IQueryRequest {

        IFilterMapper withQueryRequest(QueryRequest queryRequest);
    }

    public interface IClazz {

        IQueryRequest withEntity(Class clazz);
    }

    public interface IFilterMapper {

        Build withFilterFieldToEntityFieldMapper(FilterFieldToEntityFieldMapper filterBuilder);
    }

    public interface Build {

        PageHelper build();
    }

}
