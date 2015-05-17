package com.oilMap.server.drive;

import com.oilMap.server.util.AbstractListFilter;
import com.oilMap.server.util.DateUtil;

/**
 * Created by Francis on 2015-05-17.
 */
public class DrivePointFilter extends AbstractListFilter{
    
    private String id;

    private String startDate;
    
    private String endDate;
    
    public DrivePointFilter(String id) {
        this.id = id;
        this.startDate = DateUtil.getToday("YYYY-MM-DD");
        this.endDate = DateUtil.get30DayAgoToday();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "DrivePointFilter{" +
                "id='" + id + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                '}';
    }
}
