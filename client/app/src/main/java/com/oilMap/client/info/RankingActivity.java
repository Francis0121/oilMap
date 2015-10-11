package com.oilMap.client.info;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.oilMap.client.R;
import com.oilMap.client.common.UserInfoPrefs_;
import com.oilMap.client.gps.MapsActivity;
import com.oilMap.client.ranking.Efficiency;
import com.oilMap.client.ranking.Ranking;
import com.oilMap.client.ranking.RankingFilter;
import com.oilMap.client.ranking.RankingResponse;
import com.oilMap.client.rest.AARestProtocol;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

// ~ Use Activity
@EActivity(R.layout.ranking_list)
public class RankingActivity extends Activity {

    private static final String TAG = RankingActivity_.class.getSimpleName();

    private List<RankingItem> rankingItemData = new ArrayList<>();

    @Pref
    UserInfoPrefs_ userInfoPrefs;

    @ViewById(R.id.rankingListView)
    ListView listView;

    @RestService
    AARestProtocol aaRestProtocol;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        rankingSelect();
    }

    @Background
    void rankingSelect(){
        RankingFilter rankingFilter = new RankingFilter();
        rankingFilter.setId(userInfoPrefs.id().get());

        RankingResponse rankingResponse = aaRestProtocol.rankingSelectUrl(rankingFilter);
        viewRankingSelect(rankingResponse);
    }

    @UiThread
    void viewRankingSelect(RankingResponse rankingResponse){
        Log.d(TAG, rankingResponse.toString());

        List<Ranking> rankings = rankingResponse.getRankingList();
        Double avsGasoline = rankingResponse.getAvgGasoline();

        for(Ranking ranking : rankings){
            RankingItem rankingItem = null;
            Efficiency e = ranking.getEfficiency();
            Double efficiency = e.getEfficiency();
            DecimalFormat df = new DecimalFormat("#,##0.0");
            String strEfficiency= df.format(efficiency);

            Double cash = avsGasoline * efficiency;
            DecimalFormat dfCash = new DecimalFormat("#,##0");
            String strCash = dfCash.format(cash);

            if(e.getRanking() <= 3) {
                switch (e.getRanking()) {
                    case 1:
                        rankingItem= new RankingItem(R.drawable.effeicency, e.getRanking() + ". "+ranking.getAuth().getName(), R.drawable.ranking01, strEfficiency  + "km/L  -  "+ strCash + "￦", ranking.getAuth().getId());
                        break;
                    case 2:
                        rankingItem= new RankingItem(R.drawable.effeicency, e.getRanking() + ". "+ranking.getAuth().getName(), R.drawable.ranking02, strEfficiency+ "km/L  -  "+ strCash + "￦", ranking.getAuth().getId());
                        break;
                    case 3:
                        rankingItem= new RankingItem(R.drawable.effeicency, e.getRanking() + ". "+ranking.getAuth().getName(), R.drawable.ranking03, strEfficiency+ "km/L  -  "+ strCash + "￦", ranking.getAuth().getId());
                        break;
                }
            }else{
                rankingItem = new RankingItem(R.drawable.effeicency, e.getRanking() + ". "+ranking.getAuth().getName(), R.drawable.ranking04, strEfficiency + "km/L  -  "+ strCash + "￦", ranking.getAuth().getId());
            }

            if(rankingItem != null) {
                rankingItemData.add(rankingItem);
            }
        }

        RankingviewAdapter adapter = new RankingviewAdapter(RankingActivity.this, R.layout.ranking_item, rankingItemData);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String authId = rankingItemData.get(position).getKey();
                Intent map = new Intent(RankingActivity.this, MapsActivity.class);
                map.putExtra("id", authId);
                startActivity(map);
            }
        });


        Ranking ranking = rankingResponse.getMyRanking();

        TextView myName = (TextView) findViewById(R.id.myRankingLeft);
        TextView content = (TextView) findViewById(R.id.myRankingRight);

        Efficiency e = ranking.getEfficiency();
        Double efficiency = e.getEfficiency();
        DecimalFormat df = new DecimalFormat("#,##0.0");
        String strEfficiency= df.format(efficiency);

        Double cash = avsGasoline * efficiency;
        DecimalFormat dfCash = new DecimalFormat("#,##0");
        String strCash = dfCash.format(cash);

        myName.setText(e.getRanking() + ". "+ranking.getAuth().getName());
        content.setText(strEfficiency + "km/L  -  "+ strCash + "￦");

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.relativeLayout01);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = userInfoPrefs.id().get();
                Intent map = new Intent(RankingActivity.this, MapsActivity.class);
                map.putExtra("id", id);
                startActivity(map);
            }
        });
    }
}
