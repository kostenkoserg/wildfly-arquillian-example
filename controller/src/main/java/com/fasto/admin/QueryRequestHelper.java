package com.fasto.admin;

import com.fasto.datamanager.paging.QueryRequest;

/**
 * 
 * @author kostenko
 */
public class QueryRequestHelper {
    
    private QueryRequestHelper() {}
    
    /**
     *
     * @param firstResult firstResult
     * @param maxResults maxResults
     * @param orderind Comma separated fields for sorting (DESC:-field,ASC:field
     *  Example: field1,field2,-field3 )
     * @param filters Comma separated triplets for filtering
     *  Example: field1-eq-name1,field2-lt-123
     */
    public static QueryRequest buildQueryRequest(Integer firstResult, Integer maxResults, String orderind, String filters) {
        
        QueryRequest queryRequest = new QueryRequest();
        queryRequest.setFirstResult(firstResult);
        queryRequest.setMaxResults(maxResults);
        // build ordering
        if (orderind != null && !orderind.isEmpty()) {
            String[] fields = orderind.split(",");
            for (String field : fields) {
                if (field.startsWith("-")) {
                    queryRequest.getOrdering().put(field.substring(1), QueryRequest.Sort.DESC);
                } else {
                    queryRequest.getOrdering().put(field, QueryRequest.Sort.ASC);
                }
            }
        }
        // build filter
        if (filters != null && !filters.isEmpty()) {
            String[] triplets = filters.split(",");
            for (String triplet : triplets) {
                String[] split = triplet.split("-");
                queryRequest.getFilterUnits().add(new QueryRequest.FilterUnit(split[0], QueryRequest.Operator.valueOf(split[1].toUpperCase()), split[2]));
            }
        }
        return queryRequest;
    }
}
