package com.fasto.admin.model;

import java.util.ArrayList;
import java.util.List;

/**
 * PayOut
 */
public class PayOut   {

  private Long id;
  private String name;
  private List<PayOutDetail> payoutdetails = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PayOutDetail> getPayoutdetails() {
        return payoutdetails;
    }

    public void setPayoutdetails(List<PayOutDetail> payoutdetails) {
        this.payoutdetails = payoutdetails;
    }
  
}

