package com.oilMap.client;

/**
 * Created by Francis on 2015-03-10.
 */
public class Test {
    
    private Integer intValue;

    private String strValue;

    public Test() {
    }

    public Test(Integer intValue, String strValue) {
        this.intValue = intValue;
        this.strValue = strValue;
    }

    public Integer getIntValue() {
        return intValue;
    }

    public void setIntValue(Integer intValue) {
        this.intValue = intValue;
    }

    public String getStrValue() {
        return strValue;
    }

    public void setStrValue(String strValue) {
        this.strValue = strValue;
    }

    @Override
    public String toString() {
        return "Test{" +
                "intValue=" + intValue +
                ", strValue='" + strValue + '\'' +
                '}';
    }
}
