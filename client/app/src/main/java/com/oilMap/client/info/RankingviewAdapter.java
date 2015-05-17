package com.oilMap.client.info;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.oilMap.client.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 김현준 on 2015-05-16.
 */
public class RankingviewAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<RankingItem> data;
    private int layout;

    public RankingviewAdapter(Context context, int layout, List<RankingItem> data){
        this.inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data=data;
        this.layout=layout;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public String getItem(int position) {
        return data.get(position).getId();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = inflater.inflate(layout, parent, false);
        }

        if(position < 3) {
            switch (position) {
                case 0:
                    convertView.setBackgroundResource(R.drawable.layer_list_01);
                    break;
                case 1:
                    convertView.setBackgroundResource(R.drawable.layer_list_02);
                    break;
                case 2:
                    convertView.setBackgroundResource(R.drawable.layer_list_03);
                    break;
            }
        }else{
            convertView.setBackgroundResource(R.drawable.layer_list_04);
        }


        RankingItem listviewitem = data.get(position);
        ImageView rankingIcon=(ImageView)convertView.findViewById(R.id.rank_item_imageview);
        rankingIcon.setImageResource(listviewitem.getRankingIcon());

        TextView id=(TextView)convertView.findViewById(R.id.rank_item_textview);
        id.setText(listviewitem.getId());

        ImageView icon=(ImageView)convertView.findViewById(R.id.item_imageview);
        icon.setImageResource(listviewitem.getIcon());

        TextView efficiency=(TextView)convertView.findViewById(R.id.efficiencytextview);
        efficiency.setText(listviewitem.getEfficiency());

        return convertView;
    }
}
