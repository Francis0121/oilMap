package com.oilMap.server.bsfc;

/**
 * Created by SungGeun on 2015-10-12.
 */
public class Bsfc {

    private Integer rpm;

    private Integer engineLoad;

    private Integer rmpChange;

    private Integer elChange;

    private Integer level;

    public Bsfc() {
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

    public Integer getRmpChange() {
        return rmpChange;
    }

    public void setRmpChange(Integer rmpChange) {
        this.rmpChange = rmpChange;
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

    @Override
    public String toString() {
        return "Bsfc{" +
                "rpm=" + rpm +
                ", engineLoad=" + engineLoad +
                ", rmpChange=" + rmpChange +
                ", elChange=" + elChange +
                ", level=" + level +
                '}';
    }
}
