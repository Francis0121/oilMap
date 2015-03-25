package com.oilMap.server.user.fuel;

/**
 * Created by Francis on 2015-03-24.
 */
public class UserFuel {

    /**
     * 유저고유번호 
     */
    private Integer userPn;

    /**
     * 배기량 
     */
    private Integer displacement;

    /**
     * 평균 유류 비용
     */
    private Integer cost;

    /**
     * 평균 유류 주기 
     */
    private Integer period;

    /**
     * 업데이트 날짜 
     */
    private String updateDate;

    public UserFuel() {
    }

    public UserFuel(Integer userPn, Integer displacement, Integer cost, Integer period  ) {
        this.userPn = userPn;
        this.displacement = displacement;
        this.cost = cost;
        this.period = period;
    }
    
    public Integer getUserPn() {
        return userPn;
    }

    public void setUserPn(Integer userPn) {
        this.userPn = userPn;
    }

    public Integer getDisplacement() {
        return displacement;
    }

    public void setDisplacement(Integer displacement) {
        this.displacement = displacement;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    @Override
    public String toString() {
        return "UserFuel{" +
                "userPn=" + userPn +
                ", displacement=" + displacement +
                ", cost=" + cost +
                ", period=" + period +
                ", updateDate='" + updateDate + '\'' +
                '}';
    }
}
