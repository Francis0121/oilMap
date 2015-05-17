package com.oilMap.client.ranking;

/**
 * Created by Francis on 2015-05-17.
 */
public class Ranking {
    
    private Batch batch;
    
    private Auth auth;
    
    private Efficiency efficiency;

    public Ranking() {
        
    }

    public Batch getBatch() {
        return batch;
    }

    public void setBatch(Batch batch) {
        this.batch = batch;
    }

    public Auth getAuth() {
        return auth;
    }

    public void setAuth(Auth auth) {
        this.auth = auth;
    }

    public Efficiency getEfficiency() {
        return efficiency;
    }

    public void setEfficiency(Efficiency efficiency) {
        this.efficiency = efficiency;
    }

    @Override
    public String toString() {
        return "Ranking{" +
                "batch=" + batch +
                ", auth=" + auth +
                ", efficiency=" + efficiency +
                '}';
    }
}
