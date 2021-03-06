package com.oilMap.server.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by Francis on 2015-05-03.
 */
@Service
public class Http {
    
    private static Logger logger = LoggerFactory.getLogger(Http.class);
    
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.45 Safari/537.36";
    private static final String QUERY_URL = "http://p.search.naver.com/soil/search.naver?_callback=window.__jindo2_callback._oilsearch1234_0&where=nx&rcode=09290&company=0&kind=1&self=0&search_result=1&api_type=2&start=1&q_sid=";
    
    public Double sendGet() throws Exception {
        URL obj = new URL(QUERY_URL);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        
        con.setRequestMethod("GET");// optional default is GET
        con.setRequestProperty("User-Agent", USER_AGENT);//add request header
        con.setRequestProperty("Accept-Charset", "UTF-8");
        
        int responseCode = con.getResponseCode();
        logger.debug("Sending 'GET' request to URL : " + QUERY_URL);
        logger.debug("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        
        String info = response.toString();
        info = info.replace("window.__jindo2_callback._oilsearch1234_0(", "");
        info = info.replace(")", "");
        
        logger.debug(info);
        JsonParser parser = new JsonParser();
        JsonObject object = (JsonObject) parser.parse(info);
        
        Integer total = Integer.parseInt(object.get("total").toString());
        Integer itemCount = Integer.parseInt(object.get("itemCount").toString());
        
        logger.debug("total " + total + " itemCount " + itemCount);

        Double totalGasoline = 0.0;
        for(int i=0; i<total/itemCount+1; i++){
            logger.debug((i * itemCount) + 1+"count");
            totalGasoline += sendGet((i * itemCount) + 1);
        }
        Double ageGasoline = totalGasoline/total;
        return ageGasoline;
    }

    public Double sendGet(Integer page) throws Exception {
        URL obj = new URL("http://p.search.naver.com/soil/search.naver?_callback=window.__jindo2_callback._oilsearch1234_0&where=nx&rcode=09290&company=0&kind=1&self=0&search_result=1&api_type=2&start="+page+"&q_sid=");
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");// optional default is GET
        con.setRequestProperty("User-Agent", USER_AGENT);//add request header
        con.setRequestProperty("Accept-Charset", "UTF-8");

        int responseCode = con.getResponseCode();
        logger.debug("Sending 'GET' request to URL : " + QUERY_URL);
        logger.debug("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        String info = response.toString();
        info = info.replace("window.__jindo2_callback._oilsearch1234_0(", "");
        info = info.replace(")", "");

        logger.debug(info);
        JsonParser parser = new JsonParser();
        JsonObject object = (JsonObject) parser.parse(info);
        JsonObject result = (JsonObject) object.get("result");
        JsonObject items = (JsonObject) result.get("items");
        JsonArray item = (JsonArray) items.getAsJsonArray("item");
        Double totalGasoline = 0.0;
        for(int i=0; i<item.size(); i++){
            JsonObject jsonObject = (JsonObject) item.get(i);
            logger.debug(item.get(i).toString());
            Double gasolinePrice = Double.parseDouble(jsonObject.get("gasolinePrice").toString().replace(",", "").replace("\"", ""));
            totalGasoline+=gasolinePrice;
        }
        return totalGasoline;
    }
}
