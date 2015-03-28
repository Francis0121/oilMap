package com.oilMap.client.info;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oilMap.client.R;

/**
 * Created by yeonsang on 2015-03-28.
 */
public class Oilgraph extends Fragment {
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.oilgraph_layout, container, false);
        return rootView;
    }
}
