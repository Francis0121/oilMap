package com.oilMap.client.info;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.oilMap.client.R;
import com.oilMap.client.bluetooth.Bluetooth_reception;

/**
 * Created by yeonsang on 2015-03-28.
 */
public class MainPage extends Fragment implements View.OnClickListener{
    Fragment fragment;
    FragmentTransaction transaction;

    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.mainpage_layout, container, false);

        Button userbutton= (Button) rootView.findViewById(R.id.userbtn);
        userbutton.setOnClickListener(this);
        Button infobutton= (Button) rootView.findViewById(R.id.infobtn);
        infobutton.setOnClickListener(this);
        Button chartbutton= (Button) rootView.findViewById(R.id.chartbtn);
        chartbutton.setOnClickListener(this);

        ///bluetooth 테스트 해볼수 있게 임시로
        //////////////////////////////////////////////////////
        Button BTTestBtn = (Button) rootView.findViewById(R.id.BTTestBtn);
        BTTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent blue_exe = new Intent(getActivity(), Bluetooth_reception.class);
                startActivity(blue_exe);
            }
        });
        /////////////////////////////////////////////////////////////

        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.userbtn:
                fragment=new MyInfo();
                transaction=getActivity().getFragmentManager().beginTransaction();
                transaction.replace(R.id.container,fragment);
                transaction.commit();
                break;
            case R.id.infobtn:
                fragment=new OilInfo();
                transaction=getActivity().getFragmentManager().beginTransaction();
                transaction.replace(R.id.container,fragment);
                transaction.commit();
                break;
            case R.id.chartbtn:
                fragment=new Oilgraph();
                transaction=getActivity().getFragmentManager().beginTransaction();
                transaction.replace(R.id.container,fragment);
                transaction.commit();
                break;
        }
    }
}