package com.fasto.datamanager.paging;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author kostenko
 */
public class QueryRequest implements Serializable {

  private Integer firstResult;
  private Integer maxResults;
  private Map<String, Sort> ordering = new LinkedHashMap<>();
  private List<FilterUnit> filterUnits = new ArrayList<>();

  public Integer getFirstResult() {
    return firstResult;
  }

  public void setFirstResult(Integer firstResult) {
    this.firstResult = firstResult;
  }

  public Integer getMaxResults() {
    return maxResults;
  }

  public void setMaxResults(Integer maxResults) {
    this.maxResults = maxResults;
  }

  public Map<String, Sort> getOrdering() {
    return ordering;
  }

  public void setOrdering(LinkedHashMap<String, Sort> ordering) {
    this.ordering = ordering;
  }

  public List<FilterUnit> getFilterUnits() {
    return filterUnits;
  }

  public FilterUnit getFilterByName(String name) {
    return filterUnits.stream().filter(f -> f.field.equals(name)).findFirst().get();
  }

  public void setFilterUnits(List<FilterUnit> filterUnits) {
    this.filterUnits = filterUnits;
  }

  public enum Sort {
    ASC, DESC;
  }

  public enum Operator {
    EQ, LT, GT, BTW, IN
  }

  public static class FilterUnit implements Serializable {

    private String field;
    private Operator operator;
    private Object value;

    public FilterUnit(String field, Operator operator, Object value) {
      this.field = field;
      this.operator = operator;
      this.value = value;
    }

    public Operator getOperator() {
      return operator;
    }

    public Object getValue() {
      return value;
    }

    public String getField() {
      return field;
    }

    @Override
    public String toString() {
        return "FilterUnit{" + "field=" + field + ", operator=" + operator + ", value=" + value + '}';
    }
  }
}
