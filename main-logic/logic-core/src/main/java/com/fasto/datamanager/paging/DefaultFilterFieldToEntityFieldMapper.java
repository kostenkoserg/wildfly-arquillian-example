package com.fasto.datamanager.paging;

import java.util.Date;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

/**
 * Expects that filter field name and entity field name are equals
 *
 * @author kostenko
 */
public class DefaultFilterFieldToEntityFieldMapper<T> implements FilterFieldToEntityFieldMapper<T> {

  @Override
  public Expression getExpressionByFilterField(String filterField, Root<T> root) {
    String[] filters = filterField.split("\\.");
    filters = filters.length == 0 ? new String[]{filterField} : filters;
    Path objectPath = null;
    for (String filterFieldName : filters) {
      if (objectPath == null) {
        if(root.get(filterFieldName).getJavaType().equals(Date.class)) {
          objectPath = root.<Date>get(filterFieldName);
        }else{
          objectPath = root.get(filterFieldName);
        }
      } else {
        objectPath = objectPath.get(filterFieldName);
      }
    }
    return objectPath;
  }
}
