package com.oilMap.server.bill;

/**
 * Created by Francis on 2015-05-16.
 */
public class FuelBill {

    private Integer pn;
    
    private String id;
    
    private String title;
    
    private Integer bill;
    
    private String billDate;

    public FuelBill() {
    }

    public FuelBill(String id, String title, Integer bill) {
        this.id = id;
        this.title = title;
        this.bill = bill;
    }

    public Integer getPn() {
        return pn;
    }

    public void setPn(Integer pn) {
        this.pn = pn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getBill() {
        return bill;
    }

    public void setBill(Integer bill) {
        this.bill = bill;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    @Override
    public String toString() {
        return "FuelBill{" +
                "pn=" + pn +
                ", id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", bill=" + bill +
                ", billDate='" + billDate + '\'' +
                '}';
    }
}
