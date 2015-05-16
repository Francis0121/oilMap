    package com.oilMap.client.info;

    import android.app.Activity;
    import android.content.Context;
    import android.os.Bundle;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.BaseAdapter;
    import android.widget.ImageView;
    import android.widget.ListView;
    import android.widget.TextView;

    import com.oilMap.client.R;

    import java.util.ArrayList;

    public class listviewpage extends Activity {
        ArrayList<Content> manylist;
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.listview);

            manylist=new ArrayList<Content>();
            Content con;
            con=new Content(R.drawable.apple, "김연상"); manylist.add(con);
            con=new Content(R.drawable.apple, "롤로"); manylist.add(con);
            con=new Content(R.drawable.apple, "루리"); manylist.add(con);

            ConlistAdapter ConAdapter=new ConlistAdapter(this,R.layout.context,manylist);

            ListView conlist;
            conlist=(ListView)findViewById(R.id.list);
            conlist.setAdapter(ConAdapter);
        }
    }

    class Content {
        Content(int aicon, String atext) {
            icon=aicon;
            text=atext;
        }
        int icon;
        String text;
    }

    class ConlistAdapter extends BaseAdapter {
        Context lefticon;
        LayoutInflater Inflater;
        ArrayList<Content> asrc;
        int layout;

        public ConlistAdapter(Context context, int alayout, ArrayList<Content> bsrc) {
            lefticon=context;
            Inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            asrc=bsrc;
            layout=alayout;
        }

        public int getCount() {
            return asrc.size();
        }

        public String getItem(int position) {
            return asrc.get(position).text;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            final int pos=position;
            if(convertView==null) {
                convertView=Inflater.inflate(layout, parent, false);
            }
            ImageView img=(ImageView)convertView.findViewById(R.id.img);
            img.setImageResource(asrc.get(position).icon);

            TextView txt=(TextView)convertView.findViewById(R.id.text);
            txt.setText(asrc.get(position).text);

            return convertView;
        }
    }