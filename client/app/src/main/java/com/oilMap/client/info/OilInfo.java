package com.oilMap.client.info;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oilMap.client.R;

/**
 * Created by yeonsang on 2015-03-28.
 */
public class OilInfo extends Fragment implements View.OnClickListener{
    TextView totaldistance;
    TextView totalmoney;
    TextView rapidstart;
    TextView rapidupspeed;
    TextView rapidstop;

    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.oilinfo_layout, container, false);
        totaldistance=(TextView) rootView.findViewById(R.id.distance);
        totalmoney=(TextView) rootView.findViewById(R.id.money);
        rapidstart=(TextView) rootView.findViewById(R.id.start);
        rapidupspeed=(TextView) rootView.findViewById(R.id.upspeed);
        rapidstop=(TextView) rootView.findViewById(R.id.stop);
        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.oilbtn:
                totaldistance.setVisibility(View.VISIBLE);
                totalmoney.setVisibility(View.VISIBLE);
                break;
            case R.id.rapidbtn:
                rapidstart.setVisibility(View.VISIBLE);
                rapidupspeed.setVisibility(View.VISIBLE);
                rapidstop.setVisibility(View.VISIBLE);
                break;
        }
    }
}