package com.fasto.datamanager.paging;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;

/**
 *
 * @author kostenko
 */
public interface FilterFieldToEntityFieldMapper<T>{

    Expression getExpressionByFilterField(String filterField, Root<T> root);

}
