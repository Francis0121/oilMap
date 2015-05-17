package com.oilMap.client.user;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsMessage;
import android.util.Log;

import com.oilMap.client.MainActivity;
import com.oilMap.client.R;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class SMSReceiver extends BroadcastReceiver {

    private static final String TAG = "SMSReceiver";
    private Integer price;
    private String id;
    private Context context;
    private String msg;
    private Notification mNoti;

    @Override
    public void onReceive(Context context, Intent intent) {
        //ID check
        SharedPreferences pref = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        this.id = pref.getString("id", "");

        if ("android.provider.Telephony.SMS_RECEIVED".equals(intent.getAction())) {
            Bundle bundle = intent.getExtras();
            Object messages[] = (Object[])bundle.get("pdus");
            SmsMessage smsMsg[] = new SmsMessage[messages.length];

            for(int i = 0; i < messages.length; i++) {
                smsMsg[i] = SmsMessage.createFromPdu((byte[])messages[i]);
            }

            msg = smsMsg[0].getMessageBody().toString();
            if(msg.contains("주유소")){
                int stopLength = 0;
                int idx = msg.indexOf(",");

                for(int i=1; i<5; i++){
                    if(msg.charAt(idx-i) == ' ')
                    {
                        stopLength = idx-i+1;
                        break;
                    }
                }
                this.price = Integer.parseInt(msg.substring(stopLength,idx + 4).replace(",", ""));
                Log.d(TAG, msg);
                Log.d(TAG, "PRICE : " + this.price);

                new BillAsyncTask().execute();

                NotificationManager mNM = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                DecimalFormat df = new DecimalFormat("#,##0");
                String strCash = df.format(this.price);

                PendingIntent mPendingIntent = PendingIntent.getActivity(
                        context.getApplicationContext(),0,
                        new Intent(context.getApplicationContext(), MainActivity.class),
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

                mNoti = new NotificationCompat.Builder(context.getApplicationContext())
                        .setContentTitle("OilMap")
                        .setContentText(strCash + "원이 주유되었습니다.")
                        //.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.actionbar_icon))           //icon크기 만들기
                        .setSmallIcon(R.drawable.notification_icon)
                        .setColor(context.getResources().getColor(R.color.orange_bg_color))
                        .setTicker("주유정보가 입력되었습니다.")
                        .setAutoCancel(true)
                        .setContentIntent(mPendingIntent)
                        .build();

                mNM.notify(7777,mNoti);
            }
        }
    }

    private class BillAsyncTask extends AsyncTask<Void, Void, Map<String, Object>>{

        private Map<String, Object> request = new HashMap<String, Object>();

        @Override
        protected Map<String, Object> doInBackground(Void... params) {
            if(id == null){
                return null;
            }

            request.put("id", id);
            request.put("bill", price);
            request.put("title", msg);

            try {
                String url = "http://14.49.39.74:8080/fuelBill/insert";
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<Map> responseEntity = restTemplate.postForEntity(url, request, Map.class);
                Map<String, Object> messages = responseEntity.getBody();
                return messages;
            } catch (Exception e) {
                Log.e("Error", e.getMessage(), e);
                throw new RuntimeException("Communication error occur");
            }
        }

        @Override
        protected void onPostExecute(Map<String, Object> stringObjectMap) {
            super.onPostExecute(stringObjectMap);
            Log.d(TAG, stringObjectMap.toString());
            if((Boolean) stringObjectMap.get("result")){
                Log.d(TAG, "Bill Async task success");
            }
        }
    }
}