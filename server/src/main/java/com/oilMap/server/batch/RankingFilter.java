package com.oilMap.server.batch;

import com.oilMap.server.util.AbstractListFilter;

/**
 * Created by Francis on 2015-05-17.
 */
public class RankingFilter extends AbstractListFilter{
    
    private String id;

    public RankingFilter() {
    }

    public RankingFilter(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "RankingFilter{" +
                "id='" + id + '\'' +
                '}';
    }
}
