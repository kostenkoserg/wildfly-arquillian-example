package com.fasto.datamanager.paging;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @TODO: Copy of ResponseWrapper
 */
public class QueryResponse<T extends Serializable> implements Serializable {

    private List<T> data = new ArrayList<>();
    private int totalPageCount;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public int getTotalPageCount() {
        return totalPageCount;
    }

    public void setTotalPageCount(int totalPageCount) {
        this.totalPageCount = totalPageCount;
    }

}
