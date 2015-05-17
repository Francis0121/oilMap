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
import com.oilMap.client.auth.Auth;
import com.oilMap.client.gps.MapsActivity;
import com.oilMap.client.ranking.Ranking;
import com.oilMap.client.ranking.RankingFilter;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 김현준 on 2015-05-16.
 */
public class RankingActivity extends Activity {

    private static final String TAG = "RankingActivity";
    private RankingFilter rankingFilter = new RankingFilter();

    RankingItem[] rankingArray = new RankingItem[30];

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); //Remove title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //Remove notification bar
        setContentView(R.layout.ranking_list);

        ListView listView = (ListView) findViewById(R.id.rankingListView);
        ArrayList<RankingItem> data = new ArrayList<>();

        for(int i = 0; i < 30; i++){
            String num = String.valueOf(i+1);

            if(i < 3) {
                switch (i) {
                    case 0:
                        rankingArray[i] = new RankingItem(R.drawable.effeicency, num + ". a_a", R.drawable.ranking01, "Efficiency " + " 3.14" + "km/L");
                        break;
                    case 1:
                        rankingArray[i] = new RankingItem(R.drawable.effeicency, num + ". a_a", R.drawable.ranking02, "Efficiency " + " 3.14" + "km/L");
                        break;
                    case 2:
                        rankingArray[i] = new RankingItem(R.drawable.effeicency, num + ". a_a", R.drawable.ranking03, "Efficiency " + " 3.14" + "km/L");
                        break;
                }
            }else{
                rankingArray[i] = new RankingItem(R.drawable.effeicency, num + ". a_a", R.drawable.ranking04, "Efficiency " + " 3.14" + "km/L");
            }
            data.add(rankingArray[i]);
        }

        RankingviewAdapter adapter = new RankingviewAdapter(this, R.layout.ranking_item, data);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String passId = rankingArray[position].getKey();
                Intent map = new Intent(RankingActivity.this, MapsActivity.class);
                map.putExtra("id", passId);
                startActivity(map);
            }
        });

        new RankingAsnycTask().execute();
    }


    private class RankingAsnycTask extends AsyncTask<Void, Void, List<Ranking>> {

        @Override
        protected List<Ranking> doInBackground(Void... params) {

            try {
                String url = getString(R.string.contextPath) + "/select/ranking";
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<List> responseEntity = restTemplate.postForEntity(url, rankingFilter, List.class);
                return responseEntity.getBody();
            } catch (Exception e) {
                Log.e("Error", e.getMessage(), e);
                throw new RuntimeException("Communication error occur");
            }
        }

        @Override
        protected void onPostExecute(List<Ranking> rankings) {
            Log.d(TAG, rankings.toString());
        }
    }

}
