package com.oilMap.server.bsfc;

/**
 * Created by SungGeun on 2015-10-12.
 */
public class Bsfc {

    private Integer rpm;

    private Integer engineLoad;

    private Integer rpmChange;

    private Integer elChange;

    private Integer level;

    public Bsfc() {
        rpm = 0;
        engineLoad = 0;
        rpmChange = 0;
        elChange = 0;
        level = 0;
    }

    public Integer getRpm() {
        return rpm;
    }

    public void setRpm(Integer rpm) {
        this.rpm = rpm;
    }

    public Integer getEngineLoad() {
        return engineLoad;
    }

    public void setEngineLoad(Integer engineLoad) {
        this.engineLoad = engineLoad;
    }


    public Integer getElChange() {
        return elChange;
    }

    public void setElChange(Integer elChange) {
        this.elChange = elChange;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getRpmChange() {
        return rpmChange;
    }

    public void setRpmChange(Integer rpmChange) {
        this.rpmChange = rpmChange;
    }

    @Override
    public String toString() {
        return "Bsfc{" +
                "rpm=" + rpm +
                ", engineLoad=" + engineLoad +
                ", rpmChange=" + rpmChange +
                ", elChange=" + elChange +
                ", level=" + level +
                '}';
    }
}
