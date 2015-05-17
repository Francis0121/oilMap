package com.oilMap.server.batch;

/**
 * Created by Francis on 2015-05-17.
 */
public class Efficiency {
    
    private Integer batchPn;

    private String id;
    
    private Double efficiency;

    public Efficiency() {
    }

    public Efficiency(Integer batchPn, String id, Double efficiency) {
        this.batchPn = batchPn;
        this.id = id;
        this.efficiency = efficiency;
    }

    public Integer getBatchPn() {
        return batchPn;
    }

    public void setBatchPn(Integer batchPn) {
        this.batchPn = batchPn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getEfficiency() {
        return efficiency;
    }

    public void setEfficiency(Double efficiency) {
        this.efficiency = efficiency;
    }

    @Override
    public String toString() {
        return "Efficiency{" +
                "batchPn=" + batchPn +
                ", id='" + id + '\'' +
                ", efficiency=" + efficiency +
                '}';
    }
}
