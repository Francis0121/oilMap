package com.oilMap.server.batch;

import com.oilMap.server.drive.Driving;
import com.oilMap.server.util.Pagination;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * Created by Francis on 2015-05-17.
 */
@Service
public class BatchServiceImpl extends SqlSessionDaoSupport implements BatchService {

    private static Logger logger = LoggerFactory.getLogger(BatchServiceImpl.class);
    
    @Override
    public void updateRankingDatabase() {
        
        // ~ Batch 시작
        Batch batch = new Batch();
        startBatch(batch);
        
        // ~ ID 조회
        List<Efficiency> efficiencies = new ArrayList<Efficiency>();
        List<String> auths = selectAuths();
        logger.debug(auths.toString());
        
        for(String id : auths){
            // ~ TODO 운행 정보 조회시 현재일자 기준으로 한달 간 (DATE 정할 수 있게 변경)
            List<Driving> drivings = selectDriving(id);
            logger.debug(drivings.toString());

            Double efficiencyVal = 0.0;

            if(drivings.size() > 1) {
                
                Double totalEfficiency = 0.0;
                int calCount = 0;
                for (int i = 0; i < drivings.size() - 1; i++) {
                    Driving drivingBefore = drivings.get(i);
                    Driving drivingEnd = drivings.get(i+1);
                    
                    Double fuelQuantity = drivingBefore.getFuelQuantity() - drivingEnd.getFuelQuantity();
                    Double distance = drivingEnd.getDistance() - drivingBefore.getDistance();

                    if(fuelQuantity.intValue() > 0) {
                        totalEfficiency += distance / fuelQuantity;
                        calCount++;
                    }
                }
                efficiencyVal = totalEfficiency / calCount;
            }
            
            Efficiency efficiency = new Efficiency(batch.getPn(), id, efficiencyVal);
            efficiencies.add(efficiency);
        }

        
        Collections.sort(efficiencies, new Comparator<Efficiency>() {
            @Override
            public int compare(Efficiency e1, Efficiency e2) {
                return Double.compare(e2.getEfficiency(), e1.getEfficiency());
            }
        });
        
        int rank = 0;
        Double beforeEfficiency = 0.0;
        for(Efficiency e : efficiencies){

            if(!beforeEfficiency.equals(e.getEfficiency())){
                e.setRanking(++rank);
                beforeEfficiency = e.getEfficiency();
            }else{
                e.setRanking(rank);
            }
            
            logger.debug(e.toString());
            insertEfficiency(e);
        }
        
    }

    @Override
    public List<Ranking> selectRanking(RankingFilter rankingFilter) {
        Pagination pagination = rankingFilter.getPagination();
        int count = selectRankingCount(rankingFilter);
        pagination.setNumItems(count);
        pagination.setNumItemsPerPage(20);
        if(count == 0){
            return new ArrayList<Ranking>();
        }
        return getSqlSession().selectList("batch.selectRanking", rankingFilter);
    }

    private int selectRankingCount(RankingFilter rankingFilter) {
        return getSqlSession().selectOne("batch.selectRankingCount", rankingFilter);
    }

    private void startBatch(Batch batch){
        getSqlSession().insert("batch.startBatch", batch);
    }

    private void insertEfficiency(Efficiency efficiency){
        getSqlSession().insert("batch.insertEfficiency", efficiency);
    }
    
    private List<String> selectAuths(){
        return getSqlSession().selectList("batch.selectAuths");
    }
    
    private List<Driving> selectDriving(String id){
        return getSqlSession().selectList("batch.selectDriving", id);
    }
    

}
