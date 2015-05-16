package com.oilMap.client.info;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.oilMap.client.R;
import com.oilMap.client.gps.MapsActivity;

import java.util.ArrayList;

/**
 * Created by 김현준 on 2015-05-16.
 */
public class RankingActivity extends Activity {

    RankingItem[] rankingArray = new RankingItem[30];

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ranking_list);

        ListView listView = (ListView) findViewById(R.id.rankingListView);

        ArrayList<RankingItem> data = new ArrayList<>();



        rankingArray[0] = new RankingItem(R.drawable.ic_icon,"a", R.drawable.ic_icon, "Efficiency " + " 3.14" + "km/L");
        rankingArray[1] = new RankingItem(R.drawable.ic_icon, "b", R.drawable.ic_icon, "Efficiency " + " 3.14" + "km/L");
        rankingArray[2] = new RankingItem(R.drawable.ic_icon, "c", R.drawable.ic_icon, "Efficiency " + " 3.14" + "km/L");

        data.add(rankingArray[0]);
        data.add(rankingArray[1]);
        data.add(rankingArray[2]);
        //////////////////////////////////////////////////////////////////////////////



        for(int i = 3; i < 30; i++){
            rankingArray[i] = new RankingItem(R.drawable.ic_icon, "a_a", R.drawable.ic_icon,"Efficiency " + " 3.14" + "km/L");
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
    }
}
