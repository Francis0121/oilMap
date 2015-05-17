package com.oilMap.server.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by Francis on 2015-05-17.
 */
@Component
public class BatchJob {
    
    private static Logger logger = LoggerFactory.getLogger(BatchJob.class);
    
    @Autowired
    private BatchService batchService;

    @Scheduled(cron = "0 10 * * * *")
    public void efficiencyRankingBatch(){
        long start = System.currentTimeMillis();
        logger.info("Start time Efficiency Ranking Batch ");
        try {
            batchService.updateRankingDatabase();
        }catch (Exception e){
            
        }
        long finish = System.currentTimeMillis();
        logger.info("End time Efficiency Ranking Batch "+ (finish - start) + "ms\n");
    }
}
