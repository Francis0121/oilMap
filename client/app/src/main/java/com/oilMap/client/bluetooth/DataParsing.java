package com.oilMap.client.bluetooth;

import android.app.Activity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 정성진 on 2015-04-28.
 */
public class DataParsing extends Activity {

    Obd obd = new Obd();

    public void dataP(String data) throws JSONException {

        //////////////////////////////////////
        //문자열로부터 JSONObject 객체 생성
        JSONObject jsonOb = null;

        try {
            jsonOb = new JSONObject(data);
        }
        catch (JSONException e) {
            e.printStackTrace();
            return;
        }
        ///////////////////////////////////

        ////////////////////////////////////////
        //JSONObject 객체 파싱
        obd.setFuelEfficiency(jsonOb.getDouble("fuel_efficiency"));
        obd.setFuelLevel(jsonOb.getDouble("fuel_level"));
        obd.setRpm(jsonOb.getDouble("rpm"));
        obd.setFuel(jsonOb.getDouble("fuel"));
        obd.setDistance(jsonOb.getDouble("distance"));
        obd.setTime(jsonOb.getLong("time"));
        /////////////////////////////////////////

    }

}
