package com.oilMap.server.drive;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Service;

/**
 * Created by Francis on 2015-05-06.
 */
@Service
public class DrivingServiceImpl extends SqlSessionDaoSupport implements  DrivingService {

    @Override
    public void insert(Driving driving) {
        getSqlSession().insert("drive.insertDriving", driving);
    }
    
}
