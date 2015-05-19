package com.oilMap.client.info;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.oilMap.client.R;
import com.oilMap.client.gps.MapsActivity;
import com.oilMap.client.ranking.Efficiency;
import com.oilMap.client.ranking.Ranking;
import com.oilMap.client.ranking.RankingFilter;
import com.oilMap.client.ranking.RankingResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 김현준 on 2015-05-16.
 */
public class RankingActivity extends Activity {

    private static final String TAG = "RankingActivity";
    private RankingFilter rankingFilter = new RankingFilter();
    private ListView listView;
    private List<RankingItem> rankingItemData = new ArrayList<>();

    private int myPosition;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); //Remove title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //Remove notification bar
        setContentView(R.layout.ranking_list);

        this.listView = (ListView) findViewById(R.id.rankingListView);
        new RankingAsnycTask().execute();
    }

    private class RankingAsnycTask extends AsyncTask<Void, Void, RankingResponse> {

        @Override
        protected RankingResponse doInBackground(Void... params) {

            String url = getString(R.string.contextPath) + "/select/ranking";
            Boolean isSuccess = false;
            RankingResponse rankingResponse = null;
            try {
                while (!isSuccess) {
                    try {
                        rankingResponse = postTemplate(url);
                        if(rankingResponse != null) {
                            isSuccess = true;
                        }
                    } catch (ResourceAccessException e) {
                        Log.e("Error", e.getMessage(), e);
                        isSuccess = false;
                    }
                }
            }catch (Exception e){
                Log.e("Error", e.getMessage(), e);
                rankingResponse = new RankingResponse();
            }

            return  rankingResponse;
        }

        private RankingResponse postTemplate(String url){
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<RankingResponse> responseEntity = restTemplate.postForEntity(url, rankingFilter, RankingResponse.class);
            return responseEntity.getBody();
        }

        @Override
        protected void onPostExecute(RankingResponse rankingResponse) {
            Log.d(TAG, rankingResponse.toString());

            List<Ranking> rankings = rankingResponse.getRankingList();

            Integer count = 1;
            for(Ranking ranking : rankings){
                RankingItem rankingItem = null;
                Efficiency e = ranking.getEfficiency();
                Double efficiency = e.getEfficiency();
                DecimalFormat df = new DecimalFormat("#,##0.0");
                String strEfficiency= df.format(efficiency);

                if(count < 3) {
                    switch (count) {
                        case 1:
                            rankingItem= new RankingItem(R.drawable.effeicency, e.getRanking() + "."+ranking.getAuth().getName(), R.drawable.ranking01, "Efficiency " + strEfficiency  + "km/L", ranking.getAuth().getId());
                            break;
                        case 2:
                            rankingItem= new RankingItem(R.drawable.effeicency, e.getRanking() + "."+ranking.getAuth().getName(), R.drawable.ranking02, "Efficiency " + strEfficiency+ "km/L", ranking.getAuth().getId());
                            break;
                        case 3:
                            rankingItem= new RankingItem(R.drawable.effeicency, e.getRanking() + "."+ranking.getAuth().getName(), R.drawable.ranking03, "Efficiency " + strEfficiency+ "km/L", ranking.getAuth().getId());
                            break;
                    }
                }else{
                    rankingItem = new RankingItem(R.drawable.effeicency, e.getRanking() + "."+ranking.getAuth().getName(), R.drawable.ranking04, "Efficiency " + strEfficiency + "km/L", ranking.getAuth().getId());
                }

                if(rankingItem != null) {
                    rankingItemData.add(rankingItem);
                }
                count++;
            }

            RankingviewAdapter adapter = new RankingviewAdapter(RankingActivity.this, R.layout.ranking_item, rankingItemData);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    myPosition = position;
                    String authId = rankingItemData.get(position).getKey();
                    Intent map = new Intent(RankingActivity.this, MapsActivity.class);
                    map.putExtra("id", authId);
                    startActivity(map);
                }
            });

//            TextView myRankText = (TextView)findViewById(R.id.myRanking);
//
//            myRankText.setOnClickListener(new View.OnClickListener() {
//
//                public void onClick(View v) {
//                    if (v.isClickable()) {
//                        // Toast.makeText(PhoneUsefull.this, R.string.phoneusefull_kr1, 1).show();
//                        String authId = rankingItemData.get(myPosition).getKey();
//                        Intent map = new Intent(RankingActivity.this, MapsActivity.class);
//                        map.putExtra("id", authId);
//                        startActivity(map);
//                    }
//                }
//            });
        }
    }
}
