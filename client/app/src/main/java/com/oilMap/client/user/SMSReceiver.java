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
import com.oilMap.client.MainActivity_;
import com.oilMap.client.R;
import com.oilMap.client.common.Constants;
import com.oilMap.client.common.UserInfoPrefs;
import com.oilMap.client.common.UserInfoPrefs_;
import com.oilMap.client.rest.AARestProtocol;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EReceiver;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

@EReceiver
public class SMSReceiver extends BroadcastReceiver {

    private static final String TAG = SMSReceiver_.class.getSimpleName();

    private static final Integer NOTIFICATION_SMS = 1;

    @RestService
    AARestProtocol aaRestProtocol;

    @Pref
    UserInfoPrefs_ userInfoPrefs;

    @SystemService
    NotificationManager notificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {

        if ("android.provider.Telephony.SMS_RECEIVED".equals(intent.getAction())) {
            Bundle bundle = intent.getExtras();
            Object messages[] = (Object[])bundle.get("pdus");
            SmsMessage smsMsg[] = new SmsMessage[messages.length];

            for(int i = 0; i < messages.length; i++) {
                smsMsg[i] = SmsMessage.createFromPdu((byte[])messages[i]);
            }

            String msg = smsMsg[0].getMessageBody().toString();
            if(msg.contains("주유소")){
                int stopLength = 0;
                int idx = msg.indexOf(",");

                if(idx > 0) {
                    for (int i = 1; i < 5; i++) {
                        if (msg.charAt(idx - i) == ' ') {
                            stopLength = idx - i + 1;
                            break;
                        }
                    }
                }
                Integer price = Integer.parseInt(msg.substring(stopLength,idx + 4).replace(",", ""));
                Log.d(TAG, "PRICE : " + price + " Message : " + msg);
                fuelBillInsert(context, price, msg);
            }
        }
    }

    @Background
    void fuelBillInsert(Context context, Integer price, String msg){

        Map<String, Object> request = new HashMap<>();
        request.put("id", userInfoPrefs.id().get());
        request.put("bill", price);
        request.put("title", msg);

        aaRestProtocol.fuelBillInsert(request);

        DecimalFormat df = new DecimalFormat("#,##0");
        String strCash = df.format(price);
        PendingIntent mPendingIntent = PendingIntent.getActivity(
                context.getApplicationContext(), 0,
                new Intent(context.getApplicationContext(), MainActivity_.class),
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        Notification notification = new NotificationCompat.Builder(context.getApplicationContext())
                .setContentTitle("OilMap")
                .setContentText(strCash + "원이 주유되었습니다.")
                .setSmallIcon(R.drawable.notification_icon)
                .setColor(context.getResources().getColor(R.color.orange_bg_color))
                .setTicker("주유정보가 입력되었습니다.")
                .setAutoCancel(true)
                .setContentIntent(mPendingIntent)
                .build();
        notificationManager.notify(NOTIFICATION_SMS, notification);
    }
}