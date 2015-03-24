package com.oilMap.client;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    
    Button getBtn;
    Button postBtn;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        getBtn = (Button) findViewById(R.id.getBtn);
        postBtn = (Button) findViewById(R.id.postBtn);
        
        getBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.i("btn", "Get Button Click");
                new JsonAsyncTask().execute("get");
            }
        });
        
        postBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.i("btn", "Post Button Click");
                new JsonAsyncTask().execute("post");
            }
        });
    }
    
    private class JsonAsyncTask extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String type = params[0];
            if(type == null || type.equals("")){
                return "fail";
            }

            try {
                String url = getString(R.string.contextPath) + "/" + type;

                // Set the Accept header for "application/json"
                HttpHeaders requestHeaders = new HttpHeaders();

                if(type.equals("get")){
                    List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
                    acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
                    requestHeaders.setAccept(acceptableMediaTypes);

                    HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);

                    RestTemplate restTemplate = new RestTemplate();
                    restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                    ResponseEntity<Test> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Test.class);
                    Test test = responseEntity.getBody();

                    return test.toString();
                }else if(type.equals("post")){
                    RestTemplate restTemplate = new RestTemplate();
                    ResponseEntity<Test> responseEntity = restTemplate.postForEntity(url, new Test(200, "Post"), Test.class);
                    Test test = responseEntity.getBody();
                    return test.toString();
                }else{
                    return "Not exist type";
                }

            } catch (Exception e) {
                Log.e("Error", e.getMessage(), e);
                return "fail";
            }
            
        }

        @Override
        protected void onPostExecute(String str) {
            Log.i("btn", "Execute " + str);
        }
        
    }
    
    
}
