package com.oilMap.client.ranking;

/**
 * Created by Francis on 2015-05-17.
 */
public class Batch {
    
    private Integer pn;
    
    private String batchDate;

    public Batch() {
    }

    public Integer getPn() {
        return pn;
    }

    public void setPn(Integer pn) {
        this.pn = pn;
    }

    public String getBatchDate() {
        return batchDate;
    }

    public void setBatchDate(String batchDate) {
        this.batchDate = batchDate;
    }

    @Override
    public String toString() {
        return "Batch{" +
                "pn=" + pn +
                ", batchDate='" + batchDate + '\'' +
                '}';
    }
}
