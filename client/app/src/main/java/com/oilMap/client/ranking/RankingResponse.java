package com.oilMap.client.ranking;

import java.util.List;

/**
 * Created by Francis on 2015-05-17.
 */
public class RankingResponse {

    private List<Ranking> rankingList;

    private Double avgGasoline;

    private Ranking myRanking;

    public RankingResponse() {
    }

    public Double getAvgGasoline() {
        return avgGasoline;
    }

    public void setAvgGasoline(Double avgGasoline) {
        this.avgGasoline = avgGasoline;
    }

    public Ranking getMyRanking() {
        return myRanking;
    }

    public void setMyRanking(Ranking myRanking) {
        this.myRanking = myRanking;
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
                ", avgGasoline=" + avgGasoline +
                ", myRanking=" + myRanking +
                '}';
    }
}
