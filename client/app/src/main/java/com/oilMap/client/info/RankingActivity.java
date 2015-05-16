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

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ranking_list);

        ListView listView = (ListView) findViewById(R.id.rankingListView);

        ArrayList<RankingItem> data = new ArrayList<>();

        RankingItem a = new RankingItem(R.drawable.ic_icon,"a", R.drawable.ic_icon, "Efficiency " + " 3.14" + "km/L");
        RankingItem b = new RankingItem(R.drawable.ic_icon, "b", R.drawable.ic_icon, "Efficiency " + " 3.14" + "km/L");
        RankingItem c = new RankingItem(R.drawable.ic_icon, "c", R.drawable.ic_icon, "Efficiency " + " 3.14" + "km/L");

        data.add(a);
        data.add(b);
        data.add(c);
        //////////////////////////////////////////////////////////////////////////////

        RankingItem[] rankingArray = new RankingItem[20];

        for(int i = 0; i < 20; i++){
            rankingArray[i] = new RankingItem(R.drawable.ic_icon, "a_a", R.drawable.ic_icon,"Efficiency " + " 3.14" + "km/L");
            data.add(rankingArray[i]);
        }

        RankingviewAdapter adapter = new RankingviewAdapter(this, R.layout.ranking_item, data);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent map = new Intent(RankingActivity.this, MapsActivity.class);
                startActivity(map);
            }
        });
    }
}
