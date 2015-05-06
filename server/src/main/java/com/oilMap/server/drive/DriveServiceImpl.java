package com.oilMap.server.drive;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Service;

/**
 * Created by Francis on 2015-05-06.
 */
@Service
public class DriveServiceImpl extends SqlSessionDaoSupport implements DriveService {

    // TODO - TEST CASE를 생성 필요
    
    @Override
    public void insertDriving(Driving driving) {
        getSqlSession().insert("drive.insertDriving", driving);
    }

    @Override
    public void insertDrivePoint(DrivePoint drivePoint) {
        getSqlSession().insert("drive.insertDrivePoint", drivePoint);
    }
}
