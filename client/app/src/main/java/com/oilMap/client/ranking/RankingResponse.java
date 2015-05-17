package com.oilMap.client.ranking;

import java.util.List;

/**
 * Created by Francis on 2015-05-17.
 */
public class RankingResponse {

    private List<Ranking> rankingList;

    public RankingResponse() {
    }

    public List<Ranking> getRankingList() {
        return rankingList;
    }

    public void setRankingList(List<Ranking> rankingList) {
        this.rankingList = rankingList;
    }

    @Override
    public String toString() {
        return "RankingResponse{" +
                "rankingList=" + rankingList +
                '}';
    }
}
