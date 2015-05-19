package com.oilMap.server.batch;

import java.util.List;
import java.util.Map;

/**
 * Created by Francis on 2015-05-17.
 */
public interface BatchService {
    
    void updateRankingDatabase();
    
    List<Ranking> selectRanking(RankingFilter rankingFilter);

    Ranking selectRankingMy(String id);
}
