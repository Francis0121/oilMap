package com.oilMap.client.info;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cocosw.bottomsheet.BottomSheet;
import com.github.lzyzsd.circleprogress.CircleProgress;
import com.oilMap.client.MainActivity;
import com.oilMap.client.R;
import com.oilMap.client.bluetooth.Bluetooth_reception;
import com.oilMap.client.util.BackPressCloseHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OilInfoActivity extends Activity {

    private BottomSheet bottomSheet;
    private BackPressCloseHandler backPressCloseHandler;
    private CircleProgress circleProgress;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); //Remove title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //Remove notification bar
        setContentView(R.layout.activity_oil_info);

        // ~ BottomSheet
        bottomSheet = new BottomSheet.Builder(this, R.style.BottomSheet_StyleDialog).title("Option").sheet(R.menu.list).listener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case R.id.bluetooth:
                        Intent blue_exe = new Intent(OilInfoActivity.this, Bluetooth_reception.class);
                        blue_exe.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(blue_exe);
                        OilInfoActivity.this.finish();
                        break;

                    case R.id.logout:
                        SharedPreferences pref = OilInfoActivity.this.getSharedPreferences("userInfo", 0);
                        SharedPreferences.Editor prefEdit = pref.edit();
                        prefEdit.putString("id","");
                        prefEdit.commit();

                        Intent idCheck = new Intent(OilInfoActivity.this, MainActivity.class);
                        startActivity(idCheck);
                        OilInfoActivity.this.finish();
                        break;
                }
            }
        }).build();

        this.backPressCloseHandler = new BackPressCloseHandler(this);

        circleProgress = (CircleProgress) findViewById(R.id.circle_progress);
        circleProgress.setProgress(10);
        circleProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circleProgress.setProgress(80);
            }
        });


        listView = (ListView) findViewById(R.id.listView);
        String[] values = new String[] { "Date : 2015-05-01  Fuel : 80%", "2015-05-02 78%", "2015-05-03 76%",
                "2015-05-04 60%", "2015-05-05 55%", "2015-05-06 51%", "2015-05-07 80%", "2015-05-08 80%",
                "2015-05-09 80%" };

        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < values.length; ++i) {
            list.add(values[i]);
        }
        final StableArrayAdapter adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view,int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                view.animate().setDuration(2000).alpha(0)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                list.remove(item);
                                adapter.notifyDataSetChanged();
                                view.setAlpha(1);
                            }
                        });
            }

        });
    }

    @Override
    public void onBackPressed() {
        this.backPressCloseHandler.onBackPressed(this.bottomSheet);
    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId, List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }
        @Override
        public View getView(int position, View convertView,
                            ViewGroup parent) {
            View view =super.getView(position, convertView, parent);

            TextView textView=(TextView) view.findViewById(android.R.id.text1);
            /*YOUR CHOICE OF COLOR*/
            textView.setTextColor(Color.WHITE);

            return view;
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }
}
