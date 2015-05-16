package com.oilMap.client.info;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.oilMap.client.R;

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

        RankingItem a = new RankingItem(R.drawable.ic_icon,"a", R.drawable.ic_icon, "3.14");
        RankingItem b = new RankingItem(R.drawable.ic_icon, "b", R.drawable.ic_icon, "3.14");
        RankingItem c = new RankingItem(R.drawable.ic_icon, "c", R.drawable.ic_icon, "3.14");

        data.add(a);
        data.add(b);
        data.add(c);


        //////////////////////////////////////////////////////////////////////////////

        RankingItem a_a = new RankingItem(R.drawable.ic_icon, "a_a", R.drawable.ic_icon, "3.14");
        RankingItem b_b = new RankingItem(R.drawable.ic_icon, "b_b", R.drawable.ic_icon, "3.14");
        RankingItem c_c = new RankingItem(R.drawable.ic_icon, "c_c", R.drawable.ic_icon, "3.14");
        RankingItem d_d = new RankingItem(R.drawable.ic_icon, "d_d", R.drawable.ic_icon, "3.14");

        data.add(a_a);
        data.add(b_b);
        data.add(c_c);
        data.add(d_d);

        RankingviewAdapter adapter = new RankingviewAdapter(this, R.layout.ranking_item, data);
        listView.setAdapter(adapter);
    }

}
